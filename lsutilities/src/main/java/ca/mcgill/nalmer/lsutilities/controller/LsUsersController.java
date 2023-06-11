package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.model.OauthToken;
import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import com.mashape.unirest.http.HttpResponse;

/**
 * LS Users Controller. Corresponds to the <code>'/api/users'</code> and <code>'/api/online'</code>
 * routes.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class LsUsersController {
  /**
   * Private constructor.
   */
  private LsUsersController() {}

  /**
   * Gets the number of {@link User}.
   * Corresponds to <code>GET '/api/online'</code>.
   *
   * @return Number of registered {@link User}.
   */
  public static int getNumberOfUsers() {
    HttpResponse<String> response = LsRequest.get("/api/online").asString();
    return Integer.parseInt(response.getBody().replaceAll("\\D", ""));
  }

  /**
   * Gets all the {@link User}.
   * Corresponds to <code>GET '/api/users'</code>.
   *
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required).
   * @return Array of all {@link User}.
   * @throws NullPointerException if <code>accessToken</code> is <code>null</code>.
   */
  public static User[] getAllUsers(String accessToken) {
    LsChecker.allNotNull(accessToken);
    return LsRequest
        .get("/api/users")
        .addAccessToken(accessToken)
        .asObject(User[].class);
  }

  /**
   * Gets all the {@link User}.
   * Corresponds to <code>GET '/api/users'</code>.
   *
   * @param token Access token ({@link UserRole#ROLE_ADMIN} required).
   * @return Array of all {@link User}.
   * @throws NullPointerException if <code>token</code> is <code>null</code>.
   */
  public static User[] getAllUsers(OauthToken token) {
    LsChecker.allNotNull(token);
    return getAllUsers(token.getAccessToken());
  }

  /**
   * Gets all the {@link User} information from a specific username.
   * Corresponds to <code>GET '/api/users/{user}'</code>.
   *
   * @param username    Username to query.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                    specified user).
   * @return {@link User} information.
   * @throws NullPointerException if <code>username</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static User getUser(String username, String accessToken) {
    LsChecker.allNotNull(username, accessToken);
    return LsRequest
        .get("/api/users/" + username)
        .addAccessToken(accessToken)
        .asObject(User.class);
  }

  /**
   * Gets all the {@link User} information from a specific username.
   * Corresponds to GET <code>'/api/users/{user}'</code>.
   *
   * @param username Username to query.
   * @param token    Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                 specified user).
   * @return User information.
   * @throws NullPointerException if <code>username</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static User getUser(String username, OauthToken token) {
    LsChecker.allNotNull(username, token);
    return getUser(username, token.getAccessToken());
  }

  /**
   * Adds a new {@link User}.
   * Corresponds to <code>PUT '/api/users/{user}'</code>.
   *
   * @param username       Username.
   * @param password       Password (plain text).
   * @param preferredColor Preferred color.
   * @param role           User role.
   * @param accessToken    Access token ({@link UserRole#ROLE_ADMIN} required).
   * @throws NullPointerException if <code>username</code>, <code>password</code>,
   *                              <code>preferredColor</code>, <code>role</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void addUser(String username, String password, String preferredColor, UserRole role,
                             String accessToken) {
    LsChecker.allNotNull(username, password, preferredColor, role, accessToken);
    User user = new User(username, password, preferredColor, role);
    LsRequest
        .put("/api/users/" + user.getName())
        .addJsonAppHeader()
        .addAccessToken(accessToken)
        .addBody(user)
        .execute();
  }

  /**
   * Adds a new {@link User}.
   * Corresponds to <code>PUT '/api/users/{user}'</code>.
   *
   * @param username       Username.
   * @param password       Password (plain text).
   * @param preferredColor Preferred color.
   * @param role           User role.
   * @param token          Access token ({@link UserRole#ROLE_ADMIN} required).
   * @throws NullPointerException if <code>username</code>, <code>password</code>,
   *                              <code>preferredColor</code>, <code>role</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void addUser(String username, String password, String preferredColor, UserRole role,
                             OauthToken token) {
    LsChecker.allNotNull(username, password, preferredColor, role, token);
    addUser(username, password, preferredColor, role, token.getAccessToken());
  }

  /**
   * Removes a {@link User}.
   * Corresponds to <code>DELETE '/api/users/{username}'</code>.
   *
   * @param username    Username to delete.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, but users cannot delete
   *                    themselves, even admins).
   * @throws NullPointerException if <code>username</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void deleteUser(String username, String accessToken) {
    LsChecker.allNotNull(username, accessToken);
    LsRequest
        .delete("/api/users/" + username)
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Removes a {@link User}.
   * Corresponds to <code>DELETE '/api/users/{user}'</code>.
   *
   * @param user        {@link User} to delete.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, but users cannot delete
   *                    themselves, even admins).
   * @throws NullPointerException if <code>user</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void deleteUser(User user, String accessToken) {
    LsChecker.allNotNull(user, accessToken);
    deleteUser(user.getName(), accessToken);
  }

  /**
   * Removes a {@link User}.
   * Corresponds to <code>DELETE '/api/users/{username}'</code>.
   *
   * @param username Username to delete.
   * @param token    Access token ({@link UserRole#ROLE_ADMIN} required, but users cannot delete
   *                 themselves, even admins).
   * @throws NullPointerException if <code>username</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void deleteUser(String username, OauthToken token) {
    LsChecker.allNotNull(username, token);
    deleteUser(username, token.getAccessToken());
  }

  /**
   * Removes a {@link User}.
   * Corresponds to <code>DELETE '/api/users/{user}'</code>.
   *
   * @param user  {@link User} to delete.
   * @param token Access token ({@link UserRole#ROLE_ADMIN} required, but users cannot delete
   *              themselves, even admins).
   * @throws NullPointerException if <code>user</code> or <code>token</code> is <code>null</code>.
   */
  public static void deleteUser(User user, OauthToken token) {
    LsChecker.allNotNull(user, token);
    deleteUser(user.getName(), token.getAccessToken());
  }

  /**
   * Updates a {@link User}'s password.
   * Corresponds to <code>POST '/api/users/{username}/password'</code>.
   *
   * @param username    Username to change password from.
   * @param oldPass     Old password (not verified if admin token specified).
   * @param newPass     New password.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                    specified user).
   * @throws NullPointerException if <code>username</code>, <code>oldPass</code>,
   *                              <code>newPass</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void updatePassword(String username, String oldPass, String newPass,
                                    String accessToken) {
    LsChecker.allNotNull(username, oldPass, newPass, accessToken);
    LsRequest
        .post("/api/users/" + username + "/password")
        .addJsonAppHeader()
        .addAccessToken(accessToken)
        .addBody(new PasswordChanger(oldPass, newPass))
        .execute();
  }

  /**
   * Updates a {@link User}'s password.
   * Corresponds to <code>POST '/api/users/{username}/password'</code>.
   *
   * @param username Username to change password from.
   * @param oldPass  Old password (not verified if admin token specified).
   * @param newPass  New password.
   * @param token    Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                 specified user).
   * @throws NullPointerException if <code>username</code>, <code>oldPass</code>,
   *                              <code>newPass</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void updatePassword(String username, String oldPass, String newPass,
                                    OauthToken token) {
    LsChecker.allNotNull(username, oldPass, newPass, token);
    updatePassword(username, oldPass, newPass, token.getAccessToken());
  }

  /**
   * Updates a {@link User}'s password.
   * Corresponds to <code>POST '/api/users/{user}/password'</code>.
   *
   * @param user        {@link User} to change password from.
   * @param oldPass     Old password (not verified if admin token specified).
   * @param newPass     New password.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                    specified user).
   * @throws NullPointerException if <code>user</code>, <code>oldPass</code>,
   *                              <code>newPass</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void updatePassword(User user, String oldPass, String newPass, String accessToken) {
    LsChecker.allNotNull(user, oldPass, newPass, accessToken);
    updatePassword(user.getName(), oldPass, newPass, accessToken);
  }

  /**
   * Updates a {@link User}'s password.
   * Corresponds to <code>POST '/api/users/{user}/password'</code>.
   *
   * @param user    {@link User} to change password from.
   * @param oldPass Old password (not verified if admin token specified).
   * @param newPass New password.
   * @param token   Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                specified user).
   * @throws NullPointerException if <code>user</code>, <code>oldPass</code>,
   *                              <code>newPass</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void updatePassword(User user, String oldPass, String newPass, OauthToken token) {
    LsChecker.allNotNull(user, oldPass, newPass, token);
    updatePassword(user.getName(), oldPass, newPass, token.getAccessToken());
  }

  /**
   * Updates a {@link User}'s password - no need to specify the old password, so an admin role is
   * required.
   * Corresponds to <code>POST '/api/users/{username}/password'</code>.
   *
   * @param username    Username to change password from.
   * @param newPass     New password.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required).
   * @throws NullPointerException if <code>username</code>, <code>newPass</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void setNewPassword(String username, String newPass, String accessToken) {
    LsChecker.allNotNull(username, newPass, accessToken);
    updatePassword(username, "", newPass, accessToken);
  }

  /**
   * Updates a {@link User}'s password - no need to specify the old password, so an admin role is
   * required.
   * Corresponds to <code>POST '/api/users/{username}/password'</code>.
   *
   * @param username Username to change password from.
   * @param newPass  New password.
   * @param token    Access token ({@link UserRole#ROLE_ADMIN} required).
   * @throws NullPointerException if <code>username</code>, <code>newPass</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void setNewPassword(String username, String newPass, OauthToken token) {
    LsChecker.allNotNull(username, newPass, token);
    setNewPassword(username, newPass, token.getAccessToken());
  }

  /**
   * Updates a {@link User}'s password - no need to specify the old password, so an admin role is
   * required.
   * Corresponds to <code>POST '/api/users/{user}/password'</code>.
   *
   * @param user        {@link User} to change password from.
   * @param newPass     New password.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required).
   * @throws NullPointerException if <code>user</code>, <code>newPass</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void setNewPassword(User user, String newPass, String accessToken) {
    LsChecker.allNotNull(user, newPass, accessToken);
    setNewPassword(user.getName(), newPass, accessToken);
  }

  /**
   * Updates a {@link User}'s password - no need to specify the old password, so an admin role is
   * required.
   * Corresponds to <code>POST '/api/users/{user}/password'</code>.
   *
   * @param user    {@link User} to change password from.
   * @param newPass New password.
   * @param token   Access token ({@link UserRole#ROLE_ADMIN} required).
   * @throws NullPointerException if <code>user</code>, <code>newPass</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void setNewPassword(User user, String newPass, OauthToken token) {
    LsChecker.allNotNull(user, newPass, token);
    setNewPassword(user.getName(), newPass, token.getAccessToken());
  }

  /**
   * Gets a {@link User}'s favourite colour.
   * Corresponds to <code>GET '/api/users/{username}/colour'</code>.
   *
   * @param username    Username to get the favourite colour from.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                    specified user).
   * @return User's favourite colour.
   * @throws NullPointerException if <code>username</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String getFavouriteColour(String username, String accessToken) {
    LsChecker.allNotNull(username, accessToken);
    return LsRequest
        .get("/api/users/" + username + "/colour")
        .addAccessToken(accessToken)
        .asObject(ColourChanger.class)
        .getColour();
  }

  /**
   * Gets a {@link User}'s favourite colour.
   * Corresponds to <code>GET '/api/users/{username}/colour'</code>.
   *
   * @param username Username to get the favourite colour from.
   * @param token    Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                 specified user).
   * @return User's favourite colour.
   * @throws NullPointerException if <code>username</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String getFavouriteColour(String username, OauthToken token) {
    LsChecker.allNotNull(username, token);
    return getFavouriteColour(username, token.getAccessToken());
  }

  /**
   * Gets a {@link User}'s favourite colour.
   * Corresponds to <code>GET '/api/users/{user}/colour'</code>.
   *
   * @param user        {@link User} to get the favourite colour from.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                    specified user).
   * @return User's favourite colour.
   * @throws NullPointerException if <code>user</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String getFavouriteColour(User user, String accessToken) {
    LsChecker.allNotNull(user, accessToken);
    return getFavouriteColour(user.getName(), accessToken);
  }

  /**
   * Gets a {@link User}'s favourite colour.
   * Corresponds to <code>GET '/api/users/{user}/colour'</code>.
   *
   * @param user  {@link User} to get the favourite colour from.
   * @param token Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *              specified user).
   * @return User's favourite colour.
   * @throws NullPointerException if <code>user</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String getFavouriteColour(User user, OauthToken token) {
    LsChecker.allNotNull(user, token);
    return getFavouriteColour(user.getName(), token.getAccessToken());
  }

  /**
   * Updates a {@link User}'s favourite colour.
   * Corresponds to <code>POST '/api/users/{username}/colour'</code>.
   *
   * @param username    Username to change the favourite colour from.
   * @param newColour   New colour to change.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                    specified user).
   * @throws NullPointerException if <code>username</code>, <code>newColour</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void updateFavouriteColour(String username, String newColour, String accessToken) {
    LsChecker.allNotNull(username, newColour, accessToken);
    LsRequest
        .post("/api/users/" + username + "/colour")
        .addJsonAppHeader()
        .addAccessToken(accessToken)
        .addBody(new ColourChanger(newColour))
        .execute();
  }

  /**
   * Updates a {@link User}'s favourite colour.
   * Corresponds to <code>POST '/api/users/{username}/colour'</code>.
   *
   * @param username  Username to change the favourite colour from.
   * @param newColour New colour to change.
   * @param token     Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                  specified user).
   * @throws NullPointerException if <code>username</code>, <code>newColour</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void updateFavouriteColour(String username, String newColour, OauthToken token) {
    LsChecker.allNotNull(username, newColour, token);
    updateFavouriteColour(username, newColour, token.getAccessToken());
  }

  /**
   * Updates a {@link User}'s favourite colour.
   * Corresponds to <code>POST '/api/users/{user}/colour'</code>.
   *
   * @param user        {@link User} to change the favourite colour from.
   * @param newColour   New colour to change.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                    specified user).
   * @throws NullPointerException if <code>user</code>, <code>newColour</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void updateFavouriteColour(User user, String newColour, String accessToken) {
    LsChecker.allNotNull(user, newColour, accessToken);
    updateFavouriteColour(user.getName(), newColour, accessToken);
  }

  /**
   * Updates a {@link User}'s favourite colour.
   * Corresponds to <code>POST '/api/users/{user}/colour'</code>.
   *
   * @param user      {@link User} to change the favourite colour from.
   * @param newColour New colour to change.
   * @param token     Access token ({@link UserRole#ROLE_ADMIN} required, or token of the
   *                  specified user).
   * @throws NullPointerException if <code>user</code>, <code>newColour</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void updateFavouriteColour(User user, String newColour, OauthToken token) {
    LsChecker.allNotNull(user, newColour, token);
    updateFavouriteColour(user.getName(), newColour, token.getAccessToken());
  }

  /**
   * Represents data to change password.
   */
  private static final class PasswordChanger {
    private final String oldPassword;
    private final String nextPassword;

    /**
     * Constructor.
     *
     * @param oldPass Old password value.
     * @param newPass New password value.
     */
    public PasswordChanger(String oldPass, String newPass) {
      oldPassword = oldPass;
      nextPassword = newPass;
    }

    /**
     * Gets the old password value.
     *
     * @return Old password.
     */
    public String getOldPassword() {
      return oldPassword;
    }

    /**
     * Gets the new password value.
     *
     * @return New password.
     */
    public String getNextPassword() {
      return nextPassword;
    }
  }

  /**
   * Represents data to change or get colour.
   */
  private static final class ColourChanger {
    private final String colour;

    /**
     * Constructor.
     *
     * @param color Colour value.
     */
    public ColourChanger(String color) {
      colour = color;
    }

    /**
     * Gets the colour value.
     *
     * @return Colour value.
     */
    public String getColour() {
      return colour;
    }
  }
}
