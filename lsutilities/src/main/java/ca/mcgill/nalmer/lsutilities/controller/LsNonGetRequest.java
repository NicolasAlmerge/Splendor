package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.model.RequestType;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;

/**
 * Represents an LS post, put or delete request.
 */
final class LsNonGetRequest extends LsRequest {
  private HttpRequestWithBody request;
  private RequestBodyEntity reqWithBody;

  private LsNonGetRequest(String path, HttpRequestWithBody req, RequestType type) {
    super(path, type);
    request = req;
  }

  static LsNonGetRequest putRequest(String subPath) {
    return new LsNonGetRequest(subPath, Unirest.put(getLsLocation() + subPath), RequestType.PUT);
  }

  static LsNonGetRequest postRequest(String subPath) {
    return new LsNonGetRequest(subPath, Unirest.post(getLsLocation() + subPath), RequestType.POST);
  }

  static LsNonGetRequest deleteRequest(String subPath) {
    return new LsNonGetRequest(
        subPath, Unirest.delete(getLsLocation() + subPath), RequestType.DELETE
    );
  }

  @Override
  public LsNonGetRequest addJsonAppHeader() {
    request = request.header("Content-Type", "application/json");
    return this;
  }

  @Override
  public LsNonGetRequest addUserCredentials(String username, String password) {
    request = request
        .header("Authorization", getAuthHeaderValue())
        .queryString("grant_type", "password")
        .queryString("username", username)
        .queryString("password", password);
    return this;
  }

  @Override
  public LsNonGetRequest addRefreshTokenCredentials(String refreshToken) {
    request = request
        .header("Authorization", getAuthHeaderValue())
        .queryString("grant_type", "refresh_token")
        .queryString("refresh_token", refreshToken);
    return this;
  }

  @Override
  public LsNonGetRequest addAccessToken(String token) {
    request = request.queryString("access_token", token);
    return this;
  }

  /**
   * Adds a body to the request.
   *
   * @param body Body content.
   * @return Self object for chaining.
   */
  public LsNonGetRequest addBody(String body) {
    reqWithBody = request.body(body);
    request = null;
    return this;
  }

  /**
   * Adds a body to the request.
   *
   * @param body Body content.
   * @return Self object for chaining.
   */
  public LsNonGetRequest addBody(Object body) {
    reqWithBody = request.body(new Gson().toJson(body));
    request = null;
    return this;
  }

  @Override
  public HttpResponse<String> getStringRequest() throws RuntimeException {
    try {
      if (reqWithBody == null) {
        return request.asString();
      }
      return reqWithBody.asString();
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T asObject(Class<? extends T> responseClass) throws RuntimeException {
    try {
      HttpResponse<String> response = (reqWithBody == null)
          ? request.asString() : reqWithBody.asString();
      checkErrorCode(response);
      return new Gson().fromJson(response.getBody(), responseClass);
    } catch (UnirestException e) {
      throw new RuntimeException(e);
    }
  }
}
