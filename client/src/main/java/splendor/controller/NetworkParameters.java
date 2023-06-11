package splendor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Contains network data. This data is read from the locations.properties file.
 */
public final class NetworkParameters {
  private static final String propFileLocation = "src"
      + File.separator + "main"
      + File.separator + "resources"
      + File.separator + "locations.properties";

  private static boolean loaded = false;
  private static String serverLocation = "";
  private static String lobbyLocation = "";

  /**
   * Private constructor.
   */
  private NetworkParameters() {}

  /**
   * Load network properties. This has no effects if they have already been loaded.
   *
   * @throws RuntimeException if properties loading failed.
   */
  public static void load() throws RuntimeException {
    if (loaded) {
      return;
    }

    Properties prop = new Properties();
    try {
      FileInputStream in = new FileInputStream(propFileLocation);
      prop.load(in);
      in.close();

      serverLocation = prop.getProperty("server.url") + ":" + prop.getProperty("server.port");
      lobbyLocation =  prop.getProperty("lobby.url") + ":" + prop.getProperty("lobby.port");
      loaded = true;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the URL of the Splendor game-service. This is read from the locations.properties file.
   *
   * @return Base URL of the Splendor game-service.
   * @throws RuntimeException if network properties not loaded.
   */
  public static String getServerLocation() throws RuntimeException {
    assertLoaded();
    return serverLocation;
  }

  /**
   * Get the URL of the LS. This is read from the locations.properties file.
   *
   * @return Base URL of the LobbyService.
   * @throws RuntimeException if network properties not loaded.
   */
  public static String getLobbyLocation() throws RuntimeException {
    assertLoaded();
    return lobbyLocation;
  }

  private static void assertLoaded() {
    if (!loaded) {
      throw new RuntimeException("network properties not loaded");
    }
  }
}
