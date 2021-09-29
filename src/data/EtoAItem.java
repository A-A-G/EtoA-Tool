/**
 *
 */
package data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.StringUtils;

/**
 * @author AAG
 *
 */
public abstract class EtoAItem implements Serializable
{

  private static final long serialVersionUID = -5651753364018817037L;

  protected StringProperty name;

  // For CheckBox ListView
  protected BooleanProperty selected;

  public EtoAItem(final String name)
  {
    this.name = new SimpleStringProperty(name);
    selected = new SimpleBooleanProperty(false);
  }

  public boolean isValid()
  {
    if (StringUtils.isNullOrBlank(name.get()))
    {
      return false;
    }
    return true;
  }

  public boolean update(final EtoAItem other)
  {
    boolean updated = false;
    if (!name.get().equals(other.name.get()))
    {
      name.set(other.name.get());
      updated = true;
    }
    return updated;
  }

  // Removed due to negativ glyph_id bug (https://bugs.openjdk.java.net/browse/JDK-8207839) which is still present on arch linux.
  public final StringProperty nameProperty()
  {
    return name;
  }

  // public String getName()
  // {
  // return name.get().replaceAll("\\P{Print}", "");
  // }
  //
  // public void setName(String name)
  // {
  // this.name.set(name);
  // }

  public BooleanProperty selectedProperty()
  {
    return selected;
  }

  private void writeObject(final ObjectOutputStream out) throws IOException
  {
    writeObjectData(out);
  }

  protected void writeObjectData(final ObjectOutputStream out) throws IOException
  {
    out.writeUTF(name.get());
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    readObjectData(in);
  }

  protected void readObjectData(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    name = new SimpleStringProperty(in.readUTF());
    selected = new SimpleBooleanProperty(false);
  }

  @Override
  public String toString()
  {
    return name.get();
  }

  public Observable[] getAllObservables()
  {
    return getObservables();
  }

  public Observable[] getObservables()
  {
    return new Observable[] { name, selected };
  }

  protected static Observable[] concatArrays(final Observable[]... arrays)
  {
    return Arrays.stream(arrays).flatMap(Arrays::stream).toArray(Observable[]::new);
  }

}
