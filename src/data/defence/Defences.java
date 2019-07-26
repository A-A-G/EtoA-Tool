package data.defence;

import static java.util.stream.Collectors.toList;

import data.DataHandler;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import utils.StringUtils;

public class Defences extends DataHandler<Defence>
{
  private static final String INITIAL_FILENAME = "deffs.rod";

  public Defences()
  {
    this(null);
  }

  public Defences(final Label statusLabel)
  {
    super(statusLabel, "Defences", INITIAL_FILENAME);
  }

  @Override
  public void update(final String text)
  {
    final String lines[] = text.split("[\\r\\n]+");
    String lastLine = null;
    String name = null;
    int titan = -1;
    int silizium = -1;
    int pvc = -1;
    int tritium = -1;
    int food = -1;
    int weapons = -1;
    int structure = -1;
    int shield = -1;
    int healing = -1;
    int fields = -1;
    int maxNumber = -1;
    for (final String line : lines)
    {
      if (!StringUtils.isNullOrBlank(lastLine) && line.contains("Verteidigung\t"))
      {
        name = lastLine;
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
      else if (line.contains("Schusskraft\t"))
      {
        weapons = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Struktur\t"))
      {
        structure = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Abwehrschild\t"))
      {
        shield = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Reparatur\t"))
      {
        healing = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Platzverbrauch\t"))
      {
        fields = StringUtils.getTabSeparatedInt(line);
      }
      else if (line.contains("Max. Anzahl\t"))
      {
        maxNumber = StringUtils.getTabSeparatedInt(line);
        break; // do not parse more!
      }
      else
      {
        lastLine = line;
      }
    }
    // System.out.println(name + " | " + titan + " | " + silizium + " | " + pvc + " | " + tritium + " | " + food + " | " + weapons + " | " + structure + " | " + shield + " | " + healing + " | " + fields + " | " + maxNumber);
    final Defence defence = new Defence(name, titan, silizium, pvc, tritium, food, weapons, structure, shield, healing, fields, maxNumber);
    if (!defence.isValid())
    {
      updateStatus("Fehlerhafte Daten. Konnte Verteidigungsanlage nicht hinzufügen.");
    }
    else
    {
      if (data.contains(defence))
      {
        if (data.get(data.indexOf(defence)).update(defence))
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
        data.add(defence);
        updateStatus(name + " hinzugefügt.");
      }
    }
  }

  @Override
  public ObservableList<Defence> getObservableSelectionList()
  {
    return FXCollections.observableList(data.stream().map(item -> item).map(Defence::new).collect(toList()), item -> new Observable[] { item.selectedProperty() });
  }

  @Override
  public ObservableList<Defence> getFullObservableList()
  {
    return FXCollections.observableList(data, dataItem -> dataItem.getAllObservables());
  }

}
