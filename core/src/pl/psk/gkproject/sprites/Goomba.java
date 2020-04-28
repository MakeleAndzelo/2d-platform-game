package pl.psk.gkproject.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime = 0.0f;
    private Array<TextureRegion> frames = new Array<>();
    private Animation<TextureRegion> walkAnimation;

    public Goomba(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);

        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }

        walkAnimation = new Animation<>(0.4f, frames);

        setBounds(getX(), getY(), 16 / PlatformGame.PPM, 16 / PlatformGame.PPM);
    }

    public void update(float dt) {
        stateTime += dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void define() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / PlatformGame.PPM, 32 / PlatformGame.PPM);
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
        body.createFixture(fixtureDef);
    }
}
