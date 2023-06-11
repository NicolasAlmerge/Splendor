package ca.mcgill.nalmer.splendormodels;

/**
 * Represents an orient card's ability. The values are {@link OrientAbility#TWO_GOLD_TOKENS},
 * {@link OrientAbility#BONUS_MATCHING}, {@link OrientAbility#BONUS_MATCHING_TAKE_LEVEL_ONE},
 * {@link OrientAbility#TWO_BONUSES}, {@link OrientAbility#RESERVE_NOBLE},
 * {@link OrientAbility#TAKE_LEVEL_TWO}, {@link OrientAbility#DISCARD_TWO_BONUSES}.
 */
public enum OrientAbility {
  /**
   * Two {@link ResourceColor#GOLD} tokens card.
   */
  TWO_GOLD_TOKENS,

  /**
   * PrestigeCard asks user to match it with a bonus.
   */
  BONUS_MATCHING,

  /**
   * PrestigeCard asks user to match it with a bonus and take a {@link DevelopmentCard} of level
   * {@link CardLevel#ONE}.
   */
  BONUS_MATCHING_TAKE_LEVEL_ONE,

  /**
   * PrestigeCard is a two (non {@link ResourceColor#GOLD}) bonus card.
   */
  TWO_BONUSES,

  /**
   * PrestigeCard asks user to reserve a {@link Noble}.
   */
  RESERVE_NOBLE,

  /**
   * PrestigeCard asks user to take a {@link DevelopmentCard} of level {@link CardLevel#TWO}.
   */
  TAKE_LEVEL_TWO,

  /**
   * PrestigeCard asks user to discard two bonuses.
   */
  DISCARD_TWO_BONUSES;

  /**
   * Returns the {@link CardLevel} associated with this ability.
   *
   * @return {@link CardLevel} of this ability.
   */
  public CardLevel getCardLevel() {
    return switch (values()[ordinal()]) {
      case TWO_GOLD_TOKENS, BONUS_MATCHING -> CardLevel.ONE;
      case BONUS_MATCHING_TAKE_LEVEL_ONE, TWO_BONUSES, RESERVE_NOBLE -> CardLevel.TWO;
      case TAKE_LEVEL_TWO, DISCARD_TWO_BONUSES -> CardLevel.THREE;
    };
  }

  /**
   * Checks whether the ability is {@link OrientAbility#BONUS_MATCHING} or
   * {@link OrientAbility#BONUS_MATCHING_TAKE_LEVEL_ONE}.
   *
   * @return <code>true</code> if ability is a bonus matching, <code>false</code> otherwise.
   */
  public boolean willMatchBonus() {
    final OrientAbility value = values()[ordinal()];
    return value == BONUS_MATCHING || value == BONUS_MATCHING_TAKE_LEVEL_ONE;
  }
}
