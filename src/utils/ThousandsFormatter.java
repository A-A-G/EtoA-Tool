/**
 * 
 */
package utils;

import java.text.DecimalFormat;
import java.text.ParseException;

import javafx.util.StringConverter;

/**
 * @author AAG
 *
 */
public class ThousandsFormatter
{
  public static StringConverter<Double> getThousandsGroupingConverterDouble()
  {
    return new StringConverter<Double>()
    {
      private final DecimalFormat df = new DecimalFormat("###,###.##");

      @Override
      public String toString(final Double value)
      {
        // If the specified value is null, return a zero-length String
        if (value == null)
        {
          return "";
        }
        return df.format(value);
      }

      @Override
      public Double fromString(String value)
      {
        try
        {
          // If the specified value is null or zero-length, return null
          if (value == null)
          {
            return null;
          }
          value = value.trim();

          if (value.length() < 1)
          {
            return null;
          }
          // Perform the requested parsing
          return df.parse(value).doubleValue();
        }
        catch (final ParseException ex)
        {
          throw new RuntimeException(ex);
        }
      }
    };
  }

  public static StringConverter<Integer> getThousandsGroupingConverterInteger()
  {
    return new StringConverter<Integer>()
    {
      private final DecimalFormat df = new DecimalFormat("###,###");

      @Override
      public String toString(final Integer value)
      {
        // If the specified value is null, return a zero-length String
        if (value == null)
        {
          return "";
        }
        return df.format(value);
      }

      @Override
      public Integer fromString(String value)
      {
        try
        {
          // If the specified value is null or zero-length, return null
          if (value == null)
          {
            return null;
          }
          value = value.trim();

          if (value.length() < 1)
          {
            return null;
          }
          // Perform the requested parsing
          return df.parse(value).intValue();
        }
        catch (final ParseException ex)
        {
          throw new RuntimeException(ex);
        }
      }
    };
  }
}
