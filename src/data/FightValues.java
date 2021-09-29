/**
 *
 */
package data;

import javafx.collections.ObservableList;

/**
 * @author AAG
 *
 */
public class FightValues
{
  public double shield = 0;
  public double structure = 0;
  public double weapons = 0;
  public double heal = 0;
  public double shieldtech = 0;
  public double armortech = 0;
  public double weapontech = 0;
  public double regenatech = 0;
  public double capacity = 0;
  public int units = 0;

  public FightValues()
  {
  }

  public void clear()
  {
    shield = 0;
    structure = 0;
    weapons = 0;
    heal = 0;
    shieldtech = 100;
    armortech = 100;
    weapontech = 100;
    regenatech = 100;
    capacity = 0;
    units = 0;
  }

  public void update(final ObservableList<PlayerValues> playerValues)
  {
    clear();
    add(playerValues);
  }

  public void add(final ObservableList<PlayerValues> playerValues)
  {
    if (playerValues.size() > 0)
    {
      double shieldtechBoni = 0;
      double armortechBoni = 0;
      double weapontechBoni = 0;
      double regenatechBoni = 0;
      for (final PlayerValues p : playerValues)
      {
        shield += p.shieldProperty().get();
        structure += p.structureProperty().get();
        weapons += p.weaponsProperty().get();
        heal += p.healProperty().get();
        units += p.unitsProperty().get();
        capacity += p.capacityProperty().get();
        shieldtechBoni += p.shieldtechProperty().get();
        armortechBoni += p.armortechProperty().get();
        weapontechBoni += p.weapontechProperty().get();
        regenatechBoni += p.regenatechProperty().get();
      }
      shieldtech = shieldtechBoni / playerValues.size();
      armortech = armortechBoni / playerValues.size();
      weapontech = weapontechBoni / playerValues.size();
      regenatech = regenatechBoni / playerValues.size();
    }
  }
}
