/**
 * 
 */
package gui.tabs;

import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ships;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author AAG
 *
 */
public class AttStatisticsTab extends EtoATab
{
  private final static String TAB_NAME = "Angriffsstatistiken";

  public AttStatisticsTab(Planets planets, Ships ships, Defences defences, Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    final TextArea copyArea = new TextArea();
    copyArea.setPromptText("Copy & Paste Kampfbericht!");
    VBox.setVgrow(copyArea, Priority.ALWAYS);
    final Button addButton = new Button("Add");
    addButton.setOnAction(actionEvent ->
    {
      //do something
      copyArea.clear();
    });
    final VBox cnpVBox = new VBox(copyArea, addButton);
    cnpVBox.setAlignment(Pos.CENTER);
    final ListView<String> playerView = new ListView<>();
//    playerView.setItems(planets.getPlayer().sorted(Comparators.playerComparator()));
    playerView.setTooltip(new Tooltip("Deselect: Strg+Click"));
    final SplitPane attStatSplitSplit = new SplitPane(cnpVBox, playerView);
//    attStatSplitSplit.setDividerPositions(0.55f, 0.75f);
    setContent(attStatSplitSplit);
  }

}
