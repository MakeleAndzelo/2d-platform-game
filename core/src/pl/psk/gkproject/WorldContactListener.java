package pl.psk.gkproject;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.items.Item;
import pl.psk.gkproject.items.ItemDef;
import pl.psk.gkproject.items.Mushroom;
import pl.psk.gkproject.scenes.Hud;
import pl.psk.gkproject.screens.PlayScreen;
import pl.psk.gkproject.sprites.Brick;
import pl.psk.gkproject.sprites.Coin;
import pl.psk.gkproject.sprites.Enemy;
import pl.psk.gkproject.sprites.Mario;

public class WorldContactListener implements ContactListener {
    private TiledMap map;
    private static TiledMapTileSet tileSet;
    private PlayScreen playScreen;

    public WorldContactListener(TiledMap map, PlayScreen playScreen) {
        this.map = map;
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        this.playScreen = playScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cdef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if (object.getUserData() instanceof Coin) {
                TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
                TiledMapTileLayer.Cell cell = layer.getCell(
                        (int) (object.getBody().getPosition().x * PlatformGame.PPM / 16),
                        (int) (object.getBody().getPosition().y * PlatformGame.PPM / 16)
                );

                if (Coin.BLANK_COIN == cell.getTile().getId()) {
                    PlatformGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
                } else {
                    if (Math.random() > 0.5) {
                        playScreen.spawnItem(new ItemDef(new Vector2(object.getBody().getPosition().x, object.getBody().getPosition().y + 16 / PlatformGame.PPM), Mushroom.class));
                        PlatformGame.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
                    } else {
                        PlatformGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
                    }
                    Hud.addScore(20);
                }

                cell.setTile(tileSet.getTile(Coin.BLANK_COIN));
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
                PlatformGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
            }
        }

        if (cdef == (PlatformGame.ENEMY_BIT_HEAD | PlatformGame.MARIO_BIT)) {
            if (PlatformGame.ENEMY_BIT_HEAD == fixA.getFilterData().categoryBits) {
                ((Enemy) fixA.getUserData()).hitOnHead();
            } else {
                ((Enemy) fixB.getUserData()).hitOnHead();
            }
        }

        if (cdef == (PlatformGame.ENEMY_BIT | PlatformGame.OBJECT_BIT)) {
            if (PlatformGame.ENEMY_BIT == fixA.getFilterData().categoryBits) {
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
            } else {
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
            }
        }

        if (cdef == (PlatformGame.ENEMY_BIT)) {
            ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
            ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
        }

        if (cdef == (PlatformGame.ITEM_BIT | PlatformGame.OBJECT_BIT)) {
            if (PlatformGame.ITEM_BIT == fixA.getFilterData().categoryBits) {
                ((Item) fixA.getUserData()).reverseVelocity(true, false);
            } else {
                ((Item) fixB.getUserData()).reverseVelocity(true, false);
            }
        }

        if (cdef == (PlatformGame.ITEM_BIT | PlatformGame.MARIO_BIT)) {
            if (PlatformGame.ITEM_BIT == fixA.getFilterData().categoryBits) {
                ((Item) fixA.getUserData()).use(((Mario) fixB.getUserData()));
            } else {
                ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
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
