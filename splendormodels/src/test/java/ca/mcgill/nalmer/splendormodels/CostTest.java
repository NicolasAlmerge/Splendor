package ca.mcgill.nalmer.splendormodels;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public final class CostTest {
  private final Cost cost1 = new Cost(Map.of(
    ResourceColor.RED, 3,
    ResourceColor.BLUE, 4,
    ResourceColor.BLACK, 2
  ));
  private final Cost cost2 = new Cost("04032");
  private final Cost cost3 = new Cost("00121");

  @Test
  public void testGoldFail() {
    Assert.assertThrows(RuntimeException.class, () -> new Cost(Map.of(ResourceColor.GOLD, 1)));
  }

  @Test
  public void testAllColorValues() {
    ResourceColor[] expected = new ResourceColor[] {
        ResourceColor.WHITE, ResourceColor.BLUE, ResourceColor.GREEN,
        ResourceColor.RED, ResourceColor.BLACK
    };
    Assert.assertArrayEquals(expected, Cost.getAllPossibleColors());
  }

  @Test
  public void testCostAccess1() {
    Assert.assertEquals(3, cost1.getCost(ResourceColor.RED));
    Assert.assertEquals(4, cost1.getCost(ResourceColor.BLUE));
    Assert.assertEquals(2, cost1.getCost(ResourceColor.BLACK));
    Assert.assertEquals(0, cost1.getCost(ResourceColor.GREEN));
    Assert.assertEquals(0, cost1.getCost(ResourceColor.WHITE));
  }

  @Test
  public void testCostAccess2() {
    Assert.assertEquals(3, cost2.getCost(ResourceColor.RED));
    Assert.assertEquals(4, cost2.getCost(ResourceColor.BLUE));
    Assert.assertEquals(2, cost2.getCost(ResourceColor.BLACK));
    Assert.assertEquals(0, cost2.getCost(ResourceColor.GREEN));
    Assert.assertEquals(0, cost2.getCost(ResourceColor.WHITE));
    Assert.assertEquals(0, cost2.getCost(ResourceColor.GOLD));
  }

  @Test
  public void testInvalidStrings() {
    Assert.assertThrows(RuntimeException.class, () -> new Cost("0000"));
    Assert.assertThrows(RuntimeException.class, () -> new Cost("000000"));
    Assert.assertThrows(RuntimeException.class, () -> new Cost("00A00"));
  }

  @Test
  public void checkEquals() {
    Assert.assertNotEquals(cost1, null);
    Assert.assertNotEquals(cost1, cost3);
    Assert.assertNotEquals(cost1, "04032");
    Assert.assertEquals(cost1, cost2);
  }
}
