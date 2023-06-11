package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.data.LobbyServiceData;
import ca.mcgill.nalmer.lsutilities.model.LobbyServiceFetchException;
import ca.mcgill.nalmer.lsutilities.model.RequestType;
import com.mashape.unirest.http.HttpResponse;

/**
 * Utility class for LS requests.
 */
abstract class LsRequest {
  private final String subPath;
  private final RequestType reqType;
  private static final String AUTH_HEADER_VALUE = "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=";

  /**
   * Constructor.
   *
   * @param path Path to get, starting with '/'.
   * @param type Request type.
   */
  protected LsRequest(String path, RequestType type) {
    subPath = path;
    reqType = type;
  }

  /**
   * Creates a new get request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static LsGetRequest get(String subPath) {
    return new LsGetRequest(subPath);
  }

  /**
   * Creates a new put request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static LsNonGetRequest put(String subPath) {
    return LsNonGetRequest.putRequest(subPath);
  }

  /**
   * Creates a new post request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static LsNonGetRequest post(String subPath) {
    return LsNonGetRequest.postRequest(subPath);
  }

  /**
   * Creates a new delete request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static LsNonGetRequest delete(String subPath) {
    return LsNonGetRequest.deleteRequest(subPath);
  }

  /**
   * Gets the lobby service location.
   *
   * @return Lobby service location url.
   */
  public static String getLsLocation() {
    return LobbyServiceData.getLsLocation();
  }

  /**
   * Get the specified sub path.
   *
   * @return Specified sub path.
   */
  public final String getSubPath() {
    return subPath;
  }

  /**
   * Get the specified request type.
   *
   * @return Specified request type.
   */
  public final RequestType getRequestType() {
    return reqType;
  }

  /**
   * Adds the 'Content-Type: application/json' header.
   *
   * @return Self object for chaining.
   */
  public abstract LsRequest addJsonAppHeader();

  /**
   * Adds the authorization header and the 'grant_type=password',
   * 'username=username', 'password=password' request parameters.
   *
   * @param username Username.
   * @param password Password.
   * @return Self object for chaining.
   */
  public abstract LsRequest addUserCredentials(String username, String password);

  /**
   * Adds the authorization header and the 'grant_type=refresh_token',
   * 'refresh_token=refreshToken' request parameters.
   *
   * @param refreshToken Refresh token.
   * @return Self object for chaining.
   */
  public abstract LsRequest addRefreshTokenCredentials(String refreshToken);

  /**
   * Adds the 'access_token=token' request parameter.
   *
   * @param token Token to add access for.
   * @return Self object for chaining.
   */
  public abstract LsRequest addAccessToken(String token);

  /**
   * Get response as string. This method checks the error code to make sure it is 200 or 201.
   *
   * @return String response object.
   */
  public final HttpResponse<String> asString() throws RuntimeException {
    HttpResponse<String> request = getStringRequest();
    checkErrorCode(request);
    return request;
  }

  /**
   * Get response as an object.
   *
   * @param <T> Type parameter.
   * @param responseClass Class to model data for.
   * @return Response as object.
   */
  public abstract <T> T asObject(Class<? extends T> responseClass) throws RuntimeException;

  /**
   * Execute the request without returning anything.
   */
  public final void execute() throws RuntimeException {
    checkErrorCode(getStringRequest());
  }

  /**
   * Get string request. This method does not check the return code.
   *
   * @return Http response containing string data.
   * @throws RuntimeException if request failed.
   */
  public abstract HttpResponse<String> getStringRequest() throws RuntimeException;

  /**
   * Get the authorisation header value for user and token credentials.
   *
   * @return Header authorisation value.
   */
  protected static String getAuthHeaderValue() {
    return AUTH_HEADER_VALUE;
  }

  /**
   * Check the error code for a response.
   *
   * @param <T> Type parameter.
   * @param response Response to check.
   * @throws RuntimeException if error code is not 200 or 201.
   */
  protected final <T> void checkErrorCode(HttpResponse<T> response) throws RuntimeException {
    if (response.getStatus() != 200 && response.getStatus() != 201) {
      throw new LobbyServiceFetchException(
          reqType, getSubPath(), response.getStatus(),
          response.getBody().toString(), response.getStatusText()
      );
    }
  }
}
