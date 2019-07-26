package utils;
/**
 * 
 */

import java.util.Map;

/**
 * @author AAG
 *
 */
public final class StringUtils
{

  public static boolean isNullOrBlank(final String param)
  {
    return (param == null) || (param.trim().length() == 0);
  }

  public static long getTabSeparatedLong(final String line)
  {
    final String tokens[] = line.trim().split("\\s+");
    return Long.parseLong(tokens[1].replaceAll("`|t|AE/h|Felder", "").trim());
  }

  public static int getTabSeparatedInt(final String line)
  {
    final String tokens[] = line.trim().split("\\s+");
    return Integer.parseInt(tokens[1].replaceAll("`|t|AE/h|Felder", "").trim());
  }

  public static void addTabSeparatedPair(final Map<String, Integer> map, final String line)
  {
    if (line.contains("\t"))
    {
      final String tokens[] = line.trim().split("\\t");
      map.put(tokens[0].trim(), Integer.parseInt(tokens[1].replaceAll("`", "").trim()));
    }
    else if (line.contains("  "))
    {
      final String tokens[] = line.trim().split("  ");
      map.put(tokens[0].trim(), Integer.parseInt(tokens[1].replaceAll("`", "").trim()));
    }
  }

  public static int getTabSeparatedSeconds(final String line)
  {
    final String lineTokens[] = line.trim().split("\\t");
    if (lineTokens.length == 1)
    {
      return 0;
    }
    final String tokens[] = lineTokens[1].split(" ");
    int seconds = 0;
    for (final String t : tokens)
    {
      if (t.contains("h"))
      {
        seconds = seconds + (Integer.parseInt(t.replace("h", "").trim()) * 3600);
      }
      else if (t.contains("m"))
      {
        seconds = seconds + (Integer.parseInt(t.replace("m", "").trim()) * 60);
      }
      else if (t.contains("s"))
      {
        seconds = seconds + Integer.parseInt(t.replace("s", "").trim());
      }
      else
      {
        return -2;
      }
    }
    return seconds;
  }

}
