package splendor.controller;

import ca.mcgill.nalmer.splendormodels.Action;
import ca.mcgill.nalmer.splendormodels.ActionGeneratorInterface;
import ca.mcgill.nalmer.splendormodels.BaseDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.CardLevel;
import ca.mcgill.nalmer.splendormodels.Constants;
import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.DevelopmentCard;
import ca.mcgill.nalmer.splendormodels.Game;
import ca.mcgill.nalmer.splendormodels.Noble;
import ca.mcgill.nalmer.splendormodels.OrientAbility;
import ca.mcgill.nalmer.splendormodels.OrientDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.Player;
import ca.mcgill.nalmer.splendormodels.PurchaseCardAction;
import ca.mcgill.nalmer.splendormodels.ReserveCardAction;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import ca.mcgill.nalmer.splendormodels.TakeTokensAction;
import ca.mcgill.nalmer.splendormodels.TradingPostAbility;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper class to generate actions.
 */
final class ActionGenerator implements ActionGeneratorInterface {
  private static final int MIN_NUMBER_FOR_DOUBLE_TOKENS = 4;
  private final IdGenerator generator = new IdGenerator();

  /**
   * Generate cascading temporary actions.
   *
   * @param player User to generate the cascade for.
   * @return Cascading data containing all cascading possibilities.
   */
  private static List<CascadingCardData> cascadeLevel1Cards(Game game, Player player) {
    List<CascadingCardData> actions = new ArrayList<>();
    for (ResourceColor level2Color : player.getNonEmptyColorBonuses()) {
      boolean hasAnyCard = false;

      // Loop through all base level 1 cards
      for (BaseDevelopmentCard level1Card : game.getBaseCards(CardLevel.ONE)) {
        hasAnyCard = true;
        actions.add(new CascadingCardData(level1Card, level2Color));
      }

      // Loop through all orient level 1 cards
      for (OrientDevelopmentCard level1Card : game.getOrientCards(CardLevel.ONE)) {
        hasAnyCard = true;
        if (level1Card.getAbility() == OrientAbility.BONUS_MATCHING) {
          for (ResourceColor level1Color : player.getNonEmptyColorBonuses()) {
            actions.add(new CascadingCardData(level1Card, level2Color, level1Color));
          }
        } else {
          actions.add(new CascadingCardData(level1Card, level2Color));
        }
      }

      // Extreme edge case where there is no level 1 card - user can just take the level 2 card
      if (!hasAnyCard) {
        actions.add(new CascadingCardData(level2Color));
      }
    }

    return actions;
  }

