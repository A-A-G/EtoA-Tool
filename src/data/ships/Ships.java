/**
 * 
 */
package data.ships;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.DataHandler;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import utils.StringUtils;

/**
 * @author AAG
 *
 */
public class Ships extends DataHandler<Ship>
{
  // public static final List<String> NO_LOOT_SHIPS = new ArrayList<>(Arrays.asList("AIN Sonde", "AURORA Sonde")); // R19
  public static final List<String> NO_LOOT_SHIPS = new ArrayList<>(); // R20
  public static final List<String> INCREASED_LOOT_SHIPS = new ArrayList<>(Arrays.asList("ORION Fighter"));
  public static final String SAME_CAPACITY_TRANSPORTER_SHIPS = "EOS, ATLAS, SAIPH";
  public static final String SAME_CAPACITY_TRANSPORTER_EXAMPLE = "EOS Transporter";
  public static final List<String> TRANSPORTER_SHIPS = new ArrayList<>(Arrays.asList("AIN Sonde", "AURORA Sonde", "DEMETER Transporter", SAME_CAPACITY_TRANSPORTER_SHIPS, "LORIAL Transportschiff", "TITAN Transporter"));

  public static final String CIVIL_SHIPS = "Ziviles Schiff";
  public static final String WAR_SHIPS = "Kriegsschiff";
  public static final String CIVIL_RACE_SHIPS = "Ziviles Rassenschiff";
  public static final String RACE_SHIPS = "Rassenspezifisches Schiff";
  public static final String SPECIAL_SHIPS = "Episches Schiff";
  public static final String COLLECT_SHIPS = "Sammlerschiff";

  private static final String INITIAL_FILENAME = "ships.rod";

  public Ships()
  {
    this(null);
  }

  public Ships(final Label statusLabel)
  {
    super(statusLabel, "Ships", INITIAL_FILENAME);
  }

  @Override
  public void update(final String text)
  {
    final String lines[] = text.split("[\\r\\n]+");
    String lastLine = null;
    String name = null;
    String category = null;
    int pilots = -1;
    int titan = -1;
    int silizium = -1;
    int pvc = -1;
    int tritium = -1;
    int food = -1;
    int starting = -1;
    int landing = -1;
    int usagePerAE = -1;
    int weapons = -1;
    int structure = -1;
    int shield = -1;
    int healing = -1;
    int capacity = -1;
    int passengers = -1;
    int speed = -1;
    int startDuration = -1;
    int landDuration = -1;
    for (final String line : lines)
    {
      if (!StringUtils.isNullOrBlank(lastLine) && line.contains("Schiff\t"))
      {
        name = lastLine;
      }
      else if (line.contains("Kategorie:"))
      {
        final String tokens[] = line.trim().split("\\t");
        category = tokens[1];
      }
      else if (line.contains("Piloten"))
      {
        pilots = StringUtils.getTabSeparatedInt(line.replace("Anzahl ", ""));
      }
      else if (line.contains("Titan\t"))
      {
        titan = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Silizium\t"))
      {
        silizium = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("PVC\t"))
      {
        pvc = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Tritium\t"))
      {
        tritium = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Nahrung\t"))
      {
        food = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Start\t"))
      {
        starting = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Landung\t"))
      {
        landing = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("/100 AE\t"))
      {
        usagePerAE = StringUtils.getTabSeparatedInt(line.replace("/100 ", ""));
      }
      else if (line.contains("Waffen\t"))
      {
        weapons = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Struktur\t"))
      {
        structure = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Schutzschild\t"))
      {
        shield = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Heilung\t"))
      {
        healing = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Laderaum\t"))
      {
        capacity = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Passagierraum\t"))
      {
        passengers = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Geschwindigkeit\t"))
      {
        speed = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Startdauer\t"))
      {
        startDuration = StringUtils.getTabSeparatedSeconds(line);
      }
      else if (line.contains("Landedauer\t"))
      {
        landDuration = StringUtils.getTabSeparatedSeconds(line);
        break; // do not parse more!
      }
      else
      {
        lastLine = line;
      }
    }
    // System.out.println(name + " | " + category + " | " + pilots + " | " + titan + " | " + silizium + " | " + pvc + " | " + tritium + " | " + food + " | " + starting + " | " + landing + " | " + usagePerAE + " | " + weapons + " | " + structure + " | " + shield + " | " + healing + " | " + capacity + " | " + passengers + " | " + speed + " | " + startDuration + " | " + landDuration);
    final Ship ship = new Ship(name, category, pilots, titan, silizium, pvc, tritium, food, starting, landing, usagePerAE, weapons, structure, shield, healing, capacity, passengers, speed, startDuration, landDuration);
    if (!ship.isValid())
    {
      updateStatus("Fehlerhafte Daten. Konnte Schiff nicht hinzufügen.");
    }
    else
    {
      if (data.contains(ship))
      {
        if (data.get(data.indexOf(ship)).update(ship))
        {
          updateStatus(name + " aktualisiert.");
        }
        else
        {
          updateStatus("Keine Aktualisierungen für " + name + " gefunden!");
        }
      }
      else
      {
        data.add(ship);
        updateStatus(name + " hinzugefügt.");
      }
    }
  }

  @Override
  public ObservableList<Ship> getObservableSelectionList()
  {
    return FXCollections.observableList(data.stream().map(item -> item).map(Ship::new).collect(toList()), item -> new Observable[] { item.selectedProperty() });
  }

  @Override
  public ObservableList<Ship> getFullObservableList()
  {
    return FXCollections.observableList(data, Ship::getAllObservables);
  }

}
