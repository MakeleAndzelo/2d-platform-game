package pl.psk.gkproject.items;

import com.badlogic.gdx.math.Vector2;

/**
 * Klasa pomocnicza. Służy do obsługi tworzenia obiektów na ekranie
 */
public class ItemDef {
    /**
     * Pozycja tworzonego obiektu
     */
    public Vector2 position;

    /**
     * Typ tworzonego obiektu
     */
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
