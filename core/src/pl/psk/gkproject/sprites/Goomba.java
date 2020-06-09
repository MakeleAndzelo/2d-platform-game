package pl.psk.gkproject.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;

/**
 * Klasa konkretnego przeciwnika
 */
public class Goomba extends Enemy {
    /**
     * Czas od początku istnienia przeciwnika
     */
    private float stateTime = 0.0f;

    /**
     * Animacja poruszania się przeciwnika
     */
    private final Animation<TextureRegion> walkAnimation;

    public Goomba(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }

        walkAnimation = new Animation<>(0.4f, frames);

        setBounds(getX(), getY(), 16 / PlatformGame.PPM, 16 / PlatformGame.PPM);
    }

    /**
     * {@inheritDoc}
     *
     * @param dt
     */
    public void update(float dt) {
        stateTime += dt;

        if (setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        }

        if (!destroyed) {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    /**
     * Rysowanie przeciwnika na mapie
     *
     * @param batch obiekt do rysowania
     */
    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void define() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PlatformGame.PPM);
        fixtureDef.filter.categoryBits = PlatformGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = PlatformGame.GROUND_BIT
                | PlatformGame.COIN_BIT
                | PlatformGame.BRICK_BIT
                | PlatformGame.ENEMY_BIT
                | PlatformGame.OBJECT_BIT
                | PlatformGame.MARIO_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / PlatformGame.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / PlatformGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / PlatformGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / PlatformGame.PPM);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = PlatformGame.ENEMY_BIT_HEAD;
        body.createFixture(fixtureDef).setUserData(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hitOnHead() {
        setToDestroy = true;
        PlatformGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }
}
