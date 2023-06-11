package ca.mcgill.nalmer.lsutilities.model;

/**
 * Represents a user.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class User {
  private final String name;
  private final String password;
  private final String preferredColour;
  private final UserRole role;

  /**
   * Constructor.
   *
   * @param name Name of the player.
   * @param password Hashed password of the user.
   * @param preferredColour User's preferred color.
   * @param role Role of the user.
   */
  public User(String name, String password, String preferredColour, UserRole role) {
    this.name = name;
    this.password = password;
    this.preferredColour = preferredColour;
    this.role = role;
  }

  /**
   * Gets the player's name.
   *
   * @return The name of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the player's hashed password.
   *
   * @return The hashed password of the player.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Gets the player's preferred color.
   *
   * @return The player's preferred color.
   */
  public String getPreferredColor() {
    return preferredColour;
  }

  /**
   * Gets the player's role.
   *
   * @return The role of the player.
   */
  public UserRole getRole() {
    return role;
  }

  /**
   * Checks if two {@link User} are equal.
   *
   * @param other Other object to compare.
   * @return <code>true</code> if objects are equal, <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (other.getClass() != User.class) {
      return false;
    }
    User user = (User) other;
    return name.equals(user.getName());
  }
}
