package ca.mcgill.nalmer.lsutilities.controller;

/**
 * Represents a helper to check LS attributes.
 */
final class LsChecker {

  /**
   * Throws a {@link NullPointerException} if any element is null.
   *
   * @param elements Elements to check.
   * @throws NullPointerException if at least one of the <code>elements</code> is null.
   */
  public static void allNotNull(Object... elements) {
    for (int i = 0; i < elements.length; ++i) {
      if (elements[i] == null) {
        throw new NullPointerException("argument " + (i + 1) + " cannot be null");
      }
    }
  }
}
