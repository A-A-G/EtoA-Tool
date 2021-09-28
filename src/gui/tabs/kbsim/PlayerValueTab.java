/**
 * 
 */
package gui.tabs.kbsim;

import java.util.prefs.Preferences;

import data.PlayerValues;
import data.ShipAndDefenceBase;
import data.defence.Defence;
import data.defence.Defences;
import data.ships.Ship;
import data.ships.Ships;
import gui.tabs.KBSimTab.MainSpinners;
import gui.utils.PlayerSpinners;
import gui.utils.ShipAndDefenceSelector;
import gui.utils.Spinners;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import logic.FightSimulation;
import properties.FightSimProperties;

/**
 * @author AAG
 *
 */
public class PlayerValueTab extends Tab
{

  private final static String WEAPONTECH = "weapontech";
  private final static String ARMORTECH = "armortech";
  private final static String SHIELDTECH = "shieldtech";
  private final static String REGENATECH = "regenatech";

  public final static String ATTACKER = "Angreifer";
  public final static String DEFENDER = "Verteidiger";

  private ObservableList<PlayerSpinners> attackerPlayerSpinnerList = null;

  private final Label statusLabel;

  private final MainSpinners spinners;

  public PlayerValueTab(final TabPane tabPane, final FightSimulation fightSimulation, final MainSpinners spinners, final String type, final Ships ships, final Defences defences, final Label statusLabel, final Node spinnerNode, final ObservableList<PlayerSpinners> attackerPlayerSpinnerList)
  {
    super(type + " " + (fightSimulation.getFightData().getCount(type) + 1));
    this.statusLabel = statusLabel;
    this.spinners = spinners;
    this.attackerPlayerSpinnerList = attackerPlayerSpinnerList;
    final ShipAndDefenceSelector<Ship> shipSelector = new ShipAndDefenceSelector<>("Schiffe " + type + " " + (fightSimulation.getFightData().getCount(type) + 1), ships);
    HBox.setHgrow(shipSelector, Priority.ALWAYS);
    shipSelector.setMaxWidth(Double.MAX_VALUE);
    final PlayerValues playerValues = new PlayerValues();
    fightSimulation.getFightData().addPlayerValues(playerValues, type);
    playerValues.setShipSelector(shipSelector);
    final VBox playerVBox = new VBox(shipSelector);
    playerVBox.getStyleClass().add("spaceandbottompadding");
    playerVBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(playerVBox, Priority.ALWAYS);
    if ((type.equals(DEFENDER)) && (fightSimulation.getFightData().getCount(type) == 1))
    {
      final ShipAndDefenceSelector<Defence> defenceSelector = new ShipAndDefenceSelector<>("Verteidigung Verteidiger", defences);
      playerValues.setDefenceSelector(defenceSelector);
      defenceSelector.setMaxWidth(Double.MAX_VALUE);
      HBox.setHgrow(defenceSelector, Priority.ALWAYS);
      playerVBox.getChildren().add(defenceSelector);
    }
    final Button addButton = new Button(type + " hinzufügen");
    addButton.setOnAction(e ->
    {
      spinnerNode.setVisible(true);
      spinnerNode.setManaged(true);
      new PlayerValueTab(tabPane, fightSimulation, spinners, type, ships, defences, statusLabel, spinnerNode, attackerPlayerSpinnerList);
    });
    addButton.getStyleClass().add("redbutton");
    final VBox dataButtonVBox = new VBox(getPlayerNode("Daten " + type + " " + (tabPane.getTabs().size() + 1), playerValues, type, fightSimulation, shipSelector), addButton);
    dataButtonVBox.setAlignment(Pos.CENTER);
    final HBox playerHBox = new HBox(playerVBox, dataButtonVBox);
    playerHBox.setAlignment(Pos.CENTER);
    playerHBox.getStyleClass().add("spacing");
    setContent(playerHBox);
    tabPane.getTabs().add(this);
  }

