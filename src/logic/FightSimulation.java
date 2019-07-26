/**
 * 
 */
package logic;

import data.PlayerValues;
import data.ShipAndDefenceBase.DebrisField;
import data.SpyReport;
import gui.utils.Dialogs;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;

/**
 * @author AAG
 *
 */
public class FightSimulation
{
  private static double MAX_LOOT = 0.3;
  private static double MAX_LOOT_ADD = 0.25;

  private PlayerValues attackerValues = null;
  private PlayerValues defenderValues = null;

  private SpyReport spyReport = null;

  String lastFightReport = "";

  private boolean attackerWins = false;

  public void SimulateFight()
  {
    if ((attackerValues == null) || (defenderValues == null))
    {
      return;
    }
    lastFightReport = "";
    if (spyReport != null)
    {
      lastFightReport = lastFightReport + "vom Planeten " + spyReport.getCoordsAndName() + System.lineSeparator();
      lastFightReport = lastFightReport + "Verteidiger: " + spyReport.getOwner() + System.lineSeparator();
      lastFightReport = lastFightReport + System.lineSeparator();
    }
    PlayerValues attacker = new PlayerValues(attackerValues);
    lastFightReport = lastFightReport + "ANGREIFENDE FLOTTE" + System.lineSeparator();
    lastFightReport = lastFightReport + attacker.getFleetString() + System.lineSeparator();
    PlayerValues defender = new PlayerValues(defenderValues);
    lastFightReport = lastFightReport + "VERTEIDIGENDE FLOTTE" + System.lineSeparator();
    lastFightReport = lastFightReport + defender.getFleetString() + System.lineSeparator();
    lastFightReport = lastFightReport + "PLANETARE VERTEIDIGUNG" + System.lineSeparator();
    lastFightReport = lastFightReport + defender.getDefencesString() + System.lineSeparator();
    lastFightReport = lastFightReport + "DATEN DES ANGREIFERS" + System.lineSeparator();
    lastFightReport = lastFightReport + attacker.getValueString() + System.lineSeparator();
    lastFightReport = lastFightReport + "DATEN DES VERTEIDIGERS" + System.lineSeparator();
    lastFightReport = lastFightReport + defender.getValueString() + System.lineSeparator();
    double attackerLeftOver = attacker.structureProperty().get() + attacker.shieldProperty().get();
    double defenderLeftOver = defender.structureProperty().get() + defender.shieldProperty().get();
    final double lastAttackerLO = attackerLeftOver;
    final double lastDefenderLO = defenderLeftOver;
    for (int i = 1; i <= 5; ++i) // 5 rounds
    {
      lastFightReport = lastFightReport + String.format("%,d Einheiten des Angreifers schießen mit einer Stärke von %,.0f auf den Verteidiger.", attacker.unitsProperty().get(), attacker.weaponsProperty().getValue()) + System.lineSeparator();
      defenderLeftOver = Math.max(defenderLeftOver - attacker.weaponsProperty().get(), 0);
      lastFightReport = lastFightReport + String.format("Der Verteidiger hat danach noch %,.0f Struktur- und Schildpunkte.", defenderLeftOver) + System.lineSeparator() + System.lineSeparator();
      lastFightReport = lastFightReport + String.format("%,d Einheiten des Verteidigers schießen mit einer Stärke von %,.0f auf den Verteidiger.", defender.unitsProperty().get(), defender.weaponsProperty().getValue()) + System.lineSeparator();
      attackerLeftOver = Math.max(attackerLeftOver - defender.weaponsProperty().get(), 0);
      lastFightReport = lastFightReport + String.format("Der Angreifer hat danach noch %,.0f Struktur- und Schildpunkte.", attackerLeftOver) + System.lineSeparator() + System.lineSeparator();
      attacker = new PlayerValues(attackerValues);
      defender = new PlayerValues(defenderValues);
      if ((attackerLeftOver > 0) && (attacker.healProperty().getValue() > 0))
      {
        final PlayerValues attackerCopy = new PlayerValues(attacker);
        attackerCopy.reduceBy(attackerLeftOver / (attacker.structureProperty().get() + attacker.shieldProperty().get()));
        lastFightReport = lastFightReport + String.format("Die Einheiten des Angreifers heilen %,.0f Struktur- und Schildpunkte.", attackerCopy.healProperty().getValue()) + System.lineSeparator();
        attackerLeftOver = attackerLeftOver + (attackerCopy.healProperty().getValue() > (0.9 * (lastAttackerLO - attackerLeftOver)) ? 0.9 * (lastDefenderLO - defenderLeftOver) : attackerCopy.healProperty().getValue());
        lastFightReport = lastFightReport + String.format("Der Angreifer hat danach wieder %,.0f Struktur- und Schildpunkte.", attackerLeftOver) + System.lineSeparator() + System.lineSeparator();
      }
      if ((defenderLeftOver > 0) && (defender.healProperty().getValue() > 0))
      {
        final PlayerValues defenderCopy = new PlayerValues(defender);
        defenderCopy.reduceBy(defenderLeftOver / (defender.structureProperty().get() + defender.shieldProperty().get()));
        lastFightReport = lastFightReport + String.format("Die Einheiten des Verteidigers heilen %,.0f Struktur- und Schildpunkte.", defenderCopy.healProperty().getValue()) + System.lineSeparator();
        defenderLeftOver = defenderLeftOver + (defenderCopy.healProperty().getValue() > (0.9 * (lastDefenderLO - defenderLeftOver)) ? 0.9 * (lastDefenderLO - defenderLeftOver) : defenderCopy.healProperty().getValue());
        lastFightReport = lastFightReport + String.format("Der Verteidiger hat danach wieder %,.0f Struktur- und Schildpunkte." + System.lineSeparator(), defenderLeftOver) + System.lineSeparator();
      }
      attacker.reduceBy(attackerLeftOver / (attacker.structureProperty().get() + attacker.shieldProperty().get()));
      defender.reduceBy(defenderLeftOver / (defender.structureProperty().get() + defender.shieldProperty().get()));
      if ((defenderLeftOver == 0) || (attackerLeftOver == 0))
      {
        lastFightReport = lastFightReport + "Der Kampf dauerte " + i + " Runden!" + System.lineSeparator() + System.lineSeparator();
        if (defenderLeftOver == 0)
        {
          lastFightReport = lastFightReport + "Der Angreifer hat den Kampf gewonnen!" + System.lineSeparator() + System.lineSeparator();
          attackerWins = true;
        }
        else
        {
          lastFightReport = lastFightReport + "Der Verteidiger hat den Kampf gewonnen!" + System.lineSeparator() + System.lineSeparator();
          attackerWins = false;
        }
        break;
      }
      else if (i == 5)
      {
        lastFightReport = lastFightReport + "Der Kampf dauerte " + i + " Runden!" + System.lineSeparator() + System.lineSeparator();
        lastFightReport = lastFightReport + "Der Kampf endet unentschieden!" + System.lineSeparator() + System.lineSeparator();
        attackerWins = false;
      }
    }
    if ((defenderLeftOver <= 0) && (attackerLeftOver >= 0) && (spyReport != null))
    {
      lastFightReport = lastFightReport + "BEUTE" + System.lineSeparator();
      lastFightReport = lastFightReport + getLoot(spyReport, attacker.getCapacity(), attacker.getIncreasedCapacity()) + System.lineSeparator();
    }
    double attackerExp = Math.floor(defender.getShipsExperiance(defenderValues.getShips()) + defender.getDefencesExperiance(defenderValues.getDefences()));
    double defenderExp = Math.floor(attacker.getShipsExperiance(attackerValues.getShips()));
    final String defenceRepair = defender.repairRemainingDefences(defenderValues.getDefences());
    lastFightReport = lastFightReport + "TRÜMMERFELD" + System.lineSeparator();
    final DebrisField attackerDebrisFieldShips = attacker.getShipsDebrisField(attackerValues.getShips());
    final DebrisField defenderDebrisFieldShips = defender.getShipsDebrisField(defenderValues.getShips());
    final DebrisField defenderDebrisFieldDefences = defender.getDefencesDebrisField(defenderValues.getDefences());
    lastFightReport = lastFightReport + String.format("Titan %,.0f", attackerDebrisFieldShips.titan + defenderDebrisFieldShips.titan + defenderDebrisFieldDefences.titan) + System.lineSeparator();
    lastFightReport = lastFightReport + String.format("Silizium %,.0f", attackerDebrisFieldShips.silizium + defenderDebrisFieldShips.silizium + defenderDebrisFieldDefences.silizium) + System.lineSeparator();
    lastFightReport = lastFightReport + String.format("PVC %,.0f", attackerDebrisFieldShips.pvc + defenderDebrisFieldShips.pvc + defenderDebrisFieldDefences.pvc) + System.lineSeparator() + System.lineSeparator();
    lastFightReport = lastFightReport + "Zustand nach dem Kampf:" + System.lineSeparator() + System.lineSeparator();
    lastFightReport = lastFightReport + "ANGREIFENDE FLOTTE" + System.lineSeparator();
    lastFightReport = lastFightReport + attacker.getFleetString() + System.lineSeparator();
    lastFightReport = lastFightReport + String.format("Gewonnene EXP: %,.0f", attackerExp) + System.lineSeparator() + System.lineSeparator();
    lastFightReport = lastFightReport + "VERTEIDIGENDE FLOTTE" + System.lineSeparator();
    lastFightReport = lastFightReport + defender.getFleetString() + System.lineSeparator();
    lastFightReport = lastFightReport + "PLANETARE VERTEIDIGUNG" + System.lineSeparator();
    lastFightReport = lastFightReport + defenceRepair + System.lineSeparator();
    lastFightReport = lastFightReport + String.format("Gewonnene EXP: %,.0f", defenderExp) + System.lineSeparator() + System.lineSeparator();
    lastFightReport = lastFightReport + "Hinweis: Die simulierte Beute berücksichtigt keine Flugkosten!" + System.lineSeparator();
  }

