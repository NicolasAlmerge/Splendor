package ca.mcgill.nalmer.splendormodels;

import ca.mcgill.nalmer.lsutilities.model.User;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the game board.
 */
public final class Game implements BroadcastContent {
  private final GameExtension extension;
  private final List<Player> players = new ArrayList<>();
  private final Map<ResourceColor, Integer> availableTokens = new EnumMap<>(ResourceColor.class);
  private final List<Noble> availableNobles;
  private final Map<CardLevel, Deck<BaseDevelopmentCard>> baseDecks =
      new EnumMap<>(CardLevel.class);
  private final Map<CardLevel, Deck<OrientDevelopmentCard>> orientDecks =
      new EnumMap<>(CardLevel.class);
  private final Map<CardLevel, List<BaseDevelopmentCard>> baseCards =
      new EnumMap<>(CardLevel.class);
  private final Map<CardLevel, List<OrientDevelopmentCard>> orientCards =
      new EnumMap<>(CardLevel.class);
  private final List<City> availableCities;
  private final Map<TradingPostAbility, TradingPost> tradingPosts = new EnumMap<>(
      TradingPostAbility.class
  );
  private final int initialNumberOfNobles;
  private final int initialNumberOfTokens;
  private final List<Integer> unlockedCities = new ArrayList<>();
  private final List<ResourceColor> unlockedTokens = new ArrayList<>();
  private String id;
  private Set<String> winners;
  private int currentPlayerIndex;
  private GamePhase currentPhase = GamePhase.TURN_STARTED;
  private GameStatus status = GameStatus.READY_TO_JOIN;
  private List<? extends Action> actions = new ArrayList<>();

  /**
   * Constructor.
   *
   * @param gameUsers       List of users.
   * @param ext             Game extension.
   * @param cardGenerator   Card generator.
   * @param actionGenerator Action generator.
   * @param gameId          Game id.
   * @throws NullPointerException if <code>gameUsers</code>, <code>ext</code>,
   *                              <code>cardGenerator</code>, <code>actionGenerator</code> or
   *                              <code>gameId</code> is <code>null</code>.
   * @throws RuntimeException     if <code>gameUsers.size()</code> is not between
   *                              {@link Constants#MIN_NUMBER_OF_PLAYERS} and
   *                              {@link Constants#MAX_NUMBER_OF_PLAYERS} inclusive, or if
   *                              <code>ext</code> is
   *                              {@link GameExtension#BASE_ORIENT_TRADING_POSTS} are
   *                              <code>cardGenerator</code> did not generate all of them, or
   *                              generated one of them twice.
   */
  public Game(
      List<User> gameUsers, GameExtension ext, CardGeneratorInterface cardGenerator,
      ActionGeneratorInterface actionGenerator, String gameId
  ) {
    if (gameUsers == null) {
      throw new NullPointerException("gameUsers cannot be null");
    }

    if (ext == null) {
      throw new NullPointerException("ext cannot be null");
    }

    if (cardGenerator == null) {
      throw new NullPointerException("cardGenerator cannot be null");
    }

    if (actionGenerator == null) {
      throw new NullPointerException("actionGenerator cannot be null");
    }

    if (gameId == null) {
      throw new NullPointerException("gameId cannot be null");
    }

    final int numUsers = gameUsers.size();
    if (numUsers < Constants.MIN_NUMBER_OF_PLAYERS || numUsers > Constants.MAX_NUMBER_OF_PLAYERS) {
      throw new RuntimeException("gameUsers' size is not between bounds");
    }

    // Set game attributes
    id = gameId;
    extension = ext;
    initialNumberOfNobles = numUsers + 1;
    initialNumberOfTokens = (numUsers == Constants.MAX_NUMBER_OF_PLAYERS) ? 7 : numUsers + 2;

    // Add player models
    for (int i = 0; i < gameUsers.size(); ++i) {
      User user = gameUsers.get(i);
      if (extension == GameExtension.BASE_ORIENT_TRADING_POSTS) {
        players.add(new Player(user, ShieldColor.get(i)));
      } else {
        players.add(new Player(user));
      }
    }

    // Add corresponding number of available tokens
    for (ResourceColor c : ResourceColor.values()) {
      availableTokens.put(c, getInitialNumberOfTokens(c));
    }

    // Initialise the decks, shuffle them and add visible cards
    initDecks(cardGenerator);
    shuffleDecks();
    initVisibleCards();

    // Initialise nobles
    List<Noble> nobles = cardGenerator.getAllNobles();
    Collections.shuffle(nobles);
    availableNobles = nobles.subList(0, initialNumberOfNobles);

    // Initialise cities
    if (extension == GameExtension.BASE_ORIENT_CITIES) {
      List<City> cities = cardGenerator.getAllCities();
      Collections.shuffle(cities);
      availableCities = cities.subList(0, Constants.NUMBER_CITIES);
    } else {
      availableCities = new ArrayList<>();
    }

    // Initialise trading posts
    if (extension == GameExtension.BASE_ORIENT_TRADING_POSTS) {
      final List<TradingPost> posts = cardGenerator.getAllTradingPosts();
      for (TradingPost post : posts) {
        if (tradingPosts.containsKey(post.getAbility())) {
          throw new RuntimeException("trading post for " + post.getAbility() + " generated twice");
        }
        tradingPosts.put(post.getAbility(), post);
      }

      // Check if all trading posts are there
      for (TradingPostAbility ability : TradingPostAbility.values()) {
        if (!tradingPosts.containsKey(ability)) {
          throw new RuntimeException("not all trading posts are set");
        }
      }
    }

    // Initialise actions
    currentPlayerIndex = players.size() - 1;
    nextPlayer(actionGenerator);
  }

