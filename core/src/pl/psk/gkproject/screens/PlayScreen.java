package pl.psk.gkproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.WorldContactListener;
import pl.psk.gkproject.scenes.Hud;
import pl.psk.gkproject.sprites.*;

public class PlayScreen implements Screen {
    private final PlatformGame game;
    private final TextureAtlas atlas = new TextureAtlas("platform_game.pack");
    private final OrthographicCamera gameCamera = new OrthographicCamera();
    private final Viewport gameViewport;
    private final OrthogonalTiledMapRenderer renderer;

    private World world = new World(new Vector2(0, -10), true);
    private Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();
    private Hud hud;

    private final Mario player;

    public PlayScreen(PlatformGame game) {
        this.game = game;
        TiledMap map = new TmxMapLoader().load("level1.tmx");
        hud = new Hud(game.getBatch());
        gameViewport = new FitViewport(PlatformGame.V_WIDTH / PlatformGame.PPM, PlatformGame.V_HEIGHT / PlatformGame.PPM, gameCamera);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PlatformGame.PPM);
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);
        player = new Mario(world, this);
        world.setContactListener(new WorldContactListener(world, map));

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            new Ground(world, ((RectangleMapObject) object).getRectangle()).makeFixture();
        }

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            new Pipe(world, ((RectangleMapObject) object).getRectangle()).makeFixture();
        }

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            new Brick(world, ((RectangleMapObject) object).getRectangle()).makeFixture();
        }

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            new Coin(world, ((RectangleMapObject) object).getRectangle()).makeFixture();

        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.body.applyLinearImpulse(new Vector2(0, 4), player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2) {
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        handleInput(dt);

        player.update(dt);

        world.step(1 / 60f, 6, 2);
        gameCamera.position.x = player.body.getPosition().x;
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
        game.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        game.getBatch().setProjectionMatrix(gameCamera.combined);
        game.getBatch().begin();
        player.draw(game.getBatch());
        game.getBatch().end();
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
