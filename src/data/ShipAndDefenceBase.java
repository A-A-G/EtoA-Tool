/**
 *
 */
package data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

import data.ships.Ship;
import data.ships.Ships;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.ObservableMap;

/**
 * @author AAG
 *
 */
public abstract class ShipAndDefenceBase extends EtoAItem implements Comparable<ShipAndDefenceBase>, Serializable
{
  private static final long serialVersionUID = -6900786368240710048L;

  // Builing costs
  protected IntegerProperty titan;
  protected IntegerProperty silizium;
  protected IntegerProperty pvc;
  protected IntegerProperty tritium;
  protected IntegerProperty food;

  // Fight values
  protected LongProperty weapons;
  protected LongProperty structure;
  protected LongProperty shield;
  protected IntegerProperty heal;

  public ShipAndDefenceBase(final String name, final int titan, final int silizium, final int pvc, final int tritium, final int food, final long weapons, final long structure, final long shield, final int heal)
  {
    super(name);

    this.titan = new SimpleIntegerProperty(titan);
    this.silizium = new SimpleIntegerProperty(silizium);
    this.pvc = new SimpleIntegerProperty(pvc);
    this.tritium = new SimpleIntegerProperty(tritium);
    this.food = new SimpleIntegerProperty(food);

    this.weapons = new SimpleLongProperty(weapons);
    this.structure = new SimpleLongProperty(structure);
    this.shield = new SimpleLongProperty(shield);
    this.heal = new SimpleIntegerProperty(heal);
  }

  @Override
  public boolean isValid()
  {
    if (titan.get() < 0)
    {
      return false;
    }
    if (silizium.get() < 0)
    {
      return false;
    }
    if (pvc.get() < 0)
    {
      return false;
    }
    if (tritium.get() < 0)
    {
      return false;
    }
    if (food.get() < 0)
    {
      return false;
    }

    if (weapons.get() < 0)
    {
      return false;
    }
    if (structure.get() < 0)
    {
      return false;
    }
    if (shield.get() < 0)
    {
      return false;
    }
    if (heal.get() < 0)
    {
      return false;
    }
    return super.isValid();
  }

  public boolean update(final ShipAndDefenceBase other)
  {
    boolean updated = super.update(other);
    if ((titan.get() != other.titan.get()))
    {
      titan.set(other.titan.get());
      updated = true;
    }
    if ((silizium.get() != other.silizium.get()))
    {
      silizium.set(other.silizium.get());
      updated = true;
    }
    if ((pvc.get() != other.pvc.get()))
    {
      pvc.set(other.pvc.get());
      updated = true;
    }
    if ((tritium.get() != other.tritium.get()))
    {
      tritium.set(other.tritium.get());
      updated = true;
    }
    if ((food.get() != other.food.get()))
    {
      food.set(other.food.get());
      updated = true;
    }
    if ((weapons.get() != other.weapons.get()))
    {
      weapons.set(other.weapons.get());
      updated = true;
    }
    if ((structure.get() != other.structure.get()))
    {
      structure.set(other.structure.get());
      updated = true;
    }
    if ((shield.get() != other.shield.get()))
    {
      shield.set(other.shield.get());
      updated = true;
    }
    if ((heal.get() != other.heal.get()))
    {
      heal.set(other.heal.get());
      updated = true;
    }
    return updated;
  }

  public double getValue()
  {
    return getTotalCost() / 1000.0;
  }

  public double getRessPerWSS()
  {
    return Math.round((100 * getTotalCost()) / getWSS()) / 100.0;
  }

  public long getWSS()
  {
    return weapons.get() + structure.get() + shield.get();
  }

  public double getTotalCost()
  {
    return (double) titan.get() + silizium.get() + pvc.get() + tritium.get() + food.get();
  }

  @Override
  public boolean equals(final Object obj)
  {
    if ((obj == null) || (getClass() != obj.getClass()))
    {
      return false;
    }
    return Objects.equals(name.get(), ((ShipAndDefenceBase) obj).name.get());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(name.get());
  }

