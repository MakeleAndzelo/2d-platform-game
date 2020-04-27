package pl.psk.gkproject.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import pl.psk.gkproject.screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body body;

    public Enemy(PlayScreen playScreen, float x, float y) {
        screen = playScreen;
        world = playScreen.getWorld();
        setPosition(x, y);
    }

    protected abstract void define();
}