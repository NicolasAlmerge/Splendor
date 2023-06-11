package splendor.view;

import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import javafx.beans.property.SimpleStringProperty;

/**
 * Models the session information needed by the LobbyPage.
 */
public final class SavedGameModel {
  private final SimpleStringProperty sessionId;
  private final SaveGame saveGame;
  private final SimpleStringProperty players;
  private boolean isLaunched;
  private int playerCount;

  /**
   * Create a new Session entry.
   *
   * @param sessionId   sessionId of the session
   * @param saveGame    savegame object
   * @param players     list of all players in the session, including the creator
   * @param isLaunched  boolean initialising the value of isLaunched
   */
  public SavedGameModel(String sessionId, SaveGame saveGame, String players, boolean isLaunched) {
    this.sessionId = new SimpleStringProperty(sessionId);
    this.saveGame = saveGame;
    this.players = new SimpleStringProperty(players);
    this.isLaunched = isLaunched;
    this.playerCount = getPlayersLength();
  }

  /**
   * Get save game.
   *
   * @return Save game.
   */
  public SaveGame getSaveGame() {
    return saveGame;
  }

  /**
   * Get players.
   *
   * @return Players.
   */
  public String getPlayers() {
    return players.get();
  }

  /**
   * Get player properties.
   *
   * @return Player properties.
   */
  public SimpleStringProperty playersProperty() {
    return players;
  }


  /**
   * Get players.
   *
   * @return Players.
   */
  public int getPlayersLength() {
    int counter = 0;
    String[] splitNames = players.get().split(",");
    return splitNames.length;
  }

  /**
   * Get session id.
   *
   * @return Session id.
   */
  public String getSessionId() {
    return sessionId.get();
  }

  /**
   * Get session id property.
   *
   * @return Session id property.
   */
  public SimpleStringProperty sessionIdProperty() {
    return sessionId;
  }

  /**
   * Get player count.
   *
   * @return Set player count.
   */
  public int getPlayerCount() {
    return playerCount;
  }

  /**
   * Set player count.
   *
   * @param playerCount New player count.
   */
  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }
}
