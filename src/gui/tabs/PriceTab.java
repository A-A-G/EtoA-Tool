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
public class PriceTab extends EtoATab
{

  private final static String TAB_NAME = "Preisrechner";

  public PriceTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
  }

}
