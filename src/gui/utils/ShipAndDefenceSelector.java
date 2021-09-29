/**
 *
 */
package gui.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import data.DataHandler;
import data.ShipAndDefenceBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author AAG
 *
 */
public class ShipAndDefenceSelector<T extends ShipAndDefenceBase> extends TitledPane
{
  final private ObservableMap<ShipAndDefenceBase, Integer> choosenItemsMap = FXCollections.observableHashMap();

  final private HashMap<ShipAndDefenceBase, ShipAndDefenceAmountBox> amountHBoxesHashMap = new HashMap<>();

  SortedList<T> sortedList;

  final String headerText;

  public ShipAndDefenceSelector(final String header, final DataHandler<T> dataHandler)
  {
    super();
    headerText = header;
    setText(headerText);
    setCollapsible(false);
    final ListView<T> checkableListView = new ListView<>();
    checkableListView.setCellFactory(CheckBoxListCell.forListView(T::selectedProperty));
    final VBox selectedItemsBox = new VBox();
    selectedItemsBox.getStyleClass().add("nospaceandpaddding");
    selectedItemsBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(selectedItemsBox, Priority.ALWAYS);
    final ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setContent(selectedItemsBox);
    scrollPane.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(scrollPane, Priority.ALWAYS);
    final HBox itemSelectionHBox = new HBox(scrollPane, checkableListView);
    itemSelectionHBox.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(itemSelectionHBox, Priority.ALWAYS);
    setContent(itemSelectionHBox);
    setMaxHeight(Double.MAX_VALUE);
    VBox.setVgrow(this, Priority.ALWAYS);
    registerItems(dataHandler, checkableListView, selectedItemsBox);
    dataHandler.getFullObservableList().addListener((ListChangeListener<ShipAndDefenceBase>) changedItems -> registerItems(dataHandler, checkableListView, selectedItemsBox)); // Can we somehow update single ships?
  }

  private void registerItems(final DataHandler<T> dataHandler, final ListView<T> checkableListView, final VBox selectedItemsBox)
  {
    selectedItemsBox.getChildren().clear();
    amountHBoxesHashMap.clear();
    sortedList = dataHandler.getObservableSelectionList().sorted();
    checkableListView.setItems(sortedList);
    sortedList.addListener((ListChangeListener<T>) changedItems ->
    {
      while (changedItems.next())
      {
        if (changedItems.wasUpdated())
        {
          final T changedItem = sortedList.get(changedItems.getFrom());
          if (changedItem.selectedProperty().get() && !amountHBoxesHashMap.containsKey(changedItem))
          {
            final ShipAndDefenceAmountBox amountHBox = new ShipAndDefenceAmountBox(changedItem, this);

            amountHBoxesHashMap.put(changedItem, amountHBox);
            selectedItemsBox.getChildren().add(amountHBox);
          }
          else if (!changedItem.selectedProperty().get() && amountHBoxesHashMap.containsKey(changedItem))
          {
            choosenItemsMap.remove(changedItem);
            final ShipAndDefenceAmountBox amountHBox = amountHBoxesHashMap.remove(changedItem);
            selectedItemsBox.getChildren().remove(amountHBox);
          }
        }
      }
    });
  }

  public void updateHeader()
  {
    if (choosenItemsMap.isEmpty())
    {
      setText(headerText);
    }
    else
    {
      final int itemPoints = ShipAndDefenceBase.getShipsAndDefencePoints(choosenItemsMap);
      setText(headerText + (itemPoints > 0 ? String.format(" (Punkte: %,d)", itemPoints) : ""));
    }
  }

  public void update(final ObservableMap<String, Integer> itemMap)
  {
    unselectChoosenItems();
    if (sortedList != null)
    {
      itemMap.forEach((s, i) ->
      {
        final T item = findItemByName(sortedList, s);
        if (item != null)
        {
          item.selectedProperty().set(true);
          if (amountHBoxesHashMap.containsKey(item))
          {
            amountHBoxesHashMap.get(item).setAmount(i);
          }
        }
        else
        {
          System.out.println("Something wrong looking für item: " + s);
        }
      });
    }
  }

  public T findItemByName(final Collection<T> items, final String name)
  {
    return items.stream().filter(item -> name.equals(item.nameProperty().get())).findFirst().orElse(null);
  }

  private void unselectChoosenItems()
  {
    for (final ShipAndDefenceBase item : new HashSet<>(choosenItemsMap.keySet())) // ugly but necessary? why? better?
    {
      item.selectedProperty().set(false);
    }
  }

  public ObservableMap<ShipAndDefenceBase, Integer> getChoosenItemsMap()
  {
    return choosenItemsMap;
  }

}
