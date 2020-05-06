package pl.psk.gkproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import pl.psk.gkproject.PlatformGame;

public class MenuScreen implements Screen {
    private final Texture background = new Texture("background.jpg");
    private final Texture playBtn = new Texture("playbtn.jpg");
    private PlatformGame game;

    public MenuScreen(PlatformGame game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen(game, game.getLevels().poll()));
            dispose();
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, 700, 480);
        game.getBatch().draw(playBtn, (PlatformGame.V_WIDTH) - (playBtn.getWidth() + 20), PlatformGame.V_HEIGHT);
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
    }
}
