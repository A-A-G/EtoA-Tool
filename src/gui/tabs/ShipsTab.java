/**
 *
 */
package gui.tabs;

import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ship;
import data.ships.Ships;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

/**
 * @author AAG
 *
 */
public class ShipsTab extends EtoATab
{
  private final static String TAB_NAME = "Schiffe";

  public ShipsTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    final TextField searchField = new TextField();
    final FilteredList<Ship> filteredShips = new FilteredList<>(ships.getData(), p -> true);
    final TableView<Ship> shipsTable = getShipsTableView(filteredShips);
    final CheckBox civilShipsCB = new CheckBox(Ships.CIVIL_SHIPS);
    civilShipsCB.setIndeterminate(false);
    civilShipsCB.selectedProperty().set(true);
    final CheckBox warShipsCB = new CheckBox(Ships.WAR_SHIPS);
    warShipsCB.setIndeterminate(false);
    warShipsCB.selectedProperty().set(true);
    final CheckBox civilRaceShipsCB = new CheckBox(Ships.CIVIL_RACE_SHIPS);
    civilRaceShipsCB.setIndeterminate(false);
    civilRaceShipsCB.selectedProperty().set(true);
    final CheckBox raceShipsCB = new CheckBox(Ships.RACE_SHIPS);
    raceShipsCB.setIndeterminate(false);
    raceShipsCB.selectedProperty().set(true);
    final CheckBox specialShipsCB = new CheckBox(Ships.SPECIAL_SHIPS);
    specialShipsCB.setIndeterminate(false);
    specialShipsCB.selectedProperty().set(true);
    final CheckBox collectShipsCB = new CheckBox(Ships.COLLECT_SHIPS);
    collectShipsCB.setIndeterminate(false);
    collectShipsCB.selectedProperty().set(true);
    final HBox filterHBox = new HBox(civilShipsCB, warShipsCB, civilRaceShipsCB, raceShipsCB, specialShipsCB, collectShipsCB);
    filterHBox.getStyleClass().add("spacing");
    filterHBox.setAlignment(Pos.CENTER);
    final VBox shipsVBox = new VBox(getSearchHBox(searchField), filterHBox, shipsTable, getAddLoadSaveHBox("Schiff", ships));
    shipsVBox.setAlignment(Pos.CENTER);
    final SplitPane shipsSplit = new SplitPane(shipsVBox, getCnPDataHBox("Copy & Paste Schiffsdaten!" + System.lineSeparator() + System.lineSeparator() + "Currently supported formats: Google Chrome", ships));
    shipsSplit.setDividerPositions(0.75f);
    setContent(shipsSplit);
    searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredShips.setPredicate(ship -> filterShipsTable(ship, newValue, civilShipsCB.isSelected(), warShipsCB.isSelected(), civilRaceShipsCB.isSelected(), raceShipsCB.isSelected(), specialShipsCB.isSelected(), collectShipsCB.isSelected())));
    civilShipsCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredShips.setPredicate(ship -> filterShipsTable(ship, searchField.getText(), newValue, warShipsCB.isSelected(), civilRaceShipsCB.isSelected(), raceShipsCB.isSelected(), specialShipsCB.isSelected(), collectShipsCB.isSelected())));
    warShipsCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredShips.setPredicate(ship -> filterShipsTable(ship, searchField.getText(), civilShipsCB.isSelected(), newValue, civilRaceShipsCB.isSelected(), raceShipsCB.isSelected(), specialShipsCB.isSelected(), collectShipsCB.isSelected())));
    civilRaceShipsCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredShips.setPredicate(ship -> filterShipsTable(ship, searchField.getText(), civilShipsCB.isSelected(), warShipsCB.isSelected(), newValue, raceShipsCB.isSelected(), specialShipsCB.isSelected(), collectShipsCB.isSelected())));
    raceShipsCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredShips.setPredicate(ship -> filterShipsTable(ship, searchField.getText(), civilShipsCB.isSelected(), warShipsCB.isSelected(), civilRaceShipsCB.isSelected(), newValue, specialShipsCB.isSelected(), collectShipsCB.isSelected())));
    specialShipsCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredShips.setPredicate(ship -> filterShipsTable(ship, searchField.getText(), civilShipsCB.isSelected(), warShipsCB.isSelected(), civilRaceShipsCB.isSelected(), raceShipsCB.isSelected(), newValue, collectShipsCB.isSelected())));
    collectShipsCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredShips.setPredicate(ship -> filterShipsTable(ship, searchField.getText(), civilShipsCB.isSelected(), warShipsCB.isSelected(), civilRaceShipsCB.isSelected(), raceShipsCB.isSelected(), specialShipsCB.isSelected(), newValue)));
  }

  private boolean filterShipsTable(final Ship ship, final String filterText, final boolean civilShips, final boolean warShips, final boolean civilRaceShips, final boolean raceShips, final boolean specialShips, final boolean collectShips)
  {
    if (!civilShips && ship.categoryProperty().get().contains(Ships.CIVIL_SHIPS))
    {
      return false;
    }
    if (!warShips && ship.categoryProperty().get().contains(Ships.WAR_SHIPS))
    {
      return false;
    }
    if (!civilRaceShips && ship.categoryProperty().get().contains(Ships.CIVIL_RACE_SHIPS))
    {
      return false;
    }
    if (!raceShips && ship.categoryProperty().get().contains(Ships.RACE_SHIPS))
    {
      return false;
    }
    if (!specialShips && ship.categoryProperty().get().contains(Ships.SPECIAL_SHIPS))
    {
      return false;
    }
    if (!collectShips && ship.categoryProperty().get().contains(Ships.COLLECT_SHIPS))
    {
      return false;
    }
    if ((filterText == null) || filterText.isEmpty())
    {
      return true;
    }
    final String lowerCaseFilter = filterText.toLowerCase();
    if (ship.nameProperty().get().toLowerCase().contains(lowerCaseFilter))
    {
      return true;
    }
    return false;
  }

  private TableView<Ship> getShipsTableView(final FilteredList<Ship> filteredShips)
  {
    final TableView<Ship> shipsTable = new TableView<>();
    shipsTable.setEditable(true);
    final SortedList<Ship> sortedShips = filteredShips.sorted();
    sortedShips.comparatorProperty().bind(shipsTable.comparatorProperty());
    shipsTable.setItems(sortedShips);
    final TableColumn<Ship, String> nameCol = new TableColumn<>("Name");
    nameCol.getStyleClass().add("table-cell-centerleft");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setCellFactory(TextFieldTableCell.<Ship>forTableColumn());
    shipsTable.getColumns().add(nameCol);
    final TableColumn<Ship, Long> weaponsCol = new TableColumn<>("Waffen");
    weaponsCol.setCellValueFactory(new PropertyValueFactory<>("weapons"));
    weaponsCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    shipsTable.getColumns().add(weaponsCol);
    final TableColumn<Ship, Long> structureCol = new TableColumn<>("Struktur");
    structureCol.setCellValueFactory(new PropertyValueFactory<>("structure"));
    structureCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    shipsTable.getColumns().add(structureCol);
    final TableColumn<Ship, Long> shieldCol = new TableColumn<>("Schild");
    shieldCol.setCellValueFactory(new PropertyValueFactory<>("shield"));
    shieldCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    shipsTable.getColumns().add(shieldCol);
    final TableColumn<Ship, Integer> healCol = new TableColumn<>("Heilung");
    healCol.setCellValueFactory(new PropertyValueFactory<>("heal"));
    healCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(healCol);
    final TableColumn<Ship, Integer> capacityCol = new TableColumn<>("Laderaum");
    capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    capacityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(capacityCol);
    final TableColumn<Ship, Integer> titanCol = new TableColumn<>("Titan");
    titanCol.setCellValueFactory(new PropertyValueFactory<>("titan"));
    titanCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(titanCol);
    final TableColumn<Ship, Integer> siliziumCol = new TableColumn<>("Silizium");
    siliziumCol.setCellValueFactory(new PropertyValueFactory<>("silizium"));
    siliziumCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(siliziumCol);
    final TableColumn<Ship, Integer> pvcCol = new TableColumn<>("PVC");
    pvcCol.setCellValueFactory(new PropertyValueFactory<>("pvc"));
    pvcCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(pvcCol);
    final TableColumn<Ship, Integer> tritiumCol = new TableColumn<>("Tritium");
    tritiumCol.setCellValueFactory(new PropertyValueFactory<>("tritium"));
    tritiumCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(tritiumCol);
    final TableColumn<Ship, Integer> foodCol = new TableColumn<>("Nahrung");
    foodCol.setCellValueFactory(new PropertyValueFactory<>("food"));
    foodCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(foodCol);
    final TableColumn<Ship, Double> valueCol = new TableColumn<>("Wert");
    valueCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getValue()));
    shipsTable.getColumns().add(valueCol);
    final TableColumn<Ship, Integer> startingCol = new TableColumn<>("Start");
    startingCol.setCellValueFactory(new PropertyValueFactory<>("starting"));
    startingCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(startingCol);
    final TableColumn<Ship, Integer> landingCol = new TableColumn<>("Landung");
    landingCol.setCellValueFactory(new PropertyValueFactory<>("landing"));
    landingCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(landingCol);
    final TableColumn<Ship, Integer> usagePerARCol = new TableColumn<>("Verbrauch");
    usagePerARCol.setCellValueFactory(new PropertyValueFactory<>("usagePerAR"));
    usagePerARCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(usagePerARCol);
    final TableColumn<Ship, Integer> speedCol = new TableColumn<>("Geschwindigkeit");
    speedCol.setCellValueFactory(new PropertyValueFactory<>("speed"));
    speedCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(speedCol);
    final TableColumn<Ship, Integer> startDurationCol = new TableColumn<>("Startdauer");
    startDurationCol.setCellValueFactory(new PropertyValueFactory<>("startDuration"));
    startDurationCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(startDurationCol);
    final TableColumn<Ship, Integer> landDurationCol = new TableColumn<>("Landedauer");
    landDurationCol.setCellValueFactory(new PropertyValueFactory<>("landDuration"));
    landDurationCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(landDurationCol);
    final TableColumn<Ship, Integer> startLandDurationCol = new TableColumn<>("Start + Landung");
    startLandDurationCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getStartLandDuration()));
    shipsTable.getColumns().add(startLandDurationCol);
    final TableColumn<Ship, Integer> pilotsCol = new TableColumn<>("Piloten");
    pilotsCol.setCellValueFactory(new PropertyValueFactory<>("pilots"));
    pilotsCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(pilotsCol);
    final TableColumn<Ship, Integer> passengersCol = new TableColumn<>("Passagiere");
    passengersCol.setCellValueFactory(new PropertyValueFactory<>("passengers"));
    passengersCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    shipsTable.getColumns().add(passengersCol);
    final TableColumn<Ship, String> categoryCol = new TableColumn<>("Kategorie");
    categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
    categoryCol.setCellFactory(TextFieldTableCell.forTableColumn());
    shipsTable.getColumns().add(categoryCol);
    final TableColumn<Ship, Double> wssCol = new TableColumn<>("Ressource/WSS");
    wssCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRessPerWSS()));
    shipsTable.getColumns().add(wssCol);
    VBox.setVgrow(shipsTable, Priority.ALWAYS);
    return shipsTable;
  }

}
