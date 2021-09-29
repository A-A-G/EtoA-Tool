/**
 *
 */
package gui.tabs;

import java.util.prefs.Preferences;

import data.defence.Defences;
import data.planets.Planets;
import data.ships.Ships;
import gui.utils.Spinners;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.Krypto;

/**
 * @author AAG
 *
 */
public class KryptoTab extends EtoATab
{
  private final static String TAB_NAME = "Kryptorechner";

  private final static String KRYPTO = "krypto";
  private final static String SPIOTECH = "spiotech";
  private final static String CTECH = "ctech";
  private final static String ALLYSPIOTECH = "allyspiotech";

  // Player
  private final Spinner<Integer> kryptoSpinner;
  private final Spinner<Integer> spioSpinner;
  private final Spinner<Integer> ctechSpinner;
  private final Spinner<Integer> allySpioSpinner;
  private final CheckBox spySpezCheckBox;

  // Target
  private final Spinner<Integer> targetMagnetronSpinner;
  private final Spinner<Integer> targetTarnSpinner;
  private final Spinner<Integer> targetCtechSpinner;
  private final Spinner<Integer> targetAllyTarnSpinner;
  private final CheckBox targetSpySpezCheckBox;

  // Result
  private final TextField chanceTF;
  private final TextField decryptLevelTF;

  // Decrypt Labels
  private final Label chanceLabel;
  private final Label lessThanZeroLabel;
  private final Label zeroToTenLabel;
  private final Label tenToFifteenLabel;
  private final Label fifteenToTwentyLabel;
  private final Label twentyToTwentyfiveLabel;
  private final Label twentyFiveToThirtyLabel;
  private final Label moreThanThirtyLabel;

