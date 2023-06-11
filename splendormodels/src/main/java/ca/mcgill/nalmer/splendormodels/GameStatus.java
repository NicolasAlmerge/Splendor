package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a game status. The values are {@link GameStatus#READY_TO_JOIN},
 * {@link GameStatus#ONGOING}, {@link GameStatus#SAVED} and {@link GameStatus#FINISHED}.
 */
public enum GameStatus {
  /**
   * People are ready to join.
   */
  READY_TO_JOIN,

  /**
   * Game is ongoing.
   */
  ONGOING,

  /**
   * Game has been saved.
   */
  SAVED,

  /**
   * Game has been finished.
   */
  FINISHED
}
