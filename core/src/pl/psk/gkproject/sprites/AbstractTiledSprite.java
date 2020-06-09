package pl.psk.gkproject.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;

/**
 * Klasa abstrakcyjna obiektów na mapie z nałożonym spritem
 */
public abstract class AbstractTiledSprite {
    /**
     * Główny ekran gry
     */
    protected PlayScreen playScreen;

    /**
     * Świat gry
     */
    protected final World world;

    /**
     * Obiekt prostokątu, który wyraża obiekt
     */
    protected final Rectangle rectangle;

    /**
     * Obiekt, za pomocą którego będziemy definiować ciało
     */
    protected final BodyDef bodyDef = new BodyDef();

    /**
     * Ksztalt obiektu
     */
    protected final PolygonShape polygonShape = new PolygonShape();

    /**
     * Klasa do tworzenia fixture obiektu na mapie
     */
    protected final FixtureDef fixtureDef = new FixtureDef();

    /**
     * Ciało tworzonego obiektu
     */
    protected final Body body;

    /**
     * Fixture potrzebny do stworzenia obiektu na mapie
     */
    protected Fixture fixture;

    public AbstractTiledSprite(PlayScreen playScreen, World world, Rectangle rectangle) {
        this.playScreen = playScreen;
        this.world = world;
        this.rectangle = rectangle;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (rectangle.getX() + rectangle.getWidth() / 2) / PlatformGame.PPM,
                (rectangle.getY() + rectangle.getHeight() / 2) / PlatformGame.PPM
        );

        body = world.createBody(bodyDef);
    }

    /**
     * Stworzenie fixture obiektu
     */
    public void makeFixture() {
        polygonShape.setAsBox(
                rectangle.getWidth() / 2 / PlatformGame.PPM,
                rectangle.getHeight() / 2 / PlatformGame.PPM
        );

        fixtureDef.shape = polygonShape;

        fixture = body.createFixture(fixtureDef);
    }
}
