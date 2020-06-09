package pl.psk.gkproject;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.psk.gkproject.scenes.Hud;
import pl.psk.gkproject.screens.MenuScreen;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Główna klasa gry. Służy do uruchomienia gry na różnych
 * środowiskach (desktop, android etc.)
 */
public class PlatformGame extends Game {
    /**
     * Szerokość viewportu
     */
    public static final int V_WIDTH = 400;

    /**
     * Wysokość viewportu
     */
    public static final int V_HEIGHT = 208;

    /**
     * Pixel per meter - służy do przeskalowania świata gry
     */
    public static final float PPM = 100;

    /**
     * Bit okreslający obiekt ziemi
     */
    public static final short GROUND_BIT = 1;

    /**
     * Bit określający obiekt Maria
     */
    public static final short MARIO_BIT = 2;

    /**
     * Bit określający kamień
     */
    public static final short BRICK_BIT = 4;

    /**
     * Bit określający obiekt z moneta/grzybem
     */
    public static final short COIN_BIT = 8;

    /**
     * Bit określający zniszczony obiekt
     */
    public static final short DESTROYED_BIT = 16;

    /**
     * Bit określający obiekt, na przykład rurę
     */
    public static final short OBJECT_BIT = 32;

    /**
     * Bit określający przeciwnika
     */
    public static final short ENEMY_BIT = 64;

    /**
     * Bit określający głowę przeciwnika
     */
    public static final short ENEMY_BIT_HEAD = 128;

    /**
     * Bit określający przedmiot - na przykład grzyba
     */
    public static final short ITEM_BIT = 256;

    /**
     * Bit określający głowę maria
     */
    public static final short MARIO_HEAD_BIT = 512;

    /**
     * Bit wspomagający - pomaga przy usuwaniu obiektów.
     */
    public static final short NOTHING_BIT = 1024;

    /**
     * Lista z poziomami gry. Po przejściu poziomu zostaje wczytany kolejny poziom.
     */
    private final Queue<String> levels = new LinkedList<>(Arrays.asList("level1.tmx", "level2.tmx", "level3.tmx"));

    /**
     * Obiektu hud'u gry.
     */
    private Hud hud;

    /**
     * Mechanizm preferencji libgdx. Dzięki któremu odbywa się zapis/wczytanie rozgrywki.
     */
    Preferences preferences;

    /**
     * Obiekt do rysowania spritów.
     */
    SpriteBatch batch;

    /**
     * Menadźer assetów. Pozwala on na przykład na wczytanie dzwięków gry.
     */
    public static AssetManager manager = new AssetManager();

    /**
     * Metoda tworząca grę. LibGDX wykorzystuje ją, aby uruchomić ekran gry.
     */
    @Override
    public void create() {
        manager.load("audio/music/mario_music.ogg", Music.class);
        manager.load("audio/sounds/coin.wav", Sound.class);
        manager.load("audio/sounds/bump.wav", Sound.class);
        manager.load("audio/sounds/breakblock.wav", Sound.class);
        manager.load("audio/sounds/powerup_spawn.wav", Sound.class);
        manager.load("audio/sounds/powerup.wav", Sound.class);
        manager.load("audio/sounds/stomp.wav", Sound.class);
        manager.load("audio/sounds/mariodie.wav", Sound.class);
        manager.load("audio/sounds/mariodie.wav", Sound.class);
        manager.finishLoading();


        preferences = Gdx.app.getPreferences("game_state");


        batch = new SpriteBatch();
        hud = new Hud(batch);
        setScreen(new MenuScreen(this));
    }

	/**
	 * Metoda odpowiadająca za wyrenderowanie gry
	 */
	@Override
    public void render() {
        super.render();
    }

	/**
	 * Getter dla obiektu do rysowania gry. Nie powinno się tworzyć wielu instancji sprite batcha,
	 * poniważ jest to kosztowne. Lepiej przechowywać jedna istancje tej klasy w aplikacji.
	 *
	 * @return SpriteBatch
	 */
	public SpriteBatch getBatch() {
        return batch;
    }

	/**
	 * Metoda czyszcząca grę. Uruchamiana przed zakońćzeniem.
	 */
	@Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }

	/**
	 * Getter dla poziomów aplikacji
	 *
	 * @return Zwraca tablicę z poziomami gry
	 */
	public Queue<String> getLevels() {
        return levels;
    }

	/**
	 * Getter dla obiektu Hud'u.
	 *
	 * @return Zwraca HUD
	 */
	public Hud getHud() {
        return hud;
    }

	/**
	 * Getter dla preferencji LibGdx.
	 *
	 * @return Zwraca preferencje
	 */
	public Preferences getPreferences() {
        return preferences;
    }
}
