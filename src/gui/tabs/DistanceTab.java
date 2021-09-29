/**
 *
 */
package gui.tabs;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import data.defence.Defences;
import data.planets.Planet;
import data.planets.Planets;
import data.ships.Ships;
import gui.application.EtoATool;
import gui.utils.DistanceAndDurationBox;
import gui.utils.SpeedHBox;
import gui.utils.Spinners;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import logic.Distances;

/**
 * @author AAG
 *
 */
public class DistanceTab extends EtoATab
{
  private final static String TAB_NAME = "Entfernungen";

  private final static String DISTANCE_TAB_CSS = "distancetab.css";

  private final static int RAK_RANGE = 3000;

  private final static String KRYPTO_LEVEL = "Krypto-Level";

  public DistanceTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    final Label p1Label = new Label("Spieler 1");
    p1Label.getStyleClass().add("player1");
    p1Label.setAlignment(Pos.CENTER);
    p1Label.setMaxWidth(Double.MAX_VALUE);
    VBox.setVgrow(p1Label, Priority.ALWAYS);
    final ListView<String> player1View = getPlayerListView();
    VBox.setVgrow(player1View, Priority.ALWAYS);
    final VBox player1VBox = new VBox(p1Label, player1View);
    player1VBox.setAlignment(Pos.CENTER);
    final Spinner<Integer> kryptoLevelSpinner = Spinners.getSpinner(0, 10, 0, 1, 60, true, null);
    final Spinner<Integer> kryptoRangeSpinner = Spinners.getSpinner(0, 7000, 0, 700, 80, true, null);
    kryptoLevelSpinner.valueProperty().addListener((obs, oldValue, newValue) -> kryptoRangeSpinner.getValueFactory().setValue(newValue * 700));
    final Preferences preferences = Preferences.userNodeForPackage(getClass());
    kryptoLevelSpinner.getValueFactory().setValue(preferences.getInt(KRYPTO_LEVEL, 0));
    kryptoLevelSpinner.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(KRYPTO_LEVEL, newValue));
    final HBox kryptoBox = new HBox(new Label("Krypto-Level:"), kryptoLevelSpinner, new Label("Krypto-Range:"), kryptoRangeSpinner);
    kryptoBox.getStyleClass().add("spacing");
    kryptoBox.setAlignment(Pos.CENTER);
    final ToggleButton distanceTB = new ToggleButton("Distanz");
    distanceTB.setSelected(true);
    distanceTB.getStyleClass().add("distance");
    final ToggleButton timeTB = new ToggleButton("Zeit");
    timeTB.getStyleClass().add("time");
    distanceTB.selectedProperty().addListener((obs, oldValue, newValue) ->
    {
      if (!distanceTB.isSelected())
      {
        timeTB.selectedProperty().set(true);
      }
      else
      {
        timeTB.selectedProperty().set(false);
      }
    });
    timeTB.selectedProperty().addListener((obs, oldValue, newValue) ->
    {
      if (!timeTB.isSelected())
      {
        distanceTB.selectedProperty().set(true);
      }
      else
      {
        distanceTB.selectedProperty().set(false);
      }
    });
    final SpeedHBox speedBox = new SpeedHBox();
    speedBox.visibleProperty().bind(timeTB.selectedProperty());
    final HBox tableButtonsBox = new HBox(distanceTB, timeTB, speedBox);
    tableButtonsBox.setAlignment(Pos.CENTER_LEFT);
    tableButtonsBox.getStyleClass().add("smallspacing");
    final GridPane distanceGP = new GridPane();
    distanceGP.setAlignment(Pos.CENTER);
    distanceGP.setVgap(0);
    final Label kryptoRangeLabelRangeLabel = new Label("Krypto");
    kryptoRangeLabelRangeLabel.getStyleClass().add("krypto");
    kryptoRangeLabelRangeLabel.getStyleClass().add("minbyplanet");
    final Label rakRangeLabel = new Label("Raketen");
    rakRangeLabel.getStyleClass().add("rakrange");
    rakRangeLabel.getStyleClass().add("minbyplanet");
    final HBox colorLegend = new HBox(kryptoRangeLabelRangeLabel, rakRangeLabel);
    colorLegend.getStyleClass().add("spacing");
    colorLegend.setAlignment(Pos.CENTER);
    final VBox tableVBox = new VBox(distanceGP, new Separator(), colorLegend);
    tableVBox.setMaxHeight(Double.MAX_VALUE);
    VBox.setVgrow(tableVBox, Priority.ALWAYS);
    tableVBox.setAlignment(Pos.CENTER);
    final VBox centerVBox = new VBox(kryptoBox, tableButtonsBox, tableVBox);
    centerVBox.setVisible(false);
    centerVBox.setMaxHeight(Double.MAX_VALUE);
    VBox.setVgrow(centerVBox, Priority.ALWAYS);
    final Label p2Label = new Label("Spieler 2");
    p2Label.getStyleClass().add("player2");
    p2Label.setAlignment(Pos.CENTER);
    p2Label.setMaxWidth(Double.MAX_VALUE);
    VBox.setVgrow(p2Label, Priority.ALWAYS);
    final ListView<String> player2View = getPlayerListView();
    final VBox player2VBox = new VBox(p2Label, player2View);
    player2VBox.setAlignment(Pos.CENTER);
    VBox.setVgrow(player2View, Priority.ALWAYS);
    final SplitPane twoPlayerCompare = new SplitPane(player1VBox, centerVBox, player2VBox);
    VBox.setVgrow(twoPlayerCompare, Priority.ALWAYS);
    twoPlayerCompare.setDividerPositions(0.15f, 0.85f);
    final VBox distanceAndDurationBox = new DistanceAndDurationBox();
    final VBox distanceTabVBox = new VBox(new Group(distanceAndDurationBox), twoPlayerCompare);
    distanceTabVBox.getStylesheets().add(getClass().getResource(EtoATool.getCSSFolder() + DISTANCE_TAB_CSS).toExternalForm());
    distanceTabVBox.setAlignment(Pos.CENTER);
    distanceAndDurationBox.getStyleClass().add("colored");
    setContent(distanceTabVBox);
    player1View.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateDistanceTable(distanceGP, player1View, player2View, centerVBox, distanceTB.selectedProperty().get(), speedBox, kryptoRangeSpinner.getValue()));
    player2View.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateDistanceTable(distanceGP, player1View, player2View, centerVBox, distanceTB.selectedProperty().get(), speedBox, kryptoRangeSpinner.getValue()));
    speedBox.speedProperty().addListener((obs, oldValue, newValue) -> updateDistanceTable(distanceGP, player1View, player2View, centerVBox, distanceTB.selectedProperty().get(), speedBox, kryptoRangeSpinner.getValue()));
    speedBox.startLandProperty().addListener((obs, oldValue, newValue) -> updateDistanceTable(distanceGP, player1View, player2View, centerVBox, distanceTB.selectedProperty().get(), speedBox, kryptoRangeSpinner.getValue()));
    distanceTB.selectedProperty().addListener((obs, oldValue, newValue) -> updateDistanceTable(distanceGP, player1View, player2View, centerVBox, distanceTB.selectedProperty().get(), speedBox, kryptoRangeSpinner.getValue()));
    kryptoRangeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateDistanceTable(distanceGP, player1View, player2View, centerVBox, distanceTB.selectedProperty().get(), speedBox, kryptoRangeSpinner.getValue()));
  }

  private void updateDistanceTable(final GridPane distanceGP, final ListView<String> player1View, final ListView<String> player2View, final VBox centerVBox, final boolean isDistance, final SpeedHBox speedBox, final int kryptoRange)
  {
    centerVBox.setVisible(false);
    distanceGP.getChildren().clear();
    distanceGP.getColumnConstraints().clear();
    if (player1View.getSelectionModel().getSelectedItems().isEmpty() || player2View.getSelectionModel().getSelectedItems().isEmpty())
    {
      return;
    }
    final FilteredList<Planet> planets2filter1Temp = new FilteredList<>(FXCollections.observableArrayList(planets.getData()), p -> true);
    final String player1 = player1View.getSelectionModel().getSelectedItem();
    planets2filter1Temp.setPredicate(planet -> planet.getOwner().equals(player1));
    final SortedList<Planet> planets2filter1 = planets2filter1Temp.sorted((p1, p2) -> p1.nameProperty().get().compareToIgnoreCase(p2.nameProperty().get()));
    if (planets2filter1.isEmpty())
    {
      planetError(player1);
      return;
    }
    final FilteredList<Planet> planets2filter2Temp = new FilteredList<>(FXCollections.observableArrayList(planets.getData()), p -> true);
    final String player2 = player2View.getSelectionModel().getSelectedItem();
    planets2filter2Temp.setPredicate(planet -> planet.getOwner().equals(player2));
    final SortedList<Planet> planets2filter2 = planets2filter2Temp.sorted((p1, p2) -> p1.nameProperty().get().compareToIgnoreCase(p2.nameProperty().get()));
    if (planets2filter2.isEmpty())
    {
      planetError(player2);
      return;
    }
    int p1Counter = 1;
    final ArrayList<Long> minDistancesPerPlanet = new ArrayList<>();
    final ArrayList<Label> minDistancesPerPlanetLabels = new ArrayList<>();
    for (final Planet p1 : planets2filter1)
    {
      if (p1Counter == 1)
      {
        final ColumnConstraints columnConst = new ColumnConstraints();
        columnConst.setPercentWidth(95d / (planets2filter2.size() + 1));
        distanceGP.getColumnConstraints().add(columnConst);
      }
      distanceGP.add(getLabel(p1, FrameType.getFrameTypeP1(p1Counter, planets2filter1.size()), "player1"), 0, p1Counter);
      int p2Counter = 1;
      for (final Planet p2 : planets2filter2)
      {
        if (p1Counter == 1)
        {
          distanceGP.add(getLabel(p2, FrameType.getFrameTypeP2(p2Counter, planets2filter2.size()), "player2"), p2Counter, 0);
          final ColumnConstraints columnConst = new ColumnConstraints();
          columnConst.setPercentWidth(95d / planets2filter2.size());
          distanceGP.getColumnConstraints().add(columnConst);
        }
        final double distanceD = Distances.getDistance(p1, p2);
        final long distance = Math.round(distanceD);
        final Label distanceLabel = new Label();
        if (isDistance)
        {
          distanceLabel.setText(String.valueOf(distance));
        }
        else
        {
          distanceLabel.setText(Distances.getDurationHMS(distanceD, speedBox.getSpeed(), speedBox.getStartLand()));
        }
        distanceLabel.setAlignment(Pos.CENTER);
        distanceLabel.getStyleClass().add("smallfont");
        distanceLabel.getStyleClass().add("tablelable");
        distanceLabel.setMaxWidth(Double.MAX_VALUE);
        distanceLabel.setMaxHeight(Double.MAX_VALUE);
        GridPane.setHgrow(distanceLabel, Priority.ALWAYS);
        GridPane.setVgrow(distanceLabel, Priority.ALWAYS);
        if ((distance < RAK_RANGE) && (distance < kryptoRange))
        {
          distanceLabel.getStyleClass().add("rakandkrypto");
        }
        else if (distance < RAK_RANGE)
        {
          distanceLabel.getStyleClass().add("rakrange");
        }
        else if (distance < kryptoRange)
        {
          distanceLabel.getStyleClass().add("krypto");
        }
        GridPane.setHalignment(distanceLabel, HPos.CENTER);
        distanceGP.add(distanceLabel, p2Counter, p1Counter);
        if ((p1Counter == planets2filter1.size()) && (p2Counter == planets2filter2.size()))
        {
          distanceLabel.getStyleClass().add("bottomright");
        }
        else if (p1Counter == planets2filter1.size())
        {
          distanceLabel.getStyleClass().add("bottom");
        }
        else if (p2Counter == planets2filter2.size())
        {
          distanceLabel.getStyleClass().add("right");
        }
        if (minDistancesPerPlanet.size() < p2Counter)
        {
          minDistancesPerPlanet.add(distance);
          minDistancesPerPlanetLabels.add(distanceLabel);
        }
        else if (distance < minDistancesPerPlanet.get(p2Counter - 1))
        {
          minDistancesPerPlanet.set(p2Counter - 1, distance);
          minDistancesPerPlanetLabels.set(p2Counter - 1, distanceLabel);
        }
        p2Counter++;
      }
      p1Counter++;
    }
    for (final Label distLabel : minDistancesPerPlanetLabels)
    {
      distLabel.getStyleClass().add("minbyplanet");
    }
    centerVBox.setVisible(true);
  }

  private Node getLabel(final Planet p, final FrameType fType, final String playerStyle)
  {
    final VBox labelBox = new VBox();
    labelBox.getStyleClass().add(fType.cssString());
    labelBox.setAlignment(Pos.CENTER);
    labelBox.setPadding(new Insets(3, 0, 3, 0));
    final Label smallNameLabel = new Label(p.nameProperty().get());
    smallNameLabel.setAlignment(Pos.CENTER);
    smallNameLabel.getStyleClass().add("smallfont");
    smallNameLabel.getStyleClass().add(playerStyle);
    smallNameLabel.setMaxWidth(Double.MAX_VALUE);
    smallNameLabel.setMaxHeight(Double.MAX_VALUE);
    VBox.setVgrow(smallNameLabel, Priority.ALWAYS);
    labelBox.getChildren().add(smallNameLabel);
    final Label smallCoordsLabel = new Label(p.getCoords());
    smallCoordsLabel.setAlignment(Pos.CENTER);
    smallCoordsLabel.getStyleClass().add("tinyfont");
    smallCoordsLabel.getStyleClass().add(playerStyle);
    smallCoordsLabel.setMaxWidth(Double.MAX_VALUE);
    smallCoordsLabel.setMaxHeight(Double.MAX_VALUE);
    VBox.setVgrow(smallCoordsLabel, Priority.ALWAYS);
    labelBox.getChildren().add(smallCoordsLabel);
    labelBox.getStyleClass().add("nospaceandpaddding");
    return labelBox;
  }

  private void planetError(final String player)
  {
    final Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Player Error");
    alert.setHeaderText("No planet found!");
    alert.setContentText("Can't find a planet from player " + player + ".");
    updateStatus("Can't find a planet from player " + player + ".");
    alert.showAndWait();
  }

  public enum FrameType
  {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT,
    TOPLEFT;

    public static FrameType getFrameTypeP1(final int p1Counter, final int max)
    {
      if (p1Counter == 1)
      {
        return FrameType.TOPLEFT;
      }
      else if (p1Counter == max)
      {
        return FrameType.BOTTOM;
      }
      else
      {
        return FrameType.LEFT;
      }
    }

    public static FrameType getFrameTypeP2(final int p2Counter, final int max)
    {
      if (p2Counter == 1)
      {
        return FrameType.TOPLEFT;
      }
      else if (p2Counter == max)
      {
        return FrameType.RIGHT;
      }
      else
      {
        return FrameType.TOP;
      }
    }

    public String cssString()
    {
      switch (this)
      {
        case TOP:
        {
          return "topbox";
        }
        case RIGHT:
        {
          return "rightbox";
        }
        case BOTTOM:
        {
          return "bottombox";
        }
        case LEFT:
        {
          return "leftbox";
        }
        case TOPLEFT:
        {
          return "topleftbox";
        }
      }
      return "";
    }
  }

}
