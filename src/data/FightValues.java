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
      shieldtech = shield * shieldtech;
      armortech = structure * armortech;
      weapontech = weapons * weapontech;
      regenatech = heal * regenatech;
      for (final PlayerValues p : playerValues)
      {
        shield += p.shieldProperty().get();
        structure += p.structureProperty().get();
        weapons += p.weaponsProperty().get();
        heal += p.healProperty().get();
        shieldtech += p.shieldProperty().get() * p.shieldtechProperty().get();
        armortech += p.structureProperty().get() * p.armortechProperty().get();
        weapontech += p.weaponsProperty().get() * p.weapontechProperty().get();
        units += p.unitsProperty().get();
      }
      shieldtech = shield > 0 ? shieldtech / shield : 100;
      armortech = structure > 0 ? armortech / structure : 100;
      weapontech = weapons > 0 ? weapontech / weapons : 100;
      regenatech = heal > 0 ? regenatech / heal : 100;
    }
  }
}
