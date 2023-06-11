package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a card level. The values are {@link CardLevel#ONE}, {@link CardLevel#TWO} and
 * {@link CardLevel#THREE}.
 */
public enum CardLevel {
  /**
   * Level one card.
   */
  ONE,

  /**
   * Level two card.
   */
  TWO,

  /**
   * Level three card.
   */
  THREE;

  /**
   * Returns the integer representation of the {@link CardLevel}.
   *
   * @return <code>1</code> for {@link CardLevel#ONE}, <code>2</code> for {@link CardLevel#TWO} and
   *         <code>3</code> for {@link CardLevel#THREE}.
   */
  public int toInteger() {
    return ordinal() + 1;
  }
}
