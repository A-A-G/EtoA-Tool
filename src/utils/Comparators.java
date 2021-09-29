package utils;

import java.util.Comparator;

public final class Comparators
{

  public static Comparator<String> playerComparator()
  {
    return String::compareToIgnoreCase;
  }

}
