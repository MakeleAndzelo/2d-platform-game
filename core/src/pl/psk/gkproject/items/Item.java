package pl.psk.gkproject.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;
import pl.psk.gkproject.sprites.Mario;

public abstract class Item extends Sprite {
    protected PlayScreen playScreen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy = false;
    protected boolean destroyed = false;
    protected Body body;

    public Item(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.world = playScreen.getWorld();
        setPosition(x ,y);
        setBounds(getX(), getY(), 16 / PlatformGame.PPM, 16 / PlatformGame.PPM);

        defineItem();
    }

    public abstract void defineItem();

    public abstract void use(Mario mario);

    public void update(float deltaTime) {
        if (toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }

    public void destroy() {
        toDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }

        if (y) {
            velocity.y = -velocity.y;
        }
    }
}
