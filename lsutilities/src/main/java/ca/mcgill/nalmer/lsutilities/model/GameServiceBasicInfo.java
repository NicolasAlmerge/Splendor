package ca.mcgill.nalmer.lsutilities.model;

/**
 * Represents basic information about a game service.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public class GameServiceBasicInfo {
  private final String name;
  private final String displayName;

  /**
   * Constructor.
   *
   * @param gsName Game service name.
   * @param gsDisplayName Game service display name.
   */
  public GameServiceBasicInfo(String gsName, String gsDisplayName) {
    name = gsName;
    displayName = gsDisplayName;
  }

  /**
   * Get the game service name.
   *
   * @return Game service name.
   */
  public final String getName() {
    return name;
  }

  /**
   * Get the game service display name.
   *
   * @return Game service display name.
   */
  public final String getDisplayName() {
    return displayName;
  }

  /**
   * Checks if two {@link GameServiceBasicInfo} objects are equal.
   *
   * @param other Other object to compare.
   * @return <code>true</code> if objects are equal, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other instanceof GameServiceBasicInfo otherInfo) {
      return name.equals(otherInfo.name) && displayName.equals(otherInfo.displayName);
    }
    return false;
  }
}
