package gui.tabs;

import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ships;
import gui.utils.RigeliaTPane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author AAG
 *
 */
public class RigeliaTab extends EtoATab
{

  public RigeliaTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super("Rigelia", planets, ships, defences, statusLabel);
    // double layouting shrinks nodes
    final VBox vbox = new VBox(new RigeliaTPane(RigeliaTPane.Type.EMP), new RigeliaTPane(RigeliaTPane.Type.ANTHRAX), new RigeliaTPane(RigeliaTPane.Type.GAS));
    vbox.setAlignment(Pos.CENTER);
    final HBox hbox = new HBox(vbox);
    hbox.setAlignment(Pos.CENTER);
    setContent(hbox);
  }

}
