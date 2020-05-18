package gui.application;

import java.time.LocalDate;
import java.util.ArrayList;

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

/**
 * @author AAG
 *
 */
public class EtoATool extends Application
{
  private final static String VERSION = "0.6.2";

  private final static LocalDate EXPIRE_DATE = LocalDate.of(2020, 07, 01);

  private static final String ROUND = "round20";

  public static String getRound()
  {
    return ROUND;
  }

  private static Stage PRIMARY_STAGE = null;

  private final static String CSS_FOLDER = "/css/";

  public static String getCSSFolder()
  {
    return CSS_FOLDER;
  }

  private final static String DEFAULT_APPLICATION_CSS = "application.css";

  private final static String IMAGE_FOLDER = "/images/";

  private final static String GAME_ICON = "EtoA32.png";

  private final ArrayList<Label> statusBars = new ArrayList<>();

  public static String getGameIconPath()
  {
    return EtoATool.class.getResource(IMAGE_FOLDER + GAME_ICON).toExternalForm();
  }

  @Override
  public void start(final Stage primaryStage)
  {
    if (LocalDate.now().isAfter(EXPIRE_DATE))
    {
      final Alert alertDialog = Dialogs.buildAlert(AlertType.ERROR, "EtoA-Tool", "Time's up!", "Request new version!");
      alertDialog.showAndWait();
      Platform.exit();
      System.exit(0);
    }
    try
    {
      PRIMARY_STAGE = primaryStage;
      final BorderPane root = new BorderPane();
      final MenuBar rightBar = new MenuBar();
      final Menu menuInfo = new Menu("Info", null, new MenuItem("EtoA-Tool " + VERSION), new MenuItem("by AAG"), new MenuItem("https://github.com/A-A-G/EtoA-Tool"));
      rightBar.getMenus().addAll(menuInfo);
      final Region spacer = new Region();
      spacer.getStyleClass().add("menu-bar");
      HBox.setHgrow(spacer, Priority.SOMETIMES);
      final HBox menubars = new HBox(spacer, rightBar);
      root.setTop(menubars);
      getStatusBar(); // KBSim
      getStatusBar(); // Distances
      getStatusBar(); // Krypto
      getStatusBar(); // Rak
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
      final Tab attStatsTab = new AttStatisticsTab(planets, ships, defences, statusBars.get(4));
      tabPane.getTabs().add(attStatsTab);
      attStatsTab.setDisable(true);
      final Tab priceTab = new PriceTab(planets, ships, defences, statusBars.get(5));
      tabPane.getTabs().add(priceTab);
      priceTab.setDisable(true);
      tabPane.getTabs().add(new PlanetTab(planets, ships, defences, statusBars.get(6)));
      tabPane.getTabs().add(new ShipsTab(planets, ships, defences, statusBars.get(7)));
      tabPane.getTabs().add(new DefenceTab(planets, ships, defences, statusBars.get(8)));
      tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> root.setBottom(statusBars.get(newValue.intValue())));
      root.setBottom(statusBars.get(0));
      tabPane.getSelectionModel().select(0);
      root.setCenter(tabPane);
      final Scene scene = new Scene(root, 1400, 1000);
      scene.getStylesheets().add(getClass().getResource(getCSSFolder() + DEFAULT_APPLICATION_CSS).toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("EtoA-Tool");
      primaryStage.getIcons().add(new Image(getGameIconPath()));
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

  public static Stage getPrimaryStage()
  {
    return PRIMARY_STAGE;
  }

  public static void main(final String[] args)
  {
    launch(args);
  }
}
