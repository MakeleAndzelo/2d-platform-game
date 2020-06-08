package pl.psk.gkproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import pl.psk.gkproject.PlatformGame;
import pl.psk.gkproject.scenes.Hud;

public class MenuScreen implements Screen {
    private final Texture background = new Texture("background.jpg");
    private final Texture playBtn = new Texture("new_game.png");
    private final Texture resumeBtn = new Texture("resume_game.png");
    private final Texture closeBtn = new Texture("close_game.png");
    private PlatformGame game;

    public MenuScreen(PlatformGame game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    public void handleInput() {
        if (240 <= Gdx.input.getX() && 400 >= Gdx.input.getX()) {
            if (180 <= Gdx.input.getY() && 230 > Gdx.input.getY()) {
                if (Gdx.input.isTouched()) {

                    Hud.setScore(game.getPreferences().getInteger("score"));
                    String savedLevel = game.getPreferences().getString("level");
                    String level;
                    do {
                        level = game.getLevels().poll();
                    } while (!savedLevel.equals(level));

                    game.setScreen(new PlayScreen(game, game.getLevels().poll()));
                    dispose();

                }
            }

            if (230 <= Gdx.input.getY() && 280 > Gdx.input.getY()) {
                if (Gdx.input.isTouched()) {
                    game.setScreen(new PlayScreen(game, game.getLevels().poll()));
                    dispose();
                }

            }

            if (280 <= Gdx.input.getY() && 320 >= Gdx.input.getY()) {
                if (Gdx.input.isTouched()) {
                    Gdx.app.exit();
                }
            }

        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, 700, 480);
        if (!game.getPreferences().getString("level").isEmpty()) {
            game.getBatch().draw(resumeBtn, (PlatformGame.V_WIDTH / 2) + 20, 230, 200, 80);
        }
        game.getBatch().draw(playBtn, (PlatformGame.V_WIDTH / 2) + 20, 180, 200, 80);
        game.getBatch().draw(closeBtn, (PlatformGame.V_WIDTH / 2) + 20, 140, 200, 80);
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