  /**
   * Gets the game id.
   *
   * @return Game id.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the new game id.
   *
   * @param newId New game id.
   * @throws NullPointerException if <code>newId</code> is <code>null</code>.
   */
  public void setNewId(String newId) {
    if (newId == null) {
      throw new NullPointerException("newId cannot be null");
    }
    id = newId;
  }

  /**
   * Get the current phase of the game.
   *
   * @return Game's current phase.
   */
  public GamePhase getCurrentPhase() {
    return currentPhase;
  }

  /**
   * Set the current phase of the game.
   *
   * @param newPhase New phase of the game.
   * @throws NullPointerException if <code>newPhase</code> is <code>null</code>.
   */
  public void setCurrentPhase(GamePhase newPhase) {
    if (newPhase == null) {
      throw new NullPointerException("newPhase cannot be null");
    }
    currentPhase = newPhase;
  }

  /**
   * Get game status.
   *
   * @return Game status.
   */
  public GameStatus getStatus() {
    return status;
  }

  /**
   * Set game status.
   *
   * @param newStatus New game status.
   * @throws NullPointerException if <code>newStatus</code> is <code>null</code>.
   */
  public void setStatus(GameStatus newStatus) {
    if (newStatus == null) {
      throw new NullPointerException("New status cannot be null");
    }
    status = newStatus;
  }

  /**
   * Gets the current player's index.
   *
   * @return Current player's index, or <code>-1</code> if no current player.
   */
  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  /**
   * Set the new current player's index.
   *
   * @param playerIndex New current player index.
   * @throws RuntimeException if <code>playerIndex</code> is negative or too large.
   */
  public void setCurrentPlayerIndex(int playerIndex) {
    if (playerIndex < 0 || playerIndex >= players.size()) {
      throw new RuntimeException("player index invalid");
    }
    currentPlayerIndex = playerIndex;
  }

