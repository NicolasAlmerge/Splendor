package splendor.controller;

import ca.mcgill.nalmer.splendormodels.CardLevel;
import org.junit.Assert;
import org.junit.Test;

public final class CardGeneratorTest {
  private final CardGenerator generator = new CardGenerator();

  @Test
  public void checkInvalidValues() {
    Assert.assertThrows(RuntimeException.class, () -> generator.getBaseDevelopmentCardAtLevel(null));
    Assert.assertThrows(RuntimeException.class, () -> generator.getOrientDevelopmentCardAtLevel(null));
  }
}
