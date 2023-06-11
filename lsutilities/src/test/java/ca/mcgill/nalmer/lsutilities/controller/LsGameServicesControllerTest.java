package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.Data;
import ca.mcgill.nalmer.lsutilities.model.AutoRefreshOauthToken;
import ca.mcgill.nalmer.lsutilities.model.GameService;
import ca.mcgill.nalmer.lsutilities.model.GameServiceBasicInfo;
import ca.mcgill.nalmer.lsutilities.model.LobbyServiceFetchException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class LsGameServicesControllerTest {
  private final AutoRefreshOauthToken token = new AutoRefreshOauthToken(Data.getServiceToken());
  private final static GameService firstService = new GameService("A", "Game A", 2, 4);
  private final static GameService secondService = new GameService("B", "Game B", 2, 2);
  private final static GameService thirdService = new GameService("C", "Game C", 2, 3);

  @Before
  @After
  public void clearAll() {
    for (GameServiceBasicInfo gameService : LsGameServicesController.getGameServiceNames()) {
      LsGameServicesController.deleteGameService(gameService, token.getOauthToken());
    }
  }

  @Test
  public void testEmpty() {
    assertServicesEquals();
  }

  @Test
  public void testAddOne() {
    addAndTest(firstService);
  }

  @Test
  public void testAddTwo() {
    addAndTest(firstService, secondService);
  }

  @Test
  public void testAddThree() {
    addAndTest(firstService, secondService, thirdService);
  }

  @Test
  public void testAddAndRemoveOne() {
    add(firstService);
    remove(firstService);
    assertServicesEquals();
  }

  @Test
  public void testAddTwoAndRemoveOne() {
    add(firstService, secondService);
    remove(secondService);
    assertServicesEquals(firstService);
  }

  @Test
  public void testAddTwoAndRemoveTwo() {
    add(firstService, secondService);
    remove(secondService, firstService);
    assertServicesEquals();
  }

  @Test
  public void testAddThreeAndRemoveOne() {
    add(firstService, secondService, thirdService);
    remove(thirdService);
    assertServicesEquals(firstService, secondService);
  }

  @Test
  public void testAndAddThreeAndRemoveTwo() {
    add(firstService, secondService, thirdService);
    remove(firstService, thirdService);
    assertServicesEquals(secondService);
  }

  @Test
  public void testAndAddThreeAndRemoveThree() {
    add(firstService, secondService, thirdService);
    remove(secondService, firstService, thirdService);
    assertServicesEquals();
  }

  @Test
  public void testRemoveInvalid() {
    Assert.assertThrows(LobbyServiceFetchException.class, () -> remove(firstService));
  }

  private void addAndTest(GameService... services) {
    add(services);
    assertServicesEquals(services);
  }

  private void add(GameService... services) {
    for (GameService service : services) {
      LsGameServicesController.addGameService(service, token.getOauthToken());
    }
  }

  private void remove(GameService... services) {
    for (GameService service : services) {
      LsGameServicesController.deleteGameService(service, token.getOauthToken());
    }
  }

  private void assertServicesEquals(GameService... services) {
    // Get basic data
    GameServiceBasicInfo[] basicInfos = new GameServiceBasicInfo[services.length];
    for (int i = 0; i < services.length; ++i) {
      GameService service = services[i];
      basicInfos[i] = new GameServiceBasicInfo(service.getName(), service.getDisplayName());
    }

    // Check basic names
    GameServiceBasicInfo[] actual = LsGameServicesController.getGameServiceNames();
    Assert.assertArrayEquals(actual, basicInfos);

    // Check more in depth
    for (int i = 0; i < actual.length; ++i) {
      Assert.assertEquals(LsGameServicesController.getGameService(actual[i]), services[i]);
    }
  }
}
