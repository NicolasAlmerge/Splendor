package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a card that doesn't have prestige points.
 */
public abstract class NonPrestigeCard extends Card {
  /**
   * Constructor.
   *
   * @param cardCost Cost of the card.
   * @param cardId   Card id.
   * @throws NullPointerException if <code>cardCost</code> is <code>null</code>.
   */
  protected NonPrestigeCard(Cost cardCost, int cardId) {
    super(cardCost, cardId);
  }

  /**
   * Returns the {@link NonPrestigeCardType} of the card.
   *
   * @return {@link NonPrestigeCardType} of the card.
   */
  public abstract NonPrestigeCardType getNonPrestigeCardType();

  /**
   * Checks whether the card is obtainable by a {@link Player}.
   *
   * @param player {@link Player} to check.
   * @return <code>true</code> if the card is obtainable by <code>player</code>,
   *         <code>false</code> otherwise.
   * @throws NullPointerException if <code>player</code> is <code>null</code>.
   */
  public abstract boolean obtainableBy(Player player);
}
