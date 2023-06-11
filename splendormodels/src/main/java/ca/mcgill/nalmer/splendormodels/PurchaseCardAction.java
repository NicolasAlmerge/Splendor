package ca.mcgill.nalmer.splendormodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action that represents a card purchase.
 */
public final class PurchaseCardAction extends Action {
  private final DevelopmentCard card1;
  private final DevelopmentCard card2;
  private final DevelopmentCard card3;
  private final ResourceColor firstMatchingColor;
  private final ResourceColor secondMatchingColor;
  private final ResourceColor thirdMatchingColor;
  private final Noble reservedNoble;
  private final Noble nobleToTake;

  private PurchaseCardAction(
      DevelopmentCard card1, DevelopmentCard card2, DevelopmentCard card3,
      ResourceColor firstMatchingColor, ResourceColor secondMatchingColor,
      ResourceColor thirdMatchingColor, Noble reservedNoble, Noble nobleToTake, int id
  ) {
    super(id);
    this.card1 = card1;
    this.card2 = card2;
    this.card3 = card3;
    this.firstMatchingColor = firstMatchingColor;
    this.secondMatchingColor = secondMatchingColor;
    this.thirdMatchingColor = thirdMatchingColor;
    this.reservedNoble = reservedNoble;
    this.nobleToTake = nobleToTake;
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card1 PrestigeCard to purchase.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card1, int id
  ) {
    return all(game, player, card1, null, null, null, null, null, null, id);
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card1 PrestigeCard to purchase.
   * @param card2 Cascading card.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card1, DevelopmentCard card2, int id
  ) {
    return all(game, player, card1, card2, null, null, null, null, null, id);
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card1 PrestigeCard to purchase.
   * @param matchingColor Bonus matching color.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card1, ResourceColor matchingColor, int id
  ) {
    return all(game, player, card1, null, null, matchingColor, null, null, null, id);
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card1 PrestigeCard to purchase.
   * @param card2 Cascading card.
   * @param matchingColor Bonus matching color of first card.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card1, DevelopmentCard card2,
      ResourceColor matchingColor, int id
  ) {
    return all(game, player, card1, card2, null, matchingColor, null, null, null, id);
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card1 PrestigeCard to purchase.
   * @param card2 Cascading card.
   * @param first Bonus matching color of the first card.
   * @param second Bonus matching color of the cascading card.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card1, DevelopmentCard card2,
      ResourceColor first, ResourceColor second, int id
  ) {
    return all(game, player, card1, card2, null, first, second, null, null, id);
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card1 PrestigeCard to purchase.
   * @param card2 Level 2 cascading card.
   * @param card3 Level 1 cascading card.
   * @param second Level 2 color.
   * @param third Level 3 color.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card1, DevelopmentCard card2, DevelopmentCard card3,
      ResourceColor second, ResourceColor third, int id
  ) {
    return all(game, player, card1, card2, card3, null, second, third, null, id);
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card PrestigeCard to purchase.
   * @param reservedNoble Noble to reserve.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card, Noble reservedNoble, int id
  ) {
    return all(game, player, card, null, null, null, null, null, reservedNoble, id);
  }

  /**
   * Get all actions from data.
   *
   * @param game Current game.
   * @param player Player to play.
   * @param card1 PrestigeCard to purchase.
   * @param card2 Level 2 cascading card.
   * @param reservedNoble Noble to reserve.
   * @param id Action id.
   * @return List of card purchasing actions.
   */
  public static List<PurchaseCardAction> allFrom(
      Game game, Player player, DevelopmentCard card1, DevelopmentCard card2, Noble reservedNoble,
      int id
  ) {
    return all(game, player, card1, card2, null, null, null, null, reservedNoble, id);
  }

  /**
   * Gets the first card.
   *
   * @return First card if cascading, or only card if not.
   */
  public DevelopmentCard getFirstCard() {
    return card1;
  }

  /**
   * Gets the second card.
   *
   * @return Second card if cascading, or <code>null</code> if not.
   */
  public DevelopmentCard getSecondCard() {
    return card2;
  }

  /**
   * Gets the third card.
   *
   * @return Third card if cascading, or <code>null</code> if not.
   */
  public DevelopmentCard getThirdCard() {
    return card3;
  }

  /**
   * Get first color to pair card with, or <code>null</code> if none.
   *
   * @return First color to pair card with, or <code>null</code> if none.
   */
  public ResourceColor getFirstMatchingColor() {
    return firstMatchingColor;
  }

  /**
   * Get second color to pair card with, or <code>null</code> if none.
   *
   * @return Second color to pair card with, or <code>null</code> if none.
   */
  public ResourceColor getSecondMatchingColor() {
    return secondMatchingColor;
  }

