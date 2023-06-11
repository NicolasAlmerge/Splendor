package ca.mcgill.nalmer.splendormodels;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * Represents a cost to pay.
 */
public final class Cost {
  private static final ResourceColor[] ALL_VALUES = {
      ResourceColor.WHITE, ResourceColor.BLUE, ResourceColor.GREEN,
      ResourceColor.RED, ResourceColor.BLACK
  };
  private final Map<ResourceColor, Integer> values = new EnumMap<>(ResourceColor.class);

  /**
   * Constructor. This creates a cost with all values set to <code>0</code>.
   */
  public Cost() {
    for (ResourceColor c : ResourceColor.values()) {
      values.put(c, 0);
    }
  }

  /**
   * Constructor.
   *
   * @param cost The cost in terms of token colours.
   * @throws NullPointerException if <code>cost</code> is <code>null</code>.
   * @throws RuntimeException     if <code>cost</code> contains a non-zero value for
   *                              {@link ResourceColor#GOLD}, or contains negative or null values
   *                              for any value.
   */
  public Cost(Map<ResourceColor, Integer> cost) {
    if (cost == null) {
      throw new NullPointerException("cost cannot be null");
    }

    // Check that all costs are non-null and non-negative
    for (ResourceColor color : cost.keySet()) {
      Integer value = cost.get(color);
      if (value == null || value < 0) {
        throw new RuntimeException("cost values cannot be null or negative");
      }
    }

    // Cost should not contain gold tokens
    if (cost.get(ResourceColor.GOLD) != null && cost.get(ResourceColor.GOLD) != 0) {
      throw new RuntimeException("cost cannot contain gold tokens");
    }

    // Fill in values
    for (ResourceColor color : cost.keySet()) {
      values.put(color, cost.get(color));
    }

    // Fill the rest with 0s
    for (ResourceColor c : ResourceColor.values()) {
      values.putIfAbsent(c, 0);
    }
  }

  /**
   * Constructor.
   *
   * @param cost String representing the cost ({@link ResourceColor#WHITE},
   *             {@link ResourceColor#BLUE}, {@link ResourceColor#GREEN}, {@link ResourceColor#RED},
   *             {@link ResourceColor#BLACK}).
   * @throws NullPointerException if <code>cost</code> is <code>null</code>.
   * @throws RuntimeException     if <code>cost</code> has an invalid format (should only be 5
   *                              digits).
   */
  public Cost(String cost) {
    if (cost == null) {
      throw new NullPointerException("cost cannot be null");
    }

    // Make sure cost has the appropriate length
    if (cost.length() != ALL_VALUES.length) {
      throw new RuntimeException("cost string invalid length");
    }

    // Read character one by one
    for (int i = 0; i < ALL_VALUES.length; ++i) {
      values.put(ALL_VALUES[i], readChar(cost, i));
    }

    // No gold tokens
    values.put(ResourceColor.GOLD, 0);
  }

  /**
   * Get all the possible colours for a cost (all colours except {@link ResourceColor#GOLD}).
   *
   * @return Array of all possible colours.
   */
  public static ResourceColor[] getAllPossibleColors() {
    return Arrays.copyOf(ALL_VALUES, ALL_VALUES.length);
  }

  private static int readChar(String stringCost, int index) throws RuntimeException {
    try {
      return Integer.parseInt(String.valueOf(stringCost.charAt(index)));
    } catch (NumberFormatException ignored) {
      throw new RuntimeException("cost string invalid numbers");
    }
  }

  /**
   * Returns the cost of a particular color.
   *
   * @param color Color to get the cost from.
   * @return Integer representing how many items of this colour are needed.
   * @throws NullPointerException if <code>color</code> is <code>null</code>.
   */
  public int getCost(ResourceColor color) {
    if (color == null) {
      throw new NullPointerException("color cannot be null");
    }
    return values.get(color);
  }

  /**
   * Checks if two {@link Cost} are equal.
   *
   * @param object Object to compare with.
   * @return <code>true</code> if {@link Cost} are equal, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }

    if (object instanceof Cost otherCost) {
      return values.equals(otherCost.values);
    }

    return false;
  }
}
