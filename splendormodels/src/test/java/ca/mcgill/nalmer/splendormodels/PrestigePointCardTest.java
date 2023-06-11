package ca.mcgill.nalmer.splendormodels;

import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public final class PrestigePointCardTest {
  private final PrestigeCard fakeBase = new BaseDevelopmentCard(
      new Cost("01234"), ResourceColor.BLUE, 3, CardLevel.TWO, 5
  );

  private final PrestigeCard
      fakeOrient = OrientDevelopmentCard.twoGoldBonuses(new Cost("00053"), 98);

  @Test
  public void checkCardData1() {
    Cost c = new Cost(Map.of(
        ResourceColor.BLUE, 1, ResourceColor.GREEN, 2, ResourceColor.RED, 3,
        ResourceColor.BLACK, 4
    ));
    Assert.assertEquals(fakeBase.getCost(), c);
    Assert.assertEquals(fakeBase.getPrestigeCardType(), PrestigeCardType.BASE);
    Assert.assertEquals(fakeBase.getId(), 5);
    Assert.assertEquals(fakeBase.getPoints(), 3);
  }

  @Test
  public void checkCardData2() {
    Cost c = new Cost(Map.of(ResourceColor.RED, 5, ResourceColor.BLACK, 3));
    Assert.assertEquals(fakeOrient.getCost(), c);
    Assert.assertEquals(fakeOrient.getPrestigeCardType(), PrestigeCardType.ORIENT);
    Assert.assertEquals(fakeOrient.getId(), 98);
    Assert.assertEquals(fakeOrient.getPoints(), 0);
  }

  @Test
  public void checkEquals() {
    Assert.assertNotEquals(fakeBase, null);
    Assert.assertNotEquals(fakeBase, fakeOrient);
    Assert.assertNotEquals(fakeBase, "");
    Assert.assertEquals(fakeBase, fakeBase);
  }
}
