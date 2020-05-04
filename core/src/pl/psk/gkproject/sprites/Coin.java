package pl.psk.gkproject.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.WorldContactListener;
import pl.psk.gkproject.items.ItemDef;
import pl.psk.gkproject.items.Mushroom;
import pl.psk.gkproject.scenes.Hud;
import pl.psk.gkproject.screens.PlayScreen;

public class Coin extends AbstractTiledSprite implements InteractiveSprite {
    public final static int BLANK_COIN = 28;

    public Coin(PlayScreen playScreen, World world, Rectangle rectangle) {
        super(playScreen, world, rectangle);
    }

    @Override
    public void makeFixture() {
        super.makeFixture();

        Filter filter = new Filter();
        filter.categoryBits = PlatformGame.COIN_BIT;
        fixture.setUserData(this);
        fixture.setFilterData(filter);
    }

    @Override
    public void onHeadHit(TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        TiledMapTileLayer.Cell cell = layer.getCell(
                (int) (body.getPosition().x * PlatformGame.PPM / 16),
                (int) (body.getPosition().y * PlatformGame.PPM / 16)
        );

        if (Coin.BLANK_COIN == cell.getTile().getId()) {
            PlatformGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            if (Math.random() > 0.5) {
                playScreen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / PlatformGame.PPM), Mushroom.class));
                PlatformGame.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            } else {
                PlatformGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
            }
            Hud.addScore(20);
        }

        cell.setTile(WorldContactListener.tileSet.getTile(Coin.BLANK_COIN));
    }
}
