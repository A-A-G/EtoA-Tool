/**
 *
 */
package data;

import gui.tabs.EtoATab;

/**
 * @author AAG
 *
 */
public class AttackReport
{
//  private final ObservableMap<String, Integer> attackerShips = FXCollections.observableHashMap();
//  private final ObservableMap<String, Integer> attackerShipsLost = FXCollections.observableHashMap();
//  private final ObservableMap<String, Integer> defenderShips = FXCollections.observableHashMap();
//  private final ObservableMap<String, Integer> defenderShipsLost = FXCollections.observableHashMap();
//  private final ObservableMap<String, Integer> defenderDefences = FXCollections.observableHashMap();
//  private final ObservableMap<String, Integer> defenderDefencesLost = FXCollections.observableHashMap();
//
//  private String attackerName = "";
//  private String defenderName = "";
//
  private String coordsAndName = "";
//  private String date = "";
//
//  private Boolean attackerIsWinner = false;
//
//  private int lootTitan = 0;
//  private int lootSilizium = 0;
//  private int lootPVC = 0;
//  private int lootTritium = 0;
//  private int lootNahrung = 0;
//
//  private int tfTitan = 0;
//  private int tfSilizium = 0;
//  private int tfPVC = 0;
//
//  private int expAttacker = 0;
//  private int expDefender = 0;

  public void analyseAttackReport(final String attackReport, final EtoATab etab)
  {
    final String lines[] = attackReport.split("[\\r\\n]");
    for (final String line : lines)
    {
      if (line.contains("vom Planeten"))
      {
        coordsAndName = line.replace("vom Planeten ", "");
        if (etab != null)
        {
          etab.updateStatus("Reading attack report report from " + coordsAndName + " " + coordsAndName + ".");
        }
      }
    }
  }

}