  /**
   * Get current player.
   *
   * @return Current player.
   */
  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /**
   * Set the new current player.
   *
   * @param player New current player.
   * @throws NullPointerException if <code>player</code> is <code>null</code>.
   * @throws RuntimeException     if player is not a game's player.
   */
  public void setCurrentPlayer(Player player) {
    if (player == null) {
      throw new NullPointerException("player cannot be null");
    }
    int position = players.indexOf(player);
    if (position == -1) {
      throw new RuntimeException("player is not part of the game's users");
    }
    currentPlayerIndex = position;
  }

  /**
   * Gets all the {@link ShieldColor} set for the trading post.
   *
   * @param tradingPost {@link TradingPost} to check.
   * @return Map of {@link ShieldColor} and {@link Boolean} determining which colors has been set.
   * @throws NullPointerException if <code>tradingPost</code> or <code>players</code> is
   *                              <code>null</code>.
   */
  public Map<ShieldColor, Boolean> getColors(TradingPost tradingPost) {
    if (tradingPost == null) {
      throw new NullPointerException("tradingPost cannot be null");
    }
    return tradingPost.getColors(players);
  }

  /**
   * Checks whether the player can obtain a {@link TradingPostAbility}.
   *
   * @param player  {@link Player} player to check.
   * @param ability {@link TradingPostAbility} to check.
   * @return <code>true</code> if obtainable by <code>player</code>, <code>false</code> otherwise.
   * @throws NullPointerException if <code>player</code> or <code>ability</code> is
   *                              <code>null</code>.
   */
  public boolean playerCanObtain(Player player, TradingPostAbility ability) {
    if (ability == null) {
      throw new NullPointerException("ability cannot be null");
    }
    if (player == null) {
      throw new NullPointerException("player cannot be null");
    }
    TradingPost post = tradingPosts.get(ability);
    if (post == null) {
      return false;
    }
    return post.obtainableBy(player);
  }

  /**
   * Gets the list of all the game players.
   *
   * @return Game players.
   */
  public List<Player> getPlayers() {
    return List.copyOf(players);
  }

  /**
   * Sets the current player to the next player in the list. This has no effect if the game has
   * ended.
   */
  private void nextPlayer(ActionGeneratorInterface actionGeneratorInterface) {
    updateWinner();

    // If there are winners already, stop
    if (winners != null) {
      return;
    }

    // Clear data
    unlockedCities.clear();
    unlockedTokens.clear();
    currentPhase = GamePhase.TURN_STARTED;

    // Next player
    nextPlayerValue();
    final int firstIndex = currentPlayerIndex;

    // Get next player who can play
    do {
      actions = actionGeneratorInterface.generateAllActions(this);

      // If actions is not empty, stop
      if (!actions.isEmpty()) {
        return;
      }

      // Get next player value
      nextPlayerValue();
    } while (currentPlayerIndex != firstIndex);

    // Extreme case - no one can play - game ends in a draw
    winners = new HashSet<>();
    markGameAsFinished();
  }

  private void nextPlayerValue() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  /**
   * Get available tokens.
   *
   * @return Available tokens.
   */
  public Map<ResourceColor, Integer> getAvailableTokens() {
    return Map.copyOf(availableTokens);
  }

  /**
   * Get the number of available tokens of a particular color.
   *
   * @param color Token color.
   * @return Token count of this color.
   * @throws NullPointerException if <code>color</code> is <code>null</code>.
   */
  public int getNumberOfTokens(ResourceColor color) {
    if (color == null) {
      throw new NullPointerException("color cannot be null");
    }
    return availableTokens.get(color);
  }

  /**
   * Checks whether there are tokens of this particular color available (more than 0).
   *
   * @param color Token color.
   * @return True if there is at least one token of this color available.
   * @throws NullPointerException if <code>color</code> is <code>null</code>.
   */
  public boolean hasTokensOfColor(ResourceColor color) {
    if (color == null) {
      throw new NullPointerException("color cannot be null");
    }
    return availableTokens.get(color) > 0;
  }

