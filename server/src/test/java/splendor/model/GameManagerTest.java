package splendor.model;

import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import ca.mcgill.nalmer.splendormodels.Action;
import ca.mcgill.nalmer.splendormodels.ActionGeneratorInterface;
import ca.mcgill.nalmer.splendormodels.ActionType;
import ca.mcgill.nalmer.splendormodels.BaseDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.CardGeneratorInterface;
import ca.mcgill.nalmer.splendormodels.CardLevel;
import ca.mcgill.nalmer.splendormodels.City;
import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.Deck;
import ca.mcgill.nalmer.splendormodels.Game;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import ca.mcgill.nalmer.splendormodels.Noble;
import ca.mcgill.nalmer.splendormodels.OrientDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.Player;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import ca.mcgill.nalmer.splendormodels.TradingPost;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public final class GameManagerTest {
  private static Game game1;
  private static Game game2;
  private static Game game3;
  private static Game saveGame1;
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();
  private final GameManager gameManager = new GameManager();

  @BeforeClass
  public static void initialise() {
    List<User> users = new ArrayList<>();
    users.add(new User("maex", "password", "FF0000", UserRole.ROLE_ADMIN));
    users.add(new User("linus", "password", "00FF00", UserRole.ROLE_PLAYER));

    game1 = new Game(
        users, GameExtension.BASE_ORIENT, new SampleCardGenerator(), new SampleActionGenerator(),
        "15"
    );
    game2 = new Game(
        users, GameExtension.BASE_ORIENT, new SampleCardGenerator(), new SampleActionGenerator(),
        "18"
    );
    game3 = new Game(
        users, GameExtension.BASE_ORIENT, new SampleCardGenerator(), new SampleActionGenerator(),
        "20"
    );

    saveGame1 = new Game(
            users, GameExtension.BASE_ORIENT, new SampleCardGenerator(), new SampleActionGenerator(),
            "15"
    );
  }

  @After
  public void clearGameManager() {
    gameManager.clear();
  }

  @Test
  public void testEmpty() {
    Assert.assertTrue(gameManager.isEmpty());
    Assert.assertEquals(0, gameManager.getAllGames().size());
  }

  @Test
  public void testAddOneGame() {
    gameManager.addGame(game1);
    Assert.assertEquals(1, gameManager.getAllGames().size());
    Assert.assertTrue(gameManager.hasGame(game1.getId()));
    Assert.assertEquals(game1, gameManager.getGame(game1.getId()));
  }

  @Test
  public void testAddTwoGames() {
    gameManager.addGame(game1);
    gameManager.addGame(game2);
    Assert.assertEquals(2, gameManager.getAllGames().size());
    Assert.assertTrue(gameManager.hasGame(game2.getId()));
    Assert.assertEquals(game2, gameManager.getGame(game2.getId()));
  }

  @Test
  public void testAddThreeGames() {
    gameManager.addGame(game1);
    gameManager.addGame(game2);
    gameManager.addGame(game3);
    Assert.assertEquals(3, gameManager.getAllGames().size());
    Assert.assertTrue(gameManager.hasGame(game3.getId()));
    Assert.assertEquals(game3, gameManager.getGame(game3.getId()));
  }

  @Test
  public void testAddTwoRemoveOne() {
    gameManager.addGame(game1);
    gameManager.addGame(game2);
    gameManager.removeGame(game2);
    Assert.assertEquals(1, gameManager.getAllGames().size());
    Assert.assertTrue(gameManager.hasGame(game1.getId()));
    Assert.assertEquals(game1, gameManager.getGame(game1.getId()));
  }

  @Test
  public void testAddAndRemoveTwo() {
    gameManager.addGame(game1);
    gameManager.addGame(game2);
    gameManager.removeGame(game2);
    gameManager.removeGame(game1);
    Assert.assertEquals(0, gameManager.getAllGames().size());
    Assert.assertTrue(gameManager.isEmpty());
  }

  @Test
  public void removeInvalid() {
    gameManager.addGame(game1);
    gameManager.removeGame("-1");
    Assert.assertEquals(1, gameManager.getAllGames().size());
    try {
      gameManager.removeGame((String) null);
    } catch (NullPointerException e) {
      Assert.assertEquals("id cannot be null", e.getMessage());
    }
    try {
      gameManager.removeGame((Game) null);
    } catch (NullPointerException e) {
      Assert.assertEquals("game cannot be null", e.getMessage());
    }
  }

  @Test
  public void testGetGame() {
    Assert.assertThrows(RuntimeException.class, () -> gameManager.getGame(game1.getId()));
    gameManager.addGame(game1);
    Assert.assertEquals(game1, gameManager.getGame(game1.getId()));
  }

  @Test
  public void testHasGame() {
    Assert.assertFalse(gameManager.hasGame(game1.getId()));
    gameManager.addGame(game1);
    Assert.assertTrue(gameManager.hasGame(game1.getId()));
  }

  @Test
  public void testNullGame() {
    exceptionRule.expect(NullPointerException.class);
    gameManager.getGame(null);
  }

  @Test
  public void testAddGameWithNullId() {
    try {
      gameManager.addGame(null);
    } catch (NullPointerException e) {
      Assert.assertEquals("game cannot be null", e.getMessage());
    }
  }

  @Test
  public void testHasGameWithNullId() {
    try {
      gameManager.hasGame(null);
    } catch (NullPointerException e) {
      Assert.assertEquals("id cannot be null", e.getMessage());
    }
  }


  @Test
  public void testAddOneSaveGame() {
    Assert.assertTrue(gameManager.isSaveGameEmpty());
    gameManager.addSaveGame(saveGame1.getId(), saveGame1);
    Assert.assertEquals(1, gameManager.getAllSaveGames().size());
    Assert.assertTrue(gameManager.hasSaveGame(game1.getId()));
    Assert.assertEquals(saveGame1, gameManager.getSaveGame(game1.getId()));
  }

  @Test
  public void testInvalidSaveGame() {
    Assert.assertTrue(gameManager.isSaveGameEmpty());
    Assert.assertEquals(0, gameManager.getAllSaveGames().size());
    Assert.assertFalse(gameManager.hasSaveGame("0"));
    exceptionRule.expect(NullPointerException.class);
    gameManager.addSaveGame(null, null);
  }

  @Test
  public void testAddSaveGameWithNullId() {
    try {
      gameManager.addSaveGame(null, null);
    } catch (NullPointerException e) {
      Assert.assertEquals("save game cannot be null", e.getMessage());
    }
  }

  @Test
  public void testHasSaveGameWithNullId() {
    try {
      gameManager.hasSaveGame(null);
    } catch (NullPointerException e) {
      Assert.assertEquals("id cannot be null", e.getMessage());
    }
  }

  @Test
  public void testGetSaveGameWithNulls() {
    try {
      gameManager.getSaveGame(null);
    } catch (NullPointerException e) {
      Assert.assertEquals("game id cannot be null", e.getMessage());
    }

    Assert.assertThrows(RuntimeException.class, () -> gameManager.getSaveGame("0"));
  }

  private static final class SampleAction extends Action {
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

  private static final class SampleActionGenerator implements ActionGeneratorInterface {
    @Override
    public List<? extends Action> generateAllActions(Game game) {
      return new ArrayList<>(List.of(new SampleAction()));
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
          new BaseDevelopmentCard(new Cost("12101"), ResourceColor.BLACK, 0, CardLevel.ONE, 4),
      };

      BaseDevelopmentCard[] baseTwo = new BaseDevelopmentCard[] {
          new BaseDevelopmentCard(new Cost("01111"), ResourceColor.GREEN, 0, CardLevel.ONE, 5),
          new BaseDevelopmentCard(new Cost("10120"), ResourceColor.BLUE, 0, CardLevel.ONE, 6),
          new BaseDevelopmentCard(new Cost("04000"), ResourceColor.BLUE, 1, CardLevel.ONE, 7),
          new BaseDevelopmentCard(new Cost("10111"), ResourceColor.RED, 0, CardLevel.ONE, 8),
          new BaseDevelopmentCard(new Cost("12101"), ResourceColor.BLACK, 0, CardLevel.ONE, 9),
      };

      BaseDevelopmentCard[] baseThree = new BaseDevelopmentCard[] {
          new BaseDevelopmentCard(new Cost("01111"), ResourceColor.GREEN, 0, CardLevel.ONE, 10),
          new BaseDevelopmentCard(new Cost("10120"), ResourceColor.BLUE, 0, CardLevel.ONE, 11),
          new BaseDevelopmentCard(new Cost("04000"), ResourceColor.BLUE, 1, CardLevel.ONE, 12),
          new BaseDevelopmentCard(new Cost("10111"), ResourceColor.RED, 0, CardLevel.ONE, 13),
          new BaseDevelopmentCard(new Cost("12101"), ResourceColor.BLACK, 0, CardLevel.ONE, 14),
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
          new Noble(new Cost("03033"), 29),
      };

      City[] cityArray = {
          new City(new Cost("30300"), 11, 30),
          new City(new Cost("00043"), 13, 31),
          new City(new Cost("00304"), 13, 32),
          new City(new Cost("34000"), 13, 33),
          new City(new Cost("40030"), 13, 34),
          new City(17, 35),
      };

      TradingPost[] tdArray = {
          TradingPost.tokenAfterCard(36),
          TradingPost.tokenAfterTwoTokens(37),
          TradingPost.goldWorthTwoTokens(38),
          TradingPost.fivePoints(39),
          TradingPost.onePointForEachShield(40)
      };

      base.put(CardLevel.ONE, new ArrayList<>(List.of(baseOne)));
      base.put(CardLevel.TWO, new ArrayList<>(List.of(baseTwo)));
      base.put(CardLevel.THREE, new ArrayList<>(List.of(baseThree)));
      orient.put(CardLevel.ONE, new ArrayList<>(List.of(orientOne)));
      orient.put(CardLevel.TWO, new ArrayList<>(List.of(orientTwo)));
      orient.put(CardLevel.THREE, new ArrayList<>(List.of(orientThree)));
      nobles = new ArrayList<>(List.of(nobleArray));
      cities = new ArrayList<>(List.of(cityArray));
      tradingPosts = new ArrayList<>(List.of(tdArray));
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
