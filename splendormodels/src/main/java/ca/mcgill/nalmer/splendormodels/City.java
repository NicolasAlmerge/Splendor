package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a city.
 */
public final class City extends NonPrestigeCard {
  private final int additionalBonusCost;
  private final int requiredCost;

  /**
   * Constructor.
   *
   * @param cityCost     Cost of the city.
   * @param anyBonusCost Additional bonus cost.
   * @param pointCost    Number of points required to get it.
   * @param id           Card id.
   * @throws NullPointerException if <code>cityCost</code> is <code>null</code>.
   * @throws RuntimeException     if <code>anyBonusCost</code> is less than <code>0</code>.
   */
  public City(Cost cityCost, int anyBonusCost, int pointCost, int id) {
    super(cityCost, id);
    if (anyBonusCost < 0) {
      throw new RuntimeException("anyBonusCost cannot be less than 0");
    }
    additionalBonusCost = anyBonusCost;
    requiredCost = pointCost;
  }

  /**
   * Constructor.
   *
   * @param cityCost  Cost of the city.
   * @param pointCost Number of points required to get it.
   * @param id        Card id.
   * @throws NullPointerException if <code>cityCost</code> is <code>null</code>.
   */
  public City(Cost cityCost, int pointCost, int id) {
    this(cityCost, 0, pointCost, id);
  }

  /**
   * Constructor.
   *
   * @param anyBonusCost Additional bonus cost.
   * @param pointCost    Number of points required to get it.
   * @param id           Card id.
   * @throws RuntimeException if <code>anyBonusCost</code> is less than <code>0</code>.
   */
  public City(int anyBonusCost, int pointCost, int id) {
    this(new Cost(), anyBonusCost, pointCost, id);
  }

  /**
   * Constructor.
   *
   * @param pointCost    Number of points required to get it.
   * @param id           Card id.
   */
  public City(int pointCost, int id) {
    this(new Cost(), 0, pointCost, id);
  }

  /**
   * Gets the additional bonus cost.
   *
   * @return Additional bonus cost.
   */
  public int getAdditionalBonusCost() {
    return additionalBonusCost;
  }

  /**
   * Gets the number of development points required to get the card.
   *
   * @return Required number of development points.
   */
  public int getRequiredPointCost() {
    return requiredCost;
  }

  @Override
  public boolean obtainableBy(Player player) {
    if (player == null) {
      throw new NullPointerException("player cannot be null");
    }

    // Not enough prestige points
    if (player.getPrestigePts() < requiredCost) {
      return false;
    }

    // Check bonus counts
    boolean passed = (additionalBonusCost == 0);
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      final int bonusCount = player.getNumberOfBonusesOfColor(color);
      if (bonusCount < getCost().getCost(color)) {
        return false;
      }
      if (bonusCount >= additionalBonusCost) {
        passed = true;
      }
    }

    // Verifies that everything passes
    return passed;
  }

  @Override
  public CardType getCardType() {
    return CardType.CITY;
  }

  @Override
  public NonPrestigeCardType getNonPrestigeCardType() {
    return NonPrestigeCardType.CITY;
  }
}
