package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a card or token color. The values are {@link ResourceColor#WHITE},
 * {@link ResourceColor#BLUE}, {@link ResourceColor#GREEN}, {@link ResourceColor#RED},
 * {@link ResourceColor#BLACK} and {@link ResourceColor#GOLD}.
 */
public enum ResourceColor {
  /**
   * White color.
   */
  WHITE,

  /**
   * Blue color.
   */
  BLUE,

  /**
   * Green color.
   */
  GREEN,

  /**
   * Red color.
   */
  RED,

  /**
   * Black color.
   */
  BLACK,

  /**
   * Gold color (joker).
   */
  GOLD;

  /**
   * Checks whether the color is the joker token ({@link ResourceColor#GOLD}).
   *
   * @return <code>true</code> if color is {@link ResourceColor#GOLD}, <code>false</code> otherwise.
   */
  public boolean isJoker() {
    return ordinal() == GOLD.ordinal();
  }
}
