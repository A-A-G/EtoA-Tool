/**
 *
 */
package gui.utils;

import data.ShipAndDefenceBase;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author AAG
 *
 */
public class ShipAndDefenceAmountBox extends HBox
{
  final private Spinner<Integer> valueSpinner;

  public ShipAndDefenceAmountBox(final ShipAndDefenceBase dataItem, final ShipAndDefenceSelector<? extends ShipAndDefenceBase> shipAndDefenceSelector)
  {
    this(dataItem, shipAndDefenceSelector, 0);
  }

  public ShipAndDefenceAmountBox(final ShipAndDefenceBase dataItem, final ShipAndDefenceSelector<? extends ShipAndDefenceBase> SADSelector, final int startValue)
  {
    final Label nameLabel = new Label(dataItem.nameProperty().get());
    nameLabel.setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(nameLabel, Priority.ALWAYS);
    valueSpinner = Spinners.getSpinner(0, 10000000, startValue, 100, 110, true, null);
    SADSelector.getChoosenItemsMap().put(dataItem, 0);
    valueSpinner.valueProperty().addListener((obs, oldValue, newValue) -> SADSelector.getChoosenItemsMap().put(dataItem, newValue));
    valueSpinner.valueProperty().addListener((obs, oldValue, newValue) -> SADSelector.updateHeader());
    getChildren().addAll(nameLabel, valueSpinner);
    setAlignment(Pos.CENTER_RIGHT);
    setMaxWidth(Double.MAX_VALUE);
    HBox.setHgrow(this, Priority.ALWAYS);
    getStyleClass().add("elementstyle");
  }

  public void setAmount(final int amount)
  {
    valueSpinner.getValueFactory().setValue(amount);
  }
}
