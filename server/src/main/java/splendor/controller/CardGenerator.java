package splendor.controller;

import ca.mcgill.nalmer.splendormodels.BaseDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.CardGeneratorInterface;
import ca.mcgill.nalmer.splendormodels.CardLevel;
import ca.mcgill.nalmer.splendormodels.City;
import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.Deck;
import ca.mcgill.nalmer.splendormodels.Noble;
import ca.mcgill.nalmer.splendormodels.OrientDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import ca.mcgill.nalmer.splendormodels.TradingPost;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for card generation.
 */
final class CardGenerator implements CardGeneratorInterface {
  private final BaseDevelopmentCard[] baseLevelOne;
  private final BaseDevelopmentCard[] baseLevelTwo;
  private final BaseDevelopmentCard[] baseLevelThree;
  private final OrientDevelopmentCard[] orientLevelOne;
  private final OrientDevelopmentCard[] orientLevelTwo;
  private final OrientDevelopmentCard[] orientLevelThree;
  private final Noble[] nobles;
  private final City[] cities;
  private final TradingPost[] tradingPosts;
  private final IdGenerator generator = new IdGenerator();

  /**
   * Constructor.
   */
  public CardGenerator() {
    baseLevelOne = getBaseLevelOneCards();
    baseLevelTwo = getBaseLevelTwoCards();
    baseLevelThree = getBaseLevelThreeCards();
    orientLevelOne = getOrientLevelOneCards();
    orientLevelTwo = getOrientLevelTwoCards();
    orientLevelThree = getOrientLevelThreeCards();
    nobles = getNobleCards();
    cities = getCities();
    tradingPosts = getTradingPosts();
  }

  @Override
  public Deck<BaseDevelopmentCard> getBaseDevelopmentCardAtLevel(CardLevel level) {
    if (level == null) {
      throw new NullPointerException("Base level cannot be null");
    }

    return switch (level) {
      case ONE -> new Deck<>(baseLevelOne);
      case TWO -> new Deck<>(baseLevelTwo);
      case THREE -> new Deck<>(baseLevelThree);
    };
  }

  @Override
  public Deck<OrientDevelopmentCard> getOrientDevelopmentCardAtLevel(CardLevel level) {
    if (level == null) {
      throw new NullPointerException("Orient level cannot be null");
    }

    return switch (level) {
      case ONE -> new Deck<>(orientLevelOne);
      case TWO -> new Deck<>(orientLevelTwo);
      case THREE -> new Deck<>(orientLevelThree);
    };
  }

  @Override
  public List<Noble> getAllNobles() {
    return new ArrayList<>(List.of(nobles));
  }

  @Override
  public List<City> getAllCities() {
    return new ArrayList<>(List.of(cities));
  }

  @Override
  public List<TradingPost> getAllTradingPosts() {
    return new ArrayList<>(List.of(tradingPosts));
  }

  ///////////////////////////////// GENERATORS FUNCTIONS /////////////////////////////////

