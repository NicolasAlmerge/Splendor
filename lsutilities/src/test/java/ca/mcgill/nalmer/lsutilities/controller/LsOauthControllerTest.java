package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.Data;
import ca.mcgill.nalmer.lsutilities.model.AutoRefreshOauthToken;
import ca.mcgill.nalmer.lsutilities.model.LobbyServiceFetchException;
import ca.mcgill.nalmer.lsutilities.model.OauthToken;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import org.junit.Assert;
import org.junit.Test;

public final class LsOauthControllerTest {
  private final AutoRefreshOauthToken token = new AutoRefreshOauthToken(Data.getServiceToken());

  @Test
  public void testGetToken() {
    OauthToken newToken = LsOauthController.getOauthToken(
        Data.getServiceUsername(), Data.getServicePassword()
    );
    checkEquals(newToken);
  }

  @Test
  public void testDeleteToken() {
    OauthToken newToken = LsOauthController.getOauthToken(
        Data.getServiceUsername(), Data.getServicePassword()
    );
    LsOauthController.deleteOauthToken(newToken);
    Assert.assertThrows(
        LobbyServiceFetchException.class, () -> LsOauthController.getUsername(newToken)
    );
  }

  @Test
  public void testGetRole() {
    Assert.assertEquals(LsOauthController.getRole(token.getOauthToken()), UserRole.ROLE_SERVICE);
  }

  @Test
  public void testGetUsername() {
    Assert.assertEquals(LsOauthController.getUsername(
        token.getOauthToken()), Data.getServiceUsername()
    );
  }

  private void checkEquals(OauthToken token1, OauthToken token2) {
    Assert.assertEquals(token1.getAccessToken(), token2.getAccessToken());
  }

  private void checkEquals(OauthToken token1) {
    checkEquals(token1, token.getOauthToken());
  }
}
