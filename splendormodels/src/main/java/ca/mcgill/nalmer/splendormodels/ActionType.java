package ca.mcgill.nalmer.splendormodels;

/**
 * Represents an action type. The values are {@link ActionType#TAKE_TOKENS},
 * {@link ActionType#PURCHASE_CARD} and {@link ActionType#RESERVE_CARD}.
 */
public enum ActionType {
  /**
   * Take tokens action.
   */
  TAKE_TOKENS,

  /**
   * Purchase card action.
   */
  PURCHASE_CARD,

  /**
   * Reserve card action.
   */
  RESERVE_CARD
}
