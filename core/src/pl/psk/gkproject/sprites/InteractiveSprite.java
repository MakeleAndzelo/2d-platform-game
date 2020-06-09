package pl.psk.gkproject.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;

public interface InteractiveSprite {
    void onHeadHit(TiledMap tiledMap);
}
