package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.model.RequestType;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

/**
 * Represents an LS Get request.
 */
final class LsGetRequest extends LsRequest {
  private HttpRequest request;

  LsGetRequest(String path) {
    super(path, RequestType.GET);
    request = Unirest.get(getLsLocation() + getSubPath());
  }

  @Override
  public LsGetRequest addJsonAppHeader() {
    request = request.header("Content-Type", "application/json");
    return this;
  }

  @Override
  public LsGetRequest addUserCredentials(String username, String password) {
    request = request
        .header("Authorization", getAuthHeaderValue())
        .queryString("grant_type", "password")
        .queryString("username", username)
        .queryString("password", password);
    return this;
  }

  @Override
  public LsGetRequest addRefreshTokenCredentials(String refreshToken) {
    request = request
        .header("Authorization", getAuthHeaderValue())
        .queryString("grant_type", "refresh_token")
        .queryString("refresh_token", refreshToken);
    return this;
  }

  @Override
  public LsGetRequest addAccessToken(String token) {
    request = request.queryString("access_token", token);
    return this;
  }

  /**
   * Add hash parameter, useful for long polling.
   *
   * @param hash Hash to add.
   * @return Self object for chaining.
   */
  public LsGetRequest addHashParameter(String hash) {
    request = request.queryString("hash", hash);
    return this;
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
      return new Gson().fromJson(response.getBody(), responseClass);
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
  }
}
