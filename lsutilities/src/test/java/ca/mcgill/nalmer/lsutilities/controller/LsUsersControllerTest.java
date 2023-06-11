package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.Data;
import ca.mcgill.nalmer.lsutilities.model.AutoRefreshOauthToken;
import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import org.junit.Assert;
import org.junit.Test;

public final class LsUsersControllerTest {
  private final AutoRefreshOauthToken token = new AutoRefreshOauthToken(Data.getAdminToken());

  @Test
  public void testNumberOfUsers1() {
    Assert.assertEquals(
        LsUsersController.getNumberOfUsers(),
        LsUsersController.getAllUsers(token.getOauthToken()).length
    );
  }

  @Test
  public void testNumberOfUsers2() {
    Assert.assertEquals(LsUsersController.getAllUsers(token.getOauthToken()).length, 5);
  }

  @Test
  public void testFavouriteColour() {
    Assert.assertEquals(
        LsUsersController.getFavouriteColour(Data.getServiceUsername(), token.getOauthToken()),
        "FFFFFF"
    );
  }

  @Test
  public void testGetOneUser() {
    User service = LsUsersController.getUser(Data.getServiceUsername(), token.getOauthToken());
    Assert.assertEquals(service.getName(), Data.getServiceUsername());
    Assert.assertEquals(service.getRole(), UserRole.ROLE_SERVICE);
    Assert.assertEquals(service.getPreferredColor(), "FFFFFF");
  }
}
