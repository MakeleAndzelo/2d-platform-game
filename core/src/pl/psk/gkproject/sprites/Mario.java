package pl.psk.gkproject.sprites;

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

/**
 * Klasa określająca Maria, którym steruje gracza
 */
public class Mario extends Sprite {
    /**
     * Stany określające maria
     *
     * FALLING - spadanie
     * JUMPING - skok
     * STANDING - bezczynność
     * RUNNING - bieg
     * DEAD - mario nie zyje
     */
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DEAD}

    /**
     * Aktualny stan maria
     */
    public State currentState;

    /**
     * Poprzedni stan maria
     */
    public State previousState;

    /**
     * Animacja wyświetlana podczas poruszania maria
     */
    private final Animation<TextureRegion> marioRun;

    /**
     * Tekstura dla skaczącego maria
     */
    private final TextureRegion marioJump;

    /**
     * Czas od początku istnienia maria
     */
    private float stateTimer;

    /**
     * Flaga okreslajaca, czy mario biegnie w prawo.
     */
    private boolean runningRight;

    /**
     * Świat gry
     */
    public World world;

    /**
     * Ciało maria
     */
    public Body body;

    /**
     * Tekstura gdy mario stoi
     */
    private final TextureRegion marioStand;

    /**
     * Tekstura niezywego maria
     */
    private final TextureRegion marioDead;

    /**
     * Flaga określająca, że mario nie zyje
     */
    private boolean marioIsDead = false;

    /**
     * Flaga określająca, ze gracz przeszedł grę
     */
    private boolean marioWon = false;

    /**
     * Tekstura dla dużego maria w bezruchu
     */
    private final TextureRegion bigMarioStand;

    /**
     * Tekstura dla skaczącego dużego maria
     */
    private final TextureRegion bigMarioJump;

    /**
     * Animacja dla poruszania się dużego maria
     */
    private final Animation<TextureRegion> bigMarioRun;

    /**
     * Flaga określająca, czy mario jest duży (czy zjadl grzyba)
     */
    private boolean marioIsBig = false;

    /**
     * Flaga określająca, czy mario powinien urosnąć
     */
    private boolean timeToDefineBigMario = false;

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

        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        Array<TextureRegion> frames = new Array<>();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
        }
        marioRun = new Animation<>(0.1f, frames);
        frames.clear();


        for(int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
        }
        bigMarioRun = new Animation<>(0.1f, frames);

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

    /**
     * Aktualizacja maria na ekranie
     *
     * @param dt czas aktualizacji
     */
    public void update(float dt) {
        if (marioIsBig) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 - 6 / PlatformGame.PPM);
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }

        if (0 > body.getPosition().y && !isMarioIsDead()) {
            die();
        }

        if (33 < body.getPosition().x) {
            marioWon = true;
        }

        setRegion(getFrame(dt));
        
        if (timeToDefineBigMario) {
            defineBigMario();
        }
    }

    /**
     * Definiowanie dużego maria. Jest tutaj po prostu podmieniana fixture maria
     */
    private void defineBigMario() {
        Vector2 currentPosition = body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0, 10 / PlatformGame.PPM));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PlatformGame.PPM);
        fixtureDef.filter.categoryBits = PlatformGame.MARIO_BIT;
        fixtureDef.filter.maskBits = PlatformGame.GROUND_BIT | PlatformGame.COIN_BIT | PlatformGame.BRICK_BIT | PlatformGame.ENEMY_BIT | PlatformGame.OBJECT_BIT | PlatformGame.ENEMY_BIT_HEAD | PlatformGame.ITEM_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / PlatformGame.PPM));
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / PlatformGame.PPM, 5 / PlatformGame.PPM), new Vector2(2 / PlatformGame.PPM, 5 / PlatformGame.PPM));
        fixtureDef.filter.categoryBits = PlatformGame.MARIO_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);
        timeToDefineBigMario = false;
    }

    /**
     * Pobranie regionu tekstury dla maria
     *
     * @param dt czas iteracji
     * @return region tekstury
     */
    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;

        switch (currentState) {
            case DEAD:
                region = marioDead;
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
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

    /**
     * Pobranie stanu maria
     *
     * @return stan maria
     */
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

    /**
     * Metoda uśmiercająca maria. Ustawia odpowiednie flagi oraz dodaje animację.
     */
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

    /**
     * @return Flaga określająca, czy mario nie zyje
     */
    public boolean isMarioIsDead() {
        return marioIsDead;
    }

    /**
     * @return czas istnienia obiektu maria
     */
    public float getStateTimer() {
        return stateTimer;
    }

    /**
     * @return Flaga określająca czy gracz przeszedł grę
     */
    public boolean isMarioWon() {
        return marioWon;
    }

    /**
     * Metoda do wywołania rośniecia przez Maria
     */
    public void grow() {
        marioIsBig = true;
        timeToDefineBigMario = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        PlatformGame.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

    /**
     * @return Flaga określająca, czy Mario jest duży
     */
    public boolean isMarioIsBig() {
        return marioIsBig;
    }
}
