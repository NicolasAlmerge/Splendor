package splendor.controller;

import ca.mcgill.nalmer.lsutilities.controller.LsGameServicesController;
import ca.mcgill.nalmer.lsutilities.model.GameService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class GameServiceRegistratorTest {
  private final GameServiceRegistrator registrator = new GameServiceRegistrator();

  @Before
  public void register() {
    registrator.registerSplendor();
  }

  @After
  public void unregister() {
    registrator.unregisterSplendor();
  }

  @Test
  public void checkGameServiceName() {
    Assert.assertEquals("splendor", GameServiceRegistrator.getSplendorGameService().getName());
  }

  @Test
  public void checkRegistered() {
    GameService service = GameServiceRegistrator.getSplendorGameService();
    GameService lsData = LsGameServicesController.getGameService(service);
    Assert.assertEquals(service.getName(), lsData.getName());
  }
}
