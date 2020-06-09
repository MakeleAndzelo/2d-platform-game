package pl.psk.gkproject.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;

/**
 * Klasa reprezentująca rure na planszy
 */
public class Pipe extends AbstractTiledSprite {
    public Pipe(PlayScreen playScreen, World world, Rectangle rectangle) {
        super(playScreen, world, rectangle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeFixture() {
        super.makeFixture();

        Filter filter = new Filter();
        filter.categoryBits = PlatformGame.OBJECT_BIT;
        fixture.setUserData(this);
        fixture.setFilterData(filter);
    }
}
