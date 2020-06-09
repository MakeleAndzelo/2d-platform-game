package pl.psk.gkproject.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.screens.PlayScreen;

/**
 * Klasa reprezentująca klocek "cegieł" na mapie
 */
public class Brick extends AbstractTiledSprite implements InteractiveSprite {
    public Brick(PlayScreen playScreen, World world, Rectangle rectangle) {
        super(playScreen, world, rectangle);
    }

    /**
     * Stworzenie fixture obiektu
     */
    @Override
    public void makeFixture() {
        super.makeFixture();

        Filter filter = new Filter();
        filter.categoryBits = PlatformGame.BRICK_BIT;

        fixture.setUserData(this);
        fixture.setFilterData(filter);
    }

    /**
     * Ustawienie filtru dla fixture
     *
     * @param filter filter dla fixture
     */
    public void setFixtureFilter(Filter filter) {
        fixture.setFilterData(filter);
    }

    /**
     * Metoda, która jest używana podczas interakcji głowy Maria z klockiem
     *
     * @param map aktualna mapa gry
     */
    @Override
    public void onHeadHit(TiledMap map) {
        Filter filter = new Filter();
        filter.categoryBits = PlatformGame.DESTROYED_BIT;
        this.setFixtureFilter(filter);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        TiledMapTileLayer.Cell cell = layer.getCell(
                (int) (this.body.getPosition().x * PlatformGame.PPM / 16),
                (int) (this.body.getPosition().y * PlatformGame.PPM / 16)
        );
        cell.setTile(null);
        PlatformGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
