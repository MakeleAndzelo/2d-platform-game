package pl.psk.gkproject.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;

public class Brick extends AbstractTiledSprite {
    public Brick(World world, Rectangle rectangle) {
        super(world, rectangle);
    }

    @Override
    public void makeFixture() {
        super.makeFixture();

        Filter filter = new Filter();
        filter.categoryBits = PlatformGame.BRICK_BIT;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("bricks");
        fixture.setFilterData(filter);
    }
}
