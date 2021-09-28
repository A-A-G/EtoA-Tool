/**
 * 
 */
package properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * @author AAG
 *
 */
public class AppProperties extends Properties
{
  private static final long serialVersionUID = -2122663607890424230L;

  // Keys
  public final static String NAME = "name";
  public final static String ROUND = "round";
  public final static String CSS_FOLDER = "css_folder";
  public final static String APPLICATION_CSS = "css";
  public final static String IMAGE_FOLDER = "images";
  public final static String GAME_ICON = "icon";
  public final static String GITHUB_URL = "github";
  public final static String CONFIG_PATH = "config";
  public final static String VERSION = "version";
  public final static String EXPIRE_DATE = "expire_date";
  public final static String CHANGELOG_PATH = "changelog";
  public final static String AUTHOR = "author";

  // Default values
  private final static String DEFAULT_ROUND = "round";
  private final static String DEFAULT_CSS_FOLDER = "/css/";
  private final static String DEFAULT_APPLICATION_CSS = "application.css";
  private final static String DEFAULT_IMAGE_FOLDER = "/images/";
  private final static String DEFAULT_GAME_ICON = "EtoA32.png";
  private final static String DEFAULT_GITHUB_URL = "https://github.com/A-A-G/EtoA-Tool";
  private final static String DEFAULT_CONFIG_PATH = "app.properties";
  private final static String DEFAULT_CHANGELOG_PATH = "changelog.txt";
  private final static String DEFAULT_AUTHOR = "AAG";

  /**
   * Load and update application properties.
   */
  public AppProperties(final String appName, final String version, final LocalDate expireDate)
  {
    super();
    final File file = new File(DEFAULT_CONFIG_PATH);
    if (file.exists() && !file.isDirectory())
    {
      try
      {
        load(new FileInputStream(file));
        System.out.println("Properties loaded.");
      }
      catch (final FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (final IOException e)
      {
        e.printStackTrace();
      }
    }
    if (getProperty(ROUND) == null)
    {
      setProperty(ROUND, DEFAULT_ROUND);
    }
    if (getProperty(CSS_FOLDER) == null)
    {
      setProperty(CSS_FOLDER, DEFAULT_CSS_FOLDER);
    }
    if (getProperty(APPLICATION_CSS) == null)
    {
      setProperty(APPLICATION_CSS, DEFAULT_APPLICATION_CSS);
    }
    if (getProperty(IMAGE_FOLDER) == null)
    {
      setProperty(IMAGE_FOLDER, DEFAULT_IMAGE_FOLDER);
    }
    if (getProperty(GAME_ICON) == null)
    {
      setProperty(GAME_ICON, DEFAULT_GAME_ICON);
    }
    if (getProperty(GITHUB_URL) == null)
    {
      setProperty(GITHUB_URL, DEFAULT_GITHUB_URL);
    }
    setProperty(NAME, appName);
    setProperty(CONFIG_PATH, DEFAULT_CONFIG_PATH);
    setProperty(VERSION, version);
    setProperty(EXPIRE_DATE, expireDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    setProperty(CHANGELOG_PATH, DEFAULT_CHANGELOG_PATH);
    setProperty(AUTHOR, DEFAULT_AUTHOR);
    try (OutputStream out = new FileOutputStream(DEFAULT_CONFIG_PATH))
    {
      store(out, "Application Properties");
      System.out.println("Properties updated.\n");
    }
    catch (final FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (final IOException e)
    {
      e.printStackTrace();
    }
  }

}
