package splendor.controller;

import ca.mcgill.nalmer.lsutilities.model.RequestType;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

/**
 * Represents an server post, put or delete request.
 */
final class ServerNonGetRequest extends ServerRequest {
  private final HttpRequestWithBody request;

  private ServerNonGetRequest(String path, HttpRequestWithBody req, RequestType type) {
    super(path, type);
    request = req;
  }

  static ServerNonGetRequest putRequest(String subPath) {
    return new ServerNonGetRequest(
        subPath, Unirest.put(getServerLocation() + subPath), RequestType.PUT
    );
  }

  static ServerNonGetRequest postRequest(String subPath) {
    return new ServerNonGetRequest(
        subPath, Unirest.post(getServerLocation() + subPath), RequestType.POST
    );
  }

  static ServerNonGetRequest deleteRequest(String subPath) {
    return new ServerNonGetRequest(
        subPath, Unirest.delete(getServerLocation() + subPath), RequestType.DELETE
    );
  }

  @Override
  public HttpResponse<String> getStringRequest() throws RuntimeException {
    try {
      return request.asString();
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T asObject(Class<? extends T> responseClass) throws RuntimeException {
    try {
      HttpResponse<String> response = request.asString();
      checkErrorCode(response);
      return getGson().fromJson(response.getBody(), responseClass);
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets the {@link GameExtension}.
   *
   * @param extension {@link GameExtension} to use.
   * @return Self-object for chaining.
   */
  public ServerNonGetRequest setExtension(GameExtension extension) {
    request.queryString("extension", extension.toString());
    return this;
  }

  /**
   * Sets the old session id.
   *
   * @param oldId old session id.
   * @return Self-object for chaining.
   */
  public ServerNonGetRequest setOldId(String oldId) {
    request.queryString("oldId", oldId);
    return this;
  }

  @Override
  public ServerNonGetRequest setGameId(String gameId) {
    request.routeParam("gameId", gameId);
    return this;
  }

  /**
   * Sets the action id.
   *
   * @param actionId Action id.
   * @return Self-object for chaining.
   */
  public ServerNonGetRequest setActionId(int actionId) {
    request.routeParam("actionId", String.valueOf(actionId));
    return this;
  }

  /**
   * Sets the session id.
   *
   * @param sessionId Session id.
   * @return Self-object for chaining.
   */
  public ServerNonGetRequest setSessionId(String sessionId) {
    request.routeParam("sessionId", sessionId);
    return this;
  }

  /**
   * Sets the city id.
   *
   * @param cityId Action id.
   * @return Self-object for chaining.
   */
  public ServerNonGetRequest setCityId(int cityId) {
    request.routeParam("cityId", String.valueOf(cityId));
    return this;
  }

  public ServerNonGetRequest setColor(String color) {
    request.routeParam("color", color);
    return this;
  }
}