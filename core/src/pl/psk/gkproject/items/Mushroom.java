package pl.psk.gkproject.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.scenes.Hud;
import pl.psk.gkproject.screens.PlayScreen;
import pl.psk.gkproject.sprites.Mario;

/**
 * Klasa obiektu grzyba
 */
public class Mushroom extends Item {
    /**
     * Wartość punktowa po zjedzeniu grzyba
     */
    private static final int MUSHROOM_SCORE_VALUE = 30;

    /**
     * Konstruktor klasy grzyba
     *
     * @param playScreen główny ekran gry
     * @param x pozycja X grzyba
     * @param y pozycja Y grzyba
     */
    public Mushroom(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        setRegion(playScreen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, -0.5f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PlatformGame.PPM);
        fixtureDef.filter.categoryBits = PlatformGame.ITEM_BIT;
        fixtureDef.filter.maskBits = PlatformGame.MARIO_BIT
                | PlatformGame.OBJECT_BIT
                | PlatformGame.GROUND_BIT
                | PlatformGame.COIN_BIT
                | PlatformGame.BRICK_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void use(Mario mario) {
        if (!mario.isMarioIsBig()) {
            mario.grow();
        }
        Hud.addScore(MUSHROOM_SCORE_VALUE);
        destroy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        body.setLinearVelocity(velocity);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
