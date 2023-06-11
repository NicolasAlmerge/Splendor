package ca.mcgill.nalmer.lsutilities.model;

import java.util.Arrays;

/**
 * Represents a save game.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class SaveGame {
  private final String gamename;
  private final String[] players;
  private final String savegameid;

  /**
   * Constructor.
   *
   * @param name        Save game name.
   * @param playerNames Player names.
   * @param id          Save game id.
   */
  public SaveGame(String name, String[] playerNames, String id) {
    gamename = name;
    players = playerNames.clone();
    savegameid = id;
  }

  /**
   * Get the game name.
   *
   * @return Game name.
   */
  public String getGameName() {
    return gamename;
  }

  /**
   * Get the list of player names.
   *
   * @return Player names.
   */
  public String[] getPlayerNames() {
    return players.clone();
  }

  /**
   * Get the save game id.
   *
   * @return Save game id.
   */
  public String getId() {
    return savegameid;
  }

  /**
   * Checks if two {@link SaveGame} are equal.
   *
   * @param other Other object to compare.
   * @return <code>true</code> if objects are equal, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other instanceof SaveGame otherGame) {
      return gamename.equals(otherGame.gamename)
          && Arrays.equals(players, otherGame.players)
          && savegameid.equals(otherGame.savegameid);
    }
    return false;
  }
}