  private String getLoot(final SpyReport spyReport, final double capacity, final double increasedCapacity)
  {
    String loot = "";
    double titan = spyReport.getTitan() * MAX_LOOT;
    double silizium = spyReport.getSilizium() * MAX_LOOT;
    double pvc = spyReport.getPvc() * MAX_LOOT;
    double tritium = spyReport.getTritium() * MAX_LOOT;
    double food = spyReport.getNahrung() * MAX_LOOT;
    final double ressSum = titan + silizium + pvc + tritium + food;
    final double reduceBy = (capacity + increasedCapacity) / ressSum;
    if (reduceBy < 1)
    {
      titan = titan * reduceBy;
      silizium = silizium * reduceBy;
      pvc = pvc * reduceBy;
      tritium = tritium * reduceBy;
      food = food * reduceBy;
    }
    else if (increasedCapacity > 0)
    {
      double titanAdd = titan * MAX_LOOT_ADD;
      double siliziumAdd = silizium * MAX_LOOT_ADD;
      double pvcAdd = pvc * MAX_LOOT_ADD;
      double tritiumAdd = tritium * MAX_LOOT_ADD;
      double foodAdd = food * MAX_LOOT_ADD;
      final double capacityLeft = increasedCapacity - Math.max(ressSum - capacity, 0);
      final double reduceByAdd = capacityLeft / (titanAdd + siliziumAdd + pvcAdd + tritiumAdd + foodAdd);
      if (reduceByAdd < 1)
      {
        titanAdd = titanAdd * reduceByAdd;
        siliziumAdd = siliziumAdd * reduceByAdd;
        pvcAdd = pvcAdd * reduceByAdd;
        tritiumAdd = tritiumAdd * reduceByAdd;
        foodAdd = foodAdd * reduceByAdd;
      }
      titan = titan + titanAdd;
      silizium = silizium + siliziumAdd;
      pvc = pvc + pvcAdd;
      tritium = tritium + tritiumAdd;
      food = food + foodAdd;
    }
    loot = loot + String.format("Titan %,.0f", titan) + System.lineSeparator();
    loot = loot + String.format("Silizium %,.0f", silizium) + System.lineSeparator();
    loot = loot + String.format("PVC %,.0f", pvc) + System.lineSeparator();
    loot = loot + String.format("Tritium %,.0f", tritium) + System.lineSeparator();
    loot = loot + String.format("Nahrung %,.0f", food) + System.lineSeparator();
    return loot;
  }

  public void showReport()
  {
    if (lastFightReport.isEmpty())
    {
      return;
    }
    final Alert alertDialog = Dialogs.buildAlert(AlertType.INFORMATION, "Kampfbericht", "", "");
    alertDialog.setGraphic(null);
    alertDialog.initModality(Modality.NONE);
    alertDialog.setResizable(true);
    final TextArea tArea = new TextArea(lastFightReport);
    tArea.setEditable(false);
    alertDialog.getDialogPane().setContent(tArea);
    alertDialog.getDialogPane().setPrefSize(800, 1000);
    alertDialog.show();
  }

  public void setAttackerValues(final PlayerValues attackerValues)
  {
    this.attackerValues = attackerValues;
  }

  public void setDefenderValues(final PlayerValues defenderValues)
  {
    this.defenderValues = defenderValues;
  }

  public void setSpyReport(final SpyReport spyReport)
  {
    this.spyReport = spyReport;
  }

  public boolean isAttackerWins()
  {
    return attackerWins;
  }

}
