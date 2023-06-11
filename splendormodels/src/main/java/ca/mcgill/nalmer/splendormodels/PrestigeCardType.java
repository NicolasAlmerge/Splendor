package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a {@link PrestigeCard} type. The values are {@link PrestigeCardType#BASE},
 * {@link PrestigeCardType#ORIENT} and {@link PrestigeCardType#NOBLE}.
 */
public enum PrestigeCardType {
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
  NOBLE;

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
