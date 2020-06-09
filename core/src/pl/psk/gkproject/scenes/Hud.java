package pl.psk.gkproject.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import pl.psk.gkproject.PlatformGame;

/**
 * Klasa hudu gry. Jest odpowiedzialna za wynik w grze
 */
public class Hud {
    /**
     * Obiekt sceny, do którego zostanie dodany HUD
     */
    public Stage stage;

    /**
     * Liczba punktów uzyskanych przez gracza.
     */
    public static Integer score;

    /**
     * Etykieta dla wyniku gracza
     */
    public static Label scoreLabel;

    public Hud(SpriteBatch spriteBatch) {
        score = 0;
        Viewport viewport = new FitViewport(
                PlatformGame.V_WIDTH,
                PlatformGame.V_HEIGHT,
                new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        table.add(scoreLabel).expandX();

        stage.addActor(table);
    }

    /**
     * Dodanie puntków do wyniku gracza
     *
     * @param value ilość punktów do dodania
     */
    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    /**
     * Ustawienie wyniku gracza. Przydaje się do wczytania gry.
     *
     * @param newScore ilość punktów do ustawienia
     */
    public static void setScore(int newScore) {
        score = newScore;
        scoreLabel.setText(String.format("%06d", score));
    }
}
