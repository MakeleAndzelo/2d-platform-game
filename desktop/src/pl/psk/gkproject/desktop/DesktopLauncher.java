package pl.psk.gkproject.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.psk.gkproject.PlatformGame;

/**
 * Główna klasa aplikacji dla dekstopów. Odpowiedzialna jest za
 * uruchomienie gry.
 */
public class DesktopLauncher {
	/**
	 * Metoda uruchamiająca grę na desktopach
	 *
	 * @param arg Argumenty przekazane do wejścia aplikacji
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PlatformGame(), config);
	}
}
