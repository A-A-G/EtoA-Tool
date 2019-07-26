/**
 * 
 */
package gui.tabs;

import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ships;
import javafx.scene.control.Label;

/**
 * @author AAG
 *
 */
public class RakTab extends EtoATab
{

  public RakTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super("Raketen", planets, ships, defences, statusLabel);

  }

}
