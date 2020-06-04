package logic;

/**
 * @author AAG
 *
 */
public class Rigelia
{

  private static int EMP_CHANCE_BASE = 10;
  private static int ANTHRAX_CHANCE_BASE = 30;
  private static int GAS_CHANCE_BASE = 25;
  private static int EMP_BASE_DURATION = 10;
  private static int ANTHRAX_BASE_DAMAGE = 10;
  private static int GAS_BASE_DAMAGE = 10;
  private static int ANTHRAX_DAMAGE_MAX_PERCENTAGE = 90;
  private static int GAS_DAMAGE_MAX_PERCENTAGE = 95;

  private static double SHIP_COUNT_CONST = 10000;

  public static double chance(final int chanceBase, final int empLevel, final int shipCount, final double shipBoni)
  {
    final double one = 101;
    final double two = chanceBase + Math.ceil(shipCount / SHIP_COUNT_CONST) + (empLevel * 5) + (shipBoni * 100);
    return Math.min(two / one, 1);
  }

  public static int minEmpDuration()
  {
    return 1;
  }

  public static int maxEmpDuration(final int empLevel)
  {
    return EMP_BASE_DURATION + empLevel;
  }

  public static double minAnthraxDamage(final int shipCount)
  {
    return Math.ceil(shipCount / SHIP_COUNT_CONST);
  }

  public static double maxAnthraxDamage(final int poisonLevel, final int shipCount)
  {
    return Math.min(ANTHRAX_DAMAGE_MAX_PERCENTAGE, ANTHRAX_BASE_DAMAGE + (poisonLevel * 3)) + Math.ceil(shipCount / SHIP_COUNT_CONST);
  }

  public static double minGasDamage(final int shipCount)
  {
    return Math.ceil(shipCount / SHIP_COUNT_CONST);
  }

  public static double maxGasDamage(final int poisonLevel, final int shipCount)
  {
    return Math.min(GAS_DAMAGE_MAX_PERCENTAGE, GAS_BASE_DAMAGE + (poisonLevel * 3)) + Math.ceil(shipCount / SHIP_COUNT_CONST);
  }

  public static String empString(final int empLevel, final int shipCount, final double shipBoni)
  {
    return String.format("Chance: %,.0f%% \t\t\t Duration: %dh to %dh", chance(EMP_CHANCE_BASE, empLevel, shipCount, shipBoni) * 100, minEmpDuration(), maxEmpDuration(empLevel));
  }

  public static String anthraxString(final int poisonLevel, final int shipCount, final double shipBoni)
  {
    return String.format("Chance: %,.0f%% \t\t\t People and Food reduced by: %,.0f%% to %,.0f%%", chance(ANTHRAX_CHANCE_BASE, poisonLevel, shipCount, shipBoni) * 100, minAnthraxDamage(shipCount), maxAnthraxDamage(poisonLevel, shipCount));
  }

  public static String gasString(final int poisonLevel, final int shipCount, final double shipBoni)
  {
    return String.format("Chance: %,.0f%% \t\t\t People reduced by: %,.0f%% to %,.0f%%", chance(GAS_CHANCE_BASE, poisonLevel, shipCount, shipBoni) * 100, minGasDamage(shipCount), maxGasDamage(poisonLevel, shipCount));
  }

}
