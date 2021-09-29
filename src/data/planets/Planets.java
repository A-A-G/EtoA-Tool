/**
 * 
 */
package data.planets;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import data.DataHandler;
import gui.application.EtoATool;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import properties.AppProperties;
import utils.Comparators;

/**
 * @author AAG
 *
 */
public class Planets extends DataHandler<Planet>
{

  public static final String NO_OWNER = "Niemand";
  public static final String EMPTY_SPACE = "Leerer Raum";
  public static final String STAR = "Stern";
  public static final String ASTEROID = "Asteroidenfeld";
  public static final String WORMHOLE = "Wurmloch";

  private static final String INITIAL_FILENAME = "planets.rod";

  private ObservableList<String> player = null;

  public Planets()
  {
    this(null);
  }

  public Planets(final Label statusLabel)
  {
    super(statusLabel, "Planets", INITIAL_FILENAME);
  }

  @Override
  public void update(String text)
  {
    String system = "";
    text = text.replaceAll("[\\r\\n]+Tr\\S*\t", "\t");
    text = text.replaceAll("[\\r\\n]+Ziel: ", "\t");
    final String lines[] = text.split("[\\r\\n]+");
    int planetCounter = 0;
    int changed = 0;
    int added = 0;
    for (String line : lines)
    {
      if (line.matches("System [0-9][0-9]?/[0-9][0-9]? : [0-9][0-9]?/[0-9][0-9]?"))
      {
        line = line.replace("System ", "");
        line = line.replace(" ", "");
        system = line;
      }
      else if (line.contains("icon"))
      {
        line = line.replace("icon", "");
        final String tokens[] = line.trim().split("\\t");
        Planet newPlanet = null;
        if (tokens[1].contains(WORMHOLE))
        {
          newPlanet = new Planet(tokens[2].replaceAll(" ", ""), tokens[4], system + ":" + tokens[0], tokens[1]);
        }
        else
        {
          newPlanet = new Planet(tokens[2], tokens[3], system + ":" + tokens[0], tokens[1]);
        }
        if (data.contains(newPlanet))
        {
          if (data.get(data.indexOf(newPlanet)).update(newPlanet))
          {
            changed++;
          }
        }
        else
        {
          data.add(newPlanet);
          added++;
        }
        planetCounter++;
      }
    }
    updateStatus(system + ": " + planetCounter + " Planeten hinzugefügt/aktualisiert (Changed: " + changed + ", Added: " + added + ").");
  }
  
  public long getPlayerPlanetsCount()
  {
    if (data == null)
    {
      return 0;
    }
    return data.stream().filter(p -> !p.getOwner().equals(NO_OWNER)).count();
  }
  
  public int getPlayerCount()
  {
    if (player == null)
    {
      fillPlayer();
    }
    return player.size();
  }

  public ObservableList<String> getPlayer()
  {
    if (player == null)
    {
      fillPlayer();
    }
    return player;
  }

  private void fillPlayer()
  {
    player = FXCollections.observableArrayList();
    if (data == null)
    {
      data = FXCollections.observableArrayList();
    }
    else
    {
      for (final Planet p : getData())
      {
        putPlayer(p.getOwner());
      }
    }
    data.addListener((ListChangeListener<Planet>) change ->
    {
      while (change.next())
      {
        if (change.wasUpdated())
        {
          for (final Planet p : change.getList())
          {
            putPlayer(p.getOwner());
          }
        }
        else if (change.wasAdded())
        {
          for (final Planet p : change.getAddedSubList())
          {
            putPlayer(p.getOwner());
          }
        }
      }
    });
  }

  private void putPlayer(final String owner)
  {
    if (!owner.equals(NO_OWNER) && !player.contains(owner))
    {
      player.add(owner);
    }
  }

