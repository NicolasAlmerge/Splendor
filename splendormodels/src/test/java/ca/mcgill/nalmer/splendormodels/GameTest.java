package ca.mcgill.nalmer.splendormodels;

import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class GameTest {
  private final List<User> users = new ArrayList<>();
  private final List<? extends Action> actions = new ArrayList<>(List.of(new SampleAction()));
  private Game game;

  @Before
  public void initialise() {
    users.clear();
    users.add(new User("maex", "password", "FF0000", UserRole.ROLE_ADMIN));
    users.add(new User("linus", "password", "00FF00", UserRole.ROLE_PLAYER));
    game = new Game(
        users, GameExtension.BASE_ORIENT, new SampleCardGenerator(), game -> actions, "0"
    );
  }

  @Test
  public void testTokens() {
    Map<ResourceColor, Integer> tokens = new EnumMap<>(ResourceColor.class);
    Map<ResourceColor, Integer> initial = new EnumMap<>(ResourceColor.class);
    for (ResourceColor color : ResourceColor.values()) {
      initial.put(color, (color.isJoker()) ? 5 : 4);
    }
    Map<ResourceColor, Integer> newCount = new HashMap<>(initial);

    Assert.assertEquals(initial, game.getAvailableTokens());

    tokens.put(ResourceColor.RED, 2);
    tokens.put(ResourceColor.BLUE, 3);
    newCount.put(ResourceColor.RED, newCount.get(ResourceColor.RED) + 2);
    newCount.put(ResourceColor.BLUE, newCount.get(ResourceColor.BLUE) + 3);
    game.addTokens(tokens);
    Assert.assertEquals(newCount, game.getAvailableTokens());

    Assert.assertTrue(game.hasTokensOfColor(ResourceColor.RED));
    Assert.assertEquals(6, game.getNumberOfTokens(ResourceColor.RED));

    game.removeTokens(tokens);
    Assert.assertEquals(initial, game.getAvailableTokens());

    game.removeTokens(Map.of(ResourceColor.RED, 8));
    Assert.assertFalse(game.hasTokensOfColor(ResourceColor.RED));
  }

  @Test
  public void testCurrentUser() {
    Assert.assertEquals(2, game.getPlayers().size());

    Assert.assertEquals(0, game.getCurrentPlayerIndex());
    game.setCurrentPlayerIndex(1);
    Assert.assertEquals(1, game.getCurrentPlayerIndex());

    game.executeAction(0, game -> List.of(new SampleAction()));
    Assert.assertEquals(0, game.getCurrentPlayerIndex());

    game.setCurrentPlayer(game.getPlayers().get(1));
    Assert.assertEquals(1, game.getCurrentPlayerIndex());
    Assert.assertEquals(game.getPlayers().get(1), game.getCurrentPlayer());

    User fakeUser = new User("kot", "password", "000000", UserRole.ROLE_PLAYER);
    Assert.assertThrows(RuntimeException.class, () -> game.setCurrentPlayerIndex(2));
    Assert.assertThrows(RuntimeException.class, () -> game.setCurrentPlayerIndex(-1));
    Assert.assertThrows(RuntimeException.class, () -> game.setCurrentPlayer(new Player(fakeUser)));
  }

  @Test
  public void testOtherAttributes() {
    Assert.assertEquals("0", game.getId());
    Assert.assertEquals(GameExtension.BASE_ORIENT, game.getExtension());

    Assert.assertEquals(GamePhase.TURN_STARTED, game.getCurrentPhase());
    game.setCurrentPhase(GamePhase.TOKEN_SELECTION);
    Assert.assertEquals(GamePhase.TOKEN_SELECTION, game.getCurrentPhase());

    Assert.assertEquals(GameStatus.READY_TO_JOIN, game.getStatus());
    game.setStatus(GameStatus.ONGOING);
    Assert.assertEquals(GameStatus.ONGOING, game.getStatus());
  }

  @Test
  public void testGameCreationWithoutUsers() {
    Assert.assertThrows(RuntimeException.class, () -> new Game(
        null, GameExtension.BASE_ORIENT, new SampleCardGenerator(), game -> actions, "0")
    );
    Assert.assertThrows(RuntimeException.class, () -> new Game(
        new ArrayList<>(), GameExtension.BASE_ORIENT, new SampleCardGenerator(),
        game -> actions, "0")
    );
  }

  @Test
  public void testEqual() {
    game = new Game(
        users, GameExtension.BASE_ORIENT, new SampleCardGenerator(),
        g -> PurchaseCardAction.allFrom(g, g.getCurrentPlayer(), g.getBaseCards(CardLevel.ONE).get(0), 0), "0"
    );

    BaseDevelopmentCard card = game.getBaseCards(CardLevel.ONE).get(0);
    game.getCurrentPlayer().addTokens(Map.of(
        ResourceColor.RED, 10,
        ResourceColor.GREEN, 10,
        ResourceColor.BLACK, 10,
        ResourceColor.BLUE, 10,
        ResourceColor.WHITE, 10
    ));

    Assert.assertEquals(game.getCurrentPlayer(), game.getPlayers().get(0));

    game.executeAction(0, g -> PurchaseCardAction.allFrom(g, g.getCurrentPlayer(), card, 0));
    DevelopmentCard cardReserved = game.getPlayers().get(0).getBonuses().get(card.getColor()).get(0);

    Assert.assertEquals(card.getId(), cardReserved.getId());
    Assert.assertEquals(card, cardReserved);
    Assert.assertSame(card, cardReserved);

    Gson gson = Utils.getGameGson();
    Game copy = gson.fromJson(gson.toJson(game), Game.class);

    DevelopmentCard second = copy.getPlayers().get(0).getBonuses().get(card.getColor()).get(0);
    Assert.assertEquals(cardReserved.getId(), second.getId());
    Assert.assertEquals(cardReserved, second);
  }

  private final static class SampleAction extends Action {
    /**
     * Constructor.
     */
    public SampleAction() {
      super(0);
    }

    @Override
    public void execute(Game game, Player player) {
    }

    @Override
    public ActionType getType() {
      return null;
    }
  }

  private static final class SampleCardGenerator implements CardGeneratorInterface {
    private final Map<CardLevel, List<BaseDevelopmentCard>> base = new EnumMap<>(CardLevel.class);
    private final Map<CardLevel, List<OrientDevelopmentCard>> orient =
        new EnumMap<>(CardLevel.class);
    private final List<Noble> nobles;
    private final List<City> cities;
    private final List<TradingPost> tradingPosts;

    public SampleCardGenerator() {
      BaseDevelopmentCard[] baseOne = new BaseDevelopmentCard[] {
          new BaseDevelopmentCard(new Cost("01111"), ResourceColor.GREEN, 0, CardLevel.ONE, 0),
          new BaseDevelopmentCard(new Cost("10120"), ResourceColor.BLUE, 0, CardLevel.ONE, 1),
          new BaseDevelopmentCard(new Cost("04000"), ResourceColor.BLUE, 1, CardLevel.ONE, 2),
          new BaseDevelopmentCard(new Cost("10111"), ResourceColor.RED, 0, CardLevel.ONE, 3),
          new BaseDevelopmentCard(new Cost("12101"), ResourceColor.BLACK, 0, CardLevel.ONE, 4)
      };

      BaseDevelopmentCard[] baseTwo = new BaseDevelopmentCard[] {
          new BaseDevelopmentCard(new Cost("01111"), ResourceColor.GREEN, 0, CardLevel.ONE, 5),
          new BaseDevelopmentCard(new Cost("10120"), ResourceColor.BLUE, 0, CardLevel.ONE, 6),
          new BaseDevelopmentCard(new Cost("04000"), ResourceColor.BLUE, 1, CardLevel.ONE, 7),
          new BaseDevelopmentCard(new Cost("10111"), ResourceColor.RED, 0, CardLevel.ONE, 8),
          new BaseDevelopmentCard(new Cost("12101"), ResourceColor.BLACK, 0, CardLevel.ONE, 9)
      };

      BaseDevelopmentCard[] baseThree = new BaseDevelopmentCard[] {
          new BaseDevelopmentCard(new Cost("01111"), ResourceColor.GREEN, 0, CardLevel.ONE, 10),
          new BaseDevelopmentCard(new Cost("10120"), ResourceColor.BLUE, 0, CardLevel.ONE, 11),
          new BaseDevelopmentCard(new Cost("04000"), ResourceColor.BLUE, 1, CardLevel.ONE, 12),
          new BaseDevelopmentCard(new Cost("10111"), ResourceColor.RED, 0, CardLevel.ONE, 13),
          new BaseDevelopmentCard(new Cost("12101"), ResourceColor.BLACK, 0, CardLevel.ONE, 14)
      };

      OrientDevelopmentCard[] orientOne = new OrientDevelopmentCard[] {
          OrientDevelopmentCard.bonusMatching(new Cost("01100"), 15),
          OrientDevelopmentCard.bonusMatching(new Cost("00011"), 16),
          OrientDevelopmentCard.twoGoldBonuses(new Cost("01010"), 17)
      };

      OrientDevelopmentCard[] orientTwo = new OrientDevelopmentCard[] {
          OrientDevelopmentCard.bonusMatchingAndTake(new Cost("01111"), 18),
          OrientDevelopmentCard.twoBonuses(ResourceColor.BLUE, new Cost("01331"), 19),
          OrientDevelopmentCard.reserveNoble(ResourceColor.GREEN, new Cost("30311"), 20)
      };

      OrientDevelopmentCard[] orientThree = new OrientDevelopmentCard[] {
          OrientDevelopmentCard.discard2Bonuses(ResourceColor.RED, ResourceColor.BLACK, 21),
          OrientDevelopmentCard.discard2Bonuses(ResourceColor.GREEN, ResourceColor.BLUE, 22),
          OrientDevelopmentCard.takeLevel2(ResourceColor.BLACK, new Cost("43301"), 23)
      };

      Noble[] nobleArray = {
          new Noble(new Cost("03330"), 24),
          new Noble(new Cost("30033"), 25),
          new Noble(new Cost("30303"), 26),
          new Noble(new Cost("40040"), 27),
          new Noble(new Cost("00440"), 28),
          new Noble(new Cost("03033"), 29)
      };

      City[] cityArray = {
          new City(new Cost("00043"), 13, 30),
          new City(new Cost("00304"), 13, 31),
          new City(new Cost("34000"), 13, 32)
      };

      TradingPost[] tpArray = {
          TradingPost.tokenAfterCard(33),
          TradingPost.tokenAfterTwoTokens(34),
          TradingPost.goldWorthTwoTokens(35),
          TradingPost.fivePoints(36),
          TradingPost.onePointForEachShield(37)
      };

      base.put(CardLevel.ONE, new ArrayList<>(List.of(baseOne)));
      base.put(CardLevel.TWO, new ArrayList<>(List.of(baseTwo)));
      base.put(CardLevel.THREE, new ArrayList<>(List.of(baseThree)));
      orient.put(CardLevel.ONE, new ArrayList<>(List.of(orientOne)));
      orient.put(CardLevel.TWO, new ArrayList<>(List.of(orientTwo)));
      orient.put(CardLevel.THREE, new ArrayList<>(List.of(orientThree)));
      nobles = new ArrayList<>(List.of(nobleArray));
      cities = new ArrayList<>(List.of(cityArray));
      tradingPosts = new ArrayList<>(List.of(tpArray));
    }

    @Override
    public Deck<BaseDevelopmentCard> getBaseDevelopmentCardAtLevel(CardLevel level) {
      return new Deck<>(base.get(level));
    }

    @Override
    public Deck<OrientDevelopmentCard> getOrientDevelopmentCardAtLevel(CardLevel level) {
      return new Deck<>(orient.get(level));
    }

    @Override
    public List<Noble> getAllNobles() {
      return new ArrayList<>(nobles);
    }

    @Override
    public List<City> getAllCities() {
      return new ArrayList<>(cities);
    }

    @Override
    public List<TradingPost> getAllTradingPosts() {
      return new ArrayList<>(tradingPosts);
    }
  }
}
