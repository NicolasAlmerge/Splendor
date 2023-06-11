package ca.mcgill.nalmer.lsutilities.model;

import ca.mcgill.nalmer.lsutilities.data.LobbyServiceData;

/**
 * Represents a game service.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class GameService extends GameServiceBasicInfo {
  private final String location;
  private final int maxSessionPlayers;
  private final int minSessionPlayers;
  private final boolean webSupport;

  /**
   * Constructor.
   *
   * @param name        Name (id).
   * @param displayName Display name.
   * @param minPlayers  Min number of players.
   * @param maxPlayers  Max number of players.
   * @param gsLocation  Location.
   * @param supportWeb  Whether it supports web.
   */
  public GameService(
      String name, String displayName, int minPlayers,
      int maxPlayers, String gsLocation, boolean supportWeb) {
    super(name, displayName);
    location = gsLocation;
    maxSessionPlayers = maxPlayers;
    minSessionPlayers = minPlayers;
    webSupport = supportWeb;
  }

  /**
   * Constructor.
   *
   * @param name        Name (id).
   * @param displayName Display name.
   * @param minPlayers  Min number of players.
   * @param maxPlayers  Max number of players.
   * @param supportWeb  Whether it supports web.
   */
  public GameService(
      String name, String displayName, int minPlayers, int maxPlayers, boolean supportWeb) {
    this(
        name, displayName, minPlayers, maxPlayers,
        LobbyServiceData.getLsLocation() + "/" + name,
        supportWeb
    );
  }

  /**
   * Constructor.
   *
   * @param name        Name (id).
   * @param displayName Display name.
   * @param minPlayers  Min number of players.
   * @param maxPlayers  Max number of players.
   * @param gsLocation  Location.
   */
  public GameService(
      String name, String displayName, int minPlayers, int maxPlayers, String gsLocation) {
    this(name, displayName, minPlayers, maxPlayers, gsLocation, true);
  }

  /**
   * Constructor.
   *
   * @param name        Name (id).
   * @param displayName Display name.
   * @param minPlayers  Min number of players.
   * @param maxPlayers  Max number of players.
   */
  public GameService(String name, String displayName, int minPlayers, int maxPlayers) {
    this(name, displayName, minPlayers, maxPlayers, true);
  }

  /**
   * Get the location URL.
   *
   * @return Location URL.
   */
  public String getLocation() {
    return location;
  }

  /**
   * Get the minimum number of players in the game service.
   *
   * @return Minimum number of players in the game service.
   */
  public int getMinSessionPlayers() {
    return minSessionPlayers;
  }

  /**
   * Get the maximum number of players in the game service.
   *
   * @return Maximum number of players in the game service.
   */
  public int getMaxSessionPlayers() {
    return maxSessionPlayers;
  }

  /**
   * Get whether the game service has web support.
   *
   * @return Whether the game service has web support.
   */
  public boolean hasWebSupport() {
    return webSupport;
  }

  /**
   * Checks if two {@link GameService} are equal.
   *
   * @param other Other object to compare.
   * @return <code>true</code> if objects are equal, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (!super.equals(other)) {
      return false;
    }
    if (other instanceof GameService otherService) {
      return location.equals(otherService.location)
          && maxSessionPlayers == otherService.maxSessionPlayers
          && minSessionPlayers == otherService.minSessionPlayers
          && webSupport == otherService.webSupport;
    }
    return false;
  }
}
