package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a {@link Card} type. The values are {@link CardType#BASE}, {@link CardType#ORIENT},
 * {@link CardType#NOBLE}, {@link CardType#CITY} and {@link CardType#TRADING_POST}.
 */
public enum CardType {
  /**
   * Base development card.
   */
  BASE,

  /**
   * Orient development card.
   */
  ORIENT,

  /**
   * Noble card.
   */
  NOBLE,

  /**
   * City card.
   */
  CITY,

  /**
   * Trading post card.
   */
  TRADING_POST;

  /**
   * Checks whether the card type maps to a {@link PrestigeCardType}.
   *
   * @return <code>true</code> if type represents a {@link PrestigeCard},
   *         <code>false</code> otherwise.
   */
  public boolean isPrestige() {
    final int value = ordinal();
    return value == BASE.ordinal() || value == ORIENT.ordinal() || value == NOBLE.ordinal();
  }

  /**
   * Checks whether the card type maps to a {@link DevelopmentCardType}.
   *
   * @return <code>true</code> if type represents a {@link DevelopmentCard},
   *         <code>false</code> otherwise.
   */
  public boolean isDevelopmentCard() {
    return ordinal() == BASE.ordinal() || ordinal() == ORIENT.ordinal();
  }
}
