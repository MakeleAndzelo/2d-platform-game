package pl.psk.gkproject.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;

public abstract class AbstractTiledSprite {
    protected final World world;
    protected final Rectangle rectangle;
    protected final BodyDef bodyDef = new BodyDef();
    protected final PolygonShape polygonShape = new PolygonShape();
    protected final FixtureDef fixtureDef = new FixtureDef();
    protected final Body body;
    protected Fixture fixture;

    public AbstractTiledSprite(World world, Rectangle rectangle) {
        this.world = world;
        this.rectangle = rectangle;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (rectangle.getX() + rectangle.getWidth() / 2) / PlatformGame.PPM,
                (rectangle.getY() + rectangle.getHeight() / 2) / PlatformGame.PPM
        );

        body = world.createBody(bodyDef);
    }

    public void makeFixture() {
        polygonShape.setAsBox(
                rectangle.getWidth() / 2 / PlatformGame.PPM,
                rectangle.getHeight() / 2 / PlatformGame.PPM
        );

        fixtureDef.shape = polygonShape;
        fixture = body.createFixture(fixtureDef);
    }
}
