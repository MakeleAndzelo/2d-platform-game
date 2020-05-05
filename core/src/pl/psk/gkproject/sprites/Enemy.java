package pl.psk.gkproject.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import pl.psk.gkproject.screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body body;
    protected boolean setToDestroy = false;
    protected boolean destroyed = false;
    public Vector2 velocity = new Vector2(0.7f, -0.5f);

    public Enemy(PlayScreen playScreen, float x, float y) {
        screen = playScreen;
        world = playScreen.getWorld();
        setPosition(x, y);
        define();
    }

    protected abstract void define();
    public abstract void update(float deltaTime);
    public abstract void hitOnHead();

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }

        if (y) {
            velocity.y = -velocity.y;
        }
    }
}
