package ca.mcgill.nalmer.splendormodels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a trading post.
 */
public final class TradingPost extends NonPrestigeCard {
  private final TradingPostAbility ability;
  private final boolean nobleRequired;

  /**
   * Constructor.
   *
   * @param cardCost  Cost of the card.
   * @param cardId    Card id.
   * @param tpAbility Trading post ability.
   * @param nobleCost Whether a noble is needed or not.
   * @throws NullPointerException if <code>cardCost</code> or <code>tpAbility</code> is
   *                              <code>null</code>.
   */
  private TradingPost(Cost cardCost, int cardId, TradingPostAbility tpAbility, boolean nobleCost) {
    super(cardCost, cardId);
    if (tpAbility == null) {
      throw new NullPointerException("tpAbility cannot be null");
    }

    // Set field values
    ability = tpAbility;
    nobleRequired = nobleCost;
  }

  /**
   * Creates a new trading post that represents a
   * {@link TradingPostAbility#TAKE_TOKEN_AFTER_TAKING_CARD}.
   *
   * @param id Card id.
   * @return New trading post.
   */
  public static TradingPost tokenAfterCard(int id) {
    return new TradingPost(
        new Cost(Map.of(ResourceColor.RED, 3, ResourceColor.WHITE, 1)), id,
        TradingPostAbility.TAKE_TOKEN_AFTER_TAKING_CARD, false
    );
  }

  /**
   * Creates a new trading post that represents a
   * {@link TradingPostAbility#TAKE_TOKEN_AFTER_TWO_TOKENS}.
   *
   * @param id Card id.
   * @return New trading post.
   */
  public static TradingPost tokenAfterTwoTokens(int id) {
    return new TradingPost(
        new Cost(Map.of(ResourceColor.WHITE, 2)), id,
        TradingPostAbility.TAKE_TOKEN_AFTER_TWO_TOKENS, false
    );
  }

  /**
   * Creates a new trading post that represents a
   * {@link TradingPostAbility#GOLD_TOKEN_WORTH_TWO_TOKENS}.
   *
   * @param id Card id.
   * @return New trading post.
   */
  public static TradingPost goldWorthTwoTokens(int id) {
    return new TradingPost(
        new Cost(Map.of(ResourceColor.BLUE, 3, ResourceColor.BLACK, 1)), id,
        TradingPostAbility.GOLD_TOKEN_WORTH_TWO_TOKENS, false
    );
  }

  /**
   * Creates a new trading post that represents a
   * {@link TradingPostAbility#FIVE_POINTS}.
   *
   * @param id Card id.
   * @return New trading post.
   */
  public static TradingPost fivePoints(int id) {
    return new TradingPost(
        new Cost(Map.of(ResourceColor.GREEN, 5)), id, TradingPostAbility.FIVE_POINTS, true
    );
  }

  /**
   * Creates a new trading post that represents a
   * {@link TradingPostAbility#ONE_POINT_FOR_EACH_SHIELD}.
   *
   * @param id Card id.
   * @return New trading post.
   */
  public static TradingPost onePointForEachShield(int id) {
    return new TradingPost(
        new Cost(Map.of(ResourceColor.BLACK, 3)), id,
        TradingPostAbility.ONE_POINT_FOR_EACH_SHIELD, false
    );
  }

  /**
   * Gets the {@link TradingPostAbility} of the trading post.
   *
   * @return {@link TradingPostAbility} of the trading post.
   */
  public TradingPostAbility getAbility() {
    return ability;
  }

  /**
   * Gets whether a {@link Noble} is required to obtain it.
   *
   * @return <code>true</code> if a {@link Noble} is required, <code>false</code> otherwise.
   */
  public boolean isNobleRequired() {
    return nobleRequired;
  }

  /**
   * Gets all the {@link ShieldColor} set for the trading post.
   *
   * @param players List of {@link Player}.
   * @return Map of {@link ShieldColor} and {@link Boolean} determining which colors has been set.
   * @throws NullPointerException if <code>players</code> is <code>null</code>.
   */
  public Map<ShieldColor, Boolean> getColors(List<Player> players) {
    if (players == null) {
      throw new NullPointerException("players cannot be null");
    }

    // Loop through all the players
    Map<ShieldColor, Boolean> colors = new HashMap<>();
    for (Player player : players) {
      final ShieldColor color = player.getShieldColor();
      if (color != null) {
        colors.put(color, player.hasAbility(ability));
      }
    }

    // Fill in missing values
    for (ShieldColor color : ShieldColor.values()) {
      colors.putIfAbsent(color, false);
    }

    // Return result
    return colors;
  }

  @Override
  public CardType getCardType() {
    return CardType.TRADING_POST;
  }

  @Override
  public NonPrestigeCardType getNonPrestigeCardType() {
    return NonPrestigeCardType.TRADING_POST;
  }

  @Override
  public boolean obtainableBy(Player player) {
    if (player == null) {
      throw new NullPointerException("player cannot be null");
    }

    // Already obtained
    if (player.hasAbility(ability)) {
      return false;
    }

    // If player doesn't have enough bonuses, he cannot obtain the trading post
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      if (player.getNumberOfBonusesOfColor(color) < getCost().getCost(color)) {
        return false;
      }
    }

    // Either no noble is required, or player has nobles
    return !nobleRequired || player.hasNobles();
  }
}
