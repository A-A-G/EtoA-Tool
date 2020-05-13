/**
 * 
 */
package gui.utils;

import data.FightData;
import javafx.scene.Node;
import javafx.scene.control.Spinner;

/**
 * @author AAG
 *
 */
public class PlayerSpinners
{
  private final static String GREEN_SPINNER = "greenspinner";
  private final static String RED_SPINNER = "redspinner";

  public Spinner<Double> weapons = Spinners.getDoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 0, 1000, -1, false, null);
  public Spinner<Double> structure = Spinners.getDoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 0, 1000, -1, false, null);
  public Spinner<Double> shield = Spinners.getDoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 0, 1000, -1, false, null);
  public Spinner<Double> heal = Spinners.getDoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 0, 1000, -1, false, null);

  public void updateSpinners(final FightData fightData, final boolean attackerWins)
  {
    if (fightData.getAttackerValues().weapons >= (fightData.getDefenderValues().structure + fightData.getDefenderValues().shield))
    {
      weapons.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
      addStyle(weapons, GREEN_SPINNER);
    }
    else if (attackerWins)
    {
      weapons.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
      weapons.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
    }
    else
    {
      weapons.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
      addStyle(weapons, RED_SPINNER);
    }
    if (fightData.getDefenderValues().weapons >= (fightData.getAttackerValues().structure + fightData.getAttackerValues().shield))
    {
      shield.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
      structure.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
      addStyle(shield, RED_SPINNER);
      addStyle(structure, RED_SPINNER);
    }
    else
    {
      shield.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
      structure.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
      addStyle(shield, GREEN_SPINNER);
      addStyle(structure, GREEN_SPINNER);
    }
    if (fightData.getAttackerValues().heal < fightData.getDefenderValues().weapons)
    {
      heal.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
      addStyle(heal, RED_SPINNER);
    }
    else
    {
      heal.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
      addStyle(heal, GREEN_SPINNER);
    }
  }

  private void addStyle(final Node node, final String style)
  {
    if (!node.getStyleClass().contains(style))
    {
      node.getStyleClass().add(style);
    }
  }
}
