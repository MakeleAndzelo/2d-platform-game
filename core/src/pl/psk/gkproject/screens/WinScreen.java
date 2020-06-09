package pl.psk.gkproject.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import pl.psk.gkproject.PlatformGame;

/**
 * Ekran po przejściu gry
 */
public class WinScreen implements Screen {
    /**
     * Scena, na której zostanie umieszczona informacja o zakończonej grze
     */
    private final Stage stage;

    public WinScreen(Game game) {
        Viewport viewport = new FitViewport(PlatformGame.V_WIDTH, PlatformGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((PlatformGame) game).getBatch());

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        ((PlatformGame) game).getPreferences().clear();

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("WYGRALES", font);
        table.add(gameOverLabel).expandX();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    /**
     * Wyrenderowanie ekranu
     *
     * @param delta czas renderu
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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

    }
}
