package ca.mcgill.nalmer.splendormodels;

/**
 * Represents a shield color. The values are {@link ShieldColor#YELLOW}, {@link ShieldColor#BLACK},
 * {@link ShieldColor#RED} and {@link ShieldColor#BLUE}.
 */
public enum ShieldColor {
  /**
   * Yellow color.
   */
  YELLOW,

  /**
   * Black color.
   */
  BLACK,

  /**
   * Red color.
   */
  RED,

  /**
   * Blue color.
   */
  BLUE;

  /**
   * Gets the {@link ShieldColor} at a specified index.
   *
   * @param index Index to get the {@link ShieldColor} from.
   * @return {@link ShieldColor} at index <code>index</code>.
   * @throws ArrayIndexOutOfBoundsException if <code>index</code> is invalid.
   */
  public static ShieldColor get(int index) {
    return values()[index];
  }
}
