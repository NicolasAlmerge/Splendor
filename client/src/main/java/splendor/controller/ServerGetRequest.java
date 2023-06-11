package splendor.controller;

import ca.mcgill.nalmer.lsutilities.model.RequestType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

/**
 * Represents a server Get request.
 */
final class ServerGetRequest extends ServerRequest {
  private final HttpRequest request;

  ServerGetRequest(String path) {
    super(path, RequestType.GET);
    request = Unirest.get(getServerLocation() + getSubPath());
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

  @Override
  public ServerGetRequest setGameId(String gameId) {
    request.routeParam("gameId", gameId);
    return this;
  }

  /**
   * Sets the hash parameter.
   *
   * @param hash Object MD5 hash string.
   * @return Self-object for chaining.
   */
  public ServerGetRequest setHash(String hash) {
    request.queryString("hash", hash);
    return this;
  }
}