  private BaseDevelopmentCard[] getBaseLevelOneCards() {
    return new BaseDevelopmentCard[] {
        level1BaseCard(ResourceColor.BLACK, "00131", 0),
        level1BaseCard(ResourceColor.BLACK, "00210", 0),
        level1BaseCard(ResourceColor.BLACK, "00300", 0),
        level1BaseCard(ResourceColor.BLACK, "04000", 1),
        level1BaseCard(ResourceColor.BLACK, "11110", 0),
        level1BaseCard(ResourceColor.BLACK, "12110", 0),
        level1BaseCard(ResourceColor.BLACK, "20200", 0),
        level1BaseCard(ResourceColor.BLACK, "22010", 0),
        level1BaseCard(ResourceColor.BLUE, "00003", 0),
        level1BaseCard(ResourceColor.BLUE, "00040", 1),
        level1BaseCard(ResourceColor.BLUE, "00202", 0),
        level1BaseCard(ResourceColor.BLUE, "01310", 0),
        level1BaseCard(ResourceColor.BLUE, "10002", 0),
        level1BaseCard(ResourceColor.BLUE, "10111", 0),
        level1BaseCard(ResourceColor.BLUE, "10121", 0),
        level1BaseCard(ResourceColor.BLUE, "10220", 0),
        level1BaseCard(ResourceColor.GREEN, "00004", 1),
        level1BaseCard(ResourceColor.GREEN, "00030", 0),
        level1BaseCard(ResourceColor.GREEN, "01022", 0),
        level1BaseCard(ResourceColor.GREEN, "02020", 0),
        level1BaseCard(ResourceColor.GREEN, "11011", 0),
        level1BaseCard(ResourceColor.GREEN, "11012", 0),
        level1BaseCard(ResourceColor.GREEN, "13100", 0),
        level1BaseCard(ResourceColor.GREEN, "21000", 0),
        level1BaseCard(ResourceColor.RED, "02100", 0),
        level1BaseCard(ResourceColor.RED, "10013", 0),
        level1BaseCard(ResourceColor.RED, "11101", 0),
        level1BaseCard(ResourceColor.RED, "20020", 0),
        level1BaseCard(ResourceColor.RED, "20102", 0),
        level1BaseCard(ResourceColor.RED, "21101", 0),
        level1BaseCard(ResourceColor.RED, "30000", 0),
        level1BaseCard(ResourceColor.RED, "40000", 1),
        level1BaseCard(ResourceColor.WHITE, "00021", 0),
        level1BaseCard(ResourceColor.WHITE, "00400", 1),
        level1BaseCard(ResourceColor.WHITE, "01111", 0),
        level1BaseCard(ResourceColor.WHITE, "01211", 0),
        level1BaseCard(ResourceColor.WHITE, "02002", 0),
        level1BaseCard(ResourceColor.WHITE, "02201", 0),
        level1BaseCard(ResourceColor.WHITE, "03000", 0),
        level1BaseCard(ResourceColor.WHITE, "31001", 0)
    };
  }

  private BaseDevelopmentCard[] getBaseLevelTwoCards() {
    return new BaseDevelopmentCard[] {
        level2BaseCard(ResourceColor.BLACK, "00006", 3),
        level2BaseCard(ResourceColor.BLACK, "00530", 2),
        level2BaseCard(ResourceColor.BLACK, "01420", 2),
        level2BaseCard(ResourceColor.BLACK, "30302", 1),
        level2BaseCard(ResourceColor.BLACK, "32200", 1),
        level2BaseCard(ResourceColor.BLACK, "50000", 2),
        level2BaseCard(ResourceColor.BLUE, "02230", 1),
        level2BaseCard(ResourceColor.BLUE, "02303", 1),
        level2BaseCard(ResourceColor.BLUE, "05000", 2),
        level2BaseCard(ResourceColor.BLUE, "06000", 3),
        level2BaseCard(ResourceColor.BLUE, "20014", 2),
        level2BaseCard(ResourceColor.BLUE, "53000", 2),
        level2BaseCard(ResourceColor.GREEN, "00500", 2),
        level2BaseCard(ResourceColor.GREEN, "00600", 3),
        level2BaseCard(ResourceColor.GREEN, "05300", 2),
        level2BaseCard(ResourceColor.GREEN, "23002", 1),
        level2BaseCard(ResourceColor.GREEN, "30230", 1),
        level2BaseCard(ResourceColor.GREEN, "42001", 2),
        level2BaseCard(ResourceColor.RED, "00005", 2),
        level2BaseCard(ResourceColor.RED, "00060", 3),
        level2BaseCard(ResourceColor.RED, "03023", 1),
        level2BaseCard(ResourceColor.RED, "14200", 2),
        level2BaseCard(ResourceColor.RED, "20023", 1),
        level2BaseCard(ResourceColor.RED, "30005", 2),
        level2BaseCard(ResourceColor.WHITE, "00050", 2),
        level2BaseCard(ResourceColor.WHITE, "00053", 2),
        level2BaseCard(ResourceColor.WHITE, "00142", 2),
        level2BaseCard(ResourceColor.WHITE, "00322", 1),
        level2BaseCard(ResourceColor.WHITE, "23030", 1),
        level2BaseCard(ResourceColor.WHITE, "60000", 3)
    };
  }

  private BaseDevelopmentCard[] getBaseLevelThreeCards() {
    return new BaseDevelopmentCard[] {
        level3BaseCard(ResourceColor.BLACK, "00070", 4),
        level3BaseCard(ResourceColor.BLACK, "00073", 5),
        level3BaseCard(ResourceColor.BLACK, "00363", 4),
        level3BaseCard(ResourceColor.BLACK, "33530", 3),
        level3BaseCard(ResourceColor.BLUE, "30335", 3),
        level3BaseCard(ResourceColor.BLUE, "63003", 4),
        level3BaseCard(ResourceColor.BLUE, "70000", 4),
        level3BaseCard(ResourceColor.BLUE, "73000", 5),
        level3BaseCard(ResourceColor.GREEN, "07000", 4),
        level3BaseCard(ResourceColor.GREEN, "07300", 5),
        level3BaseCard(ResourceColor.GREEN, "36300", 4),
        level3BaseCard(ResourceColor.GREEN, "53033", 3),
        level3BaseCard(ResourceColor.RED, "00700", 4),
        level3BaseCard(ResourceColor.RED, "00730", 5),
        level3BaseCard(ResourceColor.RED, "03630", 4),
        level3BaseCard(ResourceColor.RED, "35303", 3),
        level3BaseCard(ResourceColor.WHITE, "00007", 4),
        level3BaseCard(ResourceColor.WHITE, "03353", 3),
        level3BaseCard(ResourceColor.WHITE, "30007", 5),
        level3BaseCard(ResourceColor.WHITE, "30036", 4)
    };
  }

  private OrientDevelopmentCard[] getOrientLevelOneCards() {
    return new OrientDevelopmentCard[] {
        OrientDevelopmentCard.twoGoldBonuses(new Cost("00003"), newId()),
        OrientDevelopmentCard.twoGoldBonuses(new Cost("00030"), newId()),
        OrientDevelopmentCard.twoGoldBonuses(new Cost("00300"), newId()),
        OrientDevelopmentCard.twoGoldBonuses(new Cost("03000"), newId()),
        OrientDevelopmentCard.twoGoldBonuses(new Cost("30000"), newId()),
        OrientDevelopmentCard.bonusMatching(new Cost("00302"), newId()),
        OrientDevelopmentCard.bonusMatching(new Cost("02030"), newId()),
        OrientDevelopmentCard.bonusMatching(new Cost("03200"), newId()),
        OrientDevelopmentCard.bonusMatching(new Cost("20003"), newId()),
        OrientDevelopmentCard.bonusMatching(new Cost("30020"), newId())
    };
  }

  private OrientDevelopmentCard[] getOrientLevelTwoCards() {
    return new OrientDevelopmentCard[] {
        OrientDevelopmentCard.twoBonuses(ResourceColor.BLACK, new Cost("03040"), newId()),
        OrientDevelopmentCard.twoBonuses(ResourceColor.BLUE, new Cost("30004"), newId()),
        OrientDevelopmentCard.twoBonuses(ResourceColor.GREEN, new Cost("40030"), newId()),
        OrientDevelopmentCard.reserveNoble(ResourceColor.GREEN, new Cost("22022"), newId()),
        OrientDevelopmentCard.bonusMatchingAndTake(new Cost("00431"), newId()),
        OrientDevelopmentCard.bonusMatchingAndTake(new Cost("43001"), newId()),
        OrientDevelopmentCard.twoBonuses(ResourceColor.RED, new Cost("00403"), newId()),
        OrientDevelopmentCard.reserveNoble(ResourceColor.RED, new Cost("22202"), newId()),
        OrientDevelopmentCard.twoBonuses(ResourceColor.WHITE, new Cost("04300"), newId()),
        OrientDevelopmentCard.reserveNoble(ResourceColor.WHITE, new Cost("02222"), newId())
    };
  }

  private OrientDevelopmentCard[] getOrientLevelThreeCards() {
    return new OrientDevelopmentCard[] {
        OrientDevelopmentCard.discard2Bonuses(ResourceColor.BLACK, ResourceColor.RED, newId()),
        OrientDevelopmentCard.takeLevel2(ResourceColor.BLACK, new Cost("63001"), newId()),
        OrientDevelopmentCard.discard2Bonuses(ResourceColor.BLUE, ResourceColor.WHITE, newId()),
        OrientDevelopmentCard.takeLevel2(ResourceColor.BLUE, new Cost("01630"), newId()),
        OrientDevelopmentCard.discard2Bonuses(ResourceColor.GREEN, ResourceColor.BLUE, newId()),
        OrientDevelopmentCard.takeLevel2(ResourceColor.GREEN, new Cost("00163"), newId()),
        OrientDevelopmentCard.discard2Bonuses(ResourceColor.RED, ResourceColor.GREEN, newId()),
        OrientDevelopmentCard.takeLevel2(ResourceColor.RED, new Cost("30016"), newId()),
        OrientDevelopmentCard.discard2Bonuses(ResourceColor.WHITE, ResourceColor.BLACK, newId()),
        OrientDevelopmentCard.takeLevel2(ResourceColor.WHITE, new Cost("16300"), newId())
    };
  }

  private Noble[] getNobleCards() {
    return new Noble[] {
        new Noble(new Cost("00044"), newId()),
        new Noble(new Cost("00333"), newId()),
        new Noble(new Cost("00440"), newId()),
        new Noble(new Cost("03330"), newId()),
        new Noble(new Cost("04400"), newId()),
        new Noble(new Cost("30033"), newId()),
        new Noble(new Cost("33003"), newId()),
        new Noble(new Cost("33300"), newId()),
        new Noble(new Cost("40004"), newId()),
        new Noble(new Cost("44000"), newId()),
        new Noble(new Cost("33300"), newId())
    };
  }

  private City[] getCities() {
    return new City[] {
        new City(new Cost("30300"), 11, newId()),
        new City(new Cost("00043"), 13, newId()),
        new City(new Cost("00304"), 13, newId()),
        new City(new Cost("34000"), 13, newId()),
        new City(new Cost("40030"), 13, newId()),
        new City(17, newId()),
        new City(new Cost("11111"), 16, newId()),
        new City(new Cost("30333"), 11, newId()),
        new City(new Cost("33033"), 11, newId()),
        new City(5, 15, newId()),
        new City(6, 12, newId()),
        new City(new Cost("00400"), 4, 14, newId()),
        new City(new Cost("03400"), 13, newId()),
        new City(new Cost("21122"), 14, newId()),
        new City(new Cost("22222"), 13, newId())
    };
  }

  private TradingPost[] getTradingPosts() {
    return new TradingPost[] {
        TradingPost.tokenAfterCard(newId()),
        TradingPost.tokenAfterTwoTokens(newId()),
        TradingPost.goldWorthTwoTokens(newId()),
        TradingPost.fivePoints(newId()),
        TradingPost.onePointForEachShield(newId())
    };
  }

  ///////////////////////////////// HELPER FUNCTIONS /////////////////////////////////

  private BaseDevelopmentCard level1BaseCard(ResourceColor color, String cost, int points) {
    return new BaseDevelopmentCard(new Cost(cost), color, points, CardLevel.ONE, newId());
  }

  private BaseDevelopmentCard level2BaseCard(ResourceColor color, String cost, int points) {
    return new BaseDevelopmentCard(new Cost(cost), color, points, CardLevel.TWO, newId());
  }

  private BaseDevelopmentCard level3BaseCard(ResourceColor color, String cost, int points) {
    return new BaseDevelopmentCard(new Cost(cost), color, points, CardLevel.THREE, newId());
  }

  private int newId() {
    return generator.getNext();
  }
}