  /**
   * Add available tokens.
   *
   * @param tokensToAdd Token count to add.
   * @throws NullPointerException if <code>tokensToAdd</code> is <code>null</code>.
   */
  public void addTokens(Map<ResourceColor, Integer> tokensToAdd) {
    if (tokensToAdd == null) {
      throw new RuntimeException("tokensToAdd cannot be null");
    }
    for (ResourceColor color : tokensToAdd.keySet()) {
      availableTokens.put(color, availableTokens.get(color) + tokensToAdd.get(color));
    }
  }

  /**
   * Remove available tokens.
   *
   * @param tokensToRemove Token count to remove.
   * @throws NullPointerException if <code>tokensToRemove</code> is <code>null</code>.
   */
  public void removeTokens(Map<ResourceColor, Integer> tokensToRemove) {
    if (tokensToRemove == null) {
      throw new RuntimeException("tokensToRemove cannot be null");
    }
    for (ResourceColor color : tokensToRemove.keySet()) {
      int newCount = availableTokens.get(color) - tokensToRemove.get(color);
      if (newCount < 0) {
        newCount = 0;
      }
      availableTokens.put(color, newCount);
    }
  }

  /**
   * Removes one card (development or noble) from the game.
   *
   * @param card PrestigeCard to remove.
   * @return True if successful, false otherwise.
   * @throws NullPointerException if <code>card</code> is <code>null</code>.
   */
  public boolean removeCard(PrestigeCard card) {
    if (card == null) {
      throw new NullPointerException("card cannot be null");
    }

    return switch (card.getPrestigeCardType()) {
      case BASE, ORIENT -> removeDevelopmentCard((DevelopmentCard) card);
      case NOBLE -> removeNoble((Noble) card);
    };
  }

  /**
   * Removes one development card from the game.
   *
   * @param card PrestigeCard to remove.
   * @return <code>true</code> if successful, <code>false</code> otherwise.
   * @throws NullPointerException if <code>card</code> is <code>null</code>.
   */
  public boolean removeDevelopmentCard(DevelopmentCard card) {
    if (card == null) {
      throw new NullPointerException("card cannot be null");
    }

    CardLevel level = card.getLevel();
    return switch (card.getDevelopmentCardType()) {
      case BASE -> {
        BaseDevelopmentCard c = (BaseDevelopmentCard) card;
        BaseDevelopmentCard topDeckCard = baseDecks.get(level).peek();

        // If card is in the base cards
        if (baseCards.get(level).remove(c)) {
          if (topDeckCard == null) {
            yield true;
          }
          baseDecks.get(level).pop();
          baseCards.get(level).add(topDeckCard);
          yield true;
        }

        // Else, if card is in the base deck
        if (c.equals(topDeckCard)) {
          baseDecks.get(level).pop();
          yield true;
        }

        // Card not found
        yield false;
      }
      case ORIENT -> {
        OrientDevelopmentCard c = (OrientDevelopmentCard) card;
        OrientDevelopmentCard topDeckCard = orientDecks.get(level).peek();

        // If card is in the orient cards
        if (orientCards.get(level).remove(c)) {
          if (topDeckCard == null) {
            yield true;
          }
          orientDecks.get(level).pop();
          orientCards.get(level).add(topDeckCard);
          yield true;
        }

        // Else, if card is in the orient deck
        if (c.equals(topDeckCard)) {
          orientDecks.get(level).pop();
          yield true;
        }

        // Card not found
        yield false;
      }
    };
  }

  /**
   * Removes one noble card from the game.
   *
   * @param noble Noble to remove.
   * @return True if successful, false otherwise.
   * @throws NullPointerException if <code>noble</code> is <code>null</code>.
   */
  public boolean removeNoble(Noble noble) {
    if (noble == null) {
      throw new NullPointerException("noble cannot be null");
    }
    return availableNobles.remove(noble);
  }

  /**
   * Removes one token from the pile of gold tokens.
   */
  public void removeOneGoldToken() {
    removeTokens(Map.of(ResourceColor.GOLD, 1));
  }

