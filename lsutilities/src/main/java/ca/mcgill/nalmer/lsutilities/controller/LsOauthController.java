package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.model.OauthToken;
import ca.mcgill.nalmer.lsutilities.model.UserRole;

/**
 * LS OAuth Resources Controller. Corresponds to the <code>'/oauth'</code> route.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class LsOauthController {
  /**
   * Private constructor.
   */
  private LsOauthController() {}

  /**
   * Gets the {@link OauthToken} for a given username and password.
   * Corresponds to <code>POST '/oauth/token'</code>.
   *
   * @param username Username.
   * @param password Password.
   * @return {@link OauthToken} for the corresponding user.
   * @throws NullPointerException if <code>username</code> or <code>password</code> is
   *                              <code>null</code>.
   */
  public static OauthToken getOauthToken(String username, String password) {
    LsChecker.allNotNull(username, password);
    return LsRequest
        .post("/oauth/token")
        .addUserCredentials(username, password)
        .asObject(OauthToken.class);
  }

  /**
   * Refreshes the {@link OauthToken} of a refresh token.
   * Corresponds to <code>POST '/oauth/token'</code>.
   *
   * @param refreshToken Refresh token.
   * @return New {@link OauthToken}.
   * @throws NullPointerException if <code>refreshToken</code> is <code>null</code>.
   */
  public static OauthToken refreshOauthToken(String refreshToken) {
    LsChecker.allNotNull(refreshToken);
    return LsRequest
        .post("/oauth/token")
        .addRefreshTokenCredentials(refreshToken)
        .asObject(OauthToken.class);
  }

  /**
   * Refreshes the {@link OauthToken} of a {@link OauthToken}.
   * Corresponds to <code>POST '/oauth/token'</code>.
   *
   * @param token {@link OauthToken} to refresh.
   * @return New {@link OauthToken}.
   * @throws NullPointerException if <code>token</code> is <code>null</code>.
   */
  public static OauthToken refreshOauthToken(OauthToken token) {
    LsChecker.allNotNull(token);
    return refreshOauthToken(token.getRefreshToken());
  }

  /**
   * Gets the user role given an access token.
   * Corresponds to <code>GET '/oauth/role'</code>.
   *
   * @param accessToken Access token.
   * @return User's role.
   * @throws NullPointerException if <code>accessToken</code> is <code>null</code>.
   */
  public static UserRole getRole(String accessToken) {
    LsChecker.allNotNull(accessToken);
    return LsRequest
        .get("/oauth/role")
        .addAccessToken(accessToken)
        .asObject(UserRoleContainer[].class)[0].getRole();
  }

  /**
   * Gets the user role given an {@link OauthToken}.
   * Corresponds to <code>GET '/oauth/role'</code>.
   *
   * @param token OAuth token.
   * @return User's role.
   * @throws NullPointerException if <code>token</code> is <code>null</code>.
   */
  public static UserRole getRole(OauthToken token) {
    LsChecker.allNotNull(token);
    return getRole(token.getAccessToken());
  }

  /**
   * Gets the user's username given an access token.
   * Corresponds to <code>GET '/oauth/username'</code>.
   *
   * @param accessToken Access token.
   * @return User's username.
   * @throws NullPointerException if <code>accessToken</code> is <code>null</code>.
   */
  public static String getUsername(String accessToken) {
    LsChecker.allNotNull(accessToken);
    return LsRequest
        .get("/oauth/username")
        .addAccessToken(accessToken)
        .asString()
        .getBody();
  }

  /**
   * Gets the user's username given an {@link OauthToken}.
   * Corresponds to <code>GET '/oauth/username'</code>.
   *
   * @param token Access token.
   * @return User's username.
   * @throws NullPointerException if <code>token</code> is <code>null</code>.
   */
  public static String getUsername(OauthToken token) {
    LsChecker.allNotNull(token);
    return getUsername(token.getAccessToken());
  }

  /**
   * Deletes an access token.
   * Corresponds to <code>DELETE '/oauth/active'</code>.
   *
   * @param accessToken Access token.
   * @throws NullPointerException if <code>accessToken</code> is <code>null</code>.
   */
  public static void deleteOauthToken(String accessToken) {
    LsChecker.allNotNull(accessToken);
    LsRequest
        .delete("/oauth/active")
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Deletes an {@link OauthToken}.
   * Corresponds to <code>DELETE '/oauth/active'</code>.
   *
   * @param token Access token.
   * @throws NullPointerException if <code>token</code> is <code>null</code>.
   */
  public static void deleteOauthToken(OauthToken token) {
    LsChecker.allNotNull(token);
    deleteOauthToken(token.getAccessToken());
  }

  /**
   * Represents a user role container.
   */
  private static final class UserRoleContainer {
    private final UserRole authority;

    /**
     * Constructor.
     *
     * @param role User role.
     */
    public UserRoleContainer(UserRole role) {
      authority = role;
    }

    /**
     * Gets the role.
     *
     * @return User role.
     */
    public UserRole getRole() {
      return authority;
    }
  }
}
