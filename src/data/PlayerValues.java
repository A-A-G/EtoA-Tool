/**
 *
 */
package data;

import data.defence.Defence;
import data.ships.Ship;
import data.ships.Ships;
import gui.utils.ShipAndDefenceSelector;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import properties.FightSimProperties;

/**
 * @author AAG
 *
 */
public class PlayerValues
{
  public static final int DEFAULT_TECH_BASE = 100;

  private final ObjectProperty<Integer> weapontech = new SimpleIntegerProperty(DEFAULT_TECH_BASE).asObject();
  private final ObjectProperty<Integer> armortech = new SimpleIntegerProperty(DEFAULT_TECH_BASE).asObject();
  private final ObjectProperty<Integer> shieldtech = new SimpleIntegerProperty(DEFAULT_TECH_BASE).asObject();
  private final ObjectProperty<Integer> regenatech = new SimpleIntegerProperty(DEFAULT_TECH_BASE).asObject();
  private final ObjectProperty<Integer> repair = new SimpleIntegerProperty((int) Math.round(FightSimProperties.getInstance().getDefenceRepair())).asObject();

  private final ObjectProperty<Double> weapons = new SimpleDoubleProperty(0).asObject();
  private final ObjectProperty<Double> structure = new SimpleDoubleProperty(0).asObject();
  private final ObjectProperty<Double> shield = new SimpleDoubleProperty(0).asObject();
  private final ObjectProperty<Double> heal = new SimpleDoubleProperty(0).asObject();
  private final ObjectProperty<Double> capacity = new SimpleDoubleProperty(0).asObject();

  private final ObjectProperty<Integer> units = new SimpleIntegerProperty(0).asObject();

  private ObservableMap<ShipAndDefenceBase, Integer> shipsMap = null;
  private ObservableMap<ShipAndDefenceBase, Integer> defencesMap = null;

  ShipAndDefenceSelector<Ship> shipSelector = null;
  ShipAndDefenceSelector<Defence> defenceSelector = null;

  public PlayerValues()
  {
    weapontech.addListener((obs, oldValue, newValue) -> updateValues());
    armortech.addListener((obs, oldValue, newValue) -> updateValues());
    shieldtech.addListener((obs, oldValue, newValue) -> updateValues());
    regenatech.addListener((obs, oldValue, newValue) -> updateValues());
  }

  public PlayerValues(final PlayerValues otherPlayer)
  {
    this();
    if (otherPlayer.getShips() != null)
    {
      shipsMap = FXCollections.observableHashMap();
      shipsMap.putAll(otherPlayer.getShips());
    }
    if (otherPlayer.getDefences() != null)
    {
      defencesMap = FXCollections.observableHashMap();
      defencesMap.putAll(otherPlayer.getDefences());
    }
    weapontech.set(otherPlayer.weapontechProperty().get());
    armortech.set(otherPlayer.armortechProperty().get());
    shieldtech.set(otherPlayer.shieldtechProperty().get());
    regenatech.set(otherPlayer.regenatechProperty().get());
    repair.set(otherPlayer.repairProperty().get());
    updateValues();
  }

  public void setChoosenShips(final ObservableMap<ShipAndDefenceBase, Integer> choosenShips)
  {
    shipsMap = choosenShips;
    shipsMap.addListener((MapChangeListener<ShipAndDefenceBase, Integer>) change -> updateValues());
  }

  public void setChoosenDefense(final ObservableMap<ShipAndDefenceBase, Integer> choosenDefence)
  {
    defencesMap = choosenDefence;
    defencesMap.addListener((MapChangeListener<ShipAndDefenceBase, Integer>) change -> updateValues());
  }

