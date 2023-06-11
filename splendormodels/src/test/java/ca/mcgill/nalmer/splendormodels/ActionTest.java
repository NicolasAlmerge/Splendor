package ca.mcgill.nalmer.splendormodels;

import org.junit.Assert;
import org.junit.Test;

public final class ActionTest {
  private final Cost fakeCost = new Cost("00000");
  @Test
  public void testId1() {
    checkId(0);
  }

  @Test
  public void testId2() {
    checkId(3125);
  }

  private void checkId(int id) {
    Assert.assertEquals(id, new TakeTokensAction(fakeCost, id).getId());
  }
}