  /**
   * Get all the available nobles.
   *
   * @return List of all available nobles.
   */
  public List<Noble> getAvailableNobles() {
    return availableNobles;
  }

  /**
   * Get the base deck.
   *
   * @param level PrestigeCard level.
   * @return Deck of cards of corresponding level.
   * @throws NullPointerException if <code>level</code> is <code>null</code>.
   */
  public Deck<BaseDevelopmentCard> getBaseDeck(CardLevel level) {
    if (level == null) {
      throw new NullPointerException("level cannot be null");
    }
    return baseDecks.get(level);
  }

  /**
   * Get the orient deck.
   *
   * @param level PrestigeCard level.
   * @return Deck of cards of corresponding level.
   * @throws NullPointerException if <code>level</code> is <code>null</code>.
   */
  public Deck<OrientDevelopmentCard> getOrientDeck(CardLevel level) {
    if (level == null) {
      throw new NullPointerException("level cannot be null");
    }
    return orientDecks.get(level);
  }

  /**
   * Get the base cards.
   *
   * @param level PrestigeCard level.
   * @return List of cards of corresponding level.
   * @throws NullPointerException if <code>level</code> is <code>null</code>.
   */
  public List<BaseDevelopmentCard> getBaseCards(CardLevel level) {
    if (level == null) {
      throw new NullPointerException("level cannot be null");
    }
    return baseCards.get(level);
  }

  /**
   * Get the base cards.
   *
   * @return List of cards at all level.
   */
  public List<BaseDevelopmentCard> getAllBaseCards() {
    List<BaseDevelopmentCard> cards = new ArrayList<>();
    for (CardLevel level : CardLevel.values()) {
      cards.addAll(baseCards.get(level));
    }
    return cards;
  }

  /**
   * Get the game extension.
   *
   * @return Game extension.
   */
  public GameExtension getExtension() {
    return extension;
  }

  /**
   * Get the orient cards.
   *
   * @param level PrestigeCard level.
   * @return List of cards of corresponding level.
   * @throws NullPointerException if <code>level</code> is <code>null</code>.
   */
  public List<OrientDevelopmentCard> getOrientCards(CardLevel level) {
    if (level == null) {
      throw new NullPointerException("level cannot be null");
    }
    return orientCards.get(level);
  }

  /**
   * Get the orient cards.
   *
   * @return List of cards at all level.
   */
  public List<OrientDevelopmentCard> getAllOrientCards() {
    List<OrientDevelopmentCard> cards = new ArrayList<>();
    for (CardLevel level : CardLevel.values()) {
      cards.addAll(orientCards.get(level));
    }
    return cards;
  }

  /**
   * Get the base and orient cards at a certain level.
   *
   * @param level PrestigeCard level.
   * @return List of cards of corresponding level.
   * @throws NullPointerException if <code>level</code> is <code>null</code>.
   */
  public List<DevelopmentCard> getCards(CardLevel level) {
    if (level == null) {
      throw new NullPointerException("level cannot be null");
    }
    List<DevelopmentCard> cards = new ArrayList<>();
    cards.addAll(baseCards.get(level));
    cards.addAll(orientCards.get(level));
    return cards;
  }

  /**
   * Get all the base and orient cards.
   *
   * @return List of all cards.
   */
  public List<DevelopmentCard> getAllCards() {
    List<DevelopmentCard> cards = new ArrayList<>();
    for (CardLevel level : CardLevel.values()) {
      cards.addAll(getCards(level));
    }
    return cards;
  }

  /**
   * Gets all available {@link City}.
   *
   * @return List of {@link City}.
   */
  public List<City> getAllCities() {
    return availableCities;
  }

  /**
   * Returns the list of {@link TradingPost}.
   *
   * @return List of {@link TradingPost}.
   */
  public List<TradingPost> getTradingPosts() {
    List<TradingPost> posts = new ArrayList<>();
    if (!tradingPosts.isEmpty()) {
      for (TradingPostAbility ability : TradingPostAbility.values()) {
        posts.add(tradingPosts.get(ability));
      }
    }
    return posts;
  }

