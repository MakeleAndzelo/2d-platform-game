package pl.psk.gkproject.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;
import pl.psk.gkproject.sprites.Mario;

public class Mushroom extends Item {
    public Mushroom(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
        setRegion(playScreen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(.7f, 0);
    }

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

    @Override
    public void use(Mario mario) {
        mario.grow();
        destroy();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        body.setLinearVelocity(velocity);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
