/**
 * 
 */
package data.planets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

import data.EtoAItem;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.SortedList;

/**
 * @author AAG
 *
 */
public class Planet extends EtoAItem implements Comparable<Planet>, Serializable
{

  private static final long serialVersionUID = 1286194160603059459L;

  public static final int GALAXY_SIZE = 2;

  private StringProperty owner;
  private StringProperty coords;
  private StringProperty type;

  public Planet(final Planet p)
  {
    this(p.name.get(), p.owner.get(), p.coords.get(), p.type.get());
  }

  public Planet(final String name, final String owner, final String coords, final String type)
  {
    super(name);
    this.owner = new SimpleStringProperty(owner);
    this.coords = new SimpleStringProperty(coords);
    this.type = new SimpleStringProperty(type);
  }

  public boolean update(final Planet other)
  {
    boolean updated = false;
    if (!name.get().equals(other.name.get()))
    {
      name.set(other.name.get());
      updated = true;
    }
    if (!owner.get().equals(other.owner.get()))
    {
      owner.set(other.owner.get());
      updated = true;
    }
    if (!coords.get().equals(other.coords.get()))
    {
      coords.set(other.coords.get());
      updated = true;
    }
    if (!type.get().equals(other.type.get()))
    {
      type.set(other.type.get());
      updated = true;
    }
    return updated;
  }

  public StringProperty ownerProperty()
  {
    return owner;
  }

  public String getOwner()
  {
    return owner.get();
  }

  public StringProperty coordsProperty()
  {
    return coords;
  }

  public String getCoords()
  {
    return coords.get();
  }

  public StringProperty typeProperty()
  {
    return type;
  }

  private void writeObject(final ObjectOutputStream out) throws IOException
  {
    writeObjectData(out);
  }

  @Override
  protected void writeObjectData(final ObjectOutputStream out) throws IOException
  {
    super.writeObjectData(out);
    out.writeUTF(this.owner.get());
    out.writeUTF(this.coords.get());
    out.writeUTF(this.type.get());
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    readObjectData(in);
  }

  @Override
  protected void readObjectData(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    super.readObjectData(in);
    owner = new SimpleStringProperty(in.readUTF());
    coords = new SimpleStringProperty(in.readUTF());
    type = new SimpleStringProperty(in.readUTF());
  }

  public int getID()
  {
    final String tokens[] = coords.get().split("/|:");
    return (((((Integer.parseInt(tokens[0]) - 1) * GALAXY_SIZE) + Integer.parseInt(tokens[1])) - 1) * 100) + ((Integer.parseInt(tokens[2]) - 1) * 10) + Integer.parseInt(tokens[3]);
  }

  public int getHL(final SortedList<Planet> planets)
  {
    return 1 + planets.indexOf(this);
  }

  public boolean identical(final Planet other)
  {
    if (!name.get().equals(other.name.get()) || !owner.get().equals(other.owner.get()) || !coords.get().equals(other.coords.get()) || !type.get().equals(other.type.get()))
    {
      return false;
    }
    return true;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if ((obj == null) || (getClass() != obj.getClass()))
    {
      return false;
    }
    return Objects.equals(coords.get(), ((Planet) obj).coords.get());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(coords.get());
  }

  @Override
  public int compareTo(final Planet other)
  {
    return compareCoords(coords.get(), other.coords.get());
  }

  public static int compareCoords(final String coords1, final String coords2)
  {
    final String tokens1[] = coords1.split("/|:");
    final String tokens2[] = coords2.split("/|:");
    for (int i = 0; i < tokens1.length; i++)
    {
      if (Integer.parseInt(tokens1[i]) < Integer.parseInt(tokens2[i]))
      {
        return -1;
      }
      if (Integer.parseInt(tokens1[i]) > Integer.parseInt(tokens2[i]))
      {
        return 1;
      }
    }
    return 0;
  }

  @Override
  public Observable[] getAllObservables()
  {
    return concatArrays(super.getAllObservables(), getObservables());
  }

  @Override
  public Observable[] getObservables()
  {
    return new Observable[] { owner, coords, type };
  }
}