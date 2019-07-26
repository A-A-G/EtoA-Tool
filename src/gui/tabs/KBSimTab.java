/**
 * 
 */
package gui.tabs;

import java.util.prefs.Preferences;

import data.PlayerValues;
import data.SpyReport;
import data.defence.Defence;
import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ship;
import data.ships.Ships;
import gui.utils.ShipAndDefenceSelector;
import gui.utils.Spinners;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import logic.FightSimulation;

/**
 * @author AAG
 *
 */
public class KBSimTab extends EtoATab
{
  private final static String TAB_NAME = "Kampfsimulator";

  private final static String WEAPONTECH = "weapontech";
  private final static String ARMORTECH = "armortech";
  private final static String SHIELDTECH = "shieldtech";
  private final static String REGENATECH = "regenatech";

  private final static String ATTACKER = "attacker";
  private final static String DEFENDER = "defender";

  // necessary scope! (GC)
  final PlayerValues defenderValues = new PlayerValues();
  final PlayerValues attackerValues = new PlayerValues();

  final PlayerSpinners attackerSpinners = new PlayerSpinners();
  final PlayerSpinners defenderSpinners = new PlayerSpinners();

  final SpyReport lastSpyReport = new SpyReport();

  final FightSimulation fightSimulation = new FightSimulation();

