package ca.mcgill.nalmer.lsutilities.model;

import ca.mcgill.nalmer.lsutilities.controller.LsOauthController;

/**
 * Utility class that will automatically refresh the token if it expired.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class AutoRefreshOauthToken {
  private OauthToken token;

  /**
   * Constructor. Will fetch a new token given a username and password.
   *
   * @param username User's username.
   * @param password User's password.
   * @throws NullPointerException if <code>username</code> or <code>password</code> is
   *                              <code>null</code>.
   */
  public AutoRefreshOauthToken(String username, String password) {
    token = LsOauthController.getOauthToken(username, password);
  }

  /**
   * Constructor. Will take this token as initial (must be a valid token, even if expired).
   *
   * @param token Initial token (assumed to be valid).
   * @throws NullPointerException if <code>token</code> is <code>null</code>.
   */
  public AutoRefreshOauthToken(OauthToken token) {
    if (token == null) {
      throw new NullPointerException("token cannot be null");
    }
    this.token = token;
  }

  /**
   * Get the oauth token. This function will refresh it if expired.
   *
   * @return OAuth token of the account.
   */
  public OauthToken getOauthToken() {
    // If token is expired, refresh it
    try {
      LsOauthController.getUsername(token);
    } catch (RuntimeException e) {
      token = LsOauthController.refreshOauthToken(token);
    }

    // Return token
    return token;
  }
}
