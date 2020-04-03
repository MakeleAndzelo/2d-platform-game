package pl.psk.gkproject;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.scenes.Hud;
import pl.psk.gkproject.sprites.Brick;
import pl.psk.gkproject.sprites.Coin;

public class WorldContactListener implements ContactListener {
    private TiledMap map;
    private static TiledMapTileSet tileSet;

    public WorldContactListener(TiledMap map) {
        this.map = map;
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() instanceof Coin) {
                TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
                TiledMapTileLayer.Cell cell = layer.getCell(
                        (int) (object.getBody().getPosition().x * PlatformGame.PPM / 16),
                        (int) (object.getBody().getPosition().y * PlatformGame.PPM / 16)
                );
                cell.setTile(tileSet.getTile(Coin.BLANK_COIN));
                Hud.addScore(20);
            }

            if (object.getUserData() instanceof Brick) {
                Filter filter = new Filter();
                filter.categoryBits = PlatformGame.DESTROYED_BIT;
                ((Brick) object.getUserData()).setFixtureFilter(filter);

                TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
                TiledMapTileLayer.Cell cell = layer.getCell(
                        (int) (object.getBody().getPosition().x * PlatformGame.PPM / 16),
                        (int) (object.getBody().getPosition().y * PlatformGame.PPM / 16)
                );
                cell.setTile(null);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
