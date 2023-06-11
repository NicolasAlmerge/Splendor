package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a card that caries prestige points.
 */
public abstract class PrestigeCard extends Card {
  private final int points;

  /**
   * Constructor.
   *
   * @param cardCost     Cost of the card.
   * @param numberPoints Number of points.
   * @param id           Card id.
   * @throws NullPointerException if <code>cardCost</code> is <code>null</code>.
   */
  protected PrestigeCard(Cost cardCost, int numberPoints, int id) {
    super(cardCost, id);
    points = numberPoints;
  }

  /**
   * Returns the number of points of the card.
   *
   * @return Number of points.
   */
  public final int getPoints() {
    return points;
  }

  /**
   * Returns the {@link PrestigeCardType} of the card.
   *
   * @return {@link PrestigeCardType} of the card.
   */
  public abstract PrestigeCardType getPrestigeCardType();
}
