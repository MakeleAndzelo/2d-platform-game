package pl.psk.gkproject.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;

public class Goombaa extends Enemy {
    public Goombaa(PlayScreen playScreen, float x, float y) {
        super(playScreen, x, y);
    }

    @Override
    protected void define() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / PlatformGame.PPM, 32 / PlatformGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PlatformGame.PPM);
        fixtureDef.filter.categoryBits = PlatformGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = PlatformGame.GROUND_BIT | PlatformGame.COIN_BIT | PlatformGame.BRICK_BIT | PlatformGame.ENEMY_BIT | PlatformGame.OBJECT_BIT
        ;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }
}
