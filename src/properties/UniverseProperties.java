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
public class UniverseProperties extends Properties
{
  private static final long serialVersionUID = -4032043818790625163L;

  // Keys
  public static final String CONFIG_PATH = "config";
  public static final String X_CELLS = "x_cells";
  public static final String Y_CELLS = "y_cells";
  public static final String CELL_LENGTH = "cell_length";
  public static final String MAX_NUMBER_PLANETS = "max_num_planets";
  public static final String RAK_RANGE = "rak_range";
  public static final String KRYPTO_RANGE = "krypto_range";

  // Default values
  private static final String DEFAULT_CONFIG_PATH = "uni.properties";
  private static final String DEFAULT_X_CELLS = "10";
  private static final String DEFAULT_Y_CELLS = "10";
  private static final String DEFAULT_CELL_LENGTH = "300";
  private static final String DEFAULT_MAX_NUMBER_PLANETS = "25";
  private static final String DEFAULT_RAK_RANGE = "3000";
  private static final String DEFAULT_KRYPTO_RANGE = "700";

  // Singleton (optional)
  private static UniverseProperties singleton = null;

  public static UniverseProperties getInstance()
  {
    if (singleton == null)
    {
      singleton = new UniverseProperties();
    }
    return singleton;
  }

  /**
   * Load and update universe properties.
   */
  public UniverseProperties()
  {
    super();
    final File file = new File(DEFAULT_CONFIG_PATH);
    if (file.exists() && !file.isDirectory())
    {
      try
      {
        load(new FileInputStream(file));
        System.out.println("Universe properties loaded.");
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
    if (getProperty(X_CELLS) == null)
    {
      setProperty(X_CELLS, DEFAULT_X_CELLS);
    }
    if (getProperty(Y_CELLS) == null)
    {
      setProperty(Y_CELLS, DEFAULT_Y_CELLS);
    }
    if (getProperty(CELL_LENGTH) == null)
    {
      setProperty(CELL_LENGTH, DEFAULT_CELL_LENGTH);
    }
    if (getProperty(MAX_NUMBER_PLANETS) == null)
    {
      setProperty(MAX_NUMBER_PLANETS, DEFAULT_MAX_NUMBER_PLANETS);
    }
    if (getProperty(RAK_RANGE) == null)
    {
      setProperty(RAK_RANGE, DEFAULT_RAK_RANGE);
    }
    if (getProperty(KRYPTO_RANGE) == null)
    {
      setProperty(KRYPTO_RANGE, DEFAULT_KRYPTO_RANGE);
    }
    try (OutputStream out = new FileOutputStream(DEFAULT_CONFIG_PATH))
    {
      store(out, "Universe Properties");
      System.out.println("Universe properties updated.\n");
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

  public int getXCells()
  {
    return Integer.parseInt(getProperty(X_CELLS));
  }

  public int getYCells()
  {
    return Integer.parseInt(getProperty(Y_CELLS));
  }

  public double getCellLength()
  {
    return Double.parseDouble(getProperty(CELL_LENGTH));
  }

  public int getMaxNumberPlanets()
  {
    return Integer.parseInt(getProperty(MAX_NUMBER_PLANETS));
  }

  public int getRakRange()
  {
    return Integer.parseInt(getProperty(RAK_RANGE));
  }

  public int getKryptoRange()
  {
    return Integer.parseInt(getProperty(KRYPTO_RANGE));
  }

}
