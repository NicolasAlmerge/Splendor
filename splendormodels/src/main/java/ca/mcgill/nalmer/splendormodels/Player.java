package ca.mcgill.nalmer.splendormodels;

import ca.mcgill.nalmer.lsutilities.model.User;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a user model.
 */
public final class Player {
  private final Map<ResourceColor, Integer> tokens = new EnumMap<>(ResourceColor.class);
  private final Map<ResourceColor, List<DevelopmentCard>> bonuses = new EnumMap<>(
      ResourceColor.class
  );
  private final List<DevelopmentCard> reservedCards = new ArrayList<>();
  private final List<Noble> reservedNobles = new ArrayList<>();
  private final List<Noble> nobles = new ArrayList<>();
  private final List<City> cities = new ArrayList<>();
  private final Set<TradingPostAbility> tpAbilities = EnumSet.noneOf(TradingPostAbility.class);
  private final ShieldColor shieldColor;
  private User user;

  /**
   * Constructor.
   *
   * @param u       {@link User} it represents.
   * @param shColor {@link ShieldColor} of the user.
   * @throws NullPointerException if <code>u</code> is <code>null</code>.
   */
  public Player(User u, ShieldColor shColor) {
    if (u == null) {
      throw new NullPointerException("user cannot be null");
    }
    user = u;
    shieldColor = shColor;
    for (ResourceColor color : ResourceColor.values()) {
      tokens.put(color, 0);
      bonuses.put(color, new ArrayList<>());
    }
  }

  /**
   * Constructor.
   *
   * @param u {@link User} it represents.
   * @throws NullPointerException if <code>u</code> is <code>null</code>.
   */
  public Player(User u) {
    this(u, null);
  }

  private static int roundedUpHalf(int x) {
    return (x % 2 == 0) ? x / 2 : x / 2 + 1;
  }

  private static boolean willMatchBonus(DevelopmentCard card) {
    if (card instanceof OrientDevelopmentCard orientCard) {
      return orientCard.getAbility().willMatchBonus();
    }
    return false;
  }

  /**
   * Counts bonuses of a certain {@link ResourceColor}.
   *
   * @param color {@link ResourceColor} to check.
   * @return Number of bonuses of this {@link ResourceColor}.
   */
  public int getNumberOfBonusesOfColor(ResourceColor color) {
    int count = 0;
    for (DevelopmentCard card : bonuses.get(color)) {
      // If card is a base development card, simply add one
      if (card.getDevelopmentCardType() == DevelopmentCardType.BASE) {
        ++count;
        continue;
      }

      // If card is an orient card worth two bonuses, add two, else add one
      OrientAbility ability = ((OrientDevelopmentCard) card).getAbility();
      if (ability == OrientAbility.TWO_BONUSES || ability == OrientAbility.TWO_GOLD_TOKENS) {
        count += 2;
      } else {
        ++count;
      }
    }
    return count;
  }

  /**
   * Checks whether the player can obtain a {@link TradingPost}.
   *
   * @param post {@link TradingPost} to check.
   * @return <code>true</code> if <code>post</code> is obtainable, <code>false</code> otherwise.
   * @throws NullPointerException if <code>post</code> is <code>null</code>.
   */
  public boolean canObtain(TradingPost post) {
    if (post == null) {
      throw new NullPointerException("post cannot be null");
    }
    return post.obtainableBy(this);
  }

  /**
   * Gets the total number of bonus cards.
   *
   * @return Total number of bonus cards.
   */
  public int getTotalNumberOfBonusCards() {
    int count = 0;
    for (List<DevelopmentCard> bonuses : bonuses.values()) {
      count += bonuses.size();
    }
    return count;
  }

