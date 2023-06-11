package ca.mcgill.nalmer.splendormodels;

import org.junit.Assert;
import org.junit.Test;

public final class NobleTest {
  private final Noble fakeNoble = new Noble(new Cost("03303"), 76);

  @Test
  public void checkNumberOfPoints() {
    Assert.assertEquals(3, fakeNoble.getPoints());
  }
}
