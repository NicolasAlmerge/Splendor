package splendor.controller;

import ca.mcgill.nalmer.lsutilities.controller.LsSessionController;
import ca.mcgill.nalmer.lsutilities.model.OauthToken;
import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import ca.mcgill.nalmer.lsutilities.model.Session;
import ca.mcgill.nalmer.splendormodels.Action;
import ca.mcgill.nalmer.splendormodels.City;
import ca.mcgill.nalmer.splendormodels.Game;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import com.mashape.unirest.http.HttpResponse;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * The ClientServerController defines all client to server interactions.
 */
public final class ClientServerController {
  private static final int LONG_POLL_SUCCESS_CODE = 200;
  private static final int LONG_POLL_NO_RESP_CODE = 408;

  /**
   * Private constructor.
   */
  private ClientServerController() {
  }

  /**
   * Asserts the server is online.
   *
   * @throws RuntimeException if it is not online (request failed).
   */
  public static void assertServerOnline() {
    ServerRequest.get("/online").execute();
  }

  /**
   * Gets a game asynchronously.
   *
   * @param previous Previous {@link Game}.
   * @return New {@link Game}.
   */
  public static Game getGameAsync(Game previous) {
    if (previous == null) {
      throw new RuntimeException("previous cannot be null");
    }

    String hashedResponse = DigestUtils.md5Hex(ServerRequest.getGson().toJson(previous));
    HttpResponse<String> resp;
    int responseCode;

    while (true) {
      do {
        resp = ServerRequest
            .get("/games/async/{gameId}")
            .setGameId(previous.getId())
            .setHash(hashedResponse)
            .getStringRequest();
        responseCode = resp.getStatus();
      } while (responseCode == LONG_POLL_NO_RESP_CODE);

      if (responseCode == LONG_POLL_SUCCESS_CODE) {
        return ServerRequest.getGson().fromJson(resp.getBody(), Game.class);
      }
    }
  }

  /**
   * Gets a {@link Game} from an id.
   * Corresponds to <code>GET '/games/{gameId}'</code>.
   *
   * @param gameId Id of the {@link Game} the user wants to see.
   * @return {@link Game} with corresponding <code>gameId</code>.
   */
  public static Game getGame(String gameId) {
    return ServerRequest.get("/games/{gameId}").setGameId(gameId).asObject(Game.class);
  }


  /**
   * Gets all {@link Action} available to this player in the {@link Game} they are in.
   * Corresponds to <code>GET '/games/{gameId}/actions'</code>.
   *
   * @param gameId Id of the {@link Game} the user wants to see.
   * @return Array of {@link Action} that can be performed by the current player.
   */
  public static Action[] getGameActions(String gameId) {
    return ServerRequest.get("/games/{gameId}/actions").setGameId(gameId).asObject(Action[].class);
  }

  /**
   * Sends the desired {@link Action} to the splendor server to be executed.
   * Corresponds to <code>PUT '/games/{gameId}/actions/{actionId}'</code>.
   *
   * @param gameId   Id of the {@link Game} the user wants to see.
   * @param actionId Id of the {@link Action} the user wants to execute.
   */
  public static void sendGameAction(String gameId, int actionId) {
    ServerRequest
        .put("/games/{gameId}/actions/{actionId}")
        .setGameId(gameId)
        .setActionId(actionId)
        .execute();
  }

  /**
   * Sends the desired {@link City} to the splendor server to be selected.
   * Corresponds to <code>PUT '/games/{gameId}/cityselection/{cityId}'</code>
   *
   * @param gameId Id of the {@link Game} the user wants to see.
   * @param cityId Index of the {@link City} the user wants to select.
   */
  public static void sendCitySelection(String gameId, int cityId) {
    ServerRequest
        .put("/games/{gameId}/cityselection/{cityId}")
        .setGameId(gameId)
        .setCityId(cityId)
        .execute();
  }

  /**
   * Sends the desired token selection to the splendor sever to be selected.
   * This is for the trading post ability.
   * Corresponds to <code>PUT '/games/{gameId}/tokenselection/{color}'</code>
   *
   * @param gameId Id of the {@link Game} the user wants to see.
   * @param color  Color of the token the user wants to select.
   */
  public static void sendTradingPostTokenSelection(String gameId, String color) {
    ServerRequest
        .put("/games/{gameId}/tokenselection/{color}")
        .setGameId(gameId)
        .setColor(color)
        .execute();
  }

  /**
   * Creates a new {@link Game}.
   * Corresponds to <code>POST '/games/{sessionId}'</code>.
   *
   * @param sessionId Id of the {@link Session} the user is in.
   * @param extension {@link GameExtension} to use.
   */
  public static void createNewGame(String sessionId, GameExtension extension) {
    ServerRequest
        .post("/games/{sessionId}")
        .setExtension(extension)
        .setSessionId(sessionId)
        .execute();
  }

  /**
   * Sends the desired {@link SaveGame} to the splendor server to be saved.
   * Corresponds to <code>PUT '/games/{gameId}/actions/{actionId}'</code>.
   *
   * @param sessionId Id of the {@link Session} the user is in.
   */
  public static void sendSaveGame(String sessionId) {
    ServerRequest
        .post("/saveGames/{gameId}")
        .setGameId(sessionId)
        .execute();
  }

  /**
   * Sends info to server to saved game can be launched.
   * Corresponds to <code>PUT '/savegames/map/{gameId}'</code>.
   *
   * @param username creator of the new session.
   * @param savegame savegame object for this session.
   * @param token    user's token.
   */
  public static void launchSessionFromSave(String username,
                                           SaveGame savegame, OauthToken token) {
    String newSessionId = LsSessionController.addSessionFromSaveGame(
        username, "splendor",
        savegame, token
    );
    System.out.println(newSessionId);
    ServerRequest
        .post("/saveGames/map/{gameId}")
        .setGameId(newSessionId)
        .setOldId(savegame.getId())
        .execute();
  }

}