  /**
   * Returns the {@link TradingPost} associated with a specific {@link TradingPostAbility}.
   *
   * @param ability {@link TradingPostAbility} to search.
   * @return {@link TradingPost} associated with <code>ability</code>.
   */
  public TradingPost getTradingPost(TradingPostAbility ability) {
    final TradingPost post = tradingPosts.get(ability);
    if (post == null) {
      throw new RuntimeException("no trading post registered");
    }
    return post;
  }

  /**
   * Get all the actions for the game.
   *
   * @return List of all actions for the current player.
   */
  public List<? extends Action> getAllActions() {
    return actions;
  }

  /**
   * Executes a {@link City} unlock action.
   *
   * @param id                 {@link City} number to take.
   * @param generatorInterface {@link ActionGeneratorInterface} to use.
   * @throws RuntimeException if city id is incorrect.
   */
  public void executeCityUnlockAction(int id, ActionGeneratorInterface generatorInterface) {
    if (id < 0 || id >= unlockedCities.size()) {
      throw new RuntimeException("city id is incorrect");
    }

    // Transfer city from available to player's collection
    transferCity(availableCities.get(unlockedCities.get(id)));

    // Next player
    nextPlayer(generatorInterface);
    availableCities.clear();
  }

  /**
   * Executes a token unlock action.
   *
   * @param color              {@link ResourceColor} color to take.
   * @param generatorInterface {@link ActionGeneratorInterface} to use.
   * @throws RuntimeException     if resource <code>color</code> is incorrect.
   * @throws NullPointerException if <code>color</code> is <code>null</code>.
   */
  public void executeTokenSelectionAction(ResourceColor color,
                                          ActionGeneratorInterface generatorInterface) {
    if (color == null) {
      throw new NullPointerException("color cannot be null");
    }

    int index = unlockedTokens.indexOf(color);
    if (index == -1) {
      throw new RuntimeException("color invalid");
    }

    // Transfer
    removeTokens(Map.of(color, 1));
    getCurrentPlayer().addTokens(Map.of(color, 1));

    // Next player
    nextPlayer(generatorInterface);
    unlockedTokens.clear();
  }

  /**
   * Executes action with specified id.
   *
   * @param id                 Action id.
   * @param generatorInterface {@link ActionGeneratorInterface} to use.
   * @throws RuntimeException if action id is incorrect.
   */
  public void executeAction(int id, ActionGeneratorInterface generatorInterface) {
    if (id < 0 || id >= actions.size()) {
      throw new RuntimeException("action size is incorrect");
    }

    // Execute action and clear previous data
    final Action action = actions.get(id);
    action.execute(this, getCurrentPlayer());
    actions.clear();

    // If extension is city, add city selection if possible
    if (extension == GameExtension.BASE_ORIENT_CITIES) {
      // Loop through all cities
      for (int i = 0; i < availableCities.size(); ++i) {
        City city = availableCities.get(i);
        if (city.obtainableBy(getCurrentPlayer())) {
          unlockedCities.add(i);
        }
      }

      // No unlocked cities
      if (unlockedCities.isEmpty()) {
        nextPlayer(generatorInterface);
        return;
      }

      // One city unlocked - automatically add it to the list of the player's cities
      if (unlockedCities.size() == 1) {
        transferCity(availableCities.get(unlockedCities.get(0)));
        nextPlayer(generatorInterface);
        return;
      }

      // Update current phase
      currentPhase = GamePhase.CITY_SELECTION;
      return;
    }

    // If extension is a trading post, add trading post selection if possible
    if (extension == GameExtension.BASE_ORIENT_TRADING_POSTS) {
      // If we just bought a card and the player has the ability to take a token after a purchase
      if (action.getType() == ActionType.PURCHASE_CARD
          && getCurrentPlayer().hasAbility(TradingPostAbility.TAKE_TOKEN_AFTER_TAKING_CARD)
          && getCurrentPlayer().getNumberOfTokens() < Constants.USER_MAX_NUMBER_OF_TOKENS) {
        List<ResourceColor> colors = new ArrayList<>();

        // Get non empty tokens
        for (ResourceColor color : Cost.getAllPossibleColors()) {
          if (availableTokens.get(color) > 0) {
            colors.add(color);
          }
        }

        // Only one possibility
        if (colors.size() == 1) {
          removeTokens(Map.of(colors.get(0), 1));
          getCurrentPlayer().addTokens(Map.of(colors.get(0), 1));
        } else if (colors.size() > 1) {
          currentPhase = GamePhase.TOKEN_SELECTION;
          unlockedTokens.addAll(colors);
          return;
        }
      }

      // Loop through all trading posts
      List<TradingPostAbility> unlockedPostAbilities = new ArrayList<>();
      for (TradingPostAbility postAbility : TradingPostAbility.values()) {
        final TradingPost tradingPost = tradingPosts.get(postAbility);
        if (tradingPost.obtainableBy(getCurrentPlayer())) {
          unlockedPostAbilities.add(tradingPost.getAbility());
        }
      }

      // Add everything to the player
      for (TradingPostAbility ability : unlockedPostAbilities) {
        getCurrentPlayer().addTradingPostAbility(ability);
      }
    }

    // Next player
    nextPlayer(generatorInterface);
  }

