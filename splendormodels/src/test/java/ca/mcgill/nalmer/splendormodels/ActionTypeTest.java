package ca.mcgill.nalmer.splendormodels;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

public final class ActionTypeTest {
  private final Cost fakeCost = new Cost("00000");
  private final DevelopmentCard fakeCard = new BaseDevelopmentCard(
      fakeCost, ResourceColor.BLUE, 0, CardLevel.ONE, 0
  );

  @Test
  public void checkActionType1() {
    Assert.assertEquals(new TakeTokensAction(fakeCost, 0).getType(), ActionType.TAKE_TOKENS);
  }

  @Test
  public void checkActionType2() {
    Assert.assertEquals(new ReserveCardAction(fakeCard, 0).getType(), ActionType.RESERVE_CARD);
  }
}
