package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a development card.
 */
public abstract class DevelopmentCard extends PrestigeCard {
  private final CardLevel cardLevel;
  private ResourceColor color;

  /**
   * Constructor.
   *
   * @param cost          Cost of the card.
   * @param resourceColor Color of the card.
   * @param numberPoints  Number of points.
   * @param level         PrestigeCard level.
   * @param id            PrestigeCard id.
   * @throws NullPointerException if <code>cost</code> or <code>level</code> is <code>null</code>.
   */
  protected DevelopmentCard(
      Cost cost, ResourceColor resourceColor, int numberPoints, CardLevel level, int id
  ) {
    super(cost, numberPoints, id);
    if (level == null) {
      throw new NullPointerException("level cannot be null");
    }
    color = resourceColor;
    cardLevel = level;
  }

  /**
   * Returns the {@link ResourceColor} of the development card.
   *
   * @return {@link ResourceColor} of the development card.
   */
  public final ResourceColor getColor() {
    return color;
  }

  /**
   * Changes the colour of the card.
   * Can only be used if colour has not already been set.
   *
   * @param newColor New colour.
   * @throws RuntimeException if color has already been set.
   * @throws NullPointerException if <code>newColor</code> is <code>null</code>.
   */
  protected final void changeColour(ResourceColor newColor) {
    if (color != null) {
      throw new RuntimeException("cannot change colour if not set");
    }
    if (newColor == null) {
      throw new NullPointerException("new colour cannot be null");
    }
    color = newColor;
  }

  /**
   * Returns the level of the card.
   *
   * @return PrestigeCard level.
   */
  public final CardLevel getLevel() {
    return cardLevel;
  }

  /**
   * Returns the {@link DevelopmentCardType} of the card.
   *
   * @return {@link DevelopmentCardType} of the card.
   */
  public abstract DevelopmentCardType getDevelopmentCardType();
}
