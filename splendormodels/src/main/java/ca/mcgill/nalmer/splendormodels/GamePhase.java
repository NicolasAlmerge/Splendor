package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a game phase. The values are {@link GamePhase#TURN_STARTED},
 * {@link GamePhase#CITY_SELECTION}, {@link GamePhase#TOKEN_SELECTION} and
 * {@link GamePhase#GAME_ENDED}.
 */
public enum GamePhase {
  /**
   * Player's turn has started.
   */
  TURN_STARTED,

  /**
   * Player selects a {@link City}.
   */
  CITY_SELECTION,

  /**
   * Player selects a token after selecting a card if he has the
   * {@link TradingPostAbility#TAKE_TOKEN_AFTER_TAKING_CARD} ability.
   */
  TOKEN_SELECTION,

  /**
   * Game has ended.
   */
  GAME_ENDED
}
