/**
 *
 */
package gui.utils;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;

/**
 * @author AAG
 *
 */
public class SpeedHBox extends HBox
{
  private static final int DEFAULT_SPEED = 15000; // TODO: make preference
  private static final int DEFAULT_STARTLAND = 120; // TODO: make preference

  private final Spinner<Integer> speedSpinner;
  private final Spinner<Integer> startLandSpinner;

  public SpeedHBox()
  {
    super();
    getStyleClass().add("smallspacing");
    setAlignment(Pos.CENTER_LEFT);
    getChildren().add(new Label("Geschwindigkeit:"));
    speedSpinner = Spinners.getSpinner(0, 200000, DEFAULT_SPEED, 100, 100, true, null);
    getChildren().add(speedSpinner);
    getChildren().add(new Label("AE/h,"));
    getChildren().add(new Label("Start + Landung:"));
    startLandSpinner = Spinners.getSpinner(0, 10000, DEFAULT_STARTLAND, 10, 80, true, null);
    getChildren().add(startLandSpinner);
    getChildren().add(new Label("s"));
  }

  public ReadOnlyObjectProperty<Integer> speedProperty()
  {
    return speedSpinner.valueProperty();
  }

  public ReadOnlyObjectProperty<Integer> startLandProperty()
  {
    return startLandSpinner.valueProperty();
  }

  public int getSpeed()
  {
    return speedSpinner.valueProperty().get().intValue();
  }

  public int getStartLand()
  {
    return startLandSpinner.valueProperty().get().intValue();
  }

}
