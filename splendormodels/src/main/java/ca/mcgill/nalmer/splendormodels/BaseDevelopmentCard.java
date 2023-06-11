package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a base development card.
 */
public final class BaseDevelopmentCard extends DevelopmentCard {
  /**
   * Constructor.
   *
   * @param cost          Cost of the card.
   * @param resourceColor PrestigeCard color.
   * @param numberPoints  Number of points.
   * @param level         PrestigeCard level.
   * @param id            PrestigeCard id.
   * @throws NullPointerException if <code>cost</code>, <code>level</code> or
   *                              <code>resourceColor</code> is <code>null</code>.
   */
  public BaseDevelopmentCard(
      Cost cost, ResourceColor resourceColor, int numberPoints,
      CardLevel level, int id
  ) {
    super(cost, resourceColor, numberPoints, level, id);
    if (resourceColor == null) {
      throw new NullPointerException("resourceColor cannot be null for a base development card");
    }
  }

  @Override
  public CardType getCardType() {
    return CardType.BASE;
  }

  @Override
  public PrestigeCardType getPrestigeCardType() {
    return PrestigeCardType.BASE;
  }

  @Override
  public DevelopmentCardType getDevelopmentCardType() {
    return DevelopmentCardType.BASE;
  }
}
