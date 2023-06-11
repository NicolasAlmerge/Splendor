package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.Data;
import ca.mcgill.nalmer.lsutilities.model.AutoRefreshOauthToken;
import ca.mcgill.nalmer.lsutilities.model.GameService;
import ca.mcgill.nalmer.lsutilities.model.LobbyServiceFetchException;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public final class LsSessionControllerTest {
  private static final AutoRefreshOauthToken serviceToken = new AutoRefreshOauthToken(
      Data.getServiceToken()
  );
  private final AutoRefreshOauthToken token = new AutoRefreshOauthToken(Data.getAdminToken());
  private static final GameService GAME_SERVICE = new GameService("test", "Test", 2, 4);
  private final Set<String> sessionSet = new HashSet<>();

  @BeforeClass
  public static void addGameService() {
    LsGameServicesController.addGameService(GAME_SERVICE, serviceToken.getOauthToken());
  }

  @AfterClass
  public static void removeGameService() {
    LsGameServicesController.deleteGameService(GAME_SERVICE, serviceToken.getOauthToken());
  }

  @Before
  @After
  public void clearAll() {
    for (String id : LsSessionController.getAllSessions().keySet()) {
      LsSessionController.removeSession(id, token.getOauthToken());
    }
    sessionSet.clear();
  }

  @Test
  public void testGetSessions() {
    assertAllSessionsEqual();
  }

  @Test
  public void testAddOneSession() {
    addAndTest();
  }

  @Test
  public void testAddTwoSessions() {
    addAndTest(2);
  }

  @Test
  public void testAddThreeSessions() {
    addAndTest(3);
  }

  @Test
  public void testAddAndRemoveOne() {
    String id = addSession();
    removeAndTest(id);
  }

  @Test
  public void testAddTwoRemoveOne() {
    String[] ids = addSessions(2);
    removeAndTest(ids[0]);
  }

  @Test
  public void testAddTwoRemoveTwo() {
    String[] ids = addSessions(2);
    removeAndTest(ids);
  }

  @Test
  public void testAddThreeRemoveOne() {
    String[] ids = addSessions(3);
    removeAndTest(ids[0]);
  }

  @Test
  public void testAddThreeRemoveTwo() {
    String[] ids = addSessions(3);
    removeSession(ids[0]);
    removeAndTest(ids[1]);
  }

  @Test
  public void testAddThreeRemoveThree() {
    String[] ids = addSessions(3);
    removeAndTest(ids);
  }

  @Test
  public void testRemoveInvalid() {
    Assert.assertThrows(LobbyServiceFetchException.class, () -> removeSession("0"));
  }

  private void addAndTest() {
    addAndTest(1);
  }

  private void addAndTest(int n) {
    addSessions(n);
    assertAllSessionsEqual();
  }

  private void removeAndTest(String... sessionIds) {
    removeSessions(sessionIds);
    assertAllSessionsEqual();
  }

  private String[] addSessions(int n) {
    String[] ids = new String[n];
    for (int i = 0; i < n; ++i) {
      ids[i] = addSession();
    }
    return ids;
  }

  private String addSession() {
    String id = LsSessionController.addSession(
        Data.getAdminUsername(), GAME_SERVICE, token.getOauthToken()
    );
    sessionSet.add(id);
    return id;
  }

  private void removeSessions(String... sessionIds) {
    for (String sessionId : sessionIds) {
      removeSession(sessionId);
    }
  }

  private void removeSession(String sessionId) {
    LsSessionController.removeSession(sessionId, token.getOauthToken());
    sessionSet.remove(sessionId);
  }

  private void assertAllSessionsEqual() {
    Assert.assertEquals(LsSessionController.getAllSessions().keySet(), sessionSet);
  }
}
