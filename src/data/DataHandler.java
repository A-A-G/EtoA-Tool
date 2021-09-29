/**
 *
 */
package data;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gui.application.EtoATool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

/**
 * @author AAG
 *
 */
public abstract class DataHandler<T>
{
  protected ObservableList<T> data = null;

  protected final Label statusLabel;

  protected final String dataType;

  private final String initialFileName;
  private String filePath;

  public DataHandler(final Label statusLabel, final String dataType, final String initialFileName)
  {
    this.statusLabel = statusLabel;
    this.dataType = dataType;
    this.initialFileName = initialFileName;
    this.filePath = initialFileName;
    loadData();
  }

  public ObservableList<T> getData()
  {
    if (data == null)
    {
      data = FXCollections.observableArrayList();
    }
    return data;
  }

  public abstract void update(String text);

  public void clear()
  {
    data.clear();
  }

  public void saveAs()
  {
    if (chooseFile(true))
    {
      writeData();
    }
  }

  public void loadFromFile()
  {
    if (chooseFile(false))
    {
      loadData();
    }
  }

  @SuppressWarnings("unchecked")
  public void loadData()
  {
    try
    {
      final FileInputStream fis = new FileInputStream(filePath);
      final ObjectInputStream ois = new ObjectInputStream(fis);
      final List<T> list = (List<T>) ois.readObject();
      ois.close();
      if (data == null)
      {
        data = FXCollections.observableList(list);
      }
      else
      {
        data.clear();
        data.addAll(list);
      }
      updateStatus(data.size() + " items loaded from file " + filePath + ".");
    }
    catch (final EOFException e)
    {
      data = FXCollections.observableArrayList();
    }
    catch (IOException | ClassNotFoundException e)
    {
      if (dataError("Can't load " + dataType + " data!", e, false))
      {
        loadData();
      }
      else
      {
        data = FXCollections.observableArrayList();
      }
    }
  }

  public void writeData()
  {
    try
    {
      final File dataFile = new File(filePath);
      dataFile.createNewFile();
      filePath = dataFile.getAbsolutePath();
      final FileOutputStream fos = new FileOutputStream(dataFile);
      final ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(new ArrayList<>(data));
      oos.close();
      updateStatus(data.size() + " items stored to file " + filePath + ".");
    }
    catch (final IOException e)
    {
      e.printStackTrace();
      if (dataError("Can't save " + dataType + "!", e, true))
      {
        writeData();
      }
    }
  }

  private boolean dataError(final String message, final Exception e, final boolean save)
  {
    System.out.println(message + "\n" + e);
    System.out.println();
    updateStatus(message + " " + e);
    final ButtonType selectFile = new ButtonType(save ? "Select File" : "Open File", ButtonBar.ButtonData.OK_DONE);
    final Alert alert = new Alert(AlertType.ERROR, e.toString(), selectFile, ButtonType.CANCEL);
    alert.setTitle(dataType);
    alert.setHeaderText(message);
    final Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && (result.get() == selectFile))
    {
      return chooseFile(save);
    }
    return false;
  }

  public boolean chooseFile(final boolean save)
  {
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
    fileChooser.setInitialFileName(initialFileName);
    File file = null;
    if (save)
    {
      file = fileChooser.showSaveDialog(EtoATool.getPrimaryStage());
    }
    else
    {
      file = fileChooser.showOpenDialog(EtoATool.getPrimaryStage());
    }
    if (file != null)
    {
      filePath = file.getAbsolutePath();
      updateStatus("New file path for " + dataType + ": " + filePath);
      return true;
    }
    return false;
  }

  protected void updateStatus(final String text)
  {
    if (statusLabel != null)
    {
      statusLabel.setText(text);
    }
    System.out.println(text);
    System.out.println();
  }

  public abstract ObservableList<T> getObservableSelectionList();

  public abstract ObservableList<T> getFullObservableList();

}
