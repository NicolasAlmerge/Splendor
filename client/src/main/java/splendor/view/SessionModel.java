package splendor.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Models the session information needed by the LobbyPage.
 */
public final class SessionModel {
  private final SimpleStringProperty sessionId;
  private final SimpleStringProperty creatorName;
  private final SimpleStringProperty players;
  private boolean isLaunched;
  private boolean isSaveGame;
  private int playerCount;

  /**
   * Create a new Session entry.
   *
   * @param sessionId   sessionId of the session
   * @param creatorName username of the player that create the session
   * @param players     list of all players in the session, including the creator
   * @param isLaunched  boolean initialising the value of isLaunched
   * @param isSaveGame  flag for if the session was created from a saved game or not.
   * @param playerCount Player count.
   */
  public SessionModel(String sessionId, String creatorName, String players,
                      boolean isLaunched, boolean isSaveGame, int playerCount) {
    this.sessionId = new SimpleStringProperty(sessionId);
    this.creatorName = new SimpleStringProperty(creatorName);
    this.players = new SimpleStringProperty(players);
    this.isLaunched = isLaunched;
    this.isSaveGame = isSaveGame;
    this.playerCount = playerCount;
  }

  /**
   * Get isLaunched.
   *
   * @return value of isLaunched.
   */
  public boolean getIsLaunched() {
    return isLaunched;
  }

  /**
   * Get isLaunched.
   *
   * @param value for isLaunched.
   */
  public void setIsLaunched(boolean value) {
    this.isLaunched = value;
  }

  /**
   * Get the session id.
   *
   * @return ID of the session as a String.
   */
  public SimpleStringProperty getSessionId() {
    return sessionId;
  }

  /**
   * Set the session id to the provided id.
   *
   * @param sessionId New sessionId
   */
  public void setSessionId(String sessionId) {
    this.sessionId.set(sessionId);
  }

  /**
   * Get the name of the session creator.
   *
   * @return Username of the session creator as a String.
   */
  public String getCreatorName() {
    return creatorName.get();
  }

  /**
   * Set the creator of the session to the provided player.
   *
   * @param creatorName Username of the session creator
   */
  public void setCreatorName(String creatorName) {
    this.creatorName.set(creatorName);
  }

  /**
   * Get the name of all players in the session.
   *
   * @return Comma seperated list of players as a string.
   */
  public String getPlayers() {
    return players.get();
  }

  /**
   * Set the list of players in the session.
   *
   * @param players Comma seperated list of players in the session.
   */
  public void setPlayers(String players) {
    this.players.set(players);
  }

  /**
   * Get an array of the usernames of all players in the session.
   *
   * @return An array with the usernames of all players.
   */
  public String[] splitPlayerNames() {
    String[] splitNames = players.get().split(",");
    for (int i = 0; i < splitNames.length; i++) {
      splitNames[i] = splitNames[i].trim();
    }
    return splitNames;
  }

  // Property getters

  /**
   * Gets session id property.
   *
   * @return Session id property.
   */
  public StringProperty sessionIdProperty() {
    return sessionId;
  }

  /**
   * Gets creator name.
   *
   * @return Creator name.
   */
  public StringProperty creatorNameProperty() {
    return creatorName;
  }

  /**
   * Get players.
   *
   * @return Players.
   */
  public StringProperty playersProperty() {
    return players;
  }

  /**
   * Get whether it is a save game.
   *
   * @return Save game or not.
   */
  public boolean isSaveGame() {
    return isSaveGame;
  }

  /**
   * Get save game.
   *
   * @param saveGame Save game.
   */
  public void setSaveGame(boolean saveGame) {
    isSaveGame = saveGame;
  }

  /**
   * Get player count.
   *
   * @return Player count.
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
