package pl.psk.gkproject.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;

public class Coin extends AbstractTiledSprite {
    public final static int BLANK_COIN = 28;

    public Coin(World world, Rectangle rectangle) {
        super(world, rectangle);
    }

    @Override
    public void makeFixture() {
        super.makeFixture();

        Filter filter = new Filter();
        filter.categoryBits = PlatformGame.COIN_BIT;
        fixture.setUserData(this);
        fixture.setFilterData(filter);
    }
}
