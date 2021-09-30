package gui.settings;

import java.util.prefs.Preferences;

import gui.application.EtoATool;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import properties.AppProperties;

public class Settings
{
  // Keys
  public static final String ROUND = "round";
  public static final String DOMAIN = "domain";
  public static final String PROTOCOL = "encryption";

  // Defaults

  private static final String WINDOW_WIDTH = "settingswindow_width";
  private static final String WINDOW_HEIGHT = "settingswindow_height";
  private static final double DEFAULT_WIDTH = 1600;
  private static final double DEFAULT_HEIGHT = 1000;

  private final GridPane gridPane = new GridPane();

  public void showSettings()
  {

    final Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);

    addStringSettings("Runde", ROUND, EtoATool.getAppProperties().getProperty(AppProperties.ROUND), 0, 0);
    addStringSettings("Domain", DOMAIN, EtoATool.getAppProperties().getProperty(AppProperties.DOMAIN), 1, 0);
    addStringSettings("Protokoll", PROTOCOL, EtoATool.getAppProperties().getProperty(AppProperties.PROTOCOL), 2, 0);
    addIntegerSettings("", "", 0, 0, 0);
    addDoubleSettings("", "", 0, 0, 0);

    final Preferences preferences = Preferences.userNodeForPackage((new Settings()).getClass());
    final Scene scene = new Scene(gridPane, preferences.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH), preferences.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT));
    stage.setTitle("Settings");
    stage.setScene(scene);
    stage.setOnCloseRequest((final WindowEvent event) ->
    {
      preferences.putDouble(WINDOW_WIDTH, Math.max(scene.getWidth(), 100));
      preferences.putDouble(WINDOW_HEIGHT, Math.max(scene.getHeight(), 100));
    });
    stage.show();
  }

  private void addStringSettings(final String text, final String preferencesKey, final String defaultValue, final int row, final int column)
  {

  }

  private void addIntegerSettings(final String text, final String preferencesKey, final int defaultValue, final int row, final int column)
  {

  }

  private void addDoubleSettings(final String text, final String preferencesKey, final double defaultValue, final int row, final int column)
  {

  }
}
