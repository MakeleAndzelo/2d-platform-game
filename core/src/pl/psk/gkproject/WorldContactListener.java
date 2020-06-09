package pl.psk.gkproject;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.items.Item;
import pl.psk.gkproject.sprites.*;

/**
 * Klasa Odpowiedzialna za kolizję w grze.
 */
public class WorldContactListener implements ContactListener {
    /**
     * Mapa gry, z aktualnie granym poziomem.
     */
    private final TiledMap map;

    /**
     * Konstruktor klasy
     *
     * @param map Mapa gry, z aktualnie granym poziomem.
     */
    public WorldContactListener(TiledMap map) {
        this.map = map;
    }

    /**
     * Metoda, która jest wywoływana podczas kolizji z różnymi obiektami.
     * Sprawdzane są bity poszczególnych obiektów oraz wykonywane odpowiednie interakcje.
     *
     * @param contact obiekt przechowujący informację na temat kolizji.
     */
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

        if (cdef == (PlatformGame.ENEMY_BIT | PlatformGame.OBJECT_BIT) || cdef == (PlatformGame.ENEMY_BIT | PlatformGame.GROUND_BIT)) {
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

        if (cdef == (PlatformGame.MARIO_BIT | PlatformGame.ENEMY_BIT)) {
            if (PlatformGame.MARIO_BIT == fixA.getFilterData().categoryBits) {
                ((Mario) fixA.getUserData()).die();
            } else {
                ((Mario) fixB.getUserData()).die();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
    }

    /**
     * {@inheritDoc}
     *
     * @param contact
     * @param oldManifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    /**
     * {@inheritDoc}
     *
     * @param contact
     * @param impulse
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
