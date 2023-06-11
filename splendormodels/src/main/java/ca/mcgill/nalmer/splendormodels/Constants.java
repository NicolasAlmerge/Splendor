package ca.mcgill.nalmer.splendormodels;

/**
 * Represents shared constants.
 */
public final class Constants {
  /**
   * Minimum number of players.
   */
  public static final int MIN_NUMBER_OF_PLAYERS = 2;

  /**
   * Maximum number of players.
   */
  public static final int MAX_NUMBER_OF_PLAYERS = 4;

  /**
   * Maximum number of tokens a user can have.
   */
  public static final int USER_MAX_NUMBER_OF_TOKENS = 10;

  /**
   * Maximum number of cards a user can reserve.
   */
  public static final int USER_MAX_NUMBER_OF_RESERVED_CARDS = 3;

  /**
   * Number of base cards in a row.
   */
  public static final int NUMBER_BASE_CARDS_PER_ROW = 4;

  /**
   * Number of orient cards in a row.
   */
  public static final int NUMBER_ORIENT_CARDS_PER_ROW = 2;

  /**
   * Initial number of {@link ResourceColor#GOLD} tokens (jokers).
   */
  public static final int INITIAL_NUMBER_JOKERS = 5;

  /**
   * Number of cities.
   */
  public static final int NUMBER_CITIES = 3;

  /**
   * Minimum number of prestige points required to win a regular game.
   */
  public static final int NUMBER_POINTS_TO_WIN = 15;

  /**
   * Private constructor.
   */
  private Constants() {}
}
