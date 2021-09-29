/**
 *
 */
package data;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import properties.FightSimProperties;

/**
 * @author AAG
 *
 */
public class DebrisField
{
  public double titan = 0;
  public double silizium = 0;
  public double pvc = 0;

  public DebrisField()
  {
  }

  public DebrisField(final double titan, final double silizium, final double pvc)
  {
    super();
    this.titan = titan;
    this.silizium = silizium;
    this.pvc = pvc;
  }

  public void plus(final DebrisField other)
  {
    titan += other.titan;
    silizium += other.silizium;
    pvc += other.pvc;
  }

  public static DebrisField getDebrisFieldShips(final ObservableList<PlayerValues> initValues, final ObservableList<PlayerValues> resultValues)
  {
    final DebrisField debrisField = new DebrisField();
    if (initValues.size() != resultValues.size())
    {
      System.out.println("getShipEXP(): Lists are not matching!");
      return debrisField;
    }
    for (int i = 0; i < initValues.size(); i++)
    {
      debrisField.plus(getDebrisField(initValues.get(i).getShips(), resultValues.get(i).getShips(), FightSimProperties.getInstance().getDfFactorShips()));
    }
    return debrisField;
  }

  public static DebrisField getDefencesDebrisField(final ObservableMap<ShipAndDefenceBase, Integer> originalMap, final ObservableMap<ShipAndDefenceBase, Integer> currentMap)
  {
    return getDebrisField(originalMap, currentMap, FightSimProperties.getInstance().getDfFactorDefence());
  }

  public static DebrisField getDebrisField(final ObservableMap<ShipAndDefenceBase, Integer> originalMap, final ObservableMap<ShipAndDefenceBase, Integer> currentMap, final double factor)
  {
    double titan = 0;
    double silizium = 0;
    double pvc = 0;
    if ((originalMap != null) && (currentMap != null))
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : currentMap.entrySet())
      {
        if (originalMap.containsKey(pair.getKey()))
        {
          final int destroyed = originalMap.get(pair.getKey()) - pair.getValue();
          final double forTF = Math.ceil(destroyed * factor);
          titan = titan + (forTF * pair.getKey().titanProperty().get());
          silizium = silizium + (forTF * pair.getKey().siliziumProperty().get());
          pvc = pvc + (forTF * pair.getKey().pvcProperty().get());
        }
        else
        {
          System.out.println("Something wrong computing debris field!");
        }
      }
    }
    return new DebrisField(titan, silizium, pvc);
  }
}
