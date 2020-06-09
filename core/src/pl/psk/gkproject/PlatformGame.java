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

public class PlatformGame extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short GROUND_BIT = 1;
	public static final short MARIO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_BIT_HEAD = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short NOTHING_BIT = 1024;

	private final Queue<String> levels = new LinkedList<>(Arrays.asList("level1.tmx", "level2.tmx", "level3.tmx"));

	private Hud hud;

	Preferences preferences;

	SpriteBatch batch;

	public static AssetManager manager = new AssetManager();

	@Override
	public void create () {
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

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

	public Queue<String> getLevels() {
		return levels;
	}

	public Hud getHud() {
		return hud;
	}

	public Preferences getPreferences() {
		return preferences;
	}
}