  public String checkForMissingSystems()
  {
    final List<String> systems = new ArrayList<>();
    for (int x = 1; x <= Planet.GALAXY_SIZE; x++)
    {
      for (int y = 1; y <= Planet.GALAXY_SIZE; y++)
      {
        for (int i = 1; i <= 10; i++)
        {
          for (int j = 1; j <= 10; j++)
          {
            systems.add(x + "/" + y + ":" + i + "/" + j + ":0");
          }
        }
      }
    }
    for (final Planet p : data)
    {
      if (systems.contains(p.getCoords()))
      {
        systems.remove(p.getCoords());
        if (systems.isEmpty())
        {
          break;
        }
      }
    }
    if (systems.isEmpty())
    {
      return "none";
    }
    return String.join(", ", systems);
  }

  public void exportForumCode()
  {
	Properties properties = EtoATool.getAppProperties();
	String round = properties.getProperty(AppProperties.ROUND);
	String encryption = properties.getProperty(AppProperties.ENCRYPTION);
	String domain = properties.getProperty(AppProperties.DOMAIN);
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    fileChooser.setInitialFileName("forum.txt");
    final File file = fileChooser.showSaveDialog(EtoATool.getPrimaryStage());
    if (file == null)
    {
      return;
    }
    try (final PrintWriter pwriter = new PrintWriter(file.getAbsolutePath()))
    {
      pwriter.println("[font=\"DejaVu Sans Mono\"][size=11]xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
      final FilteredList<Planet> planets2filter = new FilteredList<>(FXCollections.observableArrayList(getData()), p -> true);
      final SortedList<Planet> planets2sort = FXCollections.observableArrayList(getData()).sorted();
      int playerCounter = 1;
      final String tokens[] = { "  oo", "xx  ", "n.v." }; // blank is U+2000
      for (final String player : FXCollections.observableArrayList(getPlayer()).sorted(Comparators.playerComparator()))
      {
        planets2filter.setPredicate(planet ->
        {
          if (planet.getOwner().equals(player))
          {
            return true;
          }
          return false;
        });
        String output = String.format("%1$-19s\t", player).replace(" ", " ") + "|\t";
        int planetCounter = 1;
        for (final Planet p : planets2filter)
        {
          output = output + "[url=" + encryption + "://" + round + "." + domain + "/?page=cell&id=" + p.getID() + "&hl=" + p.getHL(planets2sort) + "]" + tokens[playerCounter % 2] + "[/url]\t";
          if ((planetCounter % 5) == 0)
          {
            output = output + "|\t";
          }
          planetCounter++;
        }
        for (; planetCounter <= 15; planetCounter++)
        {
          output = output + "[url=" + encryption + "://" + round + "." + domain + "/?page=cell&id=0&hl=0]" + tokens[2] + "[/url]\t";
          if ((planetCounter % 5) == 0)
          {
            output = output + "|\t";
          }
        }
        // System.out.println(output);
        pwriter.println(output);
        if ((playerCounter % 10) == 0)
        {
          pwriter.println("-------------------\t|\t----\t----\t----\t----\t----\t|\t----\t----\t----\t----\t----\t|\t----\t----\t----\t----\t----\t|");
        }
        if ((playerCounter % 50) == 0)
        {
          pwriter.println();
          pwriter.println("----------split here | split here | split here | split here | split here ----------");
          pwriter.println();
          pwriter.println("[font=\"DejaVu Sans Mono\"][size=11]");
          pwriter.println("-------------------\t|\t----\t----\t----\t----\t----\t|\t----\t----\t----\t----\t----\t|\t----\t----\t----\t----\t----\t|");
        }
        playerCounter++;
      }
      updateStatus("Forum code export to " + file.getAbsolutePath() + " successful.");
    }
    catch (final IOException e)
    {
      e.printStackTrace();
      updateStatus("Can't export forum code: " + e);
    }

  }

  @Override
  public ObservableList<Planet> getObservableSelectionList()
  {
    return FXCollections.observableList(data.stream().map(item -> item).map(Planet::new).collect(toList()), item -> new Observable[] { item.selectedProperty() });
  }

  @Override
  public ObservableList<Planet> getFullObservableList()
  {
    return FXCollections.observableList(data, dataItem -> dataItem.getAllObservables());
  }

}
