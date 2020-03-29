package pl.psk.gkproject.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;

public class Mario extends Sprite {
    public World world;
    public Body body;

    public Mario(World world) {
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / PlatformGame.PPM, 32 / PlatformGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / PlatformGame.PPM);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }
}
