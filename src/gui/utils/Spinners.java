package gui.utils;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import utils.ThousandsFormatter;

/**
 * @author AAG
 *
 */
public final class Spinners
{

  public final static Spinner<Integer> getPlayerTechSpinner(final int initialValue, final ObjectProperty<Integer> bidirectionalBindIP)
  {
    return getSpinner(100, 400, initialValue, 10, 100, true, bidirectionalBindIP);
  }

  public final static Spinner<Integer> getRepairSpinner(final ObjectProperty<Integer> bidirectionalBindIP)
  {
    return getSpinner(0, 100, 40, 10, 100, true, bidirectionalBindIP);
  }

  public final static Spinner<Double> getPlayerValueSpinner(final ObjectProperty<Double> bidirectionalBindIP)
  {
    return getDoubleSpinner(-Double.MAX_VALUE, Double.MAX_VALUE, 0, 0, -1, false, bidirectionalBindIP);
  }

  public final static Spinner<Double> getDoubleSpinner(final double min, final double max, final double initialValue, final double step, final int maxWidth, final boolean isEditable, final ObjectProperty<Double> bidirectionalBindIP)
  {
    final Spinner<Double> spinner;
    if (!isEditable)
    {
      spinner = new Spinner<>(min, max, initialValue, step);
      spinner.getValueFactory().setConverter(ThousandsFormatter.getThousandsGroupingConverterDouble());
    }
    else
    {// add commit on focus loss (see https://stackoverflow.com/questions/32340476/manually-typing-in-text-in-javafx-spinner-is-not-updating-the-value-unless-user)
      spinner = new Spinner<>();
      final SpinnerValueFactory<Double> factory = new DoubleSpinnerValueFactory(min, max, initialValue, step);
      factory.setConverter(ThousandsFormatter.getThousandsGroupingConverterDouble());
      spinner.setValueFactory(factory);
      spinner.setEditable(true);
      final TextFormatter<Double> formatter = new TextFormatter<>(factory.getConverter(), factory.getValue());
      spinner.getEditor().setTextFormatter(formatter);
      factory.valueProperty().bindBidirectional(formatter.valueProperty());
      spinner.setEditable(isEditable);
      addKeyAndScrollEvents(spinner);
    }
    if (bidirectionalBindIP != null)
    {
      bidirectionalBindIP.bindBidirectional(spinner.getValueFactory().valueProperty());
    }
    adjustSpinnerSettings(spinner, maxWidth);
    return spinner;
  }

  public final static Spinner<Integer> getSpinner(final int min, final int max, final int initialValue, final int step, final int maxWidth, final boolean isEditable, final ObjectProperty<Integer> bidirectionalBindIP)
  {
    final Spinner<Integer> spinner;
    if (!isEditable)
    {
      spinner = new Spinner<>(min, max, initialValue, step);
      spinner.getValueFactory().setConverter(ThousandsFormatter.getThousandsGroupingConverterInteger());
    }
    else
    {// add commit on focus loss (see https://stackoverflow.com/questions/32340476/manually-typing-in-text-in-javafx-spinner-is-not-updating-the-value-unless-user)
      spinner = new Spinner<>();
      final SpinnerValueFactory<Integer> factory = new IntegerSpinnerValueFactory(min, max, initialValue, step);
      factory.setConverter(ThousandsFormatter.getThousandsGroupingConverterInteger());
      spinner.setValueFactory(factory);
      spinner.setEditable(true);
      final TextFormatter<Integer> formatter = new TextFormatter<>(factory.getConverter(), factory.getValue());
      spinner.getEditor().setTextFormatter(formatter);
      factory.valueProperty().bindBidirectional(formatter.valueProperty());
      spinner.setEditable(isEditable);
      addKeyAndScrollEvents(spinner);
    }
    if (bidirectionalBindIP != null)
    {
      bidirectionalBindIP.bindBidirectional(spinner.getValueFactory().valueProperty());
    }
    adjustSpinnerSettings(spinner, maxWidth);
    return spinner;
  }

  private static void adjustSpinnerSettings(final Spinner<? extends Number> spinner, final int maxWidth)
  {
    if (maxWidth > 0)
    {
      spinner.setMaxWidth(maxWidth);
    }
  }

  private static void addKeyAndScrollEvents(final Spinner<? extends Number> spinner)
  {
    spinner.setOnScroll(event ->
    {
      if (event.getDeltaY() > 0)
      {
        spinner.increment();
      }
      else if (event.getDeltaY() < 0)
      {
        spinner.decrement();
      }
    });
    spinner.getEditor().setOnKeyPressed(event ->
    {
      switch (event.getCode())
      {
        case UP:
        {
          spinner.increment();
          break;
        }
        case DOWN:
        {
          spinner.decrement();
          break;
        }
        default:
        {
          // do nothing
          break;
        }
      }
    });
  }
}
