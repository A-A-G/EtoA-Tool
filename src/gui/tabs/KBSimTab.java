/**
 * 
 */
package gui.tabs;

import data.FightData;
import data.PlayerValues;
import data.SpyReport;
import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ships;
import gui.tabs.kbsim.PlayerValueTab;
import gui.utils.PlayerSpinners;
import gui.utils.Spinners;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import logic.FightSimulation;

/**
 * @author AAG
 *
 */
public class KBSimTab extends EtoATab
{
  private final static String TAB_NAME = "Kampfsimulator";

  final SpyReport lastSpyReport = new SpyReport();
  final FightData fightData = new FightData();
  final FightSimulation fightSimulation = new FightSimulation(fightData);

  public static class MainSpinners
  {
    public Spinner<Integer> attackerWeaponTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public Spinner<Integer> attackerArmorTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public Spinner<Integer> attackerShieldTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public Spinner<Integer> attackerRegenaTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public final PlayerSpinners attackerSpinners = new PlayerSpinners();
    public Spinner<Integer> defenderWeaponTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public Spinner<Integer> defenderArmorTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public Spinner<Integer> defenderShieldTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public Spinner<Integer> defenderRegenaTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public Spinner<Integer> defenderRepairTechSpinner = Spinners.getPlayerTechSpinner(100, null);
    public final PlayerSpinners defenderSpinners = new PlayerSpinners();
    public boolean showSum = true;
  }

  MainSpinners spinners = new MainSpinners();

