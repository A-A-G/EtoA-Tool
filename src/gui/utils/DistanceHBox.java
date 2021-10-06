/**
 *
 */
package gui.utils;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import logic.Distances;
import logic.Distances.Coordinates;

/**
 * @author AAG
 *
 */
public class DistanceHBox extends HBox
{
  private final Spinner<Integer> x_sector_out;
  private final Spinner<Integer> y_sector_out;
  private final Spinner<Integer> x_system_out;
  private final Spinner<Integer> y_system_out;
  private final Spinner<Integer> pos_out;

  private final Spinner<Integer> x_sector_in;
  private final Spinner<Integer> y_sector_in;
  private final Spinner<Integer> x_system_in;
  private final Spinner<Integer> y_system_in;
  private final Spinner<Integer> pos_in;

  private final Label distanceLabel;

  private final ReadOnlyDoubleWrapper distance = new ReadOnlyDoubleWrapper();

  public DistanceHBox()
  {
    super();
    setAlignment(Pos.CENTER_LEFT);
    getChildren().add(new Label("Startplanet:"));
    final Region spacer0 = new Region();
    spacer0.setPrefWidth(10);
    getChildren().add(spacer0);
    x_sector_out = Spinners.getSpinner(1, 3, 1, 1, 65, true, null);
    x_sector_out.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(x_sector_out);
    getChildren().add(new Label("/"));
    y_sector_out = Spinners.getSpinner(1, 3, 1, 1, 65, true, null);
    y_sector_out.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(y_sector_out);
    getChildren().add(new Label(":"));
    x_system_out = Spinners.getSpinner(1, 10, 1, 1, 75, true, null);
    x_system_out.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(x_system_out);
    getChildren().add(new Label("/"));
    y_system_out = Spinners.getSpinner(1, 10, 1, 1, 75, true, null);
    y_system_out.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(y_system_out);
    getChildren().add(new Label(":"));
    pos_out = Spinners.getSpinner(0, 30, 0, 1, 75, true, null);
    pos_out.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(pos_out);
    final Region spacer1 = new Region();
    spacer1.setPrefWidth(50);
    getChildren().add(spacer1);
    getChildren().add(new Label("Zielplanet:"));
    final Region spacer2 = new Region();
    spacer2.setPrefWidth(10);
    getChildren().add(spacer2);
    x_sector_in = Spinners.getSpinner(1, 3, 1, 1, 60, true, null);
    x_sector_in.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(x_sector_in);
    getChildren().add(new Label("/"));
    y_sector_in = Spinners.getSpinner(1, 3, 1, 1, 60, true, null);
    y_sector_in.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(y_sector_in);
    getChildren().add(new Label(":"));
    x_system_in = Spinners.getSpinner(1, 10, 1, 1, 75, true, null);
    x_system_in.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(x_system_in);
    getChildren().add(new Label("/"));
    y_system_in = Spinners.getSpinner(1, 10, 1, 1, 75, true, null);
    y_system_in.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(y_system_in);
    getChildren().add(new Label(":"));
    pos_in = Spinners.getSpinner(0, 30, 0, 1, 75, true, null);
    pos_in.valueProperty().addListener((observable, oldValue, newValue) -> distanceChanged());
    getChildren().add(pos_in);
    final Region spacer3 = new Region();
    spacer3.setPrefWidth(50);
    getChildren().add(spacer3);
    getChildren().add(new Label("Distanz: "));
    distanceLabel = new Label();
    distanceLabel.setMinWidth(50);
    distanceLabel.setMaxWidth(50);
    distanceLabel.setAlignment(Pos.CENTER_RIGHT);
    distanceLabel.setTextAlignment(TextAlignment.RIGHT);
    distanceLabel.getStyleClass().add("highlight");
    getChildren().add(distanceLabel);
    final Label unitLabel = new Label(" AE");
    unitLabel.getStyleClass().add("highlight");
    getChildren().add(unitLabel);
    distanceLabel.setText("0");
  }

  private void distanceChanged()
  {
    distance.set(Distances.getDistance(new Coordinates(x_sector_out.getValue(), y_sector_out.getValue(), x_system_out.getValue(), y_system_out.getValue(), pos_out.getValue()), new Coordinates(x_sector_in.getValue(), y_sector_in.getValue(), x_system_in.getValue(), y_system_in.getValue(), pos_in.getValue())));
    distanceLabel.setText(String.valueOf(Math.round(distance.get())));
  }

  public ReadOnlyDoubleProperty distanceProperty()
  {
    return distance.getReadOnlyProperty();
  }

}
