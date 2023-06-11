package ca.mcgill.nalmer.splendormodels;

import org.junit.Assert;
import org.junit.Test;

public final class ResourceColorTest {
  @Test
  public void checkIsJoker() {
    for (ResourceColor color : ResourceColor.values()) {
      if (color == ResourceColor.GOLD) {
        Assert.assertTrue(color.isJoker());
      } else {
        Assert.assertFalse(color.isJoker());
      }
    }
  }
}