  public KBSimTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    attackerValues.setOtherPlayer(defenderValues);
    fightSimulation.setAttackerValues(attackerValues);
    fightSimulation.setDefenderValues(defenderValues);
    final ShipAndDefenceSelector<Ship> attackerShipSelector = new ShipAndDefenceSelector<>("Schiffe Angreifer", ships);
    attackerValues.setChoosenShips(attackerShipSelector.getChoosenItemsMap());
    attackerShipSelector.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(attackerShipSelector, Priority.ALWAYS);
    final HBox attackerHBox = new HBox(attackerShipSelector, getPlayerNode("Daten Angreifer", attackerValues, attackerSpinners, ATTACKER));
    attackerHBox.setAlignment(Pos.CENTER);
    attackerHBox.getStyleClass().add("spacing");
    final ShipAndDefenceSelector<Ship> defenderShipSelector = new ShipAndDefenceSelector<>("Schiffe Verteidiger", ships);
    defenderValues.setChoosenShips(defenderShipSelector.getChoosenItemsMap());
    defenderShipSelector.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(defenderShipSelector, Priority.ALWAYS);
    final ShipAndDefenceSelector<Defence> defenderDefenceSelector = new ShipAndDefenceSelector<>("Verteidigung Verteidiger", defences);
    defenderValues.setChoosenDefense(defenderDefenceSelector.getChoosenItemsMap());
    defenderDefenceSelector.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(defenderDefenceSelector, Priority.ALWAYS);
    final VBox defenderVBox = new VBox(defenderShipSelector, defenderDefenceSelector);
    defenderVBox.getStyleClass().add("spaceandbottompadding");
    defenderVBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(defenderVBox, Priority.ALWAYS);
    final HBox defenderHBox = new HBox(defenderVBox, getPlayerNode("Daten Verteidiger", defenderValues, defenderSpinners, DEFENDER));
    defenderHBox.setAlignment(Pos.CENTER);
    defenderHBox.getStyleClass().add("spacing");
    final VBox centerVBox = new VBox(attackerHBox, new Separator(), defenderHBox);
    centerVBox.getStyleClass().add("elementstyle");
    centerVBox.setAlignment(Pos.CENTER);
    final TextArea copyArea = new TextArea();
    copyArea.setPromptText("Copy & Paste Spionagebericht!");
    VBox.setVgrow(copyArea, Priority.ALWAYS);
    final Button addButton = new Button("Analyse");
    addButton.setOnAction(actionEvent ->
    {
      lastSpyReport.analyseSpyReport(copyArea.getText(), this);
      defenderShipSelector.update(lastSpyReport.getShipsMap());
      defenderDefenceSelector.update(lastSpyReport.getDefencesMap());
      defenderValues.updateFromSpyReport(lastSpyReport);
      fightSimulation.setSpyReport(lastSpyReport);
      copyArea.clear();
    });
    final HBox addButtonBox = new HBox(addButton);
    addButtonBox.setAlignment(Pos.CENTER_RIGHT);
    addButtonBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(addButtonBox, Priority.ALWAYS);
    final Button simulateButton = new Button("Simulate");
    simulateButton.setOnAction(actionEvent ->
    {
      fightSimulation.showReport();
    });
    final HBox simulateButtonBox = new HBox(simulateButton);
    simulateButtonBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(simulateButtonBox, Priority.ALWAYS);
    simulateButtonBox.setAlignment(Pos.CENTER_RIGHT);
    final VBox cnpVBox = new VBox(copyArea, new HBox(addButtonBox, simulateButtonBox));
    cnpVBox.setAlignment(Pos.CENTER);
    final SplitPane kbSimSplit = new SplitPane(cnpVBox, centerVBox);
    kbSimSplit.setDividerPositions(0.3f);
    setContent(kbSimSplit);
  }

  private Node getPlayerNode(final String header, final PlayerValues playerValues, final PlayerSpinners playerSpinners, final String styleClass)
  {
    final Preferences preferences = Preferences.userNodeForPackage(getClass());
    if (styleClass.equals(ATTACKER))
    {
      playerValues.weapontechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(WEAPONTECH, newValue.intValue()));
      playerValues.armortechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(ARMORTECH, newValue.intValue()));
      playerValues.shieldtechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(SHIELDTECH, newValue.intValue()));
      playerValues.regenatechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(REGENATECH, newValue.intValue()));
    }

    final GridPane gridPaneBonis = new GridPane();
    gridPaneBonis.add(new Label("Waffentechnik % "), 0, 0);
    gridPaneBonis.add(Spinners.getPlayerTechSpinner(styleClass.equals(ATTACKER) ? preferences.getInt(WEAPONTECH, PlayerValues.DEFAULT_TECH) : PlayerValues.DEFAULT_TECH, playerValues.weapontechProperty()), 1, 0);
    gridPaneBonis.add(new Label("Panzerung % "), 0, 1);
    gridPaneBonis.add(Spinners.getPlayerTechSpinner(styleClass.equals(ATTACKER) ? preferences.getInt(ARMORTECH, PlayerValues.DEFAULT_TECH) : PlayerValues.DEFAULT_TECH, playerValues.armortechProperty()), 1, 1);
    gridPaneBonis.add(new Label("Schutzschilder % "), 0, 2);
    gridPaneBonis.add(Spinners.getPlayerTechSpinner(styleClass.equals(ATTACKER) ? preferences.getInt(SHIELDTECH, PlayerValues.DEFAULT_TECH) : PlayerValues.DEFAULT_TECH, playerValues.shieldtechProperty()), 1, 2);
    gridPaneBonis.add(new Label("Regenatechnik % "), 0, 3);
    gridPaneBonis.add(Spinners.getPlayerTechSpinner(styleClass.equals(ATTACKER) ? preferences.getInt(REGENATECH, PlayerValues.DEFAULT_TECH) : PlayerValues.DEFAULT_TECH, playerValues.regenatechProperty()), 1, 3);
    if (styleClass.equals(DEFENDER))
    {
      gridPaneBonis.add(new Label("Reparatur % "), 0, 4);
      Spinner<Integer> repSpinner = Spinners.getRepairSpinner(playerValues.repairProperty());
      playerValues.repairProperty().addListener((obs, oldVal, newVal) -> attackerSpinners.updateSpinners(attackerValues, defenderSpinners));
      gridPaneBonis.add(repSpinner, 1, 4);
    }
    final TitledPane bonisTP = new TitledPane("Bonis", new Group(gridPaneBonis));
    bonisTP.setAlignment(Pos.CENTER);
    bonisTP.getStyleClass().add("toppadding");
    bonisTP.setCollapsible(false);

    final GridPane gridPaneValues = new GridPane();
    gridPaneValues.add(new Label("Waffen: "), 0, 0);
    playerSpinners.weapons = Spinners.getPlayerValueSpinner(playerValues.weaponsProperty());
    playerSpinners.weapons.valueProperty().addListener((obs, oldVal, newVal) -> attackerSpinners.updateSpinners(attackerValues, defenderSpinners));
    gridPaneValues.add(playerSpinners.weapons, 1, 0);
    gridPaneValues.add(new Label("Struktur: "), 0, 1);
    playerSpinners.structure = Spinners.getPlayerValueSpinner(playerValues.structureProperty());
    playerSpinners.structure.valueProperty().addListener((obs, oldVal, newVal) -> attackerSpinners.updateSpinners(attackerValues, defenderSpinners));
    gridPaneValues.add(playerSpinners.structure, 1, 1);
    gridPaneValues.add(new Label("Schild: "), 0, 2);
    playerSpinners.shield = Spinners.getPlayerValueSpinner(playerValues.shieldProperty());
    playerSpinners.shield.valueProperty().addListener((obs, oldVal, newVal) -> attackerSpinners.updateSpinners(attackerValues, defenderSpinners));
    gridPaneValues.add(playerSpinners.shield, 1, 2);
    gridPaneValues.add(new Label("Heilung: "), 0, 3);
    playerSpinners.heal = Spinners.getPlayerValueSpinner(playerValues.healProperty());
    playerSpinners.heal.valueProperty().addListener((obs, oldVal, newVal) -> attackerSpinners.updateSpinners(attackerValues, defenderSpinners));
    gridPaneValues.add(playerSpinners.heal, 1, 3);
    final TitledPane valuesTP = new TitledPane("Werte", new Group(gridPaneValues));
    valuesTP.setAlignment(Pos.CENTER);
    valuesTP.getStyleClass().add("toppadding");
    valuesTP.setCollapsible(false);

    final VBox playerValuesVBox = new VBox(bonisTP, valuesTP);
    playerValuesVBox.getStyleClass().add("nospaceandpaddding");
    if (styleClass.equals(ATTACKER))
    {
      final String sumString = "Summe";
      final String diffString = "Differenz";
      final Button toggleButton = new Button(sumString);
      final HBox buttonHBox = new HBox(toggleButton);
      buttonHBox.setAlignment(Pos.CENTER);
      buttonHBox.getStyleClass().add("padding");
      playerValuesVBox.getChildren().add(buttonHBox);
      toggleButton.setOnAction(e ->
      {
        if (toggleButton.getText().equals(sumString))
        {
          toggleButton.setText(diffString);
          playerValues.setShowDifference(true);
        }
        else
        {
          toggleButton.setText(sumString);
          playerValues.setShowDifference(false);
        }
      });
    }
    final TitledPane playerTP = new TitledPane(header, playerValuesVBox);
    playerTP.getStyleClass().add(styleClass);
    playerTP.setCollapsible(false);
    return new Group(playerTP);
  }

  private class PlayerSpinners
  {
    private final static String GREEN_SPINNER = "greenspinner";
    private final static String RED_SPINNER = "redspinner";

    public Spinner<Double> weapons;
    public Spinner<Double> structure;
    public Spinner<Double> shield;
    public Spinner<Double> heal;

    public void updateSpinners(final PlayerValues baseValues, final PlayerSpinners otherSpinners)
    {
      fightSimulation.SimulateFight();
      PlayerValues plainValues = new PlayerValues(baseValues);
      if (plainValues.weaponsProperty().getValue() >= (otherSpinners.shield.getValue() + otherSpinners.structure.getValue()))
      {
        weapons.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
        addStyle(weapons, GREEN_SPINNER);
      }
      else if (fightSimulation.isAttackerWins())
      {
        weapons.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
        weapons.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
      }
      else
      {
        weapons.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
        addStyle(weapons, RED_SPINNER);
      }
      if (otherSpinners.weapons.getValue() >= (plainValues.shieldProperty().getValue() + plainValues.structureProperty().getValue()))
      {
        shield.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
        structure.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
        addStyle(shield, RED_SPINNER);
        addStyle(structure, RED_SPINNER);
      }
      else
      {
        shield.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
        structure.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
        addStyle(shield, GREEN_SPINNER);
        addStyle(structure, GREEN_SPINNER);
      }
      if (plainValues.healProperty().getValue() < (otherSpinners.weapons.getValue() * 0.9))
      {
        heal.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
        addStyle(heal, RED_SPINNER);
      }
      else
      {
        heal.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
        addStyle(heal, GREEN_SPINNER);
      }
    }

    private void addStyle(final Node node, final String style)
    {
      if (!node.getStyleClass().contains(style))
      {
        node.getStyleClass().add(style);
      }
    }
  }

}
