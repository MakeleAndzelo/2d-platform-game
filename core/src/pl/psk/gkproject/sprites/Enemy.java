package pl.psk.gkproject.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import pl.psk.gkproject.screens.PlayScreen;

/**
 * Klasa abstrakcyjna dla przeciwnika na mapie
 */
public abstract class Enemy extends Sprite {
    /**
     * Obiekt świata gry
     */
    protected World world;

    /**
     * Główny ekran gry
     */
    protected PlayScreen screen;

    /**
     * Ciało przeciwnika
     */
    public Body body;

    /**
     * Flaga, która oznacza, że przeciwnik jest do zniszczenia
     */
    protected boolean setToDestroy = false;

    /**
     * Flaga, która oznacza przeciwnika jako zniszczonego
     */
    protected boolean destroyed = false;

    /**
     * Wektor poruszania się przeciwnika
     */
    public Vector2 velocity = new Vector2(0.7f, -0.5f);

    public Enemy(PlayScreen playScreen, float x, float y) {
        screen = playScreen;
        world = playScreen.getWorld();
        setPosition(x, y);
        define();
    }

    /**
     * Definiowanie przeciwnika
     */
    protected abstract void define();

    /**
     * Aktualizacja przeciwnika na ekranie
     *
     * @param deltaTime czas aktualizacji
     */
    public abstract void update(float deltaTime);

    /**
     * Akcja po uderzeniu przeciwnika w głowę
     */
    public abstract void hitOnHead();

    /**
     * Zmiana kierunku przeciwnika
     *
     * @param x określa, czy kierunek powinien zostać zmieniony na osi X
     * @param y określa, czy kierunek powinien zostać zmieniony na osi Y
     */
    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }

        if (y) {
            velocity.y = -velocity.y;
        }
    }
}
