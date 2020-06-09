package pl.psk.gkproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.WorldContactListener;
import pl.psk.gkproject.items.Item;
import pl.psk.gkproject.items.ItemDef;
import pl.psk.gkproject.items.Mushroom;
import pl.psk.gkproject.scenes.Hud;
import pl.psk.gkproject.sprites.*;

import java.util.concurrent.LinkedBlockingQueue;


public class PlayScreen implements Screen {
    private final PlatformGame game;
    private final TextureAtlas atlas = new TextureAtlas("platform_game.pack");
    private final OrthographicCamera gameCamera = new OrthographicCamera();
    private final Viewport gameViewport;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world = new World(new Vector2(0, -10), true);
    private final String currentLevel;
    private final Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer(false, false, false, false, false, false);
    private final Array<Goomba> goombas = new Array<>();

    private final Mario player;

    private final Array<Item> items = new Array<>();
    private final LinkedBlockingQueue<ItemDef> itemsToSpawn = new LinkedBlockingQueue<>();

    public PlayScreen(PlatformGame game, String levelName) {
        this.game = game;
        TiledMap map = new TmxMapLoader().load(levelName);
        currentLevel = levelName;
        gameViewport = new FitViewport(PlatformGame.V_WIDTH / PlatformGame.PPM, PlatformGame.V_HEIGHT / PlatformGame.PPM, gameCamera);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PlatformGame.PPM);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);
        player = new Mario(world, this);
        world.setContactListener(new WorldContactListener(map));

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            new Ground(this, world, ((RectangleMapObject) object).getRectangle()).makeFixture();
        }

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            new Pipe(this, world, ((RectangleMapObject) object).getRectangle()).makeFixture();
        }

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            new Brick(this, world, ((RectangleMapObject) object).getRectangle()).makeFixture();
        }

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            new Coin(this, world, ((RectangleMapObject) object).getRectangle()).makeFixture();
        }

        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(this, rectangle.getX() / PlatformGame.PPM, rectangle.getY() / PlatformGame.PPM));
        }

        Music music = PlatformGame.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.02f);
        music.play();
    }

    public World getWorld() {
        return world;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {
    }

    public void handleInput() {
        if (Mario.State.DEAD != player.currentState) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && Mario.State.JUMPING != player.currentState) {
                player.body.applyLinearImpulse(new Vector2(0, 4), player.body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
                player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2 && 0 < player.body.getPosition().x) {
                player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
            }
        }
    }

    public void spawnItem(ItemDef itemDef) {
        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            ItemDef itemDef = itemsToSpawn.poll();

            if (itemDef.type == Mushroom.class) {
                items.add(new Mushroom(this, itemDef.position.x, itemDef.position.y));
            }
        }
    }

    public boolean gameOver() {
        return Mario.State.DEAD == player.currentState && 3 < player.getStateTimer();
    }

    public void update(float dt) {
        handleInput();
        handleSpawningItems();

        player.update(dt);
        for (Enemy enemy : goombas) {
            enemy.update(dt);
        }

        for (Item item : items) {
            item.update(dt);
        }
        world.step(1 / 60f, 6, 2);

        if (Mario.State.DEAD != player.currentState) {
            gameCamera.position.x = player.body.getPosition().x;
        }

        gameCamera.update();
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        renderer.render();

        box2DDebugRenderer.render(world, gameCamera.combined);
        game.getBatch().setProjectionMatrix(game.getHud().stage.getCamera().combined);
        game.getHud().stage.draw();
        game.getBatch().setProjectionMatrix(gameCamera.combined);
        game.getBatch().begin();
        player.draw(game.getBatch());
        for (Enemy enemy : goombas) {
            enemy.draw(game.getBatch());
        }

        for (Item item : items) {
            item.draw(game.getBatch());
        }
        game.getBatch().end();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        if (player.isMarioWon()) {
            if (0 == game.getLevels().size()) {
                game.setScreen(new WinScreen(game));
            } else {
                game.getPreferences().putString("level", currentLevel);
                game.getPreferences().putInteger("score", Hud.score);
                game.getPreferences().flush();
                game.setScreen(new PlayScreen(game, game.getLevels().poll()));
            }
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
