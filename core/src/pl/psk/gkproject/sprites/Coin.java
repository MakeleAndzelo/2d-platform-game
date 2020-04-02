package pl.psk.gkproject.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;

public class Coin {
    public Coin(World world, Rectangle rectangle) {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PlatformGame.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / PlatformGame.PPM);

        body = world.createBody(bodyDef);
        polygonShape.setAsBox(rectangle.getWidth() / 2 / PlatformGame.PPM, rectangle.getHeight() / 2 / PlatformGame.PPM);
        fixtureDef.shape = polygonShape;

        Filter filter = new Filter();
        filter.categoryBits = PlatformGame.COIN_BIT;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("coins");
        fixture.setFilterData(filter);
    }
}
