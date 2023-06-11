package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.model.GameServiceBasicInfo;
import ca.mcgill.nalmer.lsutilities.model.OauthToken;
import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import ca.mcgill.nalmer.lsutilities.model.Session;
import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * LS Session Controller. Corresponds to the <code>'/api/sessions'</code> route.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class LsSessionController {
  private static final int LONG_POLL_SUCCESS_CODE = 200;
  private static final int LONG_POLL_NO_RESP_CODE = 408;

  /**
   * Private constructor.
   */
  private LsSessionController() {}

  /**
   * Gets all {@link Session} information asynchronously.
   * Corresponds to <code>GET '/api/sessions'</code> with the <code>hash</code> parameter.
   *
   * @param previous Old session id to data {@link Map}.
   * @return New session id to data {@link Map}.
   * @throws NullPointerException if <code>previous</code> is <code>null</code>.
   */
  public static Map<String, Session> fetchAllSessionsAsync(Map<String, Session> previous) {
    LsChecker.allNotNull(previous);

    SessionRetrievalContainer src = new SessionRetrievalContainer(previous);
    String hashedResponse = DigestUtils.md5Hex(new Gson().toJson(src));
    HttpResponse<String> resp;
    int responseCode;

    while (true) {
      do {
        resp = LsGetRequest
            .get("/api/sessions")
            .addHashParameter(hashedResponse)
            .getStringRequest();
        responseCode = resp.getStatus();
      } while (responseCode == LONG_POLL_NO_RESP_CODE);

      if (responseCode == LONG_POLL_SUCCESS_CODE) {
        return new Gson()
            .fromJson(resp.getBody(), SessionRetrievalContainer.class)
            .getSessions();
      }
    }
  }

  /**
   * Gets all {@link Session} information synchronously.
   * Corresponds to <code>GET '/api/sessions'</code> without the <code>hash</code> parameter.
   *
   * @return Session id to data {@link Map}.
   */
  public static Map<String, Session> getAllSessions() {
    return LsGetRequest
        .get("/api/sessions")
        .asObject(SessionRetrievalContainer.class)
        .getSessions();
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param gameId      Game service id.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGameId</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, String gameId, String saveGameId,
                                              String accessToken) {
    LsChecker.allNotNull(creator, gameId, saveGameId, accessToken);
    return LsRequest
        .post("/api/sessions")
        .addJsonAppHeader()
        .addAccessToken(accessToken)
        .addBody(new SessionCreationContainer(creator, gameId, saveGameId))
        .asString().getBody();
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param gameId      Game service id.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGameId</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, String gameId, String saveGameId,
                                              String accessToken) {
    LsChecker.allNotNull(creator, gameId, saveGameId, accessToken);
    return addSessionFromSaveGame(creator.getName(), gameId, saveGameId, accessToken);
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param gameId      Game service id.
   * @param saveGame    Save game.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGame</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, String gameId, SaveGame saveGame,
                                              String accessToken) {
    LsChecker.allNotNull(creator, gameId, saveGame, accessToken);
    return addSessionFromSaveGame(creator, gameId, saveGame.getId(), accessToken);
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param gameId      Game service id.
   * @param saveGame    Save game.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGame</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, String gameId, SaveGame saveGame,
                                              String accessToken) {
    LsChecker.allNotNull(creator, gameId, saveGame, accessToken);
    return addSessionFromSaveGame(creator.getName(), gameId, saveGame.getId(), accessToken);
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator    Creator of the session.
   * @param gameId     Game service id.
   * @param saveGameId Save game id.
   * @param token      Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGameId</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, String gameId, String saveGameId,
                                              OauthToken token) {
    LsChecker.allNotNull(creator, gameId, saveGameId, token);
    return addSessionFromSaveGame(creator, gameId, saveGameId, token.getAccessToken());
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator    Creator of the session.
   * @param gameId     Game service id.
   * @param saveGameId Save game id.
   * @param token      Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGameId</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, String gameId, String saveGameId,
                                              OauthToken token) {
    LsChecker.allNotNull(creator, gameId, saveGameId, token);
    return addSessionFromSaveGame(creator.getName(), gameId, saveGameId, token.getAccessToken());
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator  Creator of the session.
   * @param gameId   Game service id.
   * @param saveGame Save game.
   * @param token    Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGame</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, String gameId, SaveGame saveGame,
                                              OauthToken token) {
    LsChecker.allNotNull(creator, gameId, saveGame, token);
    return addSessionFromSaveGame(creator, gameId, saveGame.getId(), token.getAccessToken());
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator  Creator of the session.
   * @param gameId   Game service id.
   * @param saveGame Save game.
   * @param token    Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code>,
   *                              <code>saveGame</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, String gameId, SaveGame saveGame,
                                              OauthToken token) {
    LsChecker.allNotNull(creator, gameId, saveGame, token);
    return addSessionFromSaveGame(creator.getName(), gameId, saveGame.getId(),
        token.getAccessToken());
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGameId</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, GameServiceBasicInfo serviceName,
                                              String saveGameId, String accessToken) {
    LsChecker.allNotNull(creator, serviceName, saveGameId, accessToken);
    return addSessionFromSaveGame(creator, serviceName.getName(), saveGameId, accessToken);
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGameId</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, GameServiceBasicInfo serviceName,
                                              String saveGameId, String accessToken) {
    LsChecker.allNotNull(creator, serviceName, saveGameId, accessToken);
    return addSessionFromSaveGame(creator.getName(), serviceName.getName(), saveGameId,
        accessToken);
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGame    Save game.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGame</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, GameServiceBasicInfo serviceName,
                                              SaveGame saveGame, String accessToken) {
    LsChecker.allNotNull(creator, serviceName, saveGame, accessToken);
    return addSessionFromSaveGame(creator, serviceName.getName(), saveGame.getId(), accessToken);
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGame    Save game.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGame</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, GameServiceBasicInfo serviceName,
                                              SaveGame saveGame, String accessToken) {
    LsChecker.allNotNull(creator, serviceName, saveGame, accessToken);
    return addSessionFromSaveGame(creator.getName(), serviceName.getName(), saveGame.getId(),
        accessToken);
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGameId  Save game id.
   * @param token       Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGameId</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, GameServiceBasicInfo serviceName,
                                              String saveGameId, OauthToken token) {
    LsChecker.allNotNull(creator, serviceName, saveGameId, token);
    return addSessionFromSaveGame(creator, serviceName.getName(), saveGameId,
        token.getAccessToken());
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGameId  Save game id.
   * @param token       Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGameId</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, GameServiceBasicInfo serviceName,
                                              String saveGameId, OauthToken token) {
    LsChecker.allNotNull(creator, serviceName, saveGameId, token);
    return addSessionFromSaveGame(creator.getName(), serviceName.getName(), saveGameId,
        token.getAccessToken());
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGame    Save game.
   * @param token       Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGame</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(String creator, GameServiceBasicInfo serviceName,
                                              SaveGame saveGame, OauthToken token) {
    LsChecker.allNotNull(creator, serviceName, saveGame, token);
    return addSessionFromSaveGame(creator, serviceName.getName(), saveGame.getId(),
        token.getAccessToken());
  }

  /**
   * Adds a new {@link Session} from a previously registered {@link SaveGame}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param saveGame    Save game.
   * @param token       Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code>,
   *                              <code>saveGame</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static String addSessionFromSaveGame(User creator, GameServiceBasicInfo serviceName,
                                              SaveGame saveGame, OauthToken token) {
    LsChecker.allNotNull(creator, serviceName, saveGame, token);
    return addSessionFromSaveGame(creator.getName(), serviceName.getName(), saveGame.getId(),
        token.getAccessToken());
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param gameId      Game service id.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static String addSession(String creator, String gameId, String accessToken) {
    LsChecker.allNotNull(creator, gameId, accessToken);
    return addSessionFromSaveGame(creator, gameId, "", accessToken);
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator Creator of the session.
   * @param gameId  Game service id.
   * @param token   Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static String addSession(String creator, String gameId, OauthToken token) {
    LsChecker.allNotNull(creator, gameId, token);
    return addSession(creator, gameId, token.getAccessToken());
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param gameId      Game service id.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static String addSession(User creator, String gameId, String accessToken) {
    LsChecker.allNotNull(creator, gameId, accessToken);
    return addSession(creator.getName(), gameId, accessToken);
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator Creator of the session.
   * @param gameId  Game service id.
   * @param token   Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>gameId</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static String addSession(User creator, String gameId, OauthToken token) {
    LsChecker.allNotNull(creator, gameId, token);
    return addSession(creator.getName(), gameId, token.getAccessToken());
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static String addSession(String creator, GameServiceBasicInfo serviceName,
                                  String accessToken) {
    LsChecker.allNotNull(creator, serviceName, accessToken);
    return addSession(creator, serviceName.getName(), accessToken);
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static String addSession(User creator, GameServiceBasicInfo serviceName,
                                  String accessToken) {
    LsChecker.allNotNull(creator, serviceName, accessToken);
    return addSession(creator.getName(), serviceName.getName(), accessToken);
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param token       Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static String addSession(String creator, GameServiceBasicInfo serviceName,
                                  OauthToken token) {
    LsChecker.allNotNull(creator, serviceName, token);
    return addSession(creator, serviceName.getName(), token.getAccessToken());
  }

  /**
   * Adds a new blank {@link Session}.
   * Corresponds to <code>POST '/api/sessions'</code>.
   *
   * @param creator     Creator of the session.
   * @param serviceName Game service basic information.
   * @param token       Access token ({@link UserRole#ROLE_PLAYER} required).
   * @return Session id.
   * @throws NullPointerException if <code>creator</code>, <code>serviceName</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static String addSession(User creator, GameServiceBasicInfo serviceName,
                                  OauthToken token) {
    LsChecker.allNotNull(creator, serviceName, token);
    return addSession(creator.getName(), serviceName.getName(), token.getAccessToken());
  }

  /**
   * Gets a {@link Session} information asynchronously.
   * Corresponds to <code>GET '/api/sessions/{sessionId}'</code> with the <code>hash</code>
   * parameter.
   *
   * @param sessionId Session id.
   * @param previous  Previous session data.
   * @return New session data.
   * @throws NullPointerException if <code>sessionId</code> or <code>previous</code> is
   *                              <code>null</code>.
   */
  public static Session fetchSessionAsync(String sessionId, Session previous) {
    LsChecker.allNotNull(sessionId, previous);

    String hashedResponse = DigestUtils.md5Hex(new Gson().toJson(previous));
    HttpResponse<String> resp;
    int responseCode;

    while (true) {
      do {
        resp = LsGetRequest
            .get("/api/sessions/" + sessionId)
            .addHashParameter(hashedResponse)
            .getStringRequest();
        responseCode = resp.getStatus();
      } while (responseCode == LONG_POLL_NO_RESP_CODE);

      if (responseCode == LONG_POLL_SUCCESS_CODE) {
        return new Gson().fromJson(resp.getBody(), Session.class);
      }
    }
  }

  /**
   * Gets a {@link Session} information synchronously.
   * Corresponds to <code>GET '/api/sessions/{sessionId}'</code> without the <code>hash</code>
   * parameter.
   *
   * @param sessionId Session id.
   * @return Session data.
   * @throws NullPointerException if <code>sessionId</code> is <code>null</code>.
   */
  public static Session getSession(String sessionId) {
    LsChecker.allNotNull(sessionId);
    return LsGetRequest
        .get("/api/sessions/" + sessionId)
        .asObject(Session.class);
  }

  /**
   * Launches a {@link Session}.
   * Corresponds to <code>POST '/api/sessions/{sessionId}'</code>.
   *
   * @param sessionId   Session id.
   * @param accessToken Access token (must be token of the session creator).
   * @throws NullPointerException if <code>sessionId</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void launchSession(String sessionId, String accessToken) {
    LsChecker.allNotNull(sessionId, accessToken);
    LsRequest
        .post("/api/sessions/" + sessionId)
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Launches a {@link Session}.
   * Corresponds to <code>POST '/api/sessions/{sessionId}'</code>.
   *
   * @param sessionId Session id.
   * @param token     Access token (must be token of the session creator).
   * @throws NullPointerException if <code>sessionId</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void launchSession(String sessionId, OauthToken token) {
    LsChecker.allNotNull(sessionId, token);
    launchSession(sessionId, token.getAccessToken());
  }

  /**
   * Deletes a {@link Session}.
   * Corresponds to <code>DELETE '/api/sessions/{sessionId}'</code>.
   *
   * @param sessionId   Session id.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} required, or the token owner is
   *                    the creator of session).
   * @throws NullPointerException if <code>sessionId</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void removeSession(String sessionId, String accessToken) {
    LsChecker.allNotNull(sessionId, accessToken);
    LsRequest
        .delete("/api/sessions/" + sessionId)
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Deletes a {@link Session}.
   * Corresponds to <code>DELETE '/api/sessions/{sessionId}'</code>.
   *
   * @param sessionId Session id.
   * @param token     Access token ({@link UserRole#ROLE_ADMIN} required, or the token owner is
   *                  the creator of session).
   * @throws NullPointerException if <code>sessionId</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void removeSession(String sessionId, OauthToken token) {
    LsChecker.allNotNull(sessionId, token);
    removeSession(sessionId, token.getAccessToken());
  }

  /**
   * Adds a {@link User} to an un-launched {@link Session}.
   * Corresponds to <code>PUT '/api/sessions/{sessionId}/players/{playerName}'</code>.
   *
   * @param sessionId   Session id.
   * @param playerName  Player username.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>playerName</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void addPlayerToSession(String sessionId, String playerName, String accessToken) {
    LsChecker.allNotNull(sessionId, playerName, accessToken);
    LsRequest
        .put("/api/sessions/" + sessionId + "/players/" + playerName)
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Adds a {@link User} to an un-launched {@link Session}.
   * Corresponds to <code>PUT '/api/sessions/{sessionId}/players/{user}'</code>.
   *
   * @param sessionId   Session id.
   * @param user        User to add.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>user</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void addPlayerToSession(String sessionId, User user, String accessToken) {
    LsChecker.allNotNull(sessionId, user, accessToken);
    addPlayerToSession(sessionId, user.getName(), accessToken);
  }

  /**
   * Adds a {@link User} to an un-launched {@link Session}.
   * Corresponds to <code>PUT '/api/sessions/{sessionId}/players/{playerName}'</code>.
   *
   * @param sessionId  Session id.
   * @param playerName Player username.
   * @param token      Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>playerName</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void addPlayerToSession(String sessionId, String playerName, OauthToken token) {
    LsChecker.allNotNull(sessionId, playerName, token);
    addPlayerToSession(sessionId, playerName, token.getAccessToken());
  }

  /**
   * Adds a {@link User} to an un-launched {@link Session}.
   * Corresponds to <code>PUT '/api/sessions/{sessionId}/players/{user}'</code>.
   *
   * @param sessionId Session id.
   * @param user      User to add.
   * @param token     Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>user</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void addPlayerToSession(String sessionId, User user, OauthToken token) {
    LsChecker.allNotNull(sessionId, user, token);
    addPlayerToSession(sessionId, user.getName(), token.getAccessToken());
  }

  /**
   * Removes a {@link User} from an un-launched {@link Session}.
   * Corresponds to <code>DELETE '/api/sessions/{sessionId}/players/{playerName}'</code>.
   *
   * @param sessionId   Session id.
   * @param playerName  Player username.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>playerName</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void removePlayerFromSession(String sessionId, String playerName,
                                             String accessToken) {
    LsChecker.allNotNull(sessionId, playerName, accessToken);
    LsRequest
        .delete("/api/sessions/" + sessionId + "/players/" + playerName)
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Removes a {@link User} from an un-launched {@link Session}.
   * Corresponds to <code>DELETE '/api/sessions/{sessionId}/players/{user}'</code>.
   *
   * @param sessionId   Session id.
   * @param user        User to remove.
   * @param accessToken Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>user</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void removePlayerFromSession(String sessionId, User user, String accessToken) {
    LsChecker.allNotNull(sessionId, user, accessToken);
    removePlayerFromSession(sessionId, user.getName(), accessToken);
  }

  /**
   * Removes a {@link User} from an un-launched {@link Session}.
   * Corresponds to <code>DELETE '/api/sessions/{sessionId}/players/{playerName}'</code>.
   *
   * @param sessionId  Session id.
   * @param playerName Player username.
   * @param token      Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>playerName</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void removePlayerFromSession(String sessionId, String playerName,
                                             OauthToken token) {
    LsChecker.allNotNull(sessionId, playerName, token);
    removePlayerFromSession(sessionId, playerName, token.getAccessToken());
  }

  /**
   * Removes a {@link User} from an un-launched {@link Session}.
   * Corresponds to <code>DELETE '/api/sessions/{sessionId}/players/{user}'</code>.
   *
   * @param sessionId Session id.
   * @param user      User to remove.
   * @param token     Access token ({@link UserRole#ROLE_PLAYER} required).
   * @throws NullPointerException if <code>sessionId</code>, <code>user</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void removePlayerFromSession(String sessionId, User user, OauthToken token) {
    LsChecker.allNotNull(sessionId, user, token);
    removePlayerFromSession(sessionId, user.getName(), token.getAccessToken());
  }

  /**
   * Session retrieval helper class.
   */
  private static final class SessionRetrievalContainer {
    private final Map<String, Session> sessions;

    /**
     * Constructor.
     *
     * @param sessionMap Session map.
     */
    public SessionRetrievalContainer(Map<String, Session> sessionMap) {
      sessions = Objects.requireNonNullElseGet(sessionMap, HashMap::new);
    }

    /**
     * Gets all sessions.
     *
     * @return Session map.
     */
    public Map<String, Session> getSessions() {
      return sessions;
    }
  }

  /**
   * Session creation helper class.
   */
  private static final class SessionCreationContainer {
    private final String creator;
    private final String game;
    private final String savegame;

    /**
     * Constructor.
     *
     * @param creatorName Session creator name.
     * @param gameId      Session game id.
     * @param saveGameId  Session save game id.
     */
    public SessionCreationContainer(String creatorName, String gameId, String saveGameId) {
      creator = creatorName;
      game = gameId;
      savegame = saveGameId;
    }

    /**
     * Gets creator name.
     *
     * @return Creator name.
     */
    public String getCreatorName() {
      return creator;
    }

    /**
     * Gets game id.
     *
     * @return Game id.
     */
    public String getGameId() {
      return game;
    }

    /**
     * Gets save game id.
     *
     * @return Save game id.
     */
    public String getSaveGameId() {
      return savegame;
    }
  }
}
