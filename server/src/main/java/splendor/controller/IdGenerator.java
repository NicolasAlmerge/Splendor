package splendor.controller;

/**
 * Utility class to generate unique incremental ids.
 */
final class IdGenerator {
  private int id = 0;

  /**
   * Constructor. First ID is 0.
   */
  public IdGenerator() {
  }

  /**
   * Constructor. First ID is startID.
   *
   * @param startId ID to start with.
   */
  public IdGenerator(int startId) {
    id = startId;
  }

  /**
   * Gets the next id.
   *
   * @return Next id.
   */
  public int getNext() {
    return id++; // Return id and then increment it
  }
}
