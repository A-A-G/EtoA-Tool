/**
 *
 */
package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author AAG
 *
 */
public class FightSimProperties extends Properties
{
  private static final long serialVersionUID = 4703049888419932050L;

  // Keys
  public static final String CONFIG_PATH = "config";
  // FightSimulation
  public static final String MAX_LOOT = "max_loot";
  public static final String MAX_LOOT_ADD = "max_loot_add";
  public static final String MAX_HEAL = "max_heal";
  // DebrisField
  public static final String DF_FACTOR_SHIPS = "tf_ships";
  public static final String DF_FACTOR_DEFENCE = "tf_defence";
  // PlayerValues
  public static final String DEFENCE_REPAIR = "defence_repair_base";
  public static final String CIVIL_SHIPS_RESTORE = "civil_ships_restore";

  // Default values
  private static final String DEFAULT_CONFIG_PATH = "sim.properties";
  // FightSimulation
  private static final String DEFAULT_MAX_LOOT = "0.37";
  private static final String DEFAULT_MAX_LOOT_ADD = "0.25";
  private static final String DEFAULT_MAX_HEAL = "1";
  // DebrisField
  public static final String DEFAULT_DF_FACTOR_SHIPS = "0.6";
  public static final String DEFAULT_DF_FACTOR_DEFENCE = "0.5";
  // PlayerValues
  public static final String DEFAULT_DEFENCE_REPAIR = "40";
  public static final String DEFAULT_CIVIL_SHIPS_RESTORE = "0.8";

  // Singleton (optional)
  private static FightSimProperties singleton = null;

  public static FightSimProperties getInstance()
  {
    if (singleton == null)
    {
      singleton = new FightSimProperties();
    }
    return singleton;
  }

  /**
   * Load and update fight simulation properties.
   */
  public FightSimProperties()
  {
    super();
    final File file = new File(DEFAULT_CONFIG_PATH);
    if (file.exists() && !file.isDirectory())
    {
      try
      {
        load(new FileInputStream(file));
        System.out.println("Fight simulator properties loaded.");
      }
      catch (final FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (final IOException e)
      {
        e.printStackTrace();
      }
    }
    if (getProperty(MAX_LOOT) == null)
    {
      setProperty(MAX_LOOT, DEFAULT_MAX_LOOT);
    }
    if (getProperty(MAX_LOOT_ADD) == null)
    {
      setProperty(MAX_LOOT_ADD, DEFAULT_MAX_LOOT_ADD);
    }
    if (getProperty(MAX_HEAL) == null)
    {
      setProperty(MAX_HEAL, DEFAULT_MAX_HEAL);
    }
    if (getProperty(DF_FACTOR_SHIPS) == null)
    {
      setProperty(DF_FACTOR_SHIPS, DEFAULT_DF_FACTOR_SHIPS);
    }
    if (getProperty(DF_FACTOR_DEFENCE) == null)
    {
      setProperty(DF_FACTOR_DEFENCE, DEFAULT_DF_FACTOR_DEFENCE);
    }
    if (getProperty(DEFENCE_REPAIR) == null)
    {
      setProperty(DEFENCE_REPAIR, DEFAULT_DEFENCE_REPAIR);
    }
    if (getProperty(CIVIL_SHIPS_RESTORE) == null)
    {
      setProperty(CIVIL_SHIPS_RESTORE, DEFAULT_CIVIL_SHIPS_RESTORE);
    }
    try (OutputStream out = new FileOutputStream(DEFAULT_CONFIG_PATH))
    {
      store(out, "Fight Simulator Properties");
      System.out.println("Fight simulator properties updated.\n");
    }
    catch (final FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (final IOException e)
    {
      e.printStackTrace();
    }
  }

  public double getMaxLoot()
  {
    return Double.parseDouble(getProperty(MAX_LOOT));
  }

  public double getMaxLootAdd()
  {
    return Double.parseDouble(getProperty(MAX_LOOT_ADD));
  }

  public double getMaxHeal()
  {
    return Double.parseDouble(getProperty(MAX_HEAL));
  }

  public double getDfFactorShips()
  {
    return Double.parseDouble(getProperty(DF_FACTOR_SHIPS));
  }

  public double getDfFactorDefence()
  {
    return Double.parseDouble(getProperty(DF_FACTOR_DEFENCE));
  }

  public double getDefenceRepair()
  {
    return Double.parseDouble(getProperty(DEFENCE_REPAIR));
  }

  public double getCivilShipsRestore()
  {
    return Double.parseDouble(getProperty(CIVIL_SHIPS_RESTORE));
  }

}
