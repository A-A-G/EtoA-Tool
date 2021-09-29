/**
 *
 */
package logic;

/**
 * @author AAG
 *
 * etoa-gui/htdocs/content/crypto.php
 *
 */
public class Krypto
{
  public static double chance(final double cryptoCenterLevel, final double self_spy, final double op_jam, final double op_stealth)
  {
    return ((cryptoCenterLevel - op_jam) + (0.3 * (self_spy - op_stealth))) - 1;
  }

  public static double decryptlevel(final double cryptoCenterLevel, final double self_spy, final double self_computer, final double op_jam, final double op_stealth, final double op_computer)
  {
    return ((cryptoCenterLevel - op_jam) + (0.75 * ((self_spy + self_computer) - op_stealth - op_computer))) - 1;
  }
}
