package splendor.model;

import ca.mcgill.nalmer.lsutilities.model.AutoRefreshOauthToken;
import ca.mcgill.nalmer.lsutilities.model.OauthToken;

/**
 * Represents the connected user's data.
 */
public final class UserData {
  private static AutoRefreshOauthToken token = null;
  private static String username = "";

  /**
   * Private constructor.
   */
  private UserData() {
  }

  /**
   * Gets the user's username.
   *
   * @return User's username.
   */
  public static String getUsername() {
    return username;
  }

  /**
   * Gets the OAuth token (refreshes it if expired).
   *
   * @return User's OAuth token.
   */
  public static OauthToken getToken() {
    if (token == null) {
      throw new RuntimeException("User not connected");
    }
    return token.getOauthToken();
  }

  /**
   * Checks if there is user data.
   *
   * @return True if there is user data, false otherwise.
   */
  public static boolean isConnected() {
    return token != null;
  }

  /**
   * Connects a new user given a username and token.
   *
   * @param newUsername User's username.
   * @param newToken    User's OAuth token.
   */
  public static void connect(String newUsername, OauthToken newToken) {
    token = new AutoRefreshOauthToken(newToken);
    username = newUsername;
  }

  /**
   * Connects a new user given a username and password.
   *
   * @param newUsername User's username.
   * @param newPassword User's password.
   */
  public static void connect(String newUsername, String newPassword) {
    token = new AutoRefreshOauthToken(newUsername, newPassword);
    username = newUsername;
  }

  /**
   * Disconnects the current user (clears all data).
   */
  public static void disconnect() {
    token = null;
    username = "";
  }
}