  /**
   * Generate all token selection actions a player can do.
   *
   * @param game   Game this is for.
   * @param player User to generate actions for.
   * @return All token selection actions the player can do.
   */
  public List<TakeTokensAction> generateTokenTakingActions(Game game, Player player) {
    List<TakeTokensAction> actions = new ArrayList<>();

    // Max number of tokens (10) reached
    if (player.getNumberOfTokens() >= Constants.USER_MAX_NUMBER_OF_TOKENS) {
      return actions;
    }

    // Get game tokens
    Map<ResourceColor, Integer> gameTokens = game.getAvailableTokens();

    // 9 tokens
    if (player.getNumberOfTokens() == Constants.USER_MAX_NUMBER_OF_TOKENS - 1) {
      for (ResourceColor color : Cost.getAllPossibleColors()) {
        if (gameTokens.get(color) > 0) {
          actions.add(new TakeTokensAction(new Cost(Map.of(color, 1)), nextId()));
        }
      }

      // Return actions
      return actions;
    }

    // 8 tokens
    if (player.getNumberOfTokens() == Constants.USER_MAX_NUMBER_OF_TOKENS - 2) {
      for (ResourceColor color1 : Cost.getAllPossibleColors()) {
        // Two different tokens (allowed since we are 2 under the limit)
        if (gameTokens.get(color1) > 0) {
          for (ResourceColor color2 : Cost.getAllPossibleColors()) {
            if (color2.ordinal() > color1.ordinal() && gameTokens.get(color2) > 0) {
              actions.add(new TakeTokensAction(new Cost(Map.of(color1, 1, color2, 1)), nextId()));
            }
          }
        }
      }
    } else {
      for (ResourceColor color1 : Cost.getAllPossibleColors()) {
        // Three different tokens
        if (gameTokens.get(color1) > 0) {
          for (ResourceColor color2 : Cost.getAllPossibleColors()) {
            if (color2.ordinal() > color1.ordinal() && gameTokens.get(color2) > 0) {
              for (ResourceColor color3 : Cost.getAllPossibleColors()) {
                if (color3.ordinal() > color2.ordinal() && gameTokens.get(color3) > 0) {
                  actions.add(new TakeTokensAction(
                      new Cost(Map.of(color1, 1, color2, 1, color3, 1)), nextId())
                  );
                }
              }
            }
          }
        }
      }
    }

    // Constants
    final boolean hasAbility = player.hasAbility(TradingPostAbility.TAKE_TOKEN_AFTER_TWO_TOKENS);
    final boolean maxSeven = player.getNumberOfTokens() < Constants.USER_MAX_NUMBER_OF_TOKENS - 2;
    final boolean hasAbilityAndMaxSeven = hasAbility && maxSeven;

    // At least 4 tokens - we can take two times the same
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      // Not enough tokens, continue
      if (gameTokens.get(color) < MIN_NUMBER_FOR_DOUBLE_TOKENS) {
        continue;
      }

      // If player has the ability to take a token after two tokens and has 7 cards or fewer
      if (hasAbilityAndMaxSeven) {
        List<TakeTokensAction> bonusActions = new ArrayList<>();

        // Add all possible color combinations
        for (ResourceColor bonus : Cost.getAllPossibleColors()) {
          if (bonus != color && gameTokens.get(bonus) > 0) {
            bonusActions.add(new TakeTokensAction(new Cost(Map.of(color, 2, bonus, 1)), nextId()));
          }
        }

        // If no other tokens were found, simply add the two tokens as actions
        if (bonusActions.isEmpty()) {
          bonusActions.add(new TakeTokensAction(new Cost(Map.of(color, 2)), nextId()));
        }

        // Add all the bonus actions
        actions.addAll(bonusActions);
        continue;
      }

      // Regular case
      actions.add(new TakeTokensAction(new Cost(Map.of(color, 2)), nextId()));
    }