  /**
   * Gets the list of all the {@link ResourceColor} the user has in terms of bonuses, excluding
   * {@link ResourceColor#GOLD}.
   *
   * @return List of non-empty {@link ResourceColor} bonuses.
   */
  public List<ResourceColor> getNonEmptyColorBonuses() {
    List<ResourceColor> colors = new ArrayList<>();
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      if (getNumberOfBonusesOfColor(color) > 0) {
        colors.add(color);
      }
    }
    return colors;
  }

  /**
   * Counts the total number of tokens.
   *
   * @return Total number of tokens.
   */
  public int getNumberOfTokens() {
    int count = 0;
    for (ResourceColor color : ResourceColor.values()) {
      count += tokens.get(color);
    }
    return count;
  }

  /**
   * Gets the user's number of prestige points.
   *
   * @return User's prestige point count.
   */
  public int getPrestigePts() {
    int count = hasFiveBonusPoints() ? 5 : 0;

    // Add all bonus cards count
    for (ResourceColor color : ResourceColor.values()) {
      for (DevelopmentCard card : bonuses.get(color)) {
        count += card.getPoints();
      }
    }

    // Add all noble cards count
    for (Noble noble : nobles) {
      count += noble.getPoints();
    }

    // Add all trading posts bonuses
    if (tpAbilities.contains(TradingPostAbility.ONE_POINT_FOR_EACH_SHIELD)) {
      count += tpAbilities.size();
    }

    // Return total count
    return count;
  }

  /**
   * Adds tokens to user's tokens.
   *
   * @param tokensToAdd Token set to add.
   */
  public void addTokens(Map<ResourceColor, Integer> tokensToAdd) {
    for (ResourceColor color : tokensToAdd.keySet()) {
      tokens.put(color, tokens.get(color) + tokensToAdd.get(color));
    }
  }

  /**
   * Adds one token to the user's gold token count.
   */
  public void addOneGoldToken() {
    addTokens(Map.of(ResourceColor.GOLD, 1));
  }

  private void payDiscardTwoBonusesCard(OrientDevelopmentCard oCard) {
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      final int requiredCount = oCard.getCost().getCost(color);
      if (requiredCount > 0) {
        int paidAmount = 0;
        do {
          DevelopmentCard card = bonuses.get(color).get(0);
          if (card instanceof OrientDevelopmentCard orientCard
              && orientCard.getAbility() == OrientAbility.TWO_BONUSES) {
            paidAmount += 2;
          } else {
            ++paidAmount;
          }
          bonuses.get(color).remove(0);
        } while (paidAmount < requiredCount);
      }
    }
    addBonus(oCard);
  }

  /**
   * Adds a bonus development card to the user, with payment.
   *
   * @param card PrestigeCard to pay.
   * @return Token counts to put back in the game.
   * @throws RuntimeException if the user doesn't have enough.
   */
  public Map<ResourceColor, Integer> payAndAddBonus(DevelopmentCard card) {
    // If the card is a "discard two bonuses" card, remove bonuses
    if (card instanceof OrientDevelopmentCard orientCard) {
      if (orientCard.getAbility() == OrientAbility.DISCARD_TWO_BONUSES) {
        payDiscardTwoBonusesCard(orientCard);
        return new EnumMap<>(ResourceColor.class);
      }
    }

    final int cardGoldBonuses = getNumberOfBonusesOfColor(ResourceColor.GOLD);
    final int tokenGoldBonuses = tokens.get(ResourceColor.GOLD);
    final int initialNumberJokers = tokenGoldBonuses + cardGoldBonuses;
    int jokersLeft = initialNumberJokers;

    Map<ResourceColor, Integer> newTokenCount = new HashMap<>();
    Map<ResourceColor, Integer> amountToGiveBack = new HashMap<>();

    for (ResourceColor color : Cost.getAllPossibleColors()) {
      final int tokenCount = tokens.get(color);
      final int bonusCount = getNumberOfBonusesOfColor(color);
      final int totalCount = tokenCount + bonusCount;
      final int requiredCount = card.getCost().getCost(color);

      // We need to use jokers to buy the card
      if (totalCount < requiredCount) {
        final int diff;
        if (tpAbilities.contains(TradingPostAbility.GOLD_TOKEN_WORTH_TWO_TOKENS)) {
          diff = roundedUpHalf(requiredCount - totalCount);
        } else {
          diff = requiredCount - totalCount;
        }
        jokersLeft -= diff;
        if (jokersLeft < 0) {
          throw new RuntimeException("cannot pay card - not enough bonuses, tokens and jokers");
        }

        // No more tokens of this color
        newTokenCount.put(color, 0);
        amountToGiveBack.put(color, tokenCount);
        continue;
      }

      // Not enough bonuses - we need to remove tokens
      if (bonusCount < requiredCount) {
        final int amountToRemove = requiredCount - bonusCount;
        newTokenCount.put(color, tokenCount - amountToRemove);
        amountToGiveBack.put(color, amountToRemove);
        continue;
      }

      // Enough bonuses - no changes in token counts
      newTokenCount.put(color, tokenCount);
      amountToGiveBack.put(color, 0);
    }

    // Update counts
    for (ResourceColor color : newTokenCount.keySet()) {
      tokens.put(color, newTokenCount.get(color));
    }

    // Update joker count, if it has been changed
    if (jokersLeft < initialNumberJokers) {
      int amountLost = initialNumberJokers - jokersLeft;

      // We can pay everything with gold tokens
      if (amountLost <= tokenGoldBonuses) {
        tokens.put(ResourceColor.GOLD, tokenGoldBonuses - amountLost);
        amountToGiveBack.put(ResourceColor.GOLD, amountLost);
        addBonus(card);
        return amountToGiveBack;
      }

      // We have to compromise - put back as many gold tokens as possible
      final int cardGoldToRemove = amountLost - tokenGoldBonuses;

      // Odd number of gold tokens coming from the cards - we'll try to reduce the waste
      if (cardGoldToRemove % 2 == 1) {

        // We have to waste one gold token from a card
        if (tokenGoldBonuses == 0) {
          removeBonuses(ResourceColor.GOLD, cardGoldToRemove / 2 + 1);
          amountToGiveBack.put(ResourceColor.GOLD, 0);
          addBonus(card);
          return amountToGiveBack;
        }

        // We can remove the waste by keeping one gold token
        tokens.put(ResourceColor.GOLD, 1);
        removeBonuses(ResourceColor.GOLD, cardGoldToRemove / 2 + 1);
        amountToGiveBack.put(ResourceColor.GOLD, tokenGoldBonuses - 1);
        addBonus(card);
        return amountToGiveBack;
      }

      // Even number of gold cards to remove - no waste
      tokens.put(ResourceColor.GOLD, 0);
      removeBonuses(ResourceColor.GOLD, cardGoldToRemove / 2);
      amountToGiveBack.put(ResourceColor.GOLD, tokenGoldBonuses);
      addBonus(card);
      return amountToGiveBack;
    }

    // No jokers used
    amountToGiveBack.put(ResourceColor.GOLD, 0);
    addBonus(card);
    return amountToGiveBack;
  }

  /**
   * Checks if the user can buy a card.
   *
   * @param card Card the user wants to buy.
   * @return <code>true</code> if user can buy the card, <code>false</code> otherwise.
   */
  public boolean canBuy(PrestigeCard card) {
    Cost cost = card.getCost();
    final int cardGoldBonuses = getNumberOfBonusesOfColor(ResourceColor.GOLD);
    final int tokenGoldBonuses = tokens.get(ResourceColor.GOLD);
    int jokersLeft = tokenGoldBonuses + cardGoldBonuses;

    for (ResourceColor color : Cost.getAllPossibleColors()) {
      final int tokenCount = tokens.get(color);
      final int bonusCount = getNumberOfBonusesOfColor(color);
      final int totalCount = tokenCount + bonusCount;

      if (totalCount < cost.getCost(color)) {
        final int diff;
        if (tpAbilities.contains(TradingPostAbility.GOLD_TOKEN_WORTH_TWO_TOKENS)) {
          diff = roundedUpHalf(cost.getCost(color) - totalCount);
        } else {
          diff = cost.getCost(color) - totalCount;
        }
        jokersLeft -= diff;
        if (jokersLeft < 0) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Gets user tokens.
   *
   * @return User tokens.
   */
  public Map<ResourceColor, Integer> getTokens() {
    return tokens;
  }

  /**
   * Adds a bonus card to the user (without payment).
   *
   * @param card Card to add.
   */
  public void addBonus(DevelopmentCard card) {
    final List<DevelopmentCard> correctBonuses = bonuses.get(card.getColor());

    // If card is an orient development card
    if (card instanceof OrientDevelopmentCard orientCard) {
      // If card is a bonus matching, add it to the front of the list
      if (orientCard.getAbility().willMatchBonus()) {
        correctBonuses.add(0, card);
        return;
      }

      // If card is a two bonus matching card, add it right after the bonus matching cards
      if (orientCard.getAbility() == OrientAbility.TWO_BONUSES) {
        for (int i = 0; i < correctBonuses.size(); ++i) {
          final DevelopmentCard currentCard = correctBonuses.get(i);
          if (!willMatchBonus(currentCard)) {
            correctBonuses.add(i, card);
            return;
          }
        }

        // Otherwise add it at the end
        correctBonuses.add(card);
        return;
      }
    }

    // Add it right before the card with bigger value
    for (int i = 0; i < correctBonuses.size(); ++i) {
      final DevelopmentCard currentCard = correctBonuses.get(i);
      if (currentCard.getPoints() > card.getPoints()) {
        correctBonuses.add(i, card);
        return;
      }
    }

    // Default case, add it at the end
    correctBonuses.add(card);
  }

  /**
   * Removes a certain number of bonuses of a certain {@link ResourceColor}.
   *
   * @param color          Bonus {@link ResourceColor}.
   * @param numberToRemove Number of bonuses to remove.
   */
  public void removeBonuses(ResourceColor color, int numberToRemove) {
    bonuses.get(color).subList(0, numberToRemove).clear();
  }

  /**
   * Removes one bonus of a certain {@link ResourceColor}.
   *
   * @param color Bonus {@link ResourceColor}.
   */
  public void removeOneBonus(ResourceColor color) {
    removeBonuses(color, 1);
  }

  /**
   * Gets user's bonus counts.
   *
   * @return User's bonus cards.
   */
  public Map<ResourceColor, List<DevelopmentCard>> getBonuses() {
    return Map.copyOf(bonuses);
  }

  /**
   * Gets bonuses count for all colours.
   *
   * @return Map of number of bonuses for each colour.
   */
  public Map<ResourceColor, Integer> getBonusesCount() {
    Map<ResourceColor, Integer> map = new EnumMap<>(ResourceColor.class);
    for (ResourceColor color : ResourceColor.values()) {
      map.put(color, getNumberOfBonusesOfColor(color));
    }
    return map;
  }

  /**
   * Checks whether user already has {@link Constants#USER_MAX_NUMBER_OF_RESERVED_CARDS} reserved
   * cards.
   *
   * @return <code>true</code> if user has maxed out reserved cards, <code>false</code> otherwise.
   */
  public boolean reservedCardsIsFull() {
    return reservedCards.size() >= Constants.USER_MAX_NUMBER_OF_RESERVED_CARDS;
  }

  /**
   * Gets the number of reserved cards of this user.
   *
   * @return Number of reserved cards.
   */
  public int getNumberOfReservedCards() {
    return reservedCards.size();
  }

  /**
   * Gets the user's reserved cards.
   *
   * @return List of reserved cards.
   */
  public List<DevelopmentCard> getReservedCards() {
    return reservedCards;
  }

  /**
   * Adds a development card to user's reserved development cards.
   *
   * @param card Development card to be added.
   * @throws RuntimeException if the user cannot reserve more cards, or if card is already reserved.
   */
  public void addReservedCard(DevelopmentCard card) throws RuntimeException {
    if (reservedCardsIsFull()) {
      throw new RuntimeException("player's reserved card list is full");
    }
    if (reservedCards.contains(card)) {
      throw new RuntimeException("card is already inside the player's reserved card list");
    }
    reservedCards.add(card);
  }

  /**
   * Removes card from user's reserved development cards.
   *
   * @param card PrestigeCard to remove.
   */
  public void removeReservedCard(DevelopmentCard card) {
    reservedCards.remove(card);
  }

  /**
   * Gets the user's reserved nobles.
   *
   * @return List of all the reserved nobles.
   */
  public List<Noble> getReservedNobles() {
    return reservedNobles;
  }

  /**
   * Adds a {@link Noble} to the list of the user's reserved nobles.
   *
   * @param card {@link Noble} to add.
   * @throws RuntimeException if {@link Noble} already reserved.
   */
  public void addReservedNobleCard(Noble card) {
    if (reservedNobles.contains(card)) {
      throw new RuntimeException("noble already reserved");
    }
    reservedNobles.add(card);
  }

  /**
   * Removes a {@link Noble} to the list of the user's reserved nobles.
   *
   * @param card {@link Noble} to remove.
   */
  public void removeReservedNobleCard(Noble card) {
    reservedNobles.remove(card);
  }

  /**
   * Adds a {@link Noble} to the list of the user's nobles.
   *
   * @param card {@link Noble} to add.
   */
  public void addNoble(Noble card) {
    nobles.add(card);
  }

  /**
   * Checks whether the player has {@link Noble}.
   *
   * @return <code>true</code> if player has {@link Noble}, <code>false</code> otherwise.
   */
  public boolean hasNobles() {
    return !nobles.isEmpty();
  }

  /**
   * Gets the list of {@link Noble} the player obtained.
   *
   * @return List of {@link Noble}.
   */
  public List<Noble> getNobles() {
    return nobles;
  }

  /**
   * Whether the player has five additional prestige points associated with
   * {@link TradingPostAbility#FIVE_POINTS}.
   *
   * @return <code>true</code> if player has five additional points, <code>false</code> otherwise.
   */
  public boolean hasFiveBonusPoints() {
    return tpAbilities.contains(TradingPostAbility.FIVE_POINTS);
  }

  /**
   * Whether the player has the ability to use a {@link ResourceColor#GOLD} token as two tokens
   * associated with {@link TradingPostAbility#GOLD_TOKEN_WORTH_TWO_TOKENS}.
   *
   * @return <code>true</code> if player has five additional points, <code>false</code> otherwise.
   */
  public boolean hasGoldTokensCountingForTwo() {
    return tpAbilities.contains(TradingPostAbility.GOLD_TOKEN_WORTH_TWO_TOKENS);
  }

  /**
   * Adds a {@link TradingPostAbility} to the player.
   *
   * @param ability {@link TradingPostAbility} to add.
   * @throws RuntimeException     if the player's {@link ShieldColor} is <code>null</code>.
   * @throws NullPointerException if <code>ability</code> is <code>null</code>.
   */
  public void addTradingPostAbility(TradingPostAbility ability) {
    if (shieldColor == null) {
      throw new RuntimeException("cannot add trading post ability as no shield color is set");
    }
    if (ability == null) {
      throw new NullPointerException("ability cannot be null");
    }

    tpAbilities.add(ability);
  }

  /**
   * Checks whether the player has a given {@link TradingPostAbility}.
   *
   * @param ability {@link TradingPostAbility} to check.
   * @return <code>true</code> if player has the <code>ability</code>, <code>false</code> otherwise.
   * @throws NullPointerException if <code>ability</code> is <code>null</code>.
   */
  public boolean hasAbility(TradingPostAbility ability) {
    if (ability == null) {
      throw new NullPointerException("ability cannot be null");
    }

    return tpAbilities.contains(ability);
  }

  /**
   * Gets the player's {@link ShieldColor}, or <code>null</code> if not set.
   *
   * @return Player's {@link ShieldColor}.
   */
  public ShieldColor getShieldColor() {
    return shieldColor;
  }

  /**
   * Checks whether the player has a {@link ShieldColor}. The value of the function call is
   * equivalent to <code>getShieldColor() != null</code>.
   *
   * @return <code>true</code> if player has a {@link ShieldColor}, <code>false</code> otherwise.
   */
  public boolean hasShield() {
    return shieldColor != null;
  }

  /**
   * Gets the list of {@link City} of the player.
   *
   * @return List of {@link City}.
   */
  public List<City> getCities() {
    return cities;
  }

  /**
   * Adds a {@link City}.
   *
   * @param city {@link City} to add.
   * @throws NullPointerException if <code>city</code> is <code>null</code>.
   */
  public void addCity(City city) {
    if (city == null) {
      throw new NullPointerException("city cannot be null");
    }
    cities.add(city);
  }

  /**
   * Checks whether the player has {@link City}.
   *
   * @return <code>true</code> if the player has {@link City}, <code>false</code> otherwise.
   */
  public boolean hasCities() {
    return !cities.isEmpty();
  }

  /**
   * Gets the underlying {@link User}.
   *
   * @return {@link User} the player references.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the new {@link User}.
   *
   * @param newUser New {@link User}.
   * @throws NullPointerException if <code>newUser</code> is <code>null</code>.
   */
  public void setUser(User newUser) {
    if (newUser == null) {
      throw new NullPointerException("newUser cannot be null");
    }
    user = newUser;
  }

  /**
   * Gets the player's username.
   *
   * @return Player's username.
   */
  public String getUsername() {
    return user.getName();
  }

  /**
   * Checks if two {@link Player} are equal. This only compares the player's usernames.
   *
   * @param obj Other object two compare.
   * @return <code>true</code> if players are equal, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (obj instanceof Player playerObj) {
      return user.getName().equals(playerObj.getUser().getName());
    }
    return false;
  }

  /**
   * Returns the hash code of the {@link User}. This is equal to
   * <code>getUser().getName().hashCode()</code>.
   *
   * @return Hash of the {@link User}.
   */
  @Override
  public int hashCode() {
    return user.getName().hashCode();
  }
}
