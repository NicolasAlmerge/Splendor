package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a trading post ability. The values are
 * {@link TradingPostAbility#TAKE_TOKEN_AFTER_TAKING_CARD},
 * {@link TradingPostAbility#TAKE_TOKEN_AFTER_TWO_TOKENS},
 * {@link TradingPostAbility#GOLD_TOKEN_WORTH_TWO_TOKENS}, {@link TradingPostAbility#FIVE_POINTS},
 * {@link TradingPostAbility#ONE_POINT_FOR_EACH_SHIELD}.
 */
public enum TradingPostAbility {
  /**
   * Ability to take a token after taking a {@link DevelopmentCard}.
   */
  TAKE_TOKEN_AFTER_TAKING_CARD,

  /**
   * Ability to take a different token after taking two tokens.
   */
  TAKE_TOKEN_AFTER_TWO_TOKENS,

  /**
   * Ability where a gold token is worth two tokens of the same color.
   */
  GOLD_TOKEN_WORTH_TWO_TOKENS,

  /**
   * Ability that adds five prestige points.
   */
  FIVE_POINTS,

  /**
   * Ability that adds one prestige point for each shield.
   */
  ONE_POINT_FOR_EACH_SHIELD
}
