package ca.mcgill.nalmer.splendormodels;

import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import java.util.EnumMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class PlayerTest {
  private final BaseDevelopmentCard bonus1 = new BaseDevelopmentCard(
      new Cost("11121"), ResourceColor.RED, 0, CardLevel.ONE, 1
  );
  private final OrientDevelopmentCard bonus2 = OrientDevelopmentCard.twoGoldBonuses(
      new Cost("00011"), 2
  );
  private OrientDevelopmentCard bonus3;
  private final BaseDevelopmentCard bonus4 = new BaseDevelopmentCard(
      new Cost("12211"), ResourceColor.BLUE, 3, CardLevel.TWO, 4
  );
  private final OrientDevelopmentCard bonus5 = OrientDevelopmentCard.twoGoldBonuses(
      new Cost("01100"), 5
  );
  private final Noble noble1 = new Noble(new Cost("03300"), 6);
  private final Noble noble2 = new Noble(new Cost("00033"), 7);
  private User user;
  private Player player;

  @Before
  public void reset() {
    user = new User("name", "pass", "FF0000", UserRole.ROLE_PLAYER);
    player = new Player(user);
    bonus3 = OrientDevelopmentCard.bonusMatching(new Cost("02100"), 3);
  }

  @Test
  public void testCanBuy() {
    Assert.assertFalse(player.canBuy(bonus2));
    player.addTokens(Map.of(ResourceColor.RED, 1));
    Assert.assertFalse(player.canBuy(bonus2));
    player.addTokens(Map.of(ResourceColor.BLACK, 2));
    Assert.assertTrue(player.canBuy(bonus2));
  }

  @Test
  public void testBuy1() {
    Assert.assertThrows(RuntimeException.class, () -> player.payAndAddBonus(bonus2));
    Map<ResourceColor, Integer> count = new EnumMap<>(ResourceColor.class);
    for (ResourceColor c : ResourceColor.values()) {
      count.put(c, 0);
    }
    Assert.assertEquals(count, player.getTokens());
    player.addTokens(Map.of(ResourceColor.RED, 1, ResourceColor.BLACK, 2));
    Assert.assertTrue(player.canBuy(bonus2));

    Map<ResourceColor, Integer> toPutBack = player.payAndAddBonus(bonus2);
    Assert.assertEquals(Map.of(
            ResourceColor.WHITE, 0,
            ResourceColor.RED, 1,
            ResourceColor.GREEN, 0,
            ResourceColor.BLUE, 0,
            ResourceColor.BLACK, 1,
            ResourceColor.GOLD, 0),
        toPutBack
    );
    Assert.assertEquals(Map.of(
            ResourceColor.WHITE, 0,
            ResourceColor.RED, 0,
            ResourceColor.GREEN, 0,
            ResourceColor.BLUE, 0,
            ResourceColor.BLACK, 1,
            ResourceColor.GOLD, 0),
        player.getTokens()
    );

    Assert.assertEquals(1, player.getBonuses().get(ResourceColor.GOLD).size());
    Assert.assertEquals(bonus2, player.getBonuses().get(ResourceColor.GOLD).get(0));
  }

  @Test
  public void testBuy2() {
    Map<ResourceColor, Integer> count = new EnumMap<>(ResourceColor.class);
    for (ResourceColor c : Cost.getAllPossibleColors()) {
      count.put(c, 1);
    }
    count.put(ResourceColor.GOLD, 2);

    player.addTokens(count);
    Assert.assertTrue(player.canBuy(bonus4));

    Map<ResourceColor, Integer> toPutBack = player.payAndAddBonus(bonus4);
    Assert.assertEquals(count, toPutBack);
    Assert.assertEquals(Map.of(
            ResourceColor.WHITE, 0,
            ResourceColor.RED, 0,
            ResourceColor.GREEN, 0,
            ResourceColor.BLUE, 0,
            ResourceColor.BLACK, 0,
            ResourceColor.GOLD, 0),
        player.getTokens()
    );

    Assert.assertEquals(1, player.getBonuses().get(ResourceColor.BLUE).size());
    Assert.assertEquals(bonus4, player.getBonuses().get(ResourceColor.BLUE).get(0));
  }

  @Test
  public void testBuy3() {
    Map<ResourceColor, Integer> count = new EnumMap<>(ResourceColor.class);
    for (ResourceColor c : Cost.getAllPossibleColors()) {
      count.put(c, 1);
    }
    count.put(ResourceColor.GOLD, 0);

    player.addTokens(count);
    player.addBonus(bonus2);
    Assert.assertTrue(player.canBuy(bonus4));

    Map<ResourceColor, Integer> toPutBack = player.payAndAddBonus(bonus4);
    Assert.assertEquals(count, toPutBack);
    Assert.assertEquals(Map.of(
            ResourceColor.WHITE, 0,
            ResourceColor.RED, 0,
            ResourceColor.GREEN, 0,
            ResourceColor.BLUE, 0,
            ResourceColor.BLACK, 0,
            ResourceColor.GOLD, 0),
        player.getTokens()
    );

    Assert.assertEquals(0, player.getBonuses().get(ResourceColor.GOLD).size());
    Assert.assertEquals(1, player.getBonuses().get(ResourceColor.BLUE).size());
    Assert.assertEquals(bonus4, player.getBonuses().get(ResourceColor.BLUE).get(0));
  }

  @Test
  public void testBuy4() {
    Map<ResourceColor, Integer> count = new EnumMap<>(ResourceColor.class);
    for (ResourceColor c : Cost.getAllPossibleColors()) {
      count.put(c, 1);
    }
    count.put(ResourceColor.GOLD, 0);
    count.put(ResourceColor.GREEN, 2);

    player.addTokens(count);
    player.addBonus(bonus2);
    Assert.assertTrue(player.canBuy(bonus4));

    Map<ResourceColor, Integer> toPutBack = player.payAndAddBonus(bonus4);
    Assert.assertEquals(count, toPutBack);
    Assert.assertEquals(Map.of(
            ResourceColor.WHITE, 0,
            ResourceColor.RED, 0,
            ResourceColor.GREEN, 0,
            ResourceColor.BLUE, 0,
            ResourceColor.BLACK, 0,
            ResourceColor.GOLD, 0),
        player.getTokens()
    );

    Assert.assertEquals(0, player.getBonuses().get(ResourceColor.GOLD).size());
    Assert.assertEquals(1, player.getBonuses().get(ResourceColor.BLUE).size());
    Assert.assertEquals(bonus4, player.getBonuses().get(ResourceColor.BLUE).get(0));
  }

  @Test
  public void testBuy5() {
    // User misses one green token, one white token and one red token but had two gold tokens
    Map<ResourceColor, Integer> count = new EnumMap<>(ResourceColor.class);
    for (ResourceColor c : ResourceColor.values()) {
      count.put(c, 1);
    }
    count.put(ResourceColor.GOLD, 2);
    count.put(ResourceColor.WHITE, 0);

    // We add two 2-gold bonus cards to the player
    player.addTokens(count);
    player.addBonus(bonus2);
    player.addBonus(bonus5);
    Assert.assertTrue(player.canBuy(bonus4));

    // Since the user misses three tokens, it should use one gold token and one gold card
    Map<ResourceColor, Integer> toPutBack = player.payAndAddBonus(bonus4);
    count.put(ResourceColor.GOLD, 1); // Player should only put back one gold token
    Assert.assertEquals(count, toPutBack);

    // Player should have one gold token left
    Assert.assertEquals(Map.of(
            ResourceColor.WHITE, 0,
            ResourceColor.RED, 0,
            ResourceColor.GREEN, 0,
            ResourceColor.BLUE, 0,
            ResourceColor.BLACK, 0,
            ResourceColor.GOLD, 1),
        player.getTokens()
    );

    // Player should only have one bonus card left
    Assert.assertEquals(1, player.getBonuses().get(ResourceColor.GOLD).size());
    Assert.assertEquals(bonus5, player.getBonuses().get(ResourceColor.GOLD).get(0));

    // Player should have one blue card
    Assert.assertEquals(1, player.getBonuses().get(ResourceColor.BLUE).size());
    Assert.assertEquals(bonus4, player.getBonuses().get(ResourceColor.BLUE).get(0));
  }

  @Test
  public void testTokens() {
    Map<ResourceColor, Integer> count = new EnumMap<>(ResourceColor.class);
    for (ResourceColor c : ResourceColor.values()) {
      count.put(c, 0);
    }
    Assert.assertEquals(count, player.getTokens());

    player.addTokens(Map.of(ResourceColor.RED, 2, ResourceColor.GREEN, 3));
    player.addTokens(Map.of(ResourceColor.RED, 3, ResourceColor.BLACK, 1));
    count.put(ResourceColor.RED, 5);
    count.put(ResourceColor.GREEN, 3);
    count.put(ResourceColor.BLACK, 1);
    Assert.assertEquals(count, player.getTokens());

    player.addOneGoldToken();
    player.addOneGoldToken();
    count.put(ResourceColor.GOLD, 2);
    Assert.assertEquals(count, player.getTokens());
    Assert.assertEquals(11, player.getNumberOfTokens());
  }

  @Test
  public void testNoblesReservation() {
    Assert.assertEquals(0, player.getReservedNobles().size());

    player.addReservedNobleCard(noble1);
    Assert.assertEquals(1, player.getReservedNobles().size());

    player.addReservedNobleCard(noble2);
    Assert.assertThrows(RuntimeException.class, () -> player.addReservedNobleCard(noble1));
    Assert.assertEquals(2, player.getReservedNobles().size());

    player.removeReservedNobleCard(noble1);
    Assert.assertEquals(1, player.getReservedNobles().size());

    player.removeReservedNobleCard(noble2);
    Assert.assertEquals(0, player.getReservedNobles().size());
  }

  @Test
  public void testReservedCards() {
    Assert.assertFalse(player.reservedCardsIsFull());
    Assert.assertEquals(0, player.getNumberOfReservedCards());
    Assert.assertEquals(0, player.getReservedCards().size());

    player.addReservedCard(bonus1);
    Assert.assertFalse(player.reservedCardsIsFull());
    Assert.assertEquals(1, player.getNumberOfReservedCards());

    player.addReservedCard(bonus2);
    Assert.assertFalse(player.reservedCardsIsFull());
    Assert.assertEquals(2, player.getNumberOfReservedCards());
    Assert.assertThrows(RuntimeException.class, () -> player.addReservedCard(bonus1));

    player.addReservedCard(bonus3);
    Assert.assertTrue(player.reservedCardsIsFull());
    Assert.assertEquals(3, player.getNumberOfReservedCards());
    Assert.assertThrows(RuntimeException.class, () -> player.addReservedCard(bonus4));

    player.removeReservedCard(bonus2);
    Assert.assertEquals(2, player.getNumberOfReservedCards());

    player.removeReservedCard(bonus1);
    Assert.assertEquals(1, player.getNumberOfReservedCards());

    player.removeReservedCard(bonus3);
    Assert.assertEquals(0, player.getNumberOfReservedCards());
  }

  @Test
  public void testUser() {
    Assert.assertEquals(user, player.getUser());
  }

  @Test
  public void testNumberOfPoints() {
    Assert.assertEquals(0, player.getPrestigePts());
    player.addBonus(bonus1);
    Assert.assertEquals(0, player.getPrestigePts());
    player.addBonus(bonus4);
    Assert.assertEquals(bonus4.getPoints(), player.getPrestigePts());
    player.addNoble(noble1);
    Assert.assertEquals(bonus4.getPoints() + noble1.getPoints(), player.getPrestigePts());
  }

  @Test
  public void testBonuses1() {
    Assert.assertEquals(ResourceColor.values().length, player.getBonuses().size());
    Assert.assertEquals(0, player.getNonEmptyColorBonuses().size());

    player.addBonus(bonus1);
    Assert.assertEquals(1, player.getNonEmptyColorBonuses().size());
    Assert.assertEquals(0, player.getNumberOfBonusesOfColor(ResourceColor.BLUE));
    Assert.assertEquals(0, player.getBonuses().get(ResourceColor.BLUE).size());
    Assert.assertNotNull(player.getBonusesCount().get(ResourceColor.BLUE));
    Assert.assertEquals(0, (int) player.getBonusesCount().get(ResourceColor.BLUE));

    Assert.assertEquals(1, player.getNumberOfBonusesOfColor(ResourceColor.RED));
    Assert.assertEquals(1, player.getBonuses().get(ResourceColor.RED).size());
    Assert.assertEquals(bonus1, player.getBonuses().get(ResourceColor.RED).get(0));
    Assert.assertNotNull(player.getBonusesCount().get(ResourceColor.RED));
    Assert.assertEquals(1, (int) player.getBonusesCount().get(ResourceColor.RED));
  }

  @Test
  public void testBonuses2() {
    bonus3.setColour(ResourceColor.RED);
    player.addBonus(bonus2);
    player.addBonus(bonus3);
    Assert.assertEquals(0, player.getNumberOfBonusesOfColor(ResourceColor.BLACK));
    Assert.assertEquals(1, player.getNumberOfBonusesOfColor(ResourceColor.RED));
    Assert.assertEquals(2, player.getNumberOfBonusesOfColor(ResourceColor.GOLD));
    player.removeOneBonus(ResourceColor.RED);
    Assert.assertEquals(0, player.getNumberOfBonusesOfColor(ResourceColor.RED));
    player.addBonus(bonus4);
    player.removeBonuses(ResourceColor.BLUE, 1);
    Assert.assertEquals(0, player.getNumberOfBonusesOfColor(ResourceColor.BLUE));
  }
}