  private void updateValues()
  {
    double weaponsBase = 0;
    double structureBase = 0;
    double shieldBase = 0;
    double healBase = 0;
    double capacity = 0;
    int units = 0;
    if (shipsMap != null)
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
      {
        weaponsBase = weaponsBase + ((double) pair.getKey().weapons.get() * pair.getValue());
        structureBase = structureBase + ((double) pair.getKey().structure.get() * pair.getValue());
        shieldBase = shieldBase + ((double) pair.getKey().shield.get() * pair.getValue());
        healBase = healBase + ((double) pair.getKey().heal.get() * pair.getValue());
        if (pair.getKey() instanceof Ship)
        {
          if ((!Ships.NO_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())) && (!Ships.INCREASED_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())))
          {
            capacity = capacity + (((Ship) pair.getKey()).capacityProperty().get() * (double) pair.getValue());
          }
          else if (Ships.INCREASED_LOOT_SHIPS.contains(pair.getKey().nameProperty().get()))
          {
            capacity = capacity + (((Ship) pair.getKey()).capacityProperty().get() * (double) pair.getValue());
          }
        }
        units = units + pair.getValue();
      }
    }
    if (defencesMap != null)
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : defencesMap.entrySet())
      {
        weaponsBase = weaponsBase + ((double) pair.getKey().weapons.get() * pair.getValue());
        structureBase = structureBase + ((double) pair.getKey().structure.get() * pair.getValue());
        shieldBase = shieldBase + ((double) pair.getKey().shield.get() * pair.getValue());
        healBase = healBase + ((double) pair.getKey().heal.get() * pair.getValue());
        units = units + pair.getValue();
      }
    }
    weapons.set((double) Math.round((weaponsBase * weapontech.getValue()) / 100.0));
    structure.set((double) Math.round((structureBase * armortech.getValue()) / 100.0));
    shield.set((double) Math.round((shieldBase * shieldtech.getValue()) / 100.0));
    heal.set((double) Math.round((healBase * regenatech.getValue()) / 100.0));
    capacityProperty().set(capacity);
    unitsProperty().set(units);
  }

  public double getNormalShipCapacity()
  {
    double capacity = 0;
    if (shipsMap != null)
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
      {
        if ((pair.getKey() instanceof Ship) && (!Ships.NO_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())) && (!Ships.INCREASED_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())))
        {
          capacity = capacity + (((Ship) pair.getKey()).capacityProperty().get() * (double) pair.getValue());
        }
      }
    }
    return capacity;
  }

  public double getIncreasedCapacity()
  {
    double capacity = 0;
    if (shipsMap != null)
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
      {
        if ((pair.getKey() instanceof Ship) && (Ships.INCREASED_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())))
        {
          capacity = capacity + (((Ship) pair.getKey()).capacityProperty().get() * (double) pair.getValue());
        }
      }
    }
    return capacity;
  }

  public void reduceBy(final double factor)
  {
    if (shipsMap != null)
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
      {
        shipsMap.put(pair.getKey(), (int) Math.ceil(pair.getValue() * factor));
      }
    }
    if (defencesMap != null)
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : defencesMap.entrySet())
      {
        defencesMap.put(pair.getKey(), (int) Math.ceil(pair.getValue() * factor));
      }
    }
    updateValues();
  }

  public String restoreCivilShips(final ObservableMap<ShipAndDefenceBase, Integer> originalShipsMap)
  {
    String restoreString = "";
    if ((shipsMap != null) && (originalShipsMap != null) && (!shipsMap.isEmpty()))
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
      {
        if (originalShipsMap.containsKey(pair.getKey()))
        {
          if (((Ship) pair.getKey()).categoryProperty().get().equals(Ships.CIVIL_SHIPS) || ((Ship) pair.getKey()).categoryProperty().get().equals(Ships.CIVIL_RACE_SHIPS))
          {
            final int originalNumber = originalShipsMap.get(pair.getKey());
            final int currentNumber = pair.getValue();
            final int restored = (int) Math.round((originalNumber - currentNumber) * FightSimProperties.getInstance().getCivilShipsRestore());
            restoreString = restoreString + pair.getKey().nameProperty().get() + " \t " + pair.getValue() + " (+" + restored + ")" + System.lineSeparator();
            shipsMap.put(pair.getKey(), currentNumber + restored);
          }
          else
          {
            restoreString = restoreString + pair.getKey().nameProperty().get() + " \t " + pair.getValue() + System.lineSeparator();
          }
        }
        else
        {
          System.out.println("Something wrong restoring civil ships!");
        }
      }
    }
    else
    {
      restoreString = restoreString + "Nichts vorhanden!" + System.lineSeparator();
    }
    return restoreString;
  }

  public String repairRemainingDefences(final ObservableMap<ShipAndDefenceBase, Integer> originalDefencesMap)
  {
    String repairString = "";
    if ((defencesMap != null) && (originalDefencesMap != null) && (!defencesMap.isEmpty()))
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : defencesMap.entrySet())
      {
        if (originalDefencesMap.containsKey(pair.getKey()))
        {
          final int originalNumber = originalDefencesMap.get(pair.getKey());
          final int currentNumber = pair.getValue();
          final int repaired = (int) Math.round(((originalNumber - currentNumber) * repair.get()) / 100.0);
          repairString = repairString + pair.getKey().nameProperty().get() + " \t " + pair.getValue() + " (+" + repaired + ")" + System.lineSeparator();
          defencesMap.put(pair.getKey(), currentNumber + repaired);
        }
        else
        {
          System.out.println("Something wrong repairing defences!");
        }
      }
    }
    else
    {
      repairString = repairString + "Nichts vorhanden!" + System.lineSeparator();
    }
    return repairString;
  }

  public void removeZeroDeffShips(final ObservableMap<ShipAndDefenceBase, Integer> originalShips)
  {
    if ((shipsMap != null) && (originalShips != null) && (!shipsMap.isEmpty()))
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
      {
        if (originalShips.containsKey(pair.getKey()))
        {
          if ((pair.getKey().structure.get() == 0) && (pair.getKey().shield.get() == 0) && (originalShips.get(pair.getKey()) > pair.getValue()))
          {
            shipsMap.put(pair.getKey(), 0);
          }
        }
      }
    }
  }

  public DebrisField getShipsDebrisField(final ObservableMap<ShipAndDefenceBase, Integer> originalShips)
  {
    return DebrisField.getDebrisField(originalShips, shipsMap, FightSimProperties.getInstance().getDfFactorShips());
  }

  public DebrisField getDefencesDebrisField(final ObservableMap<ShipAndDefenceBase, Integer> originalDefences)
  {
    return DebrisField.getDebrisField(originalDefences, defencesMap, FightSimProperties.getInstance().getDfFactorDefence());
  }

  public double getShipsExperiance(final ObservableMap<ShipAndDefenceBase, Integer> originalShips)
  {
    return ShipAndDefenceBase.getExperience(originalShips, shipsMap);
  }

  public double getDefencesExperiance(final ObservableMap<ShipAndDefenceBase, Integer> originalDefences)
  {
    return ShipAndDefenceBase.getExperience(originalDefences, defencesMap);
  }

  public void updateFromSpyReport(final SpyReport spyReport)
  {
    weapontech.set((spyReport.getWeapontech() * 10) + 100);
    armortech.set((spyReport.getArmortech() * 10) + 100);
    shieldtech.set((spyReport.getShieldtech() * 10) + 100);
    regenatech.set((spyReport.getRegenatech() * 10) + 100);
  }

  public String getValueString()
  {
    String valueString = String.format("Schild (%d): %,.0f", shieldtech.get(), shield.get()) + System.lineSeparator();
    valueString = valueString + String.format("Struktur (%d): %,.0f", armortech.get(), structure.get()) + System.lineSeparator();
    valueString = valueString + String.format("Waffen (%d): %,.0f", weapontech.get(), weapons.get()) + System.lineSeparator();
    return valueString;
  }

  public String getFleetString()
  {
    return ShipAndDefenceBase.getShipAndDefenceBaseString(shipsMap);
  }

  public String getDefencesString()
  {
    return ShipAndDefenceBase.getShipAndDefenceBaseString(defencesMap);
  }

  public ObservableMap<ShipAndDefenceBase, Integer> getShips()
  {
    return shipsMap;
  }

  public ObservableMap<ShipAndDefenceBase, Integer> getDefences()
  {
    return defencesMap;
  }

  public ShipAndDefenceSelector<Ship> getShipSelector()
  {
    return shipSelector;
  }

  public void setShipSelector(final ShipAndDefenceSelector<Ship> shipSelector)
  {
    this.shipSelector = shipSelector;
    setChoosenShips(shipSelector.getChoosenItemsMap());
  }

  public ShipAndDefenceSelector<Defence> getDefenceSelector()
  {
    return defenceSelector;
  }

  public void setDefenceSelector(final ShipAndDefenceSelector<Defence> defenceSelector)
  {
    this.defenceSelector = defenceSelector;
    setChoosenDefense(defenceSelector.getChoosenItemsMap());
  }

  public ObjectProperty<Integer> weapontechProperty()
  {
    return weapontech;
  }

  public ObjectProperty<Integer> armortechProperty()
  {
    return armortech;
  }

  public ObjectProperty<Integer> shieldtechProperty()
  {
    return shieldtech;
  }

  public ObjectProperty<Integer> regenatechProperty()
  {
    return regenatech;
  }

  public ObjectProperty<Integer> repairProperty()
  {
    return repair;
  }

  public ObjectProperty<Double> weaponsProperty()
  {
    return weapons;
  }

  public ObjectProperty<Double> structureProperty()
  {
    return structure;
  }

  public ObjectProperty<Double> shieldProperty()
  {
    return shield;
  }

  public ObjectProperty<Double> healProperty()
  {
    return heal;
  }

  public ObjectProperty<Double> capacityProperty()
  {
    return capacity;
  }

  public ObjectProperty<Integer> unitsProperty()
  {
    return units;
  }

}
