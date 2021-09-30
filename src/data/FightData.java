/**
 *
 */
package data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import data.ships.Ship;
import data.ships.Ships;
import gui.tabs.KBSimTab.MainSpinners;
import gui.tabs.kbsim.PlayerValueTab;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import properties.FightSimProperties;

/**
 * @author AAG
 *
 */
public class FightData
{

  private final ObservableList<PlayerValues> attackerList = FXCollections.observableArrayList();
  private final ObservableList<PlayerValues> defenderList = FXCollections.observableArrayList();
  private final ObservableList<PlayerValues> attackerListCopy = FXCollections.observableArrayList();
  private final ObservableList<PlayerValues> defenderListCopy = FXCollections.observableArrayList();

  private final FightValues attackerValues = new FightValues();
  private final FightValues defenderValues = new FightValues();
  private final FightValues attackerValuesCopy = new FightValues();
  private final FightValues defenderValuesCopy = new FightValues();

  private Ships ships = null;

  public void initFight()
  {
    backupAttacker();
    backupDefender();
    attackerValuesCopy.update(attackerListCopy);
    defenderValuesCopy.update(defenderListCopy);
  }

  public void update()
  {
    attackerValues.update(attackerList);
    defenderValues.update(defenderList);
  }

  public void removeCopyZeroDeffShips()
  {
    if (attackerListCopy.size() != attackerList.size())
    {
      System.out.println("removeCopyZeroDeffShips(): Lists are not matching!");
      return;
    }
    for (int i = 0; i < attackerList.size(); i++)
    {
      attackerListCopy.get(i).removeZeroDeffShips(attackerList.get(i).getShips());
    }
  }

  public void reduceAttackerCopy(final double attackerLeftOver)
  {
    reducePlayerValues(attackerLeftOver / (attackerValues.structure + attackerValues.shield), attackerValuesCopy, attackerList, attackerListCopy);
  }

  public void reduceDefenderCopy(final double defenderLeftOver)
  {
    reducePlayerValues(defenderLeftOver / (defenderValues.structure + defenderValues.shield), defenderValuesCopy, defenderList, defenderListCopy);
  }

  public static void reducePlayerValues(final double reduceBy, final FightValues fightValuesCopy, final ObservableList<PlayerValues> playerValues, final ObservableList<PlayerValues> playerValuesCopy)
  {
    playerValuesCopy.clear();
    for (final PlayerValues p : playerValues)
    {
      final PlayerValues pv = new PlayerValues(p);
      pv.reduceBy(reduceBy);
      playerValuesCopy.add(pv);
    }
    fightValuesCopy.update(playerValuesCopy);
  }

