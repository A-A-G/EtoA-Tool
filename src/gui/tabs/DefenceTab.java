/**
 * 
 */
package gui.tabs;

import data.defence.Defence;
import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ships;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

/**
 * @author AAG
 *
 */
public class DefenceTab extends EtoATab
{
  private final static String TAB_NAME = "Verteidigungsanlagen";

  public DefenceTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    final FilteredList<Defence> filteredDefence = new FilteredList<Defence>(defences.getData(), p -> true);
    final TextField searchField = new TextField();
    final VBox defenceVBox = new VBox(getSearchHBox(searchField), getDefenceTableView(filteredDefence), getAddLoadSaveHBox("Verteidigungsanlage", defences));
    defenceVBox.setAlignment(Pos.CENTER);
    final SplitPane defenceSplit = new SplitPane(defenceVBox, getCnPDataHBox("Copy & Paste Daten der Verteidigungsanlage!" + System.lineSeparator() + System.lineSeparator() + "Currently supported formats: Google Chrome", defences));
    defenceSplit.setDividerPositions(0.75f);
    setContent(defenceSplit);
    searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredDefence.setPredicate(defence -> filterDefencesTable(defence, newValue)));
  }

  private boolean filterDefencesTable(final Defence ship, final String filterText)
  {
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

  private TableView<Defence> getDefenceTableView(final FilteredList<Defence> filteredDefence)
  {
    final TableView<Defence> defenceTable = new TableView<>();
    defenceTable.setEditable(true);
    final SortedList<Defence> sortedDefences = filteredDefence.sorted();
    sortedDefences.comparatorProperty().bind(defenceTable.comparatorProperty());
    defenceTable.setItems(sortedDefences);
    final TableColumn<Defence, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setCellFactory(TextFieldTableCell.<Defence>forTableColumn());
    defenceTable.getColumns().add(nameCol);
    final TableColumn<Defence, Long> weaponsCol = new TableColumn<>("Schusskraft");
    weaponsCol.setCellValueFactory(new PropertyValueFactory<>("weapons"));
    weaponsCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    defenceTable.getColumns().add(weaponsCol);
    final TableColumn<Defence, Long> structureCol = new TableColumn<>("Struktur");
    structureCol.setCellValueFactory(new PropertyValueFactory<>("structure"));
    structureCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    defenceTable.getColumns().add(structureCol);
    final TableColumn<Defence, Long> shieldCol = new TableColumn<>("Abwehrschild");
    shieldCol.setCellValueFactory(new PropertyValueFactory<>("shield"));
    shieldCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    defenceTable.getColumns().add(shieldCol);
    final TableColumn<Defence, Integer> healCol = new TableColumn<>("Reparatur");
    healCol.setCellValueFactory(new PropertyValueFactory<>("heal"));
    healCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(healCol);
    final TableColumn<Defence, Integer> titanCol = new TableColumn<>("Titan");
    titanCol.setCellValueFactory(new PropertyValueFactory<>("titan"));
    titanCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(titanCol);
    final TableColumn<Defence, Integer> siliziumCol = new TableColumn<>("Silizium");
    siliziumCol.setCellValueFactory(new PropertyValueFactory<>("silizium"));
    siliziumCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(siliziumCol);
    final TableColumn<Defence, Integer> pvcCol = new TableColumn<>("PVC");
    pvcCol.setCellValueFactory(new PropertyValueFactory<>("pvc"));
    pvcCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(pvcCol);
    final TableColumn<Defence, Integer> tritiumCol = new TableColumn<>("Tritium");
    tritiumCol.setCellValueFactory(new PropertyValueFactory<>("tritium"));
    tritiumCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(tritiumCol);
    final TableColumn<Defence, Integer> foodCol = new TableColumn<>("Nahrung");
    foodCol.setCellValueFactory(new PropertyValueFactory<>("food"));
    foodCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(foodCol);
    final TableColumn<Defence, Double> valueCol = new TableColumn<>("Wert");
    valueCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getValue()));
    defenceTable.getColumns().add(valueCol);
    final TableColumn<Defence, Integer> spaceCol = new TableColumn<>("Platz");
    spaceCol.setCellValueFactory(new PropertyValueFactory<>("fields"));
    spaceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(spaceCol);
    final TableColumn<Defence, Integer> maxNumberCol = new TableColumn<>("Max. Anzahl");
    maxNumberCol.setCellValueFactory(new PropertyValueFactory<>("maxNumber"));
    maxNumberCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    defenceTable.getColumns().add(maxNumberCol);
    final TableColumn<Defence, Double> wssCol = new TableColumn<>("Ressource/WSS");
    wssCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRessPerWSS()));
    defenceTable.getColumns().add(wssCol);
    VBox.setVgrow(defenceTable, Priority.ALWAYS);
    return defenceTable;
  }
}