  /**
   * Get third color to pair card with, or <code>null</code> if none.
   *
   * @return Third color to pair card with, or <code>null</code> if none.
   */
  public ResourceColor getThirdMatchingColor() {
    return thirdMatchingColor;
  }

  /**
   * Gets the reserved noble, or <code>null</code> if none.
   *
   * @return Returns the reserved noble, or <code>null</code> if none.
   */
  public Noble getReservedNoble() {
    return reservedNoble;
  }

  /**
   * Gets the unlocked noble, or null if none.
   *
   * @return Returns the unlocked noble, or null if none.
   */
  public Noble getNobleToTake() {
    return nobleToTake;
  }

  @Override
  public void execute(Game game, Player player) {
    // Potentially change colour of first card
    if (firstMatchingColor != null) {
      card1.changeColour(firstMatchingColor);
    }

    // Add bonus card
    Map<ResourceColor, Integer> toGiveBack = player.payAndAddBonus(card1);
    game.addTokens(toGiveBack);

    // Pop card from deck of from player's reserved cards
    if (!game.removeDevelopmentCard(card1)) {
      player.removeReservedCard(card1);
    }

    // Add card 2 if any
    if (card2 != null) {
      if (secondMatchingColor != null) {
        card2.changeColour(secondMatchingColor);
      }
      player.addBonus(card2);
      game.removeDevelopmentCard(card2);
    }

    // Add card 3 if any
    if (card3 != null) {
      if (thirdMatchingColor != null) {
        card3.changeColour(thirdMatchingColor);
      }
      player.addBonus(card3);
      game.removeDevelopmentCard(card3);
    }

    // Add reserved noble if any
    if (reservedNoble != null) {
      player.addReservedNobleCard(reservedNoble);
      game.removeNoble(reservedNoble);
    }

    // Add noble to take if any
    if (nobleToTake != null) {
      player.addNoble(nobleToTake);
      game.removeNoble(nobleToTake);
    }
  }

  @Override
  public ActionType getType() {
    return ActionType.PURCHASE_CARD;
  }

  private static List<PurchaseCardAction> all(Game game, Player player, DevelopmentCard card1,
                                              DevelopmentCard card2, DevelopmentCard card3,
                                              ResourceColor first,
                                              ResourceColor second,
                                              ResourceColor third,
                                              Noble reservedNoble, int id) {
    List<PurchaseCardAction> allActions = new ArrayList<>();
    Map<ResourceColor, Integer> count = getCount(player, card1, card2, card3, first, second, third);

    // Check all available game nobles
    for (Noble noble : game.getAvailableNobles()) {
      // If we are not going to reserve this noble
      if (reservedNoble == null || !reservedNoble.equals(noble)) {
        if (canReserveNoble(noble, count)) {
          allActions.add(new PurchaseCardAction(
              card1, card2, card3, first, second, third, reservedNoble, noble, id)
          );
        }
      }
    }

    // Check all player nobles
    for (Noble noble : player.getReservedNobles()) {
      if (canReserveNoble(noble, count)) {
        allActions.add(new PurchaseCardAction(
            card1, card2, card3, first, second, third, reservedNoble, noble, id)
        );
      }
    }

    // Cannot get any noble
    if (allActions.isEmpty()) {
      allActions.add(new PurchaseCardAction(
          card1, card2, card3, first, second, third, reservedNoble, null, id)
      );
    }

    return allActions;
  }

  private static boolean canReserveNoble(Noble noble, Map<ResourceColor, Integer> count) {
    // Check if the player will have sufficient funds to get the noble
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      if (count.get(color) < noble.getCost().getCost(color)) {
        return false;
      }
    }

    // Sufficient funds
    return true;
  }

  private static Map<ResourceColor, Integer> getCount(Player player, DevelopmentCard card1,
                                                      DevelopmentCard card2, DevelopmentCard card3,
                                                      ResourceColor first, ResourceColor second,
                                                      ResourceColor third) {
    Map<ResourceColor, Integer> count = player.getBonusesCount();

    // Update with matching colors
    update(count, first);
    update(count, second);
    update(count, third);

    // Update with card information
    update(count, card1);
    update(count, card2);
    update(count, card3);

    return count;
  }

  private static void update(Map<ResourceColor, Integer> count, ResourceColor color) {
    if (color != null) {
      count.put(color, count.get(color) + 1);
    }
  }

  private static void update(Map<ResourceColor, Integer> count, DevelopmentCard card) {
    if (card != null && card.getColor() != null) {
      if (card.getDevelopmentCardType() == DevelopmentCardType.BASE) {
        count.put(card.getColor(), count.get(card.getColor()) + 1);
        return;
      }

      if (((OrientDevelopmentCard) card).getAbility() == OrientAbility.TWO_BONUSES) {
        count.put(card.getColor(), count.get(card.getColor()) + 2);
      } else {
        count.put(card.getColor(), count.get(card.getColor()) + 1);
      }
    }
  }
}