    // Return actions
    return actions;
  }

  /**
   * Generate all token selection actions the current player can do.
   *
   * @param game Game this is for.
   * @return All token selection actions the current player can do.
   */
  public List<TakeTokensAction> generateTokenTakingActions(Game game) {
    return generateTokenTakingActions(game, game.getCurrentPlayer());
  }

  /**
   * Generate all card purchasing actions a player can do.
   *
   * @param game   Game this is for.
   * @param player User to generate actions for.
   * @return All card purchasing actions the player can do.
   */
  public List<PurchaseCardAction> generateCardPurchasingActions(Game game, Player player) {
    List<PurchaseCardAction> actions = new ArrayList<>();

    // Add base development cards
    for (BaseDevelopmentCard card : game.getAllBaseCards()) {
      actions.addAll(getBaseActions(game, player, card));
    }

    // Add orient development cards
    for (OrientDevelopmentCard card : game.getAllOrientCards()) {
      actions.addAll(getOrientActions(game, player, card));
    }

    // Add player reserved cards
    for (DevelopmentCard card : player.getReservedCards()) {
      actions.addAll(getActions(game, player, card));
    }

    // Return all
    return actions;
  }

  /**
   * Generate all card purchasing actions the current player can do.
   *
   * @param game Game this is for.
   * @return All card purchasing actions the current player can do.
   */
  public List<PurchaseCardAction> generateCardPurchasingActions(Game game) {
    return generateCardPurchasingActions(game, game.getCurrentPlayer());
  }

  /**
   * Generate all card reservation actions a player can do.
   *
   * @param game   Game this is for.
   * @param player User to generate actions for.
   * @return All card reservation actions the player can do.
   */
  public List<ReserveCardAction> generateCardReservingActions(Game game, Player player) {
    List<ReserveCardAction> actions = new ArrayList<>();

    // User cannot reserve any more cards
    if (player.reservedCardsIsFull()) {
      return actions;
    }

    // Reserve any visible card
    for (DevelopmentCard card : game.getAllCards()) {
      actions.add(new ReserveCardAction(card, nextId()));
    }

    // Reserve top card at any deck, if player is feeling like it
    for (CardLevel level : CardLevel.values()) {
      BaseDevelopmentCard baseTop = game.getBaseDeck(level).peek();
      OrientDevelopmentCard orientTop = game.getOrientDeck(level).peek();

      if (baseTop != null) {
        actions.add(new ReserveCardAction(baseTop, nextId()));
      }

      if (orientTop != null) {
        actions.add(new ReserveCardAction(orientTop, nextId()));
      }
    }

    return actions;
  }

  /**
   * Generate all card reservation actions the current player can do.
   *
   * @param game Game this is for.
   * @return All card reservation actions the current player can do.
   */
  public List<ReserveCardAction> generateCardReservingActions(Game game) {
    return generateCardReservingActions(game, game.getCurrentPlayer());
  }

  /**
   * Generate all actions for a player.
   *
   * @param game   Game this is for.
   * @param player User to generate actions for.
   * @return All actions a player can do.
   */
  public List<? extends Action> generateAllActions(Game game, Player player) {
    List<Action> actions = new ArrayList<>();
    actions.addAll(generateTokenTakingActions(game, player));
    actions.addAll(generateCardPurchasingActions(game, player));
    actions.addAll(generateCardReservingActions(game, player));
    return actions;
  }

  @Override
  public List<? extends Action> generateAllActions(Game game) {
    return generateAllActions(game, game.getCurrentPlayer());
  }

  private int nextId() {
    return generator.getNext();
  }

  private List<PurchaseCardAction> getActions(Game game, Player player, DevelopmentCard card) {
    return switch (card.getDevelopmentCardType()) {
      case BASE -> getBaseActions(game, player, (BaseDevelopmentCard) card);
      case ORIENT -> getOrientActions(game, player, (OrientDevelopmentCard) card);
    };
  }

  private List<PurchaseCardAction> getBaseActions(Game game, Player player, BaseDevelopmentCard c) {
    if (player.canBuy(c)) {
      return PurchaseCardAction.allFrom(game, player, c, nextId());
    }

    return new ArrayList<>();
  }

  private List<PurchaseCardAction> getOrientActions(Game game, Player player,
                                                    OrientDevelopmentCard card) {
    List<PurchaseCardAction> actions = new ArrayList<>();

    // Forget about it if user can't buy it
    if (!player.canBuy(card)) {
      return actions;
    }

    switch (card.getAbility()) {
      // Can only buy a bonus matching if user has at least one non-empty bonus
      case BONUS_MATCHING -> {
        for (ResourceColor color : player.getNonEmptyColorBonuses()) {
          actions.addAll(PurchaseCardAction.allFrom(game, player, card, color, nextId()));
        }
      }

      // Can only buy a bonus matching if user has at least one non-empty bonus
      case BONUS_MATCHING_TAKE_LEVEL_ONE -> {
        List<CascadingCardData> cascades = cascadeLevel1Cards(game, player);
        for (CascadingCardData cascade : cascades) {
          actions.addAll(PurchaseCardAction.allFrom(
              game, player, card, cascade.getLevel1DevelopmentCard(),
              cascade.getLevel2Color(), cascade.getLevel1Color(), nextId())
          );
        }
      }

      // Noble reservation card - add all possible nobles, or none if none of them are available
      case RESERVE_NOBLE -> {
        if (game.getAvailableNobles().size() > 0) {
          for (Noble noble : game.getAvailableNobles()) {
            actions.addAll(PurchaseCardAction.allFrom(game, player, card, noble, nextId()));
          }
        } else {
          actions.addAll(PurchaseCardAction.allFrom(game, player, card, nextId()));
        }
      }

      // Take level two card - can cascade at most twice
      case TAKE_LEVEL_TWO -> {
        boolean hasAny = false;

        // Loop through all level 2 base cards
        for (BaseDevelopmentCard level2Card : game.getBaseCards(CardLevel.TWO)) {
          hasAny = true;
          actions.addAll(PurchaseCardAction.allFrom(game, player, card, level2Card, nextId()));
        }

        // Loop through all level 2 orient cards (can cascade)
        for (OrientDevelopmentCard level2Card : game.getOrientCards(CardLevel.TWO)) {
          hasAny = true;

          // Cascading if card is a "BONUS MATCHING + TAKE LEVEL 1 CARD"
          if (level2Card.getAbility() == OrientAbility.BONUS_MATCHING_TAKE_LEVEL_ONE) {
            List<CascadingCardData> cascades = cascadeLevel1Cards(game, player);
            for (CascadingCardData cascade : cascades) {
              actions.addAll(
                  PurchaseCardAction.allFrom(
                      game, player, card, level2Card, cascade.getLevel1DevelopmentCard(),
                      cascade.getLevel2Color(), cascade.getLevel1Color(), nextId()
                  )
              );
            }
          } else if (level2Card.getAbility() == OrientAbility.RESERVE_NOBLE
              && game.getAvailableNobles().size() > 0) {
            for (Noble noble : game.getAvailableNobles()) {
              actions.addAll(PurchaseCardAction.allFrom(
                  game, player, card, level2Card, noble, nextId())
              );
            }
          } else {
            actions.addAll(PurchaseCardAction.allFrom(game, player, card, level2Card, nextId()));
          }
        }

        // Extreme edge case where no level 2 card is available
        if (!hasAny) {
          actions.addAll(PurchaseCardAction.allFrom(game, player, card, nextId()));
        }
      }

      // User must have at least two cards to discard
      case DISCARD_TWO_BONUSES -> {
        boolean canBuy = true;
        Cost cost = card.getCost();

        // Iterate through the colors and check if player has the right amount of bonuses
        for (ResourceColor color : Cost.getAllPossibleColors()) {
          final int colorCost = cost.getCost(color);
          if (colorCost > 0 && player.getNumberOfBonusesOfColor(color) < colorCost) {
            canBuy = false;
            break;
          }
        }

        // Add the action if we can buy the card
        if (canBuy) {
          actions.addAll(PurchaseCardAction.allFrom(game, player, card, nextId()));
        }
      }

      // Nothing special
      default -> actions.addAll(PurchaseCardAction.allFrom(game, player, card, nextId()));
    }

    return actions;
  }

  /**
   * Temporary class to store cascading cards.
   */
  private static class CascadingCardData {
    private final DevelopmentCard level1Card;
    private final ResourceColor level2CardColor;
    private final ResourceColor level1CardColor;

    public CascadingCardData(
        DevelopmentCard l1Card, ResourceColor l2CardColor, ResourceColor l1CardColor
    ) {
      this.level1Card = l1Card;
      this.level2CardColor = l2CardColor;
      this.level1CardColor = l1CardColor;
    }

    public CascadingCardData(DevelopmentCard level1Card, ResourceColor level2CardColor) {
      this.level1Card = level1Card;
      this.level2CardColor = level2CardColor;
      level1CardColor = null;
    }

    public CascadingCardData(ResourceColor level2CardColor) {
      level1Card = null;
      this.level2CardColor = level2CardColor;
      level1CardColor = null;
    }

    public DevelopmentCard getLevel1DevelopmentCard() {
      return level1Card;
    }

    public ResourceColor getLevel2Color() {
      return level2CardColor;
    }

    public ResourceColor getLevel1Color() {
      return level1CardColor;
    }
  }
}
