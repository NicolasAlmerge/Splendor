package ca.mcgill.nalmer.splendormodels;

import org.junit.Assert;
import org.junit.Test;

public final class BaseDevelopmentCardTest {
  @Test
  public void test1() {
    Assert.assertThrows(
        NullPointerException.class,
        () -> new BaseDevelopmentCard(new Cost("00010"), null, 0, CardLevel.ONE, 4)
    );
  }
}
