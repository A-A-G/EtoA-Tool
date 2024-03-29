/**
 *
 */
package logic;

import data.planets.Planet;
import properties.UniverseProperties;

/**
 * @author AAG
 *
 */
public final class Distances
{

  public static class Coordinates
  {
    private final int x_sector;
    private final int y_sector;
    private final int x_system;
    private final int y_system;
    private final int pos;

    public Coordinates(final int[] coords)
    {
      x_sector = coords[0];
      y_sector = coords[1];
      x_system = coords[2];
      y_system = coords[3];
      pos = coords[4];
    }

    public Coordinates(final int x_sector, final int y_sector, final int x_system, final int y_system, final int pos)
    {
      this(new int[] { x_sector, y_sector, x_system, y_system, pos });
    }

    public Coordinates(final String coords)
    {
      this(splitCoordinates(coords));
    }

    public Coordinates(final Planet p)
    {
      this(p.getCoords());
    }

    private static int[] splitCoordinates(final String coords)
    {
      final String tokens[] = coords.split(":|/");
      return new int[] { Integer.valueOf(tokens[0]), Integer.valueOf(tokens[1]), Integer.valueOf(tokens[2]), Integer.valueOf(tokens[3]), Integer.valueOf(tokens[4]) };
    }

    @Override
    public boolean equals(final Object obj)
    {
      if (!(obj instanceof Coordinates))
      {
        return false;
      }
      final Coordinates other = (Coordinates) obj;
      return isSameSystem(other) && (pos == other.pos);
    }

    public boolean isSameSystem(final Coordinates other)
    {
      return (x_sector == other.x_sector) && (y_sector == other.y_sector) && (x_system == other.x_system) && (y_system == other.y_system);
    }

  }

  public static double getDistance(final Coordinates c1, final Coordinates c2)
  {
    final UniverseProperties uniProperties = UniverseProperties.getInstance();
    final double cellLength = uniProperties.getCellLength();
    final int xCells = uniProperties.getXCells();
    final int yCells = uniProperties.getYCells();
    final double distanceInnerOuterEntity = cellLength / 4.0 / uniProperties.getMaxNumberPlanets();
    if (c1.isSameSystem(c2))
    {
      return Math.round(Math.abs(c1.pos - c2.pos) * distanceInnerOuterEntity);
    }
    else
    {
      final double dx = Math.abs((((c1.x_sector - 1) * xCells) + c1.x_system) - (((c2.x_sector - 1) * xCells) + c2.x_system));
      final double dy = Math.abs((((c1.y_sector - 1) * yCells) + c1.y_system) - (((c2.y_sector - 1) * yCells) + c2.y_system));
      final double hypotenuse = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
      final double cellDistanceAE = hypotenuse * cellLength;
      return Math.round((cellDistanceAE + cellLength) - (distanceInnerOuterEntity * (c1.pos + c2.pos)));
    }
  }

  public static double getDistance(final String coords1, final String coords2) // Howto handle errors in coordinate strings?
  {
    return getDistance(new Coordinates(coords1), new Coordinates(coords2));
  }

  public static double getDistance(final Planet p1, final Planet p2) // Howto handle errors in coordinate strings?
  {
    return getDistance(new Coordinates(p1), new Coordinates(p2));
  }

  public static int getDurationSeconds(final double distance, final int speed, final int startLandDuration)
  {
    return (int) Math.ceil(startLandDuration + ((distance / speed) * 3600));
  }

  public static int getDurationSeconds(final String coords1, final String coords2, final int speed, final int startLandDuration)
  {
    return getDurationSeconds(getDistance(new Coordinates(coords1), new Coordinates(coords2)), speed, startLandDuration);
  }

  public static int getDurationSeconds(final Planet p1, final Planet p2, final int speed, final int startLandDuration)
  {
    return getDurationSeconds(getDistance(new Coordinates(p1), new Coordinates(p2)), speed, startLandDuration);
  }

  public static String getDurationHMS(final double distance, final int speed, final int startLandDuration)
  {
    return getHMSfromSecs(getDurationSeconds(distance, speed, startLandDuration));
  }

  public static String getDurationHMS(final String coords1, final String coords2, final int speed, final int startLandDuration)
  {
    return getHMSfromSecs(getDurationSeconds(getDistance(new Coordinates(coords1), new Coordinates(coords2)), speed, startLandDuration));
  }

  public static String getDurationHMS(final Planet p1, final Planet p2, final int speed, final int startLandDuration)
  {
    return getHMSfromSecs(getDurationSeconds(getDistance(new Coordinates(p1), new Coordinates(p2)), speed, startLandDuration));
  }

  public static String getHMSfromSecs(final int timeInSeconds)
  {
    final int hours = timeInSeconds / 3600;
    final int min = (timeInSeconds - (hours * 3600)) / 60;
    final int secs = timeInSeconds - (hours * 3600) - (min * 60);
    String time = "";
    if (hours > 0)
    {
      time = hours + "h";
    }
    if (min > 0)
    {
      time = time + " " + min + "m";
    }
    if (secs > 0)
    {
      time = time + " " + secs + "s";
    }
    return time;
  }
}