  public KBSimTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    final TabPane attackersTabPane = new TabPane();
    attackersTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE); // TODO: handle tab close
    HBox.setHgrow(attackersTabPane, Priority.ALWAYS);
    final Node attackerSpinnerNode = getSpinnerNode(spinners.attackerWeaponTechSpinner, spinners.attackerArmorTechSpinner, spinners.attackerShieldTechSpinner, spinners.attackerRegenaTechSpinner, spinners.attackerSpinners, PlayerValueTab.ATTACKER);
    attackerSpinnerNode.setVisible(false);
    attackerSpinnerNode.setManaged(false);
    new PlayerValueTab(attackersTabPane, fightData, spinners, PlayerValueTab.ATTACKER, ships, defences, statusLabel, attackerSpinnerNode);
    final HBox backgroundASN = new HBox(attackerSpinnerNode);
    backgroundASN.getStyleClass().add("spaceandpaddding");
    backgroundASN.setAlignment(Pos.CENTER);
    backgroundASN.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    final HBox attackersHBox = new HBox(attackersTabPane, backgroundASN);
    attackersHBox.setAlignment(Pos.CENTER);
    final TabPane defendersTabPane = new TabPane();
    defendersTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    HBox.setHgrow(defendersTabPane, Priority.ALWAYS);
    final Node defenderSpinnerNode = getSpinnerNode(spinners.defenderWeaponTechSpinner, spinners.defenderArmorTechSpinner, spinners.defenderShieldTechSpinner, spinners.defenderRegenaTechSpinner, spinners.defenderSpinners, PlayerValueTab.DEFENDER);
    defenderSpinnerNode.setVisible(false);
    defenderSpinnerNode.setManaged(false);
    new PlayerValueTab(defendersTabPane, fightData, spinners, PlayerValueTab.DEFENDER, ships, defences, statusLabel, defenderSpinnerNode);
    final HBox backgroundDSN = new HBox(defenderSpinnerNode);
    backgroundDSN.getStyleClass().add("spaceandpaddding");
    backgroundDSN.setAlignment(Pos.CENTER);
    backgroundDSN.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    final HBox defendersHBox = new HBox(defendersTabPane, backgroundDSN);
    defendersHBox.setAlignment(Pos.CENTER);
    final SplitPane playersSplit = new SplitPane(attackersHBox, defendersHBox);
    playersSplit.setOrientation(Orientation.VERTICAL);
    playersSplit.setDividerPositions(0.5f);
    playersSplit.getStyleClass().add("elementstyle");
    final TextArea copyArea = new TextArea();
    copyArea.setPromptText("Copy & Paste Spionagebericht!");
    VBox.setVgrow(copyArea, Priority.ALWAYS);
    final Button addButton = new Button("Analyse");
    addButton.setOnAction(actionEvent ->
    {
      lastSpyReport.analyseSpyReport(copyArea.getText(), this);
      if (fightData.getDefenderCount() > 0)
      {
        final PlayerValues defenderValues = fightData.getDefenderList().get(0);
        defenderValues.getShipSelector().update(lastSpyReport.getShipsMap());
        defenderValues.getDefenceSelector().update(lastSpyReport.getDefencesMap());
        defenderValues.updateFromSpyReport(lastSpyReport);
      }
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
      fightSimulation.SimulateFight();
      fightSimulation.showReport();
    });
    final HBox simulateButtonBox = new HBox(simulateButton);
    simulateButtonBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(simulateButtonBox, Priority.ALWAYS);
    simulateButtonBox.setAlignment(Pos.CENTER_RIGHT);
    final VBox cnpVBox = new VBox(copyArea, new HBox(addButtonBox, simulateButtonBox));
    cnpVBox.setAlignment(Pos.CENTER);
    final SplitPane kbSimSplit = new SplitPane(cnpVBox, playersSplit);
    kbSimSplit.setDividerPositions(0.3f);
    setContent(kbSimSplit);
  }

  private Node getSpinnerNode(final Spinner<Integer> weaponTechSpinner, final Spinner<Integer> armorTechSpinner, final Spinner<Integer> shieldTechSpinner, final Spinner<Integer> regenaTechSpinner, final PlayerSpinners playerSpinners, final String type)
  {
    final GridPane gridPaneBonis = new GridPane();
    gridPaneBonis.add(new Label("Waffentechnik % "), 0, 0);
    gridPaneBonis.add(weaponTechSpinner, 1, 0);
    gridPaneBonis.add(new Label("Panzerung % "), 0, 1);
    gridPaneBonis.add(armorTechSpinner, 1, 1);
    gridPaneBonis.add(new Label("Schutzschilder % "), 0, 2);
    gridPaneBonis.add(shieldTechSpinner, 1, 2);
    gridPaneBonis.add(new Label("Regenatechnik % "), 0, 3);
    gridPaneBonis.add(regenaTechSpinner, 1, 3);
    if (type.equals(PlayerValueTab.DEFENDER))
    {
      gridPaneBonis.add(new Label("Reparatur % "), 0, 4);
      gridPaneBonis.add(spinners.defenderRepairTechSpinner, 1, 4);
    }
    final TitledPane bonisTP = new TitledPane("Bonis", new Group(gridPaneBonis));
    bonisTP.setAlignment(Pos.CENTER);
    bonisTP.getStyleClass().add("toppadding");
    bonisTP.setCollapsible(false);
    final GridPane gridPaneValues = new GridPane();
    gridPaneValues.add(new Label("Waffen: "), 0, 0);
    gridPaneValues.add(playerSpinners.weapons, 1, 0);
    gridPaneValues.add(new Label("Struktur: "), 0, 1);
    gridPaneValues.add(playerSpinners.structure, 1, 1);
    gridPaneValues.add(new Label("Schild: "), 0, 2);
    gridPaneValues.add(playerSpinners.shield, 1, 2);
    gridPaneValues.add(new Label("Heilung: "), 0, 3);
    gridPaneValues.add(playerSpinners.heal, 1, 3);
    final TitledPane valuesTP = new TitledPane("Werte", new Group(gridPaneValues));
    valuesTP.setAlignment(Pos.CENTER);
    valuesTP.getStyleClass().add("toppadding");
    valuesTP.setCollapsible(false);
    final VBox playerValuesVBox = new VBox(bonisTP, valuesTP);
    playerValuesVBox.getStyleClass().add("spaceandpaddding");
    if (type.equals(PlayerValueTab.ATTACKER))
    {
      final String sumString = "Summe";
      final String diffString = "Differenz";
      final Button toggleButton = new Button(sumString);
      toggleButton.setOnAction(e ->
      {
        if (toggleButton.getText().equals(sumString))
        {
          toggleButton.setText(diffString);
          spinners.showSum = false;
        }
        else
        {
          toggleButton.setText(sumString);
          spinners.showSum = true;
        }
        fightData.updateMainSpinners(spinners);
      });
      final HBox buttonHBox = new HBox(toggleButton);
      buttonHBox.setAlignment(Pos.CENTER);
      buttonHBox.getStyleClass().add("spaceandpaddding");
      playerValuesVBox.getChildren().add(buttonHBox);
    }
    final TitledPane playerTP = new TitledPane(type, playerValuesVBox);
    playerTP.getStyleClass().add(type);
    playerTP.setCollapsible(false);
    return new Group(playerTP);
  }

}
