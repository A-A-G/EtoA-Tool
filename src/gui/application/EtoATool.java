package gui.application;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.prefs.Preferences;

import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ships;
import gui.tabs.AttStatisticsTab;
import gui.tabs.DefenceTab;
import gui.tabs.DistanceTab;
import gui.tabs.KBSimTab;
import gui.tabs.KryptoTab;
import gui.tabs.PlanetTab;
import gui.tabs.PriceTab;
import gui.tabs.RakTab;
import gui.tabs.RigeliaTab;
import gui.tabs.ShipsTab;
import gui.utils.Dialogs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import properties.AppProperties;

/**
 * @author AAG
 *
 */
public class EtoATool extends Application
{
  private final static String VERSION = "0.7.2";

  private final static LocalDate EXPIRE_DATE = LocalDate.of(2022, 1, 1);

  private final static String APP_NAME = "EtoA-Tool";

  private static final Properties APP_PROPERTIES = new AppProperties(APP_NAME, VERSION, EXPIRE_DATE);

  public static Properties getAppProperties()
  {
    return APP_PROPERTIES;
  }

  public static String getCSSFolder()
  {
    return APP_PROPERTIES.getProperty(AppProperties.CSS_FOLDER);
  }

  public static String getGameIconPath()
  {
    return EtoATool.class.getResource(APP_PROPERTIES.getProperty(AppProperties.IMAGE_FOLDER) + APP_PROPERTIES.getProperty(AppProperties.GAME_ICON)).toExternalForm();
  }

  private static Stage PRIMARY_STAGE = null;

  public static Stage getPrimaryStage()
  {
    return PRIMARY_STAGE;
  }

  private static final String WINDOW_WIDTH = "mainwindow_width";
  private static final String WINDOW_HEIGHT = "mainWindow_height";
  private static final double DEFAULT_WIDTH = 1600;
  private static final double DEFAULT_HEIGHT = 1000;

  private final ArrayList<Label> statusBars = new ArrayList<>();

  @Override
  public void start(final Stage primaryStage)
  {
    if (LocalDate.now().isAfter(EXPIRE_DATE))
    {
      final Alert alertDialog = Dialogs.buildAlert(AlertType.ERROR, APP_NAME, "Time's up!", "Request new version!");
      alertDialog.showAndWait();
      Platform.exit();
      System.exit(0);
    }
    try
    {
      PRIMARY_STAGE = primaryStage;
      final BorderPane root = new BorderPane();
      final Region spacer = new Region();
      spacer.getStyleClass().add("menu-bar");
      HBox.setHgrow(spacer, Priority.SOMETIMES);
      final HBox menubars = new HBox(spacer, getRightBar());
      root.setTop(menubars);
      getStatusBar(); // KBSim
      getStatusBar(); // Distances
      getStatusBar(); // Krypto
      getStatusBar(); // Rak
      getStatusBar(); // Rigelia
      getStatusBar(); // AttStatistics
      getStatusBar(); // Prices
      final Planets planets = new Planets(getStatusBar()); // Planets
      final Ships ships = new Ships(getStatusBar()); // Ships
      final Defences defences = new Defences(getStatusBar()); // Defences
      final TabPane tabPane = new TabPane();
      tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
      tabPane.getTabs().add(new KBSimTab(planets, ships, defences, statusBars.get(0)));
      tabPane.getTabs().add(new DistanceTab(planets, ships, defences, statusBars.get(1)));
      tabPane.getTabs().add(new KryptoTab(planets, ships, defences, statusBars.get(2)));
      final Tab rakTab = new RakTab(planets, ships, defences, statusBars.get(3));
      tabPane.getTabs().add(rakTab);
      rakTab.setDisable(true);
      tabPane.getTabs().add(new RigeliaTab(planets, ships, defences, statusBars.get(4)));
      final Tab attStatsTab = new AttStatisticsTab(planets, ships, defences, statusBars.get(5));
      tabPane.getTabs().add(attStatsTab);
      attStatsTab.setDisable(true);
      final Tab priceTab = new PriceTab(planets, ships, defences, statusBars.get(6));
      tabPane.getTabs().add(priceTab);
      priceTab.setDisable(true);
      tabPane.getTabs().add(new PlanetTab(planets, ships, defences, statusBars.get(7)));
      tabPane.getTabs().add(new ShipsTab(planets, ships, defences, statusBars.get(8)));
      tabPane.getTabs().add(new DefenceTab(planets, ships, defences, statusBars.get(9)));
      tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> root.setBottom(statusBars.get(newValue.intValue())));
      root.setBottom(statusBars.get(0));
      tabPane.getSelectionModel().select(0);
      root.setCenter(tabPane);
      final Preferences preferences = Preferences.userNodeForPackage(getClass());
      final Scene scene = new Scene(root, preferences.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH), preferences.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT));
      scene.getStylesheets().add(getClass().getResource(getCSSFolder() + APP_PROPERTIES.getProperty(AppProperties.APPLICATION_CSS)).toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle(APP_NAME);
      primaryStage.getIcons().add(new Image(getGameIconPath()));
      primaryStage.setOnCloseRequest((final WindowEvent event) ->
      {
        preferences.putDouble(WINDOW_WIDTH, Math.max(scene.getWidth(), 100));
        preferences.putDouble(WINDOW_HEIGHT, Math.max(scene.getHeight(), 100));
      });
      primaryStage.show();
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
  }

  private Label getStatusBar()
  {
    final Label statusbar = new Label();
    statusbar.setPrefWidth(Double.MAX_VALUE);
    statusBars.add(statusbar);
    return statusbar;
  }

  private MenuBar getRightBar()
  {
    final MenuBar rightBar = new MenuBar();
    final MenuItem githubLink = new MenuItem(APP_PROPERTIES.getProperty(AppProperties.GITHUB_URL));
    githubLink.getStyleClass().add("blue-menu-item");
    githubLink.setOnAction(e ->
    {
      if (Desktop.isDesktopSupported())
      {
        try
        {
          Desktop.getDesktop().browse(new URI(APP_PROPERTIES.getProperty(AppProperties.GITHUB_URL)));
        }
        catch (final Exception ex)
        {
          ex.printStackTrace();
        }
      }
    });
    final MenuItem changelog = new MenuItem("Changelog");
    changelog.getStyleClass().add("blue-menu-item");
    changelog.setOnAction(e ->
    {
      if (Desktop.isDesktopSupported())
      {
        try
        {
          Desktop.getDesktop().open(new File(APP_PROPERTIES.getProperty(AppProperties.CHANGELOG_PATH)));
        }
        catch (final Exception ex)
        {
          ex.printStackTrace();
        }
      }
    });
    final Menu menuInfo = new Menu("Info", null, new MenuItem(APP_NAME + " " + VERSION + " by " + APP_PROPERTIES.getProperty(AppProperties.AUTHOR)), githubLink, changelog);
    rightBar.getMenus().addAll(menuInfo);
    return rightBar;
  }

  public static void main(final String[] args)
  {
    launch(args);
  }
}
