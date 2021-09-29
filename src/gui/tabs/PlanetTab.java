/**
 *
 */
package gui.tabs;

import data.defence.Defences;
import data.planets.Planet;
import data.planets.Planets;
import data.ships.Ships;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author AAG
 *
 */
public class PlanetTab extends EtoATab
{

  private final static String TAB_NAME = "Planeten";

  private SortedList<Planet> indexList;

  public PlanetTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    final TextField searchField = new TextField();
    final FilteredList<Planet> filteredPlanets = new FilteredList<>(planets.getData(), p -> true);
    final ListView<String> playerView = getPlayerListView();
    final VBox planetsVBox = new VBox(getSearchHBox(searchField), getFilterHBoxAndHandleEvents(searchField, filteredPlanets, playerView), getPlanetsTableView(filteredPlanets), getLoadSaveHBox(planets));
    planetsVBox.setAlignment(Pos.CENTER);
    VBox.setVgrow(playerView, Priority.ALWAYS);
    final Label playerHeaderLabel = new Label(planets.getPlayerCount() + " Spieler mit " + planets.getPlayerPlanetsCount() + " Planeten.");
    planets.getPlayer().addListener((final ListChangeListener.Change<? extends String> c) -> playerHeaderLabel.setText(planets.getPlayerCount() + " Spieler mit " + planets.getPlayerPlanetsCount() + " Planeten."));
    planets.getData().addListener((final ListChangeListener.Change<? extends Planet> c) -> playerHeaderLabel.setText(planets.getPlayerCount() + " Spieler mit " + planets.getPlayerPlanetsCount() + " Planeten."));
    final VBox playerVBox = new VBox(playerHeaderLabel, playerView);
    playerVBox.setAlignment(Pos.CENTER);
    final SplitPane planetsSplit = new SplitPane(planetsVBox, playerVBox, getCnPDataHBox("Copy & Paste Solarsystem! " + System.lineSeparator() + System.lineSeparator() + "Currently supported formats: Google Chrome", planets));
    planetsSplit.setDividerPositions(0.55f, 0.75f);
    setContent(planetsSplit);
  }

  private boolean filterPlanetsTable(final Planet planet, final String filterText, final String player, final boolean noOwner, final boolean emptyRoom, final boolean stars, final boolean asteroids, final boolean wormholes)
  {
    if ((player != null) && !planet.ownerProperty().get().equals(player))
    {
      return false;
    }
    if (!noOwner && planet.ownerProperty().get().equals(Planets.NO_OWNER))
    {
      if ((!emptyRoom || !planet.typeProperty().get().equals(Planets.EMPTY_SPACE)) && (!stars || !planet.typeProperty().get().equals(Planets.STAR)) && (!asteroids || !planet.typeProperty().get().equals(Planets.ASTEROID)) && (!wormholes || !planet.typeProperty().get().contains(Planets.WORMHOLE)))
      {
        return false;
      }
    }
    if (!emptyRoom && planet.typeProperty().get().equals(Planets.EMPTY_SPACE))
    {
      return false;
    }
    if (!stars && planet.typeProperty().get().equals(Planets.STAR))
    {
      return false;
    }
    if (!asteroids && planet.typeProperty().get().equals(Planets.ASTEROID))
    {
      return false;
    }
    if (!wormholes && planet.typeProperty().get().contains(Planets.WORMHOLE))
    {
      return false;
    }
    if ((filterText == null) || filterText.isEmpty())
    {
      return true;
    }
    final String lowerCaseFilter = filterText.toLowerCase();
    if (planet.ownerProperty().get().toLowerCase().contains(lowerCaseFilter) || planet.coordsProperty().get().toLowerCase().contains(lowerCaseFilter) || planet.typeProperty().get().toLowerCase().contains(lowerCaseFilter))
    {
      return true;
    }
    return false;
  }

  private HBox getFilterHBoxAndHandleEvents(final TextField searchField, final FilteredList<Planet> filteredPlanets, final ListView<String> playerView)
  {
    final CheckBox noOwnerCB = new CheckBox("Kein Besitzer");
    noOwnerCB.setIndeterminate(false);
    final CheckBox emptySpaceCB = new CheckBox("Leerer Raum");
    emptySpaceCB.setIndeterminate(false);
    final CheckBox starCB = new CheckBox("Sterne");
    starCB.setIndeterminate(false);
    final CheckBox asteroidsCB = new CheckBox("Asteroidenfelder");
    asteroidsCB.setIndeterminate(false);
    final CheckBox wormholesCB = new CheckBox("Wurmlöcher");
    wormholesCB.setIndeterminate(false);
    final HBox filterHBox = new HBox(noOwnerCB, emptySpaceCB, starCB, asteroidsCB, wormholesCB);
    filterHBox.getStyleClass().add("spacing");
    filterHBox.setAlignment(Pos.CENTER);
    playerView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filteredPlanets.setPredicate(planet -> filterPlanetsTable(planet, searchField.getText(), newValue, noOwnerCB.isSelected(), emptySpaceCB.isSelected(), starCB.isSelected(), asteroidsCB.isSelected(), wormholesCB.isSelected())));
    searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredPlanets.setPredicate(planet -> filterPlanetsTable(planet, newValue, playerView.getSelectionModel().getSelectedItem(), noOwnerCB.isSelected(), emptySpaceCB.isSelected(), starCB.isSelected(), asteroidsCB.isSelected(), wormholesCB.isSelected())));
    noOwnerCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredPlanets.setPredicate(planet -> filterPlanetsTable(planet, searchField.getText(), playerView.getSelectionModel().getSelectedItem(), newValue, emptySpaceCB.isSelected(), starCB.isSelected(), asteroidsCB.isSelected(), wormholesCB.isSelected())));
    emptySpaceCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredPlanets.setPredicate(planet -> filterPlanetsTable(planet, searchField.getText(), playerView.getSelectionModel().getSelectedItem(), noOwnerCB.isSelected(), newValue, starCB.isSelected(), asteroidsCB.isSelected(), wormholesCB.isSelected())));
    starCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredPlanets.setPredicate(planet -> filterPlanetsTable(planet, searchField.getText(), playerView.getSelectionModel().getSelectedItem(), noOwnerCB.isSelected(), emptySpaceCB.isSelected(), newValue, asteroidsCB.isSelected(), wormholesCB.isSelected())));
    asteroidsCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredPlanets.setPredicate(planet -> filterPlanetsTable(planet, searchField.getText(), playerView.getSelectionModel().getSelectedItem(), noOwnerCB.isSelected(), emptySpaceCB.isSelected(), starCB.isSelected(), newValue, wormholesCB.isSelected())));
    wormholesCB.selectedProperty().addListener((observable, oldValue, newValue) -> filteredPlanets.setPredicate(planet -> filterPlanetsTable(planet, searchField.getText(), playerView.getSelectionModel().getSelectedItem(), noOwnerCB.isSelected(), emptySpaceCB.isSelected(), starCB.isSelected(), asteroidsCB.isSelected(), newValue)));
    noOwnerCB.setSelected(true);
    noOwnerCB.setSelected(false);
    return filterHBox;
  }

  private TableView<Planet> getPlanetsTableView(final FilteredList<Planet> filteredPlanets)
  {
    final TableView<Planet> planetsTable = new TableView<>();
    final SortedList<Planet> sortedPlanets = filteredPlanets.sorted();
    sortedPlanets.comparatorProperty().bind(planetsTable.comparatorProperty());
    planetsTable.setItems(sortedPlanets);
    final TableColumn<Planet, String> coordsCol = new TableColumn<>("Koordinaten");
    coordsCol.setCellValueFactory(new PropertyValueFactory<Planet, String>("coords"));
    coordsCol.setComparator((final String first, final String second) -> Planet.compareCoords(first, second));
    planetsTable.getColumns().add(coordsCol);
    final TableColumn<Planet, String> ownerCol = new TableColumn<>("Besitzer");
    ownerCol.setCellValueFactory(new PropertyValueFactory<Planet, String>("owner"));
    planetsTable.getColumns().add(ownerCol);
    final TableColumn<Planet, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<Planet, String>("name"));
    planetsTable.getColumns().add(nameCol);
    final TableColumn<Planet, String> typeCol = new TableColumn<>("Typ");
    typeCol.setCellValueFactory(new PropertyValueFactory<Planet, String>("type"));
    planetsTable.getColumns().add(typeCol);
    final TableColumn<Planet, Integer> systemIDCol = new TableColumn<>("System ID");
    systemIDCol.setCellValueFactory(new PropertyValueFactory<Planet, Integer>("ID"));
    planetsTable.getColumns().add(systemIDCol);
    // systemIDCol.setVisible(false);
    final TableColumn<Planet, Integer> HLCol = new TableColumn<>("HL");
    indexList = FXCollections.observableArrayList(planets.getData()).sorted();
    HLCol.setCellValueFactory(p ->
    {
      if (indexList == null)
      {
        return new ReadOnlyObjectWrapper<>(Integer.valueOf(-1));
      }
      if (planets.getData().size() != indexList.size())
      {
        indexList = FXCollections.observableArrayList(planets.getData()).sorted();
      }
      return new ReadOnlyObjectWrapper<>(p.getValue().getHL(indexList));
    });
    planetsTable.getColumns().add(HLCol);
    // HLCol.setVisible(false);
    VBox.setVgrow(planetsTable, Priority.ALWAYS);
    return planetsTable;
  }

}
