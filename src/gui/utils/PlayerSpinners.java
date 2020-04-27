/**
 * 
 */
package gui.utils;

import data.PlayerValues;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import logic.FightSimulation;

/**
 * @author AAG
 *
 */
public class PlayerSpinners
{
  private final static String GREEN_SPINNER = "greenspinner";
  private final static String RED_SPINNER = "redspinner";

  public Spinner<Double> weapons;
  public Spinner<Double> structure;
  public Spinner<Double> shield;
  public Spinner<Double> heal;

  public void updateSpinners(final FightSimulation fightSimulation, final PlayerValues baseValues, final PlayerSpinners otherSpinners)
  {
    fightSimulation.SimulateFight();
    final PlayerValues plainValues = new PlayerValues(baseValues);
    if (plainValues.weaponsProperty().getValue() >= (otherSpinners.shield.getValue() + otherSpinners.structure.getValue()))
    {
      weapons.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
      addStyle(weapons, GREEN_SPINNER);
    }
    else if (fightSimulation.isAttackerWins())
    {
      weapons.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
      weapons.getStyleClass().remove(PlayerSpinners.RED_SPINNER);
    }
    else
    {
      weapons.getStyleClass().remove(PlayerSpinners.GREEN_SPINNER);
      addStyle(weapons, RED_SPINNER);
    }
    if (otherSpinners.weapons.getValue() >= (plainValues.shieldProperty().getValue() + plainValues.structureProperty().getValue()))
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
    if (plainValues.healProperty().getValue() < (otherSpinners.weapons.getValue() * 0.9))
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
