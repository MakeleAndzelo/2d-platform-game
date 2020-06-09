package pl.psk.gkproject.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.screens.PlayScreen;

/**
 * Klasa definiująca ziemie na mapie
 */
public class Ground extends AbstractTiledSprite {
    public Ground(PlayScreen playScreen, World world, Rectangle rectangle) {
        super(playScreen, world, rectangle);
    }
}