  @Override
  public int compareTo(final ShipAndDefenceBase o)
  {
    return name.get().compareTo(o.name.get());
  }

  private void writeObject(final ObjectOutputStream out) throws IOException
  {
    writeObjectData(out);
  }

  @Override
  protected void writeObjectData(final ObjectOutputStream out) throws IOException
  {
    super.writeObjectData(out);

    out.writeInt(titan.get());
    out.writeInt(silizium.get());
    out.writeInt(pvc.get());
    out.writeInt(tritium.get());
    out.writeInt(food.get());

    out.writeLong(weapons.get());
    out.writeLong(structure.get());
    out.writeLong(shield.get());
    out.writeInt(heal.get());
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    readObjectData(in);
  }

  @Override
  protected void readObjectData(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    super.readObjectData(in);

    titan = new SimpleIntegerProperty(in.readInt());
    silizium = new SimpleIntegerProperty(in.readInt());
    pvc = new SimpleIntegerProperty(in.readInt());
    tritium = new SimpleIntegerProperty(in.readInt());
    food = new SimpleIntegerProperty(in.readInt());

    weapons = new SimpleLongProperty(in.readLong());
    structure = new SimpleLongProperty(in.readLong());
    shield = new SimpleLongProperty(in.readLong());
    heal = new SimpleIntegerProperty(in.readInt());
  }

  public static int getShipsAndDefencePoints(final ObservableMap<ShipAndDefenceBase, Integer> observableMap)
  {
    double points = 0;
    for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : observableMap.entrySet())
    {
      points = points + (pair.getKey().getValue() * pair.getValue());
    }
    return (int) Math.ceil(points);
  }

  public IntegerProperty titanProperty()
  {
    return titan;
  }

  public IntegerProperty siliziumProperty()
  {
    return silizium;
  }

  public IntegerProperty pvcProperty()
  {
    return pvc;
  }

  public IntegerProperty tritiumProperty()
  {
    return tritium;
  }

  public IntegerProperty foodProperty()
  {
    return food;
  }

  public LongProperty weaponsProperty()
  {
    return weapons;
  }

  public LongProperty structureProperty()
  {
    return structure;
  }

  public LongProperty shieldProperty()
  {
    return shield;
  }

  public IntegerProperty healProperty()
  {
    return heal;
  }

  @Override
  public Observable[] getAllObservables()
  {
    return concatArrays(super.getAllObservables(), getObservables());
  }

  @Override
  public Observable[] getObservables()
  {
    return new Observable[] { titan, silizium, pvc, tritium, food, weapons, structure, shield, heal };
  }

  public static double getExperience(final ObservableMap<ShipAndDefenceBase, Integer> originalMap, final ObservableMap<ShipAndDefenceBase, Integer> currentMap)
  {
    double exp = 0;
    if ((originalMap != null) && (currentMap != null))
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : currentMap.entrySet())
      {
        if (originalMap.containsKey(pair.getKey()))
        {
          if ((!(pair.getKey() instanceof Ship)) || ((!((Ship) pair.getKey()).categoryProperty().get().equals(Ships.CIVIL_SHIPS)) && (!((Ship) pair.getKey()).categoryProperty().get().equals(Ships.CIVIL_RACE_SHIPS))))
          {
            final int destroyed = originalMap.get(pair.getKey()) - pair.getValue();
            exp = exp + ((pair.getKey().getValue() * destroyed) / 100.0);
          }
        }
        else
        {
          System.out.println("Something wrong computing experiance!");
        }
      }
    }
    return exp;
  }

  public static String getShipAndDefenceBaseString(final ObservableMap<ShipAndDefenceBase, Integer> itemMap)
  {
    String str = "";
    if ((itemMap != null) && (!itemMap.isEmpty()))
    {
      for (final ObservableMap.Entry<ShipAndDefenceBase, Integer> pair : itemMap.entrySet())
      {
        str = str + pair.getKey().nameProperty().get() + String.format(" \t %,d", pair.getValue()) + System.lineSeparator();
      }
    }
    else
    {
      str = "Nichts vorhanden!" + System.lineSeparator();
    }
    return str;
  }
}
