package pl.psk.gkproject.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;
import pl.psk.gkproject.sprites.Mario;

/**
 * Abstrakcyjna klasa dla obiektów, z którymi Mario może wejść w interakcję
 * (na przykład grzyby).
 */
public abstract class Item extends Sprite {
    /**
     * Instancja głównego ekranu gry
     */
    protected PlayScreen playScreen;

    /**
     * Świat gry
     */
    protected World world;

    /**
     * Wektor przemieszczania się obiektu
     */
    protected Vector2 velocity;

    /**
     * Flaga oznaczająca, że obiekt powinien zostać zniszczony.
     */
    protected boolean toDestroy = false;

    /**
     * Flaga okreslająca, czy obiekt został już zniszczony
     */
    protected boolean destroyed = false;

    /**
     * Definicja ciała dla obiektu, na który jest nakładany sprite, bit etc.
     */
    protected Body body;

    /**
     * Konstruktor klasy
     *
     * @param playScreen główny ekran gry
     * @param x Pozycja X obiektu
     * @param y Pozycja Y obiektu
     */
    public Item(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        setPosition(x ,y);
        setBounds(getX(), getY(), 16 / PlatformGame.PPM, 16 / PlatformGame.PPM);

        defineItem();
    }

    /**
     * Metoda do zdefiniowania obiektu
     */
    public abstract void defineItem();

    /**
     * Metoda, w której jest określone co sie dzieje z przedmiotem po interakcji z mariem
     *
     * @param mario obiekt Maria
     */
    public abstract void use(Mario mario);

    /**
     * Metoda do aktualizacji obiektu w kolejnej iteracji
     *
     * @param deltaTime czas iteracji
     */
    public void update(float deltaTime) {
        if (toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    /**
     * Rysuje obiekt na ekranie. Jest tutaj sprawdzane, czy obiekt został usunięty.
     *
     * @param batch obiekt do rysowania
     */
    @Override
    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }

    /**
     * Ustawianie obiektu do usunięcia
     */
    public void destroy() {
        toDestroy = true;
    }

    /**
     * Metoda śłużąca do zmiany kierunku obiektu
     *
     * @param x parametr określający, czy powinniśmy zmienić kierunek w osi X
     * @param y parametr określający, czy powinniśmy zmienić kierunek w osi Y
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