  private void transferCity(City city) {
    availableCities.remove(city);
    getCurrentPlayer().addCity(city);
  }

  /**
   * Gets the list of winners, or <code>null</code> if the game is not yet finished. This function
   * returns an empty list in the extreme case where the game ended in a draw and there are no
   * winners (possible if no player can play during a round).
   *
   * @return List of winners.
   */
  public List<Player> getWinners() {
    if (winners == null) {
      return null;
    }

    List<Player> playerList = new ArrayList<>();
    for (Player player : players) {
      if (winners.contains(player.getUsername())) {
        playerList.add(player);
      }
    }
    return playerList;
  }

  /**
   * Gets the initial number of tokens of a certain {@link ResourceColor}.
   *
   * @param color {@link ResourceColor} of the tokens.
   * @return Initial number of tokens with {@link ResourceColor} <code>color</code>.
   * @throws NullPointerException if <code>color</code> is <code>null</code>.
   */
  public int getInitialNumberOfTokens(ResourceColor color) {
    if (color == null) {
      throw new NullPointerException("color cannot be null");
    }
    return (color.isJoker()) ? Constants.INITIAL_NUMBER_JOKERS : initialNumberOfTokens;
  }

  /**
   * Gets the initial number of non {@link ResourceColor#GOLD} tokens.
   *
   * @return Initial number of non {@link ResourceColor#GOLD} tokens.
   */
  public int getInitialNumberOfNonGoldTokens() {
    return initialNumberOfTokens;
  }

  /**
   * Gets the initial number of {@link Noble}.
   *
   * @return Initial number of {@link Noble}.
   */
  public int getInitialNumberOfNobles() {
    return initialNumberOfNobles;
  }

  /**
   * Initialise the decks by adding cards in them.
   */
  private void initDecks(CardGeneratorInterface generator) {
    for (CardLevel level : CardLevel.values()) {
      baseDecks.put(level, generator.getBaseDevelopmentCardAtLevel(level));
      orientDecks.put(level, generator.getOrientDevelopmentCardAtLevel(level));
    }
  }

  /**
   * Shuffles all the deck.
   */
  private void shuffleDecks() {
    for (CardLevel level : CardLevel.values()) {
      baseDecks.get(level).shuffle();
      orientDecks.get(level).shuffle();
    }
  }

