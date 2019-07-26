package utils;

import java.util.Comparator;

public final class Comparators
{

  public static Comparator<String> playerComparator()
  {
    return (p1, p2) -> p1.compareToIgnoreCase(p2);
  }

}
