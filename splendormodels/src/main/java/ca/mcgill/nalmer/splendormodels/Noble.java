package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a noble.
 */
public final class Noble extends PrestigeCard {
  /**
   * Constructor. Each noble is worth 3 points.
   *
   * @param cost Noble cost.
   * @param id   PrestigeCard id.
   * @throws NullPointerException if <code>cost</code> is <code>null</code>.
   */
  public Noble(Cost cost, int id) {
    super(cost, 3, id);
  }

  @Override
  public CardType getCardType() {
    return CardType.NOBLE;
  }

  @Override
  public PrestigeCardType getPrestigeCardType() {
    return PrestigeCardType.NOBLE;
  }
}
