/**
 *
 */
package data;

import gui.tabs.EtoATab;
import gui.utils.Dialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert.AlertType;
import utils.StringUtils;

/**
 * @author AAG
 *
 */
public class SpyReport
{
  private String coordsAndName = "";
  private String owner = "";

  private int weapontech = 0;
  private int armortech = 0;
  private int shieldtech = 0;
  private int regenatech = 0;

  private long titan = 0;
  private long silizium = 0;
  private long pvc = 0;
  private long tritium = 0;
  private long nahrung = 0;

  private final ObservableMap<String, Integer> shipsMap = FXCollections.observableHashMap();
  private final ObservableMap<String, Integer> defencesMap = FXCollections.observableHashMap();

  private boolean analysed = false;

  public SpyReport()
  {

  }

  public SpyReport(final String spyReport)
  {
    analyseSpyReport(spyReport, null);
  }

  public SpyReport(final String spyReport, final EtoATab etab)
  {
    analyseSpyReport(spyReport, etab);
  }

  public void analyseSpyReport(final String spyReport)
  {
    analyseSpyReport(spyReport, null);
  }

  public void analyseSpyReport(final String spyReport, final EtoATab etab)
  {
    clear();
    boolean techFound = false;
    boolean shipLoop = false;
    boolean shipsFound = false;
    boolean deffLoop = false;
    boolean deffFound = false;
    boolean ressLoop = false;
    boolean ressFound = false;
    final String lines[] = spyReport.split("[\\r\\n]");
    for (final String line : lines)
    {
      if (line.contains("Planet:"))
      {
        coordsAndName = line.substring(line.indexOf(" ") + 1);
        if (etab != null)
        {
          etab.updateStatus("Reading spy report from " + owner + " " + coordsAndName + ".");
        }
      }
      else if (line.contains("Besitzer:"))
      {
        owner = line.substring(line.indexOf(" ") + 1);
        if (etab != null)
        {
          etab.updateStatus("Reading spy report from " + owner + " " + coordsAndName + ".");
        }
      }
      else if (line.contains("Waffentechnik"))
      {
        weapontech = StringUtils.getTabSeparatedInt(line);
        techFound = true;
      }
      else if (line.contains("Panzerung"))
      {
        armortech = StringUtils.getTabSeparatedInt(line);
        techFound = true;
      }
      else if (line.contains("Schutzschilder"))
      {
        shieldtech = StringUtils.getTabSeparatedInt(line);
        techFound = true;
      }
      else if (line.contains("Regenatechnik"))
      {
        regenatech = StringUtils.getTabSeparatedInt(line);
        techFound = true;
      }
      else if (line.contains("SCHIFFE:"))
      {
        shipLoop = true;
      }
      else if (shipLoop)
      {
        if (line.contains("Nichts vorhanden!"))
        {
          shipLoop = false;
          shipsFound = true;
        }
        else if (line.isEmpty() && shipsFound)
        {
          shipLoop = false;
        }
        else
        {
          StringUtils.addTabSeparatedPair(shipsMap, line);
          shipsFound = true;
        }
      }
      else if (line.contains("VERTEIDIGUNG:"))
      {
        deffLoop = true;
      }
      else if (deffLoop)
      {
        if (line.contains("Nichts vorhanden!"))
        {
          deffLoop = false;
          deffFound = true;
        }
        else if (line.isEmpty() && deffFound)
        {
          deffLoop = false;
        }
        else
        {
          StringUtils.addTabSeparatedPair(defencesMap, line);
          deffFound = true;
        }
      }
      else if (line.contains("ROHSTOFFE:"))
      {
        ressLoop = true;
      }
      else if (ressLoop)
      {
        if (line.isEmpty() && ressFound)
        {
          ressLoop = false;
        }
        else if (line.contains("Titan"))
        {
          titan = StringUtils.getTabSeparatedLong(line);
          ressFound = true;
        }
        else if (line.contains("Silizium"))
        {
          silizium = StringUtils.getTabSeparatedLong(line);
          ressFound = true;
        }
        else if (line.contains("PVC"))
        {
          pvc = StringUtils.getTabSeparatedLong(line);
          ressFound = true;
        }
        else if (line.contains("Tritium"))
        {
          tritium = StringUtils.getTabSeparatedLong(line);
          ressFound = true;
        }
        else if (line.contains("Nahrung"))
        {
          nahrung = StringUtils.getTabSeparatedLong(line);
          ressFound = true;
        }
      }
    }
    String errors = "";
    if (!techFound)
    {
      errors = errors + "Techs not found!\n";
    }
    if (!shipsFound)
    {
      errors = errors + "Ships not found!\n";
    }
    if (!deffFound)
    {
      errors = errors + "Defences not found\n";
    }
    if (!ressFound)
    {
      errors = errors + "Ressources not found!\n";
    }
    if (!errors.isEmpty())
    {
      Dialogs.buildAlert(AlertType.ERROR, "EtoA-Tool", "Spy report not complete!", errors).showAndWait();
    }
    if (etab != null)
    {
      etab.updateStatus("Analyzed spy report from " + coordsAndName + ".");
    }
//    System.out.println(coordsAndName + " " + weapontech + " " + armortech + " " + shieldtech + " " + regenatech + " " + techFound + " " + shipsFound + " " + deffFound + " " + ressFound + " " + titan + " " + silizium + " " + pvc + " " + tritium + " " + nahrung);
    analysed = true;
  }

  private void clear()
  {
    coordsAndName = "";
    weapontech = 0;
    armortech = 0;
    shieldtech = 0;
    regenatech = 0;
    titan = 0;
    silizium = 0;
    pvc = 0;
    tritium = 0;
    nahrung = 0;
    shipsMap.clear();
    defencesMap.clear();
  }

  public String getCoordsAndName()
  {
    return coordsAndName;
  }

  public String getOwner()
  {
    return owner;
  }

  public int getWeapontech()
  {
    return weapontech;
  }

  public int getArmortech()
  {
    return armortech;
  }

  public int getShieldtech()
  {
    return shieldtech;
  }

  public int getRegenatech()
  {
    return regenatech;
  }

  public long getTitan()
  {
    return titan;
  }

  public long getSilizium()
  {
    return silizium;
  }

  public long getPvc()
  {
    return pvc;
  }

  public long getTritium()
  {
    return tritium;
  }

  public long getNahrung()
  {
    return nahrung;
  }

  public ObservableMap<String, Integer> getShipsMap()
  {
    return shipsMap;
  }

  public ObservableMap<String, Integer> getDefencesMap()
  {
    return defencesMap;
  }

  public boolean isAnalysed()
  {
    return analysed;
  }
}