  /**
   * Initialises the game, by popping 4 base cards and 3 orient cards for each level.
   */
  private void initVisibleCards() {
    for (CardLevel level : CardLevel.values()) {
      // Create base and orient cards
      List<BaseDevelopmentCard> base = new ArrayList<>();
      List<OrientDevelopmentCard> orient = new ArrayList<>();

      // Pop 4 base cards from the deck
      for (int i = 0; i < Constants.NUMBER_BASE_CARDS_PER_ROW; ++i) {
        base.add(baseDecks.get(level).pop());
      }

      // Pop 2 orient cards from the deck
      for (int i = 0; i < Constants.NUMBER_ORIENT_CARDS_PER_ROW; ++i) {
        orient.add(orientDecks.get(level).pop());
      }

      // Add them in the game
      baseCards.put(level, base);
      orientCards.put(level, orient);
    }
  }

  private void markGameAsFinished() {
    status = GameStatus.FINISHED;
    currentPhase = GamePhase.GAME_ENDED;
    actions.clear();
    currentPlayerIndex = -1;
  }

  private void updateWinner() {
    // Check if finished the round
    if (currentPlayerIndex < players.size() - 1) {
      return;
    }

    // Make sure we did not already compute the list of winners
    if (winners != null) {
      return;
    }

    // Temporary winners
    Set<Player> tempWinners = new HashSet<>();

    // If it is the cities extension, add all who have cities, else all who have at least 15 points
    if (extension == GameExtension.BASE_ORIENT_CITIES) {
      for (Player player : players) {
        if (player.hasCities()) {
          tempWinners.add(player);
        }
      }
    } else {
      for (Player player : players) {
        if (player.getPrestigePts() >= Constants.NUMBER_POINTS_TO_WIN) {
          tempWinners.add(player);
        }
      }
    }

    // No winners
    if (tempWinners.isEmpty()) {
      return;
    }

    // Update statuses
    markGameAsFinished();

    // Only one winner
    winners = new HashSet<>();
    if (tempWinners.size() == 1) {
      for (Player p : tempWinners) {
        winners.add(p.getUsername());
      }
      return;
    }

    // Compute maximum of prestige points
    Map<Player, Integer> prestigePoints = new HashMap<>();
    int maximumPoints = 0;
    for (Player player : tempWinners) {
      final int points = player.getPrestigePts();
      prestigePoints.put(player, points);
      if (points > maximumPoints) {
        maximumPoints = points;
      }
    }

    // Get all players who have the maximum number of prestige points
    Set<Player> newTempWinners = new HashSet<>();
    for (Player player : tempWinners) {
      if (prestigePoints.get(player) == maximumPoints) {
        newTempWinners.add(player);
      }
    }

    // Only one winner left
    if (newTempWinners.size() == 1) {
      for (Player p : newTempWinners) {
        winners.add(p.getUsername());
      }
      return;
    }

    // Compute minimum of number of bonuses
    Map<Player, Integer> bonusCounts = new HashMap<>();
    int minCards = -1;
    for (Player player : tempWinners) {
      final int count = player.getTotalNumberOfBonusCards();
      bonusCounts.put(player, count);
      if (minCards == -1 || count < minCards) {
        minCards = count;
      }
    }

    // Add all players who have the minimum number of bonuses
    for (Player player : tempWinners) {
      if (bonusCounts.get(player) == minCards) {
        winners.add(player.getUsername());
      }
    }
  }

  /**
   * Gets the unlocked cities.
   *
   * @return Unlocked cities.
   */
  public List<City> unlockedCities() {
    List<City> cities = new ArrayList<>();
    for (int i : unlockedCities) {
      cities.add(availableCities.get(i));
    }
    return cities;
  }

  /**
   * Gets the unlocked tokens.
   *
   * @return Unlocked tokens.
   */
  public List<ResourceColor> unlockedTokens() {
    return unlockedTokens;
  }

  /**
   * Always returns <code>false</code>.
   *
   * @return <code>false</code>.
   */
  @Override
  public boolean isEmpty() {
    return false;
  }
}
