/**
 * 
 */
package data.defence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import data.ShipAndDefenceBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author AAG
 *
 */
public class Defence extends ShipAndDefenceBase implements Serializable
{
  private static final long serialVersionUID = -3309247929669154254L;

  private IntegerProperty fields;
  private IntegerProperty maxNumber;

  public Defence(final Defence defence)
  {
    this(defence.name.get(), defence.titan.get(), defence.silizium.get(), defence.pvc.get(), defence.tritium.get(), defence.food.get(), defence.weapons.get(), defence.structure.get(), defence.shield.get(), defence.heal.get(), defence.fields.get(), defence.maxNumber.get());
  }

  public Defence(final String name, final int titan, final int silizium, final int pvc, final int tritium, final int food, final long weapons, final long structure, final long shield, final int repair, final int fields, final int maxNumber)
  {
    super(name, titan, silizium, pvc, tritium, food, weapons, structure, shield, repair);

    this.fields = new SimpleIntegerProperty(fields);
    this.maxNumber = new SimpleIntegerProperty(maxNumber);
  }

  @Override
  public boolean isValid()
  {
    if (fields.get() < 0)
    {
      return false;
    }
    if (maxNumber.get() < 0)
    {
      return false;
    }
    return super.isValid();
  }

  public boolean update(final Defence other)
  {
    boolean updated = super.update(other);
    if (!(fields.get() == other.fields.get()))
    {
      fields.set(other.fields.get());
      updated = true;
    }
    if (!(maxNumber.get() == other.maxNumber.get()))
    {
      maxNumber.set(other.maxNumber.get());
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
    out.writeInt(this.fields.get());
    out.writeInt(this.maxNumber.get());
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    readObjectData(in);
  }

  @Override
  protected void readObjectData(final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    super.readObjectData(in);
    fields = new SimpleIntegerProperty(in.readInt());
    maxNumber = new SimpleIntegerProperty(in.readInt());
  }

  public IntegerProperty fieldsProperty()
  {
    return fields;
  }

  public IntegerProperty maxNumberProperty()
  {
    return maxNumber;
  }
}
