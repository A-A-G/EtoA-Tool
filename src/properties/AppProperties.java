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
  public static final String NAME = "name";
  public static final String ROUND = "round";
  public static final String DOMAIN = "domain";
  public static final String PROTOCOL = "protocol";
  public static final String CSS_FOLDER = "css_folder";
  public static final String APPLICATION_CSS = "css";
  public static final String IMAGE_FOLDER = "images";
  public static final String GAME_ICON = "icon";
  public static final String GITHUB_URL = "github";
  public static final String CONFIG_PATH = "config";
  public static final String VERSION = "version";
  public static final String EXPIRE_DATE = "expire_date";
  public static final String CHANGELOG_PATH = "changelog";
  public static final String AUTHOR = "author";

  // Default values
  private static final String DEFAULT_ROUND = "round21";
  private static final String DEFAULT_DOMAIN = "etoa.net";
  private static final String DEFAULT_PROTOCOL = "https";
  private static final String DEFAULT_CSS_FOLDER = "/css/";
  private static final String DEFAULT_APPLICATION_CSS = "application.css";
  private static final String DEFAULT_IMAGE_FOLDER = "/images/";
  private static final String DEFAULT_GAME_ICON = "EtoA32.png";
  private static final String DEFAULT_GITHUB_URL = "https://github.com/A-A-G/EtoA-Tool";
  private static final String DEFAULT_CONFIG_PATH = "app.properties";
  private static final String DEFAULT_CHANGELOG_PATH = "changelog.txt";
  private static final String DEFAULT_AUTHOR = "AAG";

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
        System.out.println("App properties loaded.");
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
    if (getProperty(DOMAIN) == null)
    {
      setProperty(DOMAIN, DEFAULT_DOMAIN);
    }
    if (getProperty(PROTOCOL) == null)
    {
      setProperty(PROTOCOL, DEFAULT_PROTOCOL);
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
      System.out.println("App properties updated.\n");
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
