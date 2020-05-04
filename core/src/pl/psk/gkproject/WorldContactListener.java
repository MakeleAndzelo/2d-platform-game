package pl.psk.gkproject;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.items.Item;
import pl.psk.gkproject.screens.PlayScreen;
import pl.psk.gkproject.sprites.*;

public class WorldContactListener implements ContactListener {
    private TiledMap map;
    public static TiledMapTileSet tileSet;
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

        if (cdef == (PlatformGame.MARIO_HEAD_BIT | PlatformGame.BRICK_BIT) || cdef == (PlatformGame.MARIO_HEAD_BIT | PlatformGame.COIN_BIT)) {
            if (PlatformGame.MARIO_HEAD_BIT == fixA.getFilterData().categoryBits) {
                ((InteractiveSprite) fixB.getUserData()).onHeadHit(map);
            } else {
                ((InteractiveSprite) fixA.getUserData()).onHeadHit(map);
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
