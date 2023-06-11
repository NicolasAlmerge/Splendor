package ca.mcgill.nalmer.lsutilities.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a session.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class Session {
  private final String creator;
  private final SaveGame gameParameters;
  private final boolean launched;
  private final String[] players;
  private final Map<String, String> playerLocations;
  private final String savegameid;

  /**
   * Constructor.
   *
   * @param sessionCreator Session creator username.
   * @param saveGame       Save game parameters.
   * @param isLaunched     Whether the game is launched.
   * @param playerNames    Player usernames.
   * @param locations      Player locations.
   * @param id             Save game id.
   */
  public Session(
      String sessionCreator, SaveGame saveGame, boolean isLaunched, String[] playerNames,
      Map<String, String> locations, String id) {
    creator = sessionCreator;
    gameParameters = saveGame;
    launched = isLaunched;
    players = playerNames.clone();
    playerLocations = Map.copyOf(locations);
    savegameid = id;
  }

  /**
   * Get the creator name.
   *
   * @return Creator name.
   */
  public String getCreator() {
    return creator;
  }

  /**
   * Get the game parameters.
   *
   * @return Game parameters.
   */
  public SaveGame getGameParameters() {
    return gameParameters;
  }

  /**
   * Check whether the session has been launched.
   *
   * @return Whether the session has been launched.
   */
  public boolean isLaunched() {
    return launched;
  }

  /**
   * Get the list of player usernames.
   *
   * @return Player usernames.
   */
  public String[] getPlayers() {
    return players.clone();
  }

  /**
   * Get the player locations.
   *
   * @return Player locations.
   */
  public Map<String, String> getPlayerLocations() {
    return Map.copyOf(playerLocations);
  }

  /**
   * Get the save game id.
   *
   * @return Save game id.
   */
  public String getSaveGameId() {
    return savegameid;
  }

  /**
   * Checks if two {@link Session} are equal.
   *
   * @param other Other object to compare.
   * @return <code>true</code> if objects are equal, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other instanceof Session otherSession) {
      return creator.equals(otherSession.creator)
          && Objects.equals(gameParameters, otherSession.gameParameters)
          && launched == otherSession.launched
          && Arrays.equals(players, otherSession.players)
          && playerLocations.equals(otherSession.playerLocations)
          && savegameid.equals(otherSession.savegameid);
    }
    return false;
  }
}
