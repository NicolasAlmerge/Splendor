package ca.mcgill.nalmer.lsutilities.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an oauth token.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class OauthToken {
  @SerializedName("access_token")
  private final String accessToken;

  @SerializedName("expires_in")
  private final int expiresIn;

  @SerializedName("refresh_token")
  private final String refreshToken;

  private final String scope;

  @SerializedName("token_type")
  private final String tokenType;

  /**
   * Constructor.
   *
   * @param token Access token.
   * @param expires Expires in (seconds).
   * @param refresh Refresh token.
   * @param tokenScope Token scope.
   * @param type Type of token.
   */
  public OauthToken(String token, int expires, String refresh, String tokenScope, String type) {
    accessToken = token;
    expiresIn = expires;
    refreshToken = refresh;
    scope = tokenScope;
    tokenType = type;
  }

  /**
   * Get the access token.
   *
   * @return Access token.
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Get the time it expires in.
   *
   * @return Expiring time.
   */
  public int getExpiresIn() {
    return expiresIn;
  }

  /**
   * Get the refresh token.
   *
   * @return Refresh token.
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Get the token scope.
   *
   * @return Token scope.
   */
  public String getScope() {
    return scope;
  }

  /**
   * Get the token type.
   *
   * @return Token type.
   */
  public String getTokenType() {
    return tokenType;
  }
}
