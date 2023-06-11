package ca.mcgill.nalmer.splendormodels;

import java.util.Map;

/**
 * Represents an orient development card.
 */
public final class OrientDevelopmentCard extends DevelopmentCard {
  private final OrientAbility ability;

  private OrientDevelopmentCard(
      Cost cost, ResourceColor resourceColor,
      int numberPoints, CardLevel level, OrientAbility cardAbility, int id
  ) {
    super(cost, resourceColor, numberPoints, level, id);
    ability = cardAbility;
  }

  /**
   * Constructs a "two gold bonuses" level 1 orient card (0 points).
   *
   * @param cost PrestigeCard cost.
   * @param id   PrestigeCard id.
   * @return Orient development card.
   */
  public static OrientDevelopmentCard twoGoldBonuses(Cost cost, int id) {
    return new OrientDevelopmentCard(
        cost, ResourceColor.GOLD, 0, CardLevel.ONE, OrientAbility.TWO_GOLD_TOKENS, id
    );
  }

  /**
   * Constructs a "bonus matching" level 1 orient card (0 points).
   *
   * @param cost PrestigeCard cost.
   * @param id   PrestigeCard id.
   * @return Orient development card.
   */
  public static OrientDevelopmentCard bonusMatching(Cost cost, int id) {
    return new OrientDevelopmentCard(
        cost, null, 0, CardLevel.ONE, OrientAbility.BONUS_MATCHING, id
    );
  }

  /**
   * Constructs a "two bonuses" level 2 orient card (0 points).
   *
   * @param resourceColor PrestigeCard color (cannot be null or gold).
   * @param cost          PrestigeCard cost.
   * @param id            PrestigeCard id.
   * @return Orient development card.
   * @throws NullPointerException if <code>resourceColor</code> is <code>null</code>.
   * @throws RuntimeException     if <code>resourceColor</code> is {@link ResourceColor#GOLD}.
   */
  public static OrientDevelopmentCard twoBonuses(ResourceColor resourceColor, Cost cost, int id) {
    if (resourceColor == null) {
      throw new NullPointerException("resourceColor cannot be null");
    }
    if (resourceColor == ResourceColor.GOLD) {
      throw new RuntimeException("resourceColor cannot be gold");
    }
    return new OrientDevelopmentCard(
        cost, resourceColor, 0, CardLevel.TWO, OrientAbility.TWO_BONUSES, id
    );
  }

  /**
   * Constructs a "reverse a noble" level 2 orient card (1 point).
   *
   * @param resourceColor PrestigeCard color.
   * @param cost          PrestigeCard cost.
   * @param id            PrestigeCard id.
   * @return Orient development card.
   */
  public static OrientDevelopmentCard reserveNoble(ResourceColor resourceColor, Cost cost, int id) {
    return new OrientDevelopmentCard(
        cost, resourceColor, 1, CardLevel.TWO, OrientAbility.RESERVE_NOBLE, id
    );
  }

  /**
   * Constructs a "bonus matching and take level 1 card" level 2 orient card (0 points).
   *
   * @param cost PrestigeCard cost.
   * @param id   PrestigeCard id.
   * @return Orient development card.
   */
  public static OrientDevelopmentCard bonusMatchingAndTake(Cost cost, int id) {
    return new OrientDevelopmentCard(
        cost, null, 0, CardLevel.TWO,
        OrientAbility.BONUS_MATCHING_TAKE_LEVEL_ONE, id
    );
  }

  /**
   * Constructs a "discard 2 bonuses" level 3 orient card (3 points).
   *
   * @param color   PrestigeCard color.
   * @param discard Color to discard two bonuses from.
   * @param id      PrestigeCard id.
   * @return Orient development card.
   */
  public static OrientDevelopmentCard discard2Bonuses(
      ResourceColor color, ResourceColor discard, int id
  ) {
    return new OrientDevelopmentCard(
        new Cost(Map.of(discard, 2)), color, 3, CardLevel.THREE,
        OrientAbility.DISCARD_TWO_BONUSES, id
    );
  }

  /**
   * Constructs a "take level 2 card" level 3 orient card (0 points).
   *
   * @param color PrestigeCard color.
   * @param cost  PrestigeCard cost.
   * @param id    PrestigeCard id.
   * @return Orient development card.
   */
  public static OrientDevelopmentCard takeLevel2(ResourceColor color, Cost cost, int id) {
    return new OrientDevelopmentCard(
        cost, color, 0, CardLevel.THREE, OrientAbility.TAKE_LEVEL_TWO, id
    );
  }

  /**
   * Get the orient card's special ability.
   *
   * @return Orient ability.
   */
  public OrientAbility getAbility() {
    return ability;
  }

  /**
   * Changes the colour of the card.
   * Can only be used if colour has not yet been set.
   *
   * @param newColor New colour.
   * @throws RuntimeException     if color has already been set.
   * @throws NullPointerException if <code>newColor</code> is <code>null</code>.
   */
  public void setColour(ResourceColor newColor) {
    changeColour(newColor);
  }

  @Override
  public CardType getCardType() {
    return CardType.ORIENT;
  }

  @Override
  public PrestigeCardType getPrestigeCardType() {
    return PrestigeCardType.ORIENT;
  }

  @Override
  public DevelopmentCardType getDevelopmentCardType() {
    return DevelopmentCardType.ORIENT;
  }
}
