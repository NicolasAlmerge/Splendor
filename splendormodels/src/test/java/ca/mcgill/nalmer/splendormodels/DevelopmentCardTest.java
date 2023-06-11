package ca.mcgill.nalmer.splendormodels;

import org.junit.Assert;
import org.junit.Test;

public final class DevelopmentCardTest {
  private final DevelopmentCard fakeBase = new BaseDevelopmentCard(
      new Cost("01234"), ResourceColor.BLUE, 3, CardLevel.TWO, 5
  );

  private final DevelopmentCard fakeOrient1 = OrientDevelopmentCard.twoGoldBonuses(
      new Cost("00053"), 98
  );

  private final DevelopmentCard fakeOrient2 = OrientDevelopmentCard.bonusMatching(
      new Cost("00053"), 99
  );

  @Test
  public void checkBaseCard() {
    Assert.assertEquals(ResourceColor.BLUE, fakeBase.getColor());
    Assert.assertEquals(CardLevel.TWO, fakeBase.getLevel());
    Assert.assertThrows(
        RuntimeException.class,
        () -> fakeBase.changeColour(ResourceColor.BLACK)
    );
  }

  @Test
  public void checkOrientCard1() {
    Assert.assertEquals(ResourceColor.GOLD, fakeOrient1.getColor());
    Assert.assertEquals(CardLevel.ONE, fakeOrient1.getLevel());
    Assert.assertThrows(
        RuntimeException.class,
        () -> fakeOrient1.changeColour(ResourceColor.GREEN)
    );
  }

  @Test
  public void checkOrientCard2() {
    Assert.assertNull(fakeOrient2.getColor());
    Assert.assertEquals(CardLevel.ONE, fakeOrient2.getLevel());
    Assert.assertThrows(
        NullPointerException.class,
        () -> fakeOrient2.changeColour(null)
    );
    fakeOrient2.changeColour(ResourceColor.RED);
    Assert.assertEquals(ResourceColor.RED, fakeOrient2.getColor());
    Assert.assertThrows(
        RuntimeException.class,
        () -> fakeOrient2.changeColour(ResourceColor.BLUE)
    );
  }
}