  public String restoreDefenderCivilShips()
  {
    if (defenderListCopy.size() != defenderList.size())
    {
      System.out.println("restoreCivilShips(): Lists are not matching!");
      return "";
    }
    if ((defenderListCopy.size() == 1) && (defenderList.size() == 1))
    {
      return defenderListCopy.get(0).restoreCivilShips(defenderList.get(0).getShips());
    }
    else if ((defenderListCopy.size() > 1) && (defenderList.size() > 1))
    {
      final ObservableMap<ShipAndDefenceBase, Integer> fleetMap = FXCollections.observableHashMap();
      final ObservableMap<ShipAndDefenceBase, Integer> restoreMap = FXCollections.observableHashMap();
      final ObservableMap<ShipAndDefenceBase, Integer> originalShipsMap = defenderList.get(0).getShips();
      ObservableMap<ShipAndDefenceBase, Integer> shipsMap = defenderList.get(0).getShips();
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
      {
        if (originalShipsMap.containsKey(pair.getKey()))
        {
          if (((Ship) pair.getKey()).categoryProperty().get().equals(Ships.CIVIL_SHIPS) || ((Ship) pair.getKey()).categoryProperty().get().equals(Ships.CIVIL_RACE_SHIPS))
          {
            final int originalNumber = originalShipsMap.get(pair.getKey());
            final int currentNumber = pair.getValue();
            final int restored = (int) Math.round((originalNumber - currentNumber) * FightSimProperties.getInstance().getCivilShipsRestore());
            shipsMap.put(pair.getKey(), currentNumber + restored);
            fleetMap.put(pair.getKey(), currentNumber + restored);
            restoreMap.put(pair.getKey(), restored);
          }
          else
          {
            fleetMap.put(pair.getKey(), pair.getValue());
          }
        }
        else
        {
          System.out.println("Something wrong restoring civil ships!");
        }
      }
      for (int i = 1; i < defenderList.size(); i++)
      {
        shipsMap = defenderList.get(i).getShips();
        for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : shipsMap.entrySet())
        {
          fleetMap.put(pair.getKey(), fleetMap.getOrDefault(pair.getKey(), 0) + pair.getValue());
        }
      }
      String restoreString = "";
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : fleetMap.entrySet())
      {
        if (restoreMap.containsKey(pair.getKey()))
        {
          restoreString = restoreString + pair.getKey().nameProperty().get() + " \t " + pair.getValue() + " (+" + restoreMap.get(pair.getKey()) + ")" + System.lineSeparator();
        }
        else
        {
          restoreString = restoreString + pair.getKey().nameProperty().get() + " \t " + pair.getValue() + System.lineSeparator();
        }
      }
      restoreString = restoreString + (new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH))).format(FightSimProperties.getInstance().getCivilShipsRestore() * 100) + "% der zivilen Schiffe werden wiederhergestellt!" + System.lineSeparator();
      return restoreString;
    }
    return "FightData:restoreDefenderCivilShips(): Wrong defender count!";
  }

  public String getAttackerEXPString()
  {
    return getEXPString(getAttackerEXP(), getAttackerCount());
  }

  public String getDefenderEXPString()
  {
    return getEXPString(getDefenderEXP(), getDefenderCount());
  }

  private static String getEXPString(final double exp, final int count)
  {
    if (count > 1)
    {
      return String.format("Gewonnene EXP: %,.0f (%,.0f)", Math.floor(exp / count), Math.floor(exp)) + System.lineSeparator();
    }
    return String.format("Gewonnene EXP: %,.0f", Math.floor(exp)) + System.lineSeparator();
  }

  public double getAttackerEXP()
  {
    double exp = getShipEXP(defenderList, defenderListCopy);
    if ((defenderListCopy.size() > 0) && (defenderList.size() > 0))
    {
      exp += defenderListCopy.get(0).getDefencesExperiance(defenderList.get(0).getDefences());
    }
    return exp;
  }

  public double getDefenderEXP()
  {
    final double exp = getShipEXP(attackerList, attackerListCopy);
    return exp / getDefenderCount();
  }

  public static double getShipEXP(final ObservableList<PlayerValues> initValues, final ObservableList<PlayerValues> resultValues)
  {
    if (initValues.size() != resultValues.size())
    {
      System.out.println("getShipEXP(): Lists are not matching!");
      return -1;
    }
    double exp = 0;
    for (int i = 0; i < initValues.size(); i++)
    {
      exp += resultValues.get(i).getShipsExperiance(initValues.get(i).getShips());
    }
    return exp;
  }

  public String repairRemainingDefences()
  {
    if ((defenderListCopy.size() > 0) && (defenderListCopy.size() > 0))
    {
      return defenderListCopy.get(0).repairRemainingDefences(defenderList.get(0).getDefences());
    }
    return "";
  }

  public String getDebrisField()
  {
    final DebrisField debrisField = DebrisField.getDebrisFieldShips(attackerList, attackerListCopy);
    debrisField.plus(DebrisField.getDebrisFieldShips(defenderList, defenderListCopy));
    if ((defenderListCopy.size() > 0) && (defenderListCopy.size() > 0))
    {
      debrisField.plus(DebrisField.getDefencesDebrisField(defenderList.get(0).getDefences(), defenderListCopy.get(0).getDefences()));
    }
    String debrisFieldString = String.format("Titan %,.0f", debrisField.titan) + System.lineSeparator();
    debrisFieldString = debrisFieldString + String.format("Silizium %,.0f", debrisField.silizium) + System.lineSeparator();
    debrisFieldString = debrisFieldString + String.format("PVC %,.0f", debrisField.pvc) + System.lineSeparator() + System.lineSeparator();
    if ((ships != null) && (Ships.TRANSPORTER_SHIPS.size() > 0))
    {
      debrisFieldString = debrisFieldString + "Für das Sammeln des TF's werden benötigt:" + System.lineSeparator();
      final double total = debrisField.titan + debrisField.silizium + debrisField.pvc;
      final ObservableList<Ship> shipList = ships.getFullObservableList();
      shipList.sort((final Ship s1, final Ship s2) -> s1.name.get().compareTo(s2.name.get()));
      for (final Ship ship : shipList)
      {
        if (Ships.SAME_CAPACITY_TRANSPORTER_EXAMPLE.equals(ship.name.get()))
        {
          debrisFieldString = debrisFieldString + getTransporterString(ship, total, Ships.SAME_CAPACITY_TRANSPORTER_SHIPS);
        }
        else if (Ships.TRANSPORTER_SHIPS.contains(ship.name.get()))
        {
          debrisFieldString = debrisFieldString + getTransporterString(ship, total);
        }
      }
    }
    return debrisFieldString;
  }

  private String getTransporterString(final Ship ship, final double ressources)
  {
    return getTransporterString(ship, ressources, null);
  }

  private String getTransporterString(final Ship ship, final double ressources, String name)
  {
    if (name == null)
    {
      name = ship.name.get();
    }
    String ret = "";
    final double count = ressources / ship.capacityProperty().get();
    ret = ret + String.format("%s: %,.0f", name, Math.floor(count));
    if (getAttackerCount() > 1)
    {
      ret = ret + String.format(" (%,.0f)", Math.floor(count / getAttackerCount()));
    }
    ret = ret + System.lineSeparator();
    return ret;
  }

  public double getAttackerHeal(final double attackerLeftOver)
  {
    return getHeal(attackerLeftOver, attackerValues, attackerList);
  }

  public double getDefenderHeal(final double defenderLeftOver)
  {
    return getHeal(defenderLeftOver, defenderValues, defenderList);
  }

  public static double getHeal(final double leftOver, final FightValues fightValues, final ObservableList<PlayerValues> playerValues)
  {
    if (fightValues.heal > 0)
    {
      double heal = 0;
      final double reduceBy = leftOver / (fightValues.structure + fightValues.shield);
      for (final PlayerValues p : playerValues)
      {
        final PlayerValues pv = new PlayerValues(p);
        pv.reduceBy(reduceBy);
        heal += pv.healProperty().get();
      }
      return heal;
    }
    return 0;
  }

  private void backupAttacker()
  {
    backupList(attackerList, attackerListCopy);
  }

  private void backupDefender()
  {
    backupList(defenderList, defenderListCopy);
  }

  public static void backupList(final ObservableList<PlayerValues> from, final ObservableList<PlayerValues> to)
  {
    to.clear();
    for (final PlayerValues p : from)
    {
      to.add(new PlayerValues(p));
    }
  }

  public double getAttackerCopyCapacity()
  {
    return getCapacity(attackerListCopy);
  }

  public static double getCapacity(final ObservableList<PlayerValues> playerValues)
  {
    double capacity = 0;
    for (final PlayerValues p : playerValues)
    {
      if (p.getShips() != null)
      {
        for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : p.getShips().entrySet())
        {
          if ((pair.getKey() instanceof Ship) && (!Ships.NO_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())) && (!Ships.INCREASED_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())))
          {
            capacity = capacity + (((Ship) pair.getKey()).capacityProperty().get() * (double) pair.getValue());
          }
        }
      }
    }
    return capacity;
  }

  public double getAttackerCopyIncreasedCapacity()
  {
    return getIncreasedCapacity(attackerListCopy);
  }

  public static double getIncreasedCapacity(final ObservableList<PlayerValues> playerValues)
  {
    double capacity = 0;
    for (final PlayerValues p : playerValues)
    {
      if (p.getShips() != null)
      {
        for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : p.getShips().entrySet())
        {
          if ((pair.getKey() instanceof Ship) && (Ships.INCREASED_LOOT_SHIPS.contains(pair.getKey().nameProperty().get())))
          {
            capacity = capacity + (((Ship) pair.getKey()).capacityProperty().get() * (double) pair.getValue());
          }
        }
      }
    }
    return capacity;
  }

  public String getAttackerValueString()
  {
    return getValueString(attackerValues);
  }

  public String getDefenderValueString()
  {
    return getValueString(defenderValues);
  }

  public static String getValueString(final FightValues fightValues)
  {
    String valueString = String.format("Schild (%d): %,.0f", Math.round(fightValues.shieldtech), fightValues.shield) + System.lineSeparator();
    valueString = valueString + String.format("Struktur (%d): %,.0f", Math.round(fightValues.armortech), fightValues.structure) + System.lineSeparator();
    valueString = valueString + String.format("Waffen (%d): %,.0f", Math.round(fightValues.weapontech), fightValues.weapons) + System.lineSeparator();
    valueString = valueString + String.format("Einheiten: %d", fightValues.units) + System.lineSeparator();
    return valueString;
  }

  public String getAttackerFleetString()
  {
    return getFleetString(attackerList);
  }

  public String getAttackerCopyFleetString()
  {
    return getFleetString(attackerListCopy);
  }

  public String getDefenderFleetString()
  {
    return getFleetString(defenderList);
  }

  private static String getFleetString(final ObservableList<PlayerValues> playerValues)
  {
    final ObservableMap<ShipAndDefenceBase, Integer> itemMap = FXCollections.observableHashMap();
    for (final PlayerValues p : playerValues)
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : p.getShips().entrySet())
      {
        itemMap.put(pair.getKey(), itemMap.getOrDefault(pair.getKey(), 0) + pair.getValue());
      }
    }
    return ShipAndDefenceBase.getShipAndDefenceBaseString(itemMap);
  }

  public String getDefencesString()
  {
    if (getDefenderCount() > 0)
    {
      return ShipAndDefenceBase.getShipAndDefenceBaseString(defenderList.get(0).getDefences());
    }
    return "";
  }

  public void addAttacker(final PlayerValues attacker)
  {
    attackerList.add(attacker); // TODO: handle changes
    attackerValues.update(attackerList);
  }

  public void addDefender(final PlayerValues defender)
  {
    defenderList.add(defender); // TODO: handle changes
    defenderValues.update(defenderList);
  }

  public void addPlayerValues(final PlayerValues player, final String type)
  {
    if (type.equals(PlayerValueTab.ATTACKER))
    {
      addAttacker(player);
    }
    else if (type.equals(PlayerValueTab.DEFENDER))
    {
      addDefender(player);
    }
    else
    {
      System.out.println("FightData::addPlayer(player,type): Something wrong!");
    }
  }

  public int getAttackerCount()
  {
    return attackerList.size();
  }

  public int getDefenderCount()
  {
    return defenderList.size();
  }

  public int getCount(final String type)
  {
    if (type.equals(PlayerValueTab.ATTACKER))
    {
      return getAttackerCount();
    }
    else if (type.equals(PlayerValueTab.DEFENDER))
    {
      return getDefenderCount();
    }
    else
    {
      System.out.println("FightData::getCount(type): Something wrong!");
    }
    return -1;
  }

  public ObservableList<PlayerValues> getAttackerList()
  {
    return attackerList;
  }

  public ObservableList<PlayerValues> getDefenderList()
  {
    return defenderList;
  }

  public FightValues getAttackerValues()
  {
    attackerValues.update(attackerList);
    return attackerValues;
  }

  public FightValues getDefenderValues()
  {
    defenderValues.update(defenderList);
    return defenderValues;
  }

  public FightValues getAttackerValuesCopy()
  {
    attackerValuesCopy.update(attackerListCopy);
    return attackerValuesCopy;
  }

  public FightValues getDefenderValuesCopy()
  {
    defenderValuesCopy.update(defenderListCopy);
    return defenderValuesCopy;
  }

  public void setShips(final Ships ships)
  {
    this.ships = ships;
  }

  public void updateMainSpinners(final MainSpinners spinners)
  {
    attackerValues.update(attackerList); // TODO: replace by property bindings
    defenderValues.update(defenderList);
    spinners.attackerWeaponTechSpinner.getValueFactory().setValue((int) Math.round(attackerValues.weapontech));
    spinners.attackerArmorTechSpinner.getValueFactory().setValue((int) Math.round(attackerValues.armortech));
    spinners.attackerShieldTechSpinner.getValueFactory().setValue((int) Math.round(attackerValues.shieldtech));
    spinners.attackerRegenaTechSpinner.getValueFactory().setValue((int) Math.round(attackerValues.regenatech));
    if (spinners.showSum)
    {
      spinners.attackerSpinners.weapons.getValueFactory().setValue(attackerValues.weapons);
      spinners.attackerSpinners.structure.getValueFactory().setValue(attackerValues.structure);
      spinners.attackerSpinners.shield.getValueFactory().setValue(attackerValues.shield);
      spinners.attackerSpinners.heal.getValueFactory().setValue(attackerValues.heal);
    }
    else
    {
      spinners.attackerSpinners.weapons.getValueFactory().setValue(attackerValues.weapons - (defenderValues.structure + defenderValues.shield));
      spinners.attackerSpinners.structure.getValueFactory().setValue((attackerValues.structure + attackerValues.shield) - defenderValues.weapons);
      spinners.attackerSpinners.shield.getValueFactory().setValue((attackerValues.structure + attackerValues.shield) - defenderValues.weapons);
      spinners.attackerSpinners.heal.getValueFactory().setValue(attackerValues.heal - defenderValues.weapons);
    }
    spinners.attackerSpinners.capacity.getValueFactory().setValue(attackerValues.capacity);
    spinners.defenderWeaponTechSpinner.getValueFactory().setValue((int) Math.round(defenderValues.weapontech));
    spinners.defenderArmorTechSpinner.getValueFactory().setValue((int) Math.round(defenderValues.armortech));
    spinners.defenderShieldTechSpinner.getValueFactory().setValue((int) Math.round(defenderValues.shieldtech));
    spinners.defenderRegenaTechSpinner.getValueFactory().setValue((int) Math.round(defenderValues.regenatech));
    spinners.defenderSpinners.weapons.getValueFactory().setValue(defenderValues.weapons);
    spinners.defenderSpinners.structure.getValueFactory().setValue(defenderValues.structure);
    spinners.defenderSpinners.shield.getValueFactory().setValue(defenderValues.shield);
    spinners.defenderSpinners.heal.getValueFactory().setValue(defenderValues.heal);
  }

}
