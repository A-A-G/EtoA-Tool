/**
 *
 */
package gui.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import logic.Distances;

/**
 * @author AAG
 *
 */
public class DistanceAndDurationBox extends VBox
{
  private final Label timeLabel;

  public DistanceAndDurationBox()
  {
    getStyleClass().add("spacing");
    getStyleClass().add("padding");
    final DistanceHBox distanceBox = new DistanceHBox();
    distanceBox.setAlignment(Pos.CENTER);
    getChildren().add(distanceBox);
    final HBox speedTimeHBox = new HBox();
    speedTimeHBox.setAlignment(Pos.CENTER);
    final SpeedHBox speedBox = new SpeedHBox();
    speedTimeHBox.getChildren().add(speedBox);
    final Region spacer = new Region();
    spacer.setPrefWidth(50);
    speedTimeHBox.getChildren().add(spacer);
    speedTimeHBox.getChildren().add(new Label("Zeit: "));
    timeLabel = new Label("0s");
    timeLabel.setMinWidth(100);
    timeLabel.setMaxWidth(100);
    timeLabel.setAlignment(Pos.CENTER_RIGHT);
    timeLabel.setTextAlignment(TextAlignment.RIGHT);
    timeLabel.getStyleClass().add("highlight");
    speedTimeHBox.getChildren().add(timeLabel);
    getChildren().add(speedTimeHBox);
    distanceBox.distanceProperty().addListener((obs, oldValue, newValue) -> updateTime(distanceBox.distanceProperty().get(), speedBox.getSpeed(), speedBox.getStartLand()));
    speedBox.speedProperty().addListener((obs, oldValue, newValue) -> updateTime(distanceBox.distanceProperty().get(), speedBox.getSpeed(), speedBox.getStartLand()));
    speedBox.startLandProperty().addListener((obs, oldValue, newValue) -> updateTime(distanceBox.distanceProperty().get(), speedBox.getSpeed(), speedBox.getStartLand()));
    updateTime(distanceBox.distanceProperty().get(), speedBox.getSpeed(), speedBox.getStartLand());
  }

  private void updateTime(final double distance, final int speed, final int startLandDuration)
  {
    timeLabel.setText(Distances.getDurationHMS(distance, speed, startLandDuration));
  }
}
