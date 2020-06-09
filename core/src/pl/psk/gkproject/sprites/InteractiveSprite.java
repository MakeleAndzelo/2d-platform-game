package pl.psk.gkproject.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Interfejs obiektu, który może miec kolizję z głową Maria
 */
public interface InteractiveSprite {
    /**
     * Akcja, ktora jest wykonywana podczas kolizji z głową maria
     *
     * @param tiledMap aktualna mapa gry
     */
    void onHeadHit(TiledMap tiledMap);
}
