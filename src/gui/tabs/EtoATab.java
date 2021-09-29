/**
 *
 */
package gui.tabs;

import data.DataHandler;
import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ship;
import data.ships.Ships;
import gui.utils.Dialogs;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import utils.Comparators;

/**
 * @author AAG
 *
 */
public class EtoATab extends Tab
{
  protected final Planets planets;

  protected final Ships ships;

  protected final Defences defences;

  protected final Label statusLabel;

  public EtoATab(final String tabName, final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    setText(tabName);
    this.planets = planets;
    this.ships = ships;
    this.defences = defences;
    this.statusLabel = statusLabel;
  }

  protected ListView<String> getPlayerListView()
  {
    final ListView<String> playerView = new ListView<>();
    playerView.setItems(planets.getPlayer().sorted(Comparators.playerComparator()));
    playerView.setTooltip(new Tooltip("Deselect: Strg+Click"));
    return playerView;
  }

  public void updateStatus(final String text)
  {
    if (statusLabel != null)
    {
      statusLabel.setText(text);
    }
    System.out.println(text);
    System.out.println();
  }

  protected HBox getSearchHBox(final TextField searchField)
  {
    HBox.setHgrow(searchField, Priority.ALWAYS);
    final HBox searchHBox = new HBox(new Label("Suchen:"), searchField);
    searchHBox.setAlignment(Pos.CENTER);
    searchHBox.getStyleClass().add("smallspacing");
    return searchHBox;
  }

  protected VBox getCnPDataHBox(final String cnpText, final DataHandler<?> dataHandler)
  {
    final TextArea copyArea = new TextArea();
    copyArea.setPromptText(cnpText);
    VBox.setVgrow(copyArea, Priority.ALWAYS);
    final Button addButton = new Button("Add");
    addButton.setOnAction(actionEvent ->
    {
      dataHandler.update(copyArea.getText());
      copyArea.clear();
    });
    final VBox cnpVBox = new VBox(copyArea, addButton);
    cnpVBox.setAlignment(Pos.CENTER);
    return cnpVBox;
  }

  protected HBox getAddLoadSaveHBox(final String itemType, final DataHandler<?> dataHandler)
  {
    final Button addShipButton = new Button("Add...");
    addShipButton.setOnAction(actionEvent -> Dialogs.getNameDialog("itemType").showAndWait().ifPresent(name -> ships.getData().add(new Ship(name))));
    final HBox buttonBar = new HBox(addShipButton, getLoadSaveHBox(dataHandler));
    buttonBar.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(buttonBar, Priority.ALWAYS);
    return buttonBar;
  }

  protected HBox getLoadSaveHBox(final DataHandler<?> dataHandler)
  {
    final Button loadButton = new Button("Load");
    loadButton.setOnAction(actionEvent -> dataHandler.loadFromFile());
    final Button clearButton = new Button("Clear");
    clearButton.setOnAction(actionEvent -> dataHandler.clear());
    final Button saveButton = new Button("Save");
    saveButton.setOnAction(actionEvent -> dataHandler.writeData());
    final Button saveAsButton = new Button("Save as...");
    saveAsButton.setOnAction(actionEvent -> dataHandler.saveAs());
    final HBox buttonBar = new HBox(loadButton, clearButton, saveButton, saveAsButton);
    buttonBar.setAlignment(Pos.CENTER);
    buttonBar.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(buttonBar, Priority.ALWAYS);
    buttonBar.getStyleClass().add("spacing");
    if (dataHandler instanceof Planets)
    {
      final Button exportButton = new Button("Export");
      exportButton.setTooltip(new Tooltip("Export for EtoA forum."));
      exportButton.setOnAction(actionEvent -> ((Planets) dataHandler).exportForumCode());
      buttonBar.getChildren().add(exportButton);
    }
    return buttonBar;
  }
}
