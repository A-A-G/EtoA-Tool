/**
 * 
 */
package logic;

import data.FightData;
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

  private static double MAX_HEAL = 1;

  private FightData fightData = null;

  private SpyReport spyReport = null;

  String lastFightReport = "";

  private boolean attackerWins = false;
  private boolean enoughCapacity = true;

  public FightSimulation(final FightData fightData)
  {
    this.fightData = fightData;
  }

  public void SimulateFight()
  {
    if (fightData == null)
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
    lastFightReport = lastFightReport + "ANGREIFENDE FLOTTE" + System.lineSeparator();
    lastFightReport = lastFightReport + fightData.getAttackerFleetString() + System.lineSeparator();
    lastFightReport = lastFightReport + "VERTEIDIGENDE FLOTTE" + System.lineSeparator();
    lastFightReport = lastFightReport + fightData.getDefenderFleetString() + System.lineSeparator();
    lastFightReport = lastFightReport + "PLANETARE VERTEIDIGUNG" + System.lineSeparator();
    lastFightReport = lastFightReport + fightData.getDefencesString() + System.lineSeparator();
    lastFightReport = lastFightReport + "DATEN DES ANGREIFERS" + System.lineSeparator();
    lastFightReport = lastFightReport + fightData.getAttackerValueString() + System.lineSeparator();
    lastFightReport = lastFightReport + "DATEN DES VERTEIDIGERS" + System.lineSeparator();
    lastFightReport = lastFightReport + fightData.getDefenderValueString() + System.lineSeparator();
    double attackerLeftOver = fightData.getAttackerValues().structure + fightData.getAttackerValues().shield;
    double defenderLeftOver = fightData.getDefenderValues().structure + fightData.getDefenderValues().shield;
    final double lastAttackerLO = attackerLeftOver;
    final double lastDefenderLO = defenderLeftOver;
    fightData.initFight();
    for (int i = 1; i <= 5; ++i) // 5 rounds
    {
      lastFightReport = lastFightReport + String.format("%,d Einheiten des Angreifers schießen mit einer Stärke von %,.0f auf den Verteidiger.", fightData.getAttackerValuesCopy().units, fightData.getAttackerValuesCopy().weapons) + System.lineSeparator();
      defenderLeftOver = Math.max(defenderLeftOver - fightData.getAttackerValuesCopy().weapons, 0);
      lastFightReport = lastFightReport + String.format("Der Verteidiger hat danach noch %,.0f Struktur- und Schildpunkte.", defenderLeftOver) + System.lineSeparator() + System.lineSeparator();
      lastFightReport = lastFightReport + String.format("%,d Einheiten des Verteidigers schießen mit einer Stärke von %,.0f auf den Verteidiger.", fightData.getDefenderValuesCopy().units, fightData.getDefenderValuesCopy().weapons) + System.lineSeparator();
      attackerLeftOver = Math.max(attackerLeftOver - fightData.getDefenderValuesCopy().weapons, 0);
      lastFightReport = lastFightReport + String.format("Der Angreifer hat danach noch %,.0f Struktur- und Schildpunkte.", attackerLeftOver) + System.lineSeparator() + System.lineSeparator();
      if ((attackerLeftOver > 0) && (fightData.getAttackerValues().heal > 0))
      {
        final double heal = fightData.getAttackerHeal(attackerLeftOver);
        lastFightReport = lastFightReport + String.format("Die Einheiten des Angreifers heilen %,.0f Struktur- und Schildpunkte.", heal) + System.lineSeparator();
        attackerLeftOver = attackerLeftOver + (heal > (MAX_HEAL * (lastAttackerLO - attackerLeftOver)) ? MAX_HEAL * (lastAttackerLO - attackerLeftOver) : heal);
        lastFightReport = lastFightReport + String.format("Der Angreifer hat danach wieder %,.0f Struktur- und Schildpunkte.", attackerLeftOver) + System.lineSeparator() + System.lineSeparator();
      }
      if ((defenderLeftOver > 0) && (fightData.getDefenderValues().heal > 0))
      {
        final double heal = fightData.getDefenderHeal(defenderLeftOver);
        lastFightReport = lastFightReport + String.format("Die Einheiten des Verteidigers heilen %,.0f Struktur- und Schildpunkte.", heal) + System.lineSeparator();
        defenderLeftOver = defenderLeftOver + (heal > (MAX_HEAL * (lastDefenderLO - defenderLeftOver)) ? MAX_HEAL * (lastDefenderLO - defenderLeftOver) : heal);
        lastFightReport = lastFightReport + String.format("Der Verteidiger hat danach wieder %,.0f Struktur- und Schildpunkte." + System.lineSeparator(), defenderLeftOver) + System.lineSeparator();
      }
      fightData.reduceAttackerCopy(attackerLeftOver);
      fightData.reduceDefenderCopy(defenderLeftOver);
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
      lastFightReport = lastFightReport + getLoot(spyReport, fightData.getAttackerCopyCapacity(), fightData.getAttackerCopyIncreasedCapacity()) + System.lineSeparator();
    }
    final String defenderShips = fightData.restoreDefenderCivilShips();
    final double attackerExp = Math.floor(fightData.getAttackerEXP());
    final double defenderExp = Math.floor(fightData.getDefenderEXP());
    final String defenceRepair = fightData.repairRemainingDefences();
    lastFightReport = lastFightReport + "TRÜMMERFELD" + System.lineSeparator();
    lastFightReport = lastFightReport + fightData.getDebrisField() + System.lineSeparator();
    lastFightReport = lastFightReport + "Zustand nach dem Kampf:" + System.lineSeparator() + System.lineSeparator();
    lastFightReport = lastFightReport + "ANGREIFENDE FLOTTE" + System.lineSeparator();
    fightData.removeCopyZeroDeffShips();
    lastFightReport = lastFightReport + fightData.getAttackerCopyFleetString() + System.lineSeparator();
    lastFightReport = lastFightReport + String.format("Gewonnene EXP: %,.0f", attackerExp) + System.lineSeparator() + System.lineSeparator();
    lastFightReport = lastFightReport + "VERTEIDIGENDE FLOTTE" + System.lineSeparator();
    lastFightReport = lastFightReport + defenderShips + System.lineSeparator();
    lastFightReport = lastFightReport + "PLANETARE VERTEIDIGUNG" + System.lineSeparator();
    lastFightReport = lastFightReport + defenceRepair + System.lineSeparator();
    lastFightReport = lastFightReport + String.format("Gewonnene EXP: %,.0f", defenderExp) + System.lineSeparator() + System.lineSeparator();
    lastFightReport = lastFightReport + "Hinweis: Die simulierte Beute berücksichtigt keine Flugkosten!" + System.lineSeparator();
  }

  private String getLoot(final SpyReport spyReport, final double capacity, final double increasedCapacity)
  {
    enoughCapacity = true;
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
      enoughCapacity = false;
      loot = loot + String.format("Zu wenig Lagerraum (%,.0f %%)!", reduceBy * 100) + System.lineSeparator();
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
        loot = loot + String.format("Zu wenig Lagerraum (%,.0f %%)!", reduceBy * 100) + System.lineSeparator();
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

  public void setSpyReport(final SpyReport spyReport)
  {
    this.spyReport = spyReport;
  }

  public boolean isAttackerWins()
  {
    return attackerWins;
  }

  public void setFightData(final FightData fightData)
  {
    this.fightData = fightData;
  }

  public FightData getFightData()
  {
    return fightData;
  }

  public boolean isEnoughCapacity()
  {
    return enoughCapacity;
  }

  public boolean hasSpyReport()
  {
    return spyReport != null;
  }

}
