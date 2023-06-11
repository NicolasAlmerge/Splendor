package ca.mcgill.nalmer.lsutilities.model;

import ca.mcgill.nalmer.lsutilities.Data;
import ca.mcgill.nalmer.lsutilities.controller.LsOauthController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class AutoRefreshOauthTokenTest {
  private OauthToken token;

  @Test
  public void testNonExpired() {
    AutoRefreshOauthToken autoRefreshToken = new AutoRefreshOauthToken(token);
    Assert.assertSame(token, autoRefreshToken.getOauthToken());
  }

  @Before
  public void getToken() {
    token = Data.getServiceToken();
  }

  @After
  public void deleteToken() {
    LsOauthController.deleteOauthToken(token);
    token = null;
  }
}