  private Node getPlayerNode(final String header, final PlayerValues playerValues, final String type, final FightSimulation fightSimulation, final ShipAndDefenceSelector<Ship> selector)
  {
    final Preferences preferences = Preferences.userNodeForPackage(getClass());
    if (type.equals(ATTACKER))
    {
      playerValues.weapontechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(WEAPONTECH, newValue.intValue()));
      playerValues.armortechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(ARMORTECH, newValue.intValue()));
      playerValues.shieldtechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(SHIELDTECH, newValue.intValue()));
      playerValues.regenatechProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(REGENATECH, newValue.intValue()));
    }
    final FightSimProperties properties = FightSimProperties.getInstance();
    final int defaultTech = (int) Math.round(properties.getTechBase());
    final GridPane gridPaneBonis = new GridPane();
    gridPaneBonis.add(new Label("Waffentechnik % "), 0, 0);
    final Spinner<Integer> weapontechSpinner = Spinners.getPlayerTechSpinner(type.equals(ATTACKER) ? preferences.getInt(WEAPONTECH, defaultTech) : defaultTech, playerValues.weapontechProperty());
    weapontechSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneBonis.add(weapontechSpinner, 1, 0);
    gridPaneBonis.add(new Label("Panzerung % "), 0, 1);
    final Spinner<Integer> armortechSpinner = Spinners.getPlayerTechSpinner(type.equals(ATTACKER) ? preferences.getInt(ARMORTECH, defaultTech) : defaultTech, playerValues.armortechProperty());
    armortechSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneBonis.add(armortechSpinner, 1, 1);
    gridPaneBonis.add(new Label("Schutzschilder % "), 0, 2);
    final Spinner<Integer> shieldtechSpinner = Spinners.getPlayerTechSpinner(type.equals(ATTACKER) ? preferences.getInt(SHIELDTECH, defaultTech) : defaultTech, playerValues.shieldtechProperty());
    shieldtechSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneBonis.add(shieldtechSpinner, 1, 2);
    gridPaneBonis.add(new Label("Regenatechnik % "), 0, 3);
    final Spinner<Integer> regenatechSpinner = Spinners.getPlayerTechSpinner(type.equals(ATTACKER) ? preferences.getInt(REGENATECH, defaultTech) : defaultTech, playerValues.regenatechProperty());
    regenatechSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneBonis.add(regenatechSpinner, 1, 3);
    if (type.equals(DEFENDER))
    {
      gridPaneBonis.add(new Label("Reparatur % "), 0, 4);
      final Spinner<Integer> repSpinner = Spinners.getRepairSpinner(playerValues.repairProperty());
      repSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
      playerValues.repairProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
      gridPaneBonis.add(repSpinner, 1, 4);
    }
    final TitledPane bonisTP = new TitledPane("Bonis", new Group(gridPaneBonis));
    bonisTP.setAlignment(Pos.CENTER);
    bonisTP.getStyleClass().add("toppadding");
    bonisTP.setCollapsible(false);
    final PlayerSpinners playerSpinners = new PlayerSpinners();
    if (type.equals(ATTACKER) && (attackerPlayerSpinnerList != null))
    {
      attackerPlayerSpinnerList.add(playerSpinners);
    }
    final GridPane gridPaneValues = new GridPane();
    gridPaneValues.add(new Label("Waffen: "), 0, 0);
    playerSpinners.weapons = Spinners.getPlayerValueSpinner(playerValues.weaponsProperty());
    playerSpinners.weapons.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneValues.add(playerSpinners.weapons, 1, 0);
    gridPaneValues.add(new Label("Struktur: "), 0, 1);
    playerSpinners.structure = Spinners.getPlayerValueSpinner(playerValues.structureProperty());
    playerSpinners.structure.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneValues.add(playerSpinners.structure, 1, 1);
    gridPaneValues.add(new Label("Schild: "), 0, 2);
    playerSpinners.shield = Spinners.getPlayerValueSpinner(playerValues.shieldProperty());
    playerSpinners.shield.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneValues.add(playerSpinners.shield, 1, 2);
    gridPaneValues.add(new Label("Heilung: "), 0, 3);
    playerSpinners.heal = Spinners.getPlayerValueSpinner(playerValues.healProperty());
    playerSpinners.heal.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
    gridPaneValues.add(playerSpinners.heal, 1, 3);
    if (type.equals(ATTACKER))
    {
      gridPaneValues.add(new Label("Laderaum: "), 0, 4);
      playerSpinners.capacity = Spinners.getPlayerValueSpinner(playerValues.capacityProperty());
      playerSpinners.capacity.valueProperty().addListener((obs, oldVal, newVal) -> updateSpinnerValues(fightSimulation));
      gridPaneValues.add(playerSpinners.capacity, 1, 4);
    }
    final TitledPane valuesTP = new TitledPane("Werte", new Group(gridPaneValues));
    valuesTP.setAlignment(Pos.CENTER);
    valuesTP.getStyleClass().add("toppadding");
    valuesTP.setCollapsible(false);
    final VBox playerValuesVBox = new VBox(bonisTP, valuesTP);
    playerValuesVBox.getStyleClass().add("spaceandpaddding");
    if (type.equals(ATTACKER))
    {
      final Button optimizeButton = new Button("Optimize");
      optimizeButton.setOnAction(e ->
      {
        if (selector != null)
        {
          final ObservableMap<ShipAndDefenceBase, Integer> selectedShips = selector.getChoosenItemsMap();
          if (selectedShips.size() == 1)
          {
            final double deffValues = fightSimulation.getFightData().getDefenderValues().structure + fightSimulation.getFightData().getDefenderValues().shield;
            final ShipAndDefenceBase selected = selectedShips.keySet().iterator().next();
            if (selected.weaponsProperty().doubleValue() > 0)
            {
              final long number = Math.round(Math.ceil(deffValues / ((selected.weaponsProperty().doubleValue() * playerValues.weapontechProperty().get()) / 100.0)));
              final ObservableMap<String, Integer> itemMap = FXCollections.observableHashMap();
              itemMap.put(selected.toString(), (int) number);
              selector.update(itemMap);
            }
            else
            {
              statusLabel.setText("Select ship with an attack value!");
            }
          }
          else
          {
            statusLabel.setText("Select exactly one Ship!");
          }
        }
      });
      final Button optimizeDifferenceButton = new Button("Optimize Difference");
      optimizeDifferenceButton.setOnAction(e ->
      {
        if (selector != null)
        {
          final ObservableMap<ShipAndDefenceBase, Integer> selectedShips = selector.getChoosenItemsMap();
          if (selectedShips.size() == 1)
          {
            final ShipAndDefenceBase selected = selectedShips.keySet().iterator().next();
            if (selected.weaponsProperty().doubleValue() > 0)
            {
              final ObservableMap<String, Integer> itemMap = FXCollections.observableHashMap();
              itemMap.put(selected.toString(), 0);
              selector.update(itemMap);
              final double deffValues = (fightSimulation.getFightData().getDefenderValues().structure + fightSimulation.getFightData().getDefenderValues().shield) - fightSimulation.getFightData().getAttackerValues().weapons;
              final long number = Math.round(Math.ceil(deffValues / ((selected.weaponsProperty().doubleValue() * playerValues.weapontechProperty().get()) / 100.0)));
              itemMap.put(selected.toString(), (int) number);
              selector.update(itemMap);
            }
            else
            {
              statusLabel.setText("Select ship with an attack value!");
            }
          }
          else
          {
            statusLabel.setText("Select exactly one Ship!");
          }
        }
      });
      final HBox buttonHBox = new HBox(optimizeButton, optimizeDifferenceButton);
      buttonHBox.setAlignment(Pos.CENTER);
      buttonHBox.getStyleClass().add("spaceandpaddding");
      playerValuesVBox.getChildren().add(buttonHBox);
    }
    final TitledPane playerTP = new TitledPane(header, playerValuesVBox);
    playerTP.getStyleClass().add(type);
    playerTP.setCollapsible(false);
    return new Group(playerTP);
  }

  private void updateSpinnerValues(final FightSimulation fightSimulation)
  {
    fightSimulation.SimulateFight(); // triggers value update
    fightSimulation.getFightData().updateMainSpinners(spinners);
    if (attackerPlayerSpinnerList != null)
    {
      for (final PlayerSpinners sp : attackerPlayerSpinnerList)
      {
        sp.updateSpinners(fightSimulation);
      }
    }
    spinners.attackerSpinners.updateSpinners(fightSimulation);
  }

}
