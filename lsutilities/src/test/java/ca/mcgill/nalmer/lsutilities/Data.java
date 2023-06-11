package ca.mcgill.nalmer.lsutilities;

import ca.mcgill.nalmer.lsutilities.controller.LsOauthController;
import ca.mcgill.nalmer.lsutilities.model.OauthToken;

public final class Data {
  private static final String SERVICE_USERNAME = "xox";
  private static final String SERVICE_PASSWORD = "laaPhie*aiN0";
  private static final String ADMIN_USERNAME = "maex";
  private static final String ADMIN_PASSWORD = "abc123_ABC123";

  private Data() {}

  public static OauthToken getServiceToken() {
    return LsOauthController.getOauthToken(SERVICE_USERNAME, SERVICE_PASSWORD);
  }

  public static String getServiceUsername() {
    return SERVICE_USERNAME;
  }

  public static String getServicePassword() {
    return SERVICE_PASSWORD;
  }

  public static String getAdminUsername() {
    return ADMIN_USERNAME;
  }

  public static OauthToken getAdminToken() {
    return LsOauthController.getOauthToken(ADMIN_USERNAME, ADMIN_PASSWORD);
  }
}
