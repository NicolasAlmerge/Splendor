package ca.mcgill.nalmer.lsutilities.data;

/**
 * Lobby service data.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class LobbyServiceData {
  private static String LS_LOCATION = "http://127.0.0.1:4242";

  /**
   * Retrieves the lobby service location.
   *
   * @return Lobby service location URL.
   */
  public static String getLsLocation() {
    return LS_LOCATION;
  }

  /**
   * Sets the new lobby service location.
   *
   * @param newLocation New lobby service location URL.
   */
  public static void setLsLocation(String newLocation) {
    LS_LOCATION = newLocation;
  }

  /**
   * Private constructor.
   */
  private LobbyServiceData() {}
}
