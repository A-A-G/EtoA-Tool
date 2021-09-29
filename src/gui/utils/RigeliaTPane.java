package gui.utils;

import java.util.prefs.Preferences;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.Rigelia;

/**
 * @author AAG
 *
 */
public class RigeliaTPane extends TitledPane
{

  private final static String SHIP_COUNT = "ship_count";

  public enum Type
  {
    EMP("EMP", "EMP", "emp_level", "emp_mysti_boni"),
    ANTHRAX("ANTHRAX", "Gift", "gift_level", "anthrax_mysti_boni"),
    GAS("GAS", "Gift", "gift_level", "gas_mysti_boni");

    private final String text;
    private final String techName;
    private final String techstring;
    private final String mystistring;

    Type(final String text, final String techName, final String techstring, final String mystistring)
    {
      this.text = text;
      this.techName = techName;
      this.techstring = techstring;
      this.mystistring = mystistring;
    }

    public String techString()
    {
      return techstring;
    }

    public String mystiString()
    {
      return mystistring;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
      return text;
    }
  }

  public RigeliaTPane(final Type type)
  {
    final Preferences preferences = Preferences.userNodeForPackage(getClass());
    final Spinner<Integer> techLevel = Spinners.getSpinner(0, 50, preferences.getInt(type.techstring, 8), 1, 65, true, null);
    techLevel.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(type.techstring, newValue.intValue()));
    final Spinner<Integer> shipCount = Spinners.getSpinner(1, 1000000, preferences.getInt(SHIP_COUNT, 1), 1000, 80, true, null);
    shipCount.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(SHIP_COUNT, newValue.intValue()));
    final Spinner<Integer> mystiBoni = Spinners.getSpinner(0, 200, preferences.getInt(type.mystistring, 0), 3, 65, true, null);
    mystiBoni.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(type.mystistring, newValue.intValue()));
    final Label result = new Label();
    updateResult(result, type, techLevel.getValue(), shipCount.getValue(), mystiBoni.getValue());
    result.getStyleClass().add("highlight");
    techLevel.valueProperty().addListener((obs, oldVal, newVal) -> updateResult(result, type, techLevel.getValue(), shipCount.getValue(), mystiBoni.getValue()));
    shipCount.valueProperty().addListener((obs, oldVal, newVal) -> updateResult(result, type, techLevel.getValue(), shipCount.getValue(), mystiBoni.getValue()));
    mystiBoni.valueProperty().addListener((obs, oldVal, newVal) -> updateResult(result, type, techLevel.getValue(), shipCount.getValue(), mystiBoni.getValue()));
    final HBox spinnersHBox = new HBox(new Label(type.techName + " Level:"), techLevel, new Label("Anzahl Bomber:"), shipCount, new Label("Mysticum Boni (%):"), mystiBoni);
    spinnersHBox.setAlignment(Pos.CENTER);
    spinnersHBox.getStyleClass().add("spaceandpaddding");
    final VBox rigeliaVBox = new VBox(spinnersHBox, result);
    rigeliaVBox.setAlignment(Pos.CENTER);
    rigeliaVBox.getStyleClass().add("spaceandpaddding");
    if ((type == Type.ANTHRAX) || (type == Type.GAS))
    {
      final Label cautionLabel = new Label("Caution: Working people cannot die!");
      cautionLabel.getStyleClass().add("highlight");
      cautionLabel.getStyleClass().add("red");
      rigeliaVBox.getChildren().add(cautionLabel);
    }
    this.setContent(rigeliaVBox);
    this.setText(type.text);
    this.setCollapsible(false);
  }

  private void updateResult(final Label result, final Type type, final int techLevel, final int shipCount, final int mystiBoni)
  {
    if (type == Type.EMP)
    {
      result.setText(Rigelia.empString(techLevel, shipCount, mystiBoni / 100.0d));
    }
    else if (type == Type.ANTHRAX)
    {
      result.setText(Rigelia.anthraxString(techLevel, shipCount, mystiBoni / 100.0d));
    }
    else if (type == Type.GAS)
    {
      result.setText(Rigelia.gasString(techLevel, shipCount, mystiBoni / 100.0d));
    }
  }

}
