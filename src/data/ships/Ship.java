/**
 *
 */
package data.ships;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.ShipAndDefenceBase;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.StringUtils;

/**
 * @author AAG
 *
 */
public class Ship extends ShipAndDefenceBase implements Serializable
{
  private static final long serialVersionUID = -4351329610245435055L;

  private StringProperty category;

  private IntegerProperty pilots;

  // Tritium usage during flights
  private IntegerProperty starting;
  private IntegerProperty landing;
  private IntegerProperty usagePerAR;

  // Technical data
  private IntegerProperty capacity;
  private IntegerProperty passengers;
  private IntegerProperty speed;
  private IntegerProperty startDuration;
  private IntegerProperty landDuration;

  public Ship(final String name)
  {
    this(name, "", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  }

  public Ship(final Ship ship)
  {
    this(ship.name.get(), ship.category.get(), ship.pilots.get(), ship.titan.get(), ship.silizium.get(), ship.pvc.get(), ship.tritium.get(), ship.food.get(), ship.starting.get(), ship.landing.get(), ship.usagePerAR.get(), ship.weapons.get(), ship.structure.get(), ship.shield.get(), ship.heal.get(), ship.capacity.get(), ship.passengers.get(), ship.speed.get(), ship.startDuration.get(), ship.landDuration.get());
  }

  public Ship(final String name, final String category, final int pilots, final int titan, final int silizium, final int pvc, final int tritium, final int food, final int starting, final int landing, final int usagePerAR, final long weapons, final long structure, final long shield, final int heal, final int capacity, final int passengers, final int speed, final int startDuration, final int landDuration)
  {
    super(name, titan, silizium, pvc, tritium, food, weapons, structure, shield, heal);

    this.category = new SimpleStringProperty(category);

    this.pilots = new SimpleIntegerProperty(pilots);

    this.starting = new SimpleIntegerProperty(starting);
    this.landing = new SimpleIntegerProperty(landing);
    this.usagePerAR = new SimpleIntegerProperty(usagePerAR);

    this.capacity = new SimpleIntegerProperty(capacity);
    this.passengers = new SimpleIntegerProperty(passengers);
    this.speed = new SimpleIntegerProperty(speed);
    this.startDuration = new SimpleIntegerProperty(startDuration);
    this.landDuration = new SimpleIntegerProperty(landDuration);

  }

  @Override
  public boolean isValid()
  {
    if (StringUtils.isNullOrBlank(category.get()))
    {
      return false;
    }
    if (pilots.get() < 0)
    {
      return false;
    }

    if (starting.get() < 0)
    {
      return false;
    }
    if (landing.get() < 0)
    {
      return false;
    }
    if (usagePerAR.get() < 0)
    {
      return false;
    }

    if (capacity.get() < 0)
    {
      return false;
    }
    if (passengers.get() < 0)
    {
      return false;
    }
    if (speed.get() < 0)
    {
      return false;
    }
    if (startDuration.get() < 0)
    {
      return false;
    }
    if (landDuration.get() < 0)
    {
      return false;
    }
    return super.isValid();
  }

  public int getStartLandDuration()
  {
    return startDuration.get() + landDuration.get();
  }

  public boolean update(final Ship other)
  {
    boolean updated = super.update(other);
    if ((pilots.get() != other.pilots.get()))
    {
      pilots.set(other.pilots.get());
      updated = true;
    }
    if ((starting.get() != other.starting.get()))
    {
      starting.set(other.starting.get());
      updated = true;
    }
    if ((landing.get() != other.landing.get()))
    {
      landing.set(other.landing.get());
      updated = true;
    }
    if ((usagePerAR.get() != other.usagePerAR.get()))
    {
      usagePerAR.set(other.usagePerAR.get());
      updated = true;
    }
    if ((capacity.get() != other.capacity.get()))
    {
      capacity.set(other.capacity.get());
      updated = true;
    }
    if ((passengers.get() != other.passengers.get()))
    {
      passengers.set(other.passengers.get());
      updated = true;
    }
    if ((speed.get() != other.speed.get()))
    {
      speed.set(other.speed.get());
      updated = true;
    }
    if ((startDuration.get() != other.startDuration.get()))
    {
      startDuration.set(other.startDuration.get());
      updated = true;
    }
    if ((landDuration.get() != other.landDuration.get()))
    {
      landDuration.set(other.landDuration.get());
      updated = true;
    }
    return updated;
  }

  private void writeObject(final ObjectOutputStream out) throws IOException
  {
    writeObjectData(out);
  }

  @Override
  protected void writeObjectData(final ObjectOutputStream out) throws IOException
  {
    super.writeObjectData(out);

    out.writeUTF(category.get());

    out.writeInt(pilots.get());

    out.writeInt(starting.get());
    out.writeInt(landing.get());
    out.writeInt(usagePerAR.get());

    out.writeInt(capacity.get());
    out.writeInt(passengers.get());
    out.writeInt(speed.get());
    out.writeInt(startDuration.get());
    out.writeInt(landDuration.get());
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    readObjectData(in);
  }

  @Override
  protected void readObjectData(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    super.readObjectData(in);

    category = new SimpleStringProperty(in.readUTF());

    pilots = new SimpleIntegerProperty(in.readInt());

    starting = new SimpleIntegerProperty(in.readInt());
    landing = new SimpleIntegerProperty(in.readInt());
    usagePerAR = new SimpleIntegerProperty(in.readInt());

    capacity = new SimpleIntegerProperty(in.readInt());
    passengers = new SimpleIntegerProperty(in.readInt());
    speed = new SimpleIntegerProperty(in.readInt());
    startDuration = new SimpleIntegerProperty(in.readInt());
    landDuration = new SimpleIntegerProperty(in.readInt());
  }

  public StringProperty categoryProperty()
  {
    return category;
  }

  public IntegerProperty pilotsProperty()
  {
    return pilots;
  }

  public IntegerProperty startingProperty()
  {
    return starting;
  }

  public IntegerProperty landingProperty()
  {
    return landing;
  }

  public IntegerProperty usagePerARProperty()
  {
    return usagePerAR;
  }

  public IntegerProperty capacityProperty()
  {
    return capacity;
  }

  public IntegerProperty passengersProperty()
  {
    return passengers;
  }

  public IntegerProperty speedProperty()
  {
    return speed;
  }

  public IntegerProperty startDurationProperty()
  {
    return startDuration;
  }

  public IntegerProperty landDurationProperty()
  {
    return landDuration;
  }

  @Override
  public Observable[] getAllObservables()
  {
    return concatArrays(super.getAllObservables(), getObservables());
  }

  @Override
  public Observable[] getObservables()
  {
    return new Observable[] { category, pilots, starting, landing, usagePerAR, capacity, passengers, speed, startDuration, landDuration };
  }

}
