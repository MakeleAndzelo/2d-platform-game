package pl.psk.gkproject.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;

public class Mario extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DEAD};
    public State currentState;
    public State previousState;
    private Animation marioRun;
    private TextureRegion marioJump;
    private float stateTimer;
    private boolean runningRight;
    public World world;
    public Body body;
    private TextureRegion marioStand;
    private TextureRegion marioDead;
    private boolean marioIsDead = false;
    private boolean marioWon = false;

    public Mario(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16 / PlatformGame.PPM, 16 / PlatformGame.PPM);
        setRegion(marioStand);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        marioDead = new TextureRegion(screen.getAtlas().findRegion(("little_mario")), 96, 0, 16, 16);
        marioJump = new TextureRegion(screen.getAtlas().findRegion(("little_mario")), 80, 0, 16, 16);


        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / PlatformGame.PPM, 32 / PlatformGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PlatformGame.PPM);
        fixtureDef.filter.categoryBits = PlatformGame.MARIO_BIT;
        fixtureDef.filter.maskBits = PlatformGame.GROUND_BIT | PlatformGame.COIN_BIT | PlatformGame.BRICK_BIT | PlatformGame.ENEMY_BIT | PlatformGame.OBJECT_BIT | PlatformGame.ENEMY_BIT_HEAD | PlatformGame.ITEM_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / PlatformGame.PPM, 5 / PlatformGame.PPM), new Vector2(2 / PlatformGame.PPM, 5 / PlatformGame.PPM));
        fixtureDef.filter.categoryBits = PlatformGame.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float dt) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        if (0 > body.getPosition().y && !isMarioIsDead()) {
            die();
        }

        if (33 < body.getPosition().x) {
            marioWon = true;
        }

        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;

        switch (currentState) {
            case DEAD:
                region = marioDead;
                break;
            case JUMPING:
                region = marioJump;
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }

        if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (marioIsDead) {
            return State.DEAD;
        }

        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        }

        if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        }

        if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        }

        return State.STANDING;
    }

    public void die() {
        PlatformGame.manager.get("audio/music/mario_music.ogg", Music.class).stop();
        PlatformGame.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
        marioIsDead = true;
        Filter filter = new Filter();
        filter.maskBits = PlatformGame.NOTHING_BIT;

        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
    }

    public boolean isMarioIsDead() {
        return marioIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean isMarioWon() {
        return marioWon;
    }
}