  public KryptoTab(final Planets planets, final Ships ships, final Defences defences, final Label statusLabel)
  {
    super(TAB_NAME, planets, ships, defences, statusLabel);
    final Preferences preferences = Preferences.userNodeForPackage(getClass());
    final GridPane ownValuesGP = new GridPane();
    ownValuesGP.add(new Label("Kryptocenter: "), 0, 1);
    kryptoSpinner = Spinners.getSpinner(0, 10, preferences.getInt(KRYPTO, 1), 1, 60, true, null);
    kryptoSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    kryptoSpinner.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(KRYPTO, newValue.intValue()));
    ownValuesGP.add(kryptoSpinner, 1, 1);
    ownValuesGP.add(new Label("Spionagetechnik: "), 0, 2);
    spioSpinner = Spinners.getSpinner(0, 40, preferences.getInt(SPIOTECH, 0), 1, 60, true, null);
    spioSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    spioSpinner.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(SPIOTECH, newValue.intValue()));
    ownValuesGP.add(spioSpinner, 1, 2);
    ownValuesGP.add(new Label("Computertechnik: "), 0, 3);
    ctechSpinner = Spinners.getSpinner(0, 40, preferences.getInt(CTECH, 0), 1, 60, true, null);
    ctechSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    ctechSpinner.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(CTECH, newValue.intValue()));
    ownValuesGP.add(ctechSpinner, 1, 3);
    ownValuesGP.add(new Label("Spionagetechnik (Allianz): "), 0, 4);
    allySpioSpinner = Spinners.getSpinner(0, 40, preferences.getInt(ALLYSPIOTECH, 0), 1, 60, true, null);
    allySpioSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    allySpioSpinner.valueProperty().addListener((obs, oldValue, newValue) -> preferences.putInt(ALLYSPIOTECH, newValue.intValue()));
    ownValuesGP.add(allySpioSpinner, 1, 4);
    spySpezCheckBox = new CheckBox("Spion");
    spySpezCheckBox.selectedProperty().addListener(b -> computeKryptoValues());
    final VBox ownValuesVBox = new VBox(ownValuesGP, spySpezCheckBox);
    ownValuesVBox.setAlignment(Pos.CENTER);
    final TitledPane playerTP = new TitledPane("Spieler", ownValuesVBox);
    playerTP.setCollapsible(false);
    playerTP.setPrefHeight(Double.MAX_VALUE);

    chanceTF = new TextField();
    chanceTF.setMaxWidth(120);
    decryptLevelTF = new TextField();
    decryptLevelTF.setMaxWidth(120);
    chanceLabel = new Label("Decrypt if Chance >= 0");
    chanceLabel.getStyleClass().add("highlight");
    final HBox centerHBox = new HBox();
    centerHBox.setAlignment(Pos.CENTER);
    centerHBox.getChildren().add(new Label("Decrypt Chance: "));
    centerHBox.getChildren().add(chanceTF);
    centerHBox.getChildren().add(new Label(" Decrypt Level: "));
    centerHBox.getChildren().add(decryptLevelTF);
    lessThanZeroLabel = new Label("  < 0 \t Only show that there are some fleets");
    zeroToTenLabel = new Label("0 <= 10 \t Show that there are x fleets");
    tenToFifteenLabel = new Label("10 <= 15 \t Show that there are x fleets from y belonging to z, show hour");
    fifteenToTwentyLabel = new Label("15 <= 20 \t Also show ship types, show mninutes with +- 15 mins");
    twentyToTwentyfiveLabel = new Label("20 <= 25 \t Also show count of ships and time in minutes");
    twentyFiveToThirtyLabel = new Label("25 <= 30 \t Also show count of every ship and exact time");
    moreThanThirtyLabel = new Label("  > 30 \t Show action");
    final VBox nocenterVBox = new VBox(lessThanZeroLabel, zeroToTenLabel, tenToFifteenLabel, fifteenToTwentyLabel, twentyToTwentyfiveLabel, twentyFiveToThirtyLabel, moreThanThirtyLabel);
    nocenterVBox.getStyleClass().add("nospacing");
    final TitledPane decryptTP = new TitledPane("Decrypt Level", nocenterVBox);
    decryptTP.setCollapsible(false);
    final VBox resultVBox = new VBox(chanceLabel, centerHBox, decryptTP);
    resultVBox.setAlignment(Pos.CENTER);
    final TitledPane resultTP = new TitledPane("Ergebnis", resultVBox);
    resultTP.setCollapsible(false);

    final GridPane targetValuesGP = new GridPane();
    targetValuesGP.add(new Label("MAGNETRON Störsender: "), 0, 1);
    targetMagnetronSpinner = Spinners.getSpinner(0, 10, 0, 1, 60, true, null);
    targetMagnetronSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    targetValuesGP.add(targetMagnetronSpinner, 1, 1);
    targetValuesGP.add(new Label("Tarntechnik: "), 0, 2);
    targetTarnSpinner = Spinners.getSpinner(0, 40, 0, 1, 60, true, null);
    targetTarnSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    targetValuesGP.add(targetTarnSpinner, 1, 2);
    targetValuesGP.add(new Label("Computertechnik: "), 0, 3);
    targetCtechSpinner = Spinners.getSpinner(0, 40, 0, 1, 60, true, null);
    targetCtechSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    targetValuesGP.add(targetCtechSpinner, 1, 3);
    targetValuesGP.add(new Label("Tarntechnik (Allianz): "), 0, 4);
    targetAllyTarnSpinner = Spinners.getSpinner(0, 40, 0, 1, 60, true, null);
    targetAllyTarnSpinner.valueProperty().addListener((obs, oldValue, newValue) -> computeKryptoValues());
    targetValuesGP.add(targetAllyTarnSpinner, 1, 4);
    targetSpySpezCheckBox = new CheckBox("Spion");
    targetSpySpezCheckBox.selectedProperty().addListener(b -> computeKryptoValues());
    final VBox targetValuesVBox = new VBox(targetValuesGP, targetSpySpezCheckBox);
    targetValuesVBox.setAlignment(Pos.CENTER);
    final TitledPane targetTP = new TitledPane("Ziel", targetValuesVBox);
    targetTP.setCollapsible(false);
    targetTP.setPrefHeight(Double.MAX_VALUE);

    final HBox innerKryptoHBox = new HBox(playerTP, resultTP, targetTP);
    innerKryptoHBox.getStyleClass().add("spacing");
    innerKryptoHBox.setAlignment(Pos.BOTTOM_CENTER);
    final VBox kryptoVBox = new VBox(innerKryptoHBox);
    kryptoVBox.setAlignment(Pos.CENTER);
    setContent(kryptoVBox);
    computeKryptoValues();
  }

  private void computeKryptoValues()
  {
    final double op_stealth = targetAllyTarnSpinner.getValue() + (targetSpySpezCheckBox.selectedProperty().get() ? 2 : 0) + targetTarnSpinner.getValue();
    final double self_spy = allySpioSpinner.getValue() + (spySpezCheckBox.selectedProperty().get() ? 3 : 0) + spioSpinner.getValue();
    final double kryptoChance = Krypto.chance(kryptoSpinner.getValue(), self_spy, targetMagnetronSpinner.getValue(), op_stealth);
    chanceTF.setText(String.format("%1.2f", kryptoChance) + " bis " + String.format("%1.2f", kryptoChance + 2));
    if (kryptoChance < 0)
    {
      if (!chanceLabel.getStyleClass().contains("red"))
      {
        chanceLabel.getStyleClass().add("red");
      }
    }
    else
    {
      chanceLabel.getStyleClass().remove("red");
    }
    final double kryptoLevel = Krypto.decryptlevel(kryptoSpinner.getValue(), self_spy, ctechSpinner.getValue(), targetMagnetronSpinner.getValue(), op_stealth, targetCtechSpinner.getValue());
    decryptLevelTF.setText(String.format("%1.2f", kryptoLevel) + " bis " + String.format("%1.2f", kryptoLevel + 2));
    lessThanZeroLabel.getStyleClass().remove("red");
    zeroToTenLabel.getStyleClass().remove("red");
    tenToFifteenLabel.getStyleClass().remove("red");
    fifteenToTwentyLabel.getStyleClass().remove("red");
    twentyToTwentyfiveLabel.getStyleClass().remove("red");
    twentyFiveToThirtyLabel.getStyleClass().remove("red");
    moreThanThirtyLabel.getStyleClass().remove("red");
    if (kryptoLevel > 30)
    {
      moreThanThirtyLabel.getStyleClass().add("red");
    }
    else if (kryptoLevel > 25)
    {
      twentyFiveToThirtyLabel.getStyleClass().add("red");
    }
    else if (kryptoLevel > 20)
    {
      twentyToTwentyfiveLabel.getStyleClass().add("red");
    }
    else if (kryptoLevel > 15)
    {
      fifteenToTwentyLabel.getStyleClass().add("red");
    }
    else if (kryptoLevel >= 10)
    {
      tenToFifteenLabel.getStyleClass().add("red");
    }
    else if (kryptoLevel > 0)
    {
      zeroToTenLabel.getStyleClass().add("red");
    }
    else
    {
      lessThanZeroLabel.getStyleClass().add("red");
    }
  }

}
