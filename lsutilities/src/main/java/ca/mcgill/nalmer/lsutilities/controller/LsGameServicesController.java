package ca.mcgill.nalmer.lsutilities.controller;

import ca.mcgill.nalmer.lsutilities.model.GameService;
import ca.mcgill.nalmer.lsutilities.model.GameServiceBasicInfo;
import ca.mcgill.nalmer.lsutilities.model.OauthToken;
import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import ca.mcgill.nalmer.lsutilities.model.UserRole;

/**
 * LS Game Service Controller. Corresponds to the <code>'/api/gameservices'</code> route.
 *
 * @author Nicolas Almerge
 * @since 1.0
 */
public final class LsGameServicesController {
  /**
   * Private constructor.
   */
  private LsGameServicesController() {}

  /**
   * Gets the list of {@link GameServiceBasicInfo}.
   * Corresponds to <code>GET '/api/gameservices'</code>.
   *
   * @return Array of {@link GameServiceBasicInfo}.
   */
  public static GameServiceBasicInfo[] getGameServiceNames() {
    return LsRequest.get("/api/gameservices").asObject(GameServiceBasicInfo[].class);
  }

  /**
   * Gets a {@link GameService} data from its name.
   * Corresponds to <code>GET '/api/gameservices/{gameServiceName}'</code>.
   *
   * @param gameServiceName Game service name.
   * @return Game service data.
   * @throws NullPointerException if <code>gameServiceName</code> is <code>null</code>.
   */
  public static GameService getGameService(String gameServiceName) {
    LsChecker.allNotNull(gameServiceName);
    return LsRequest.get("/api/gameservices/" + gameServiceName).asObject(GameService.class);
  }

  /**
   * Gets the {@link GameService} data from a {@link GameServiceBasicInfo}.
   * Corresponds to <code>GET '/api/gameservices/{serviceName}'</code>.
   *
   * @param serviceName Game service name.
   * @return Game service data.
   * @throws NullPointerException if <code>serviceName</code> is <code>null</code>.
   */
  public static GameService getGameService(GameServiceBasicInfo serviceName) {
    LsChecker.allNotNull(serviceName);
    return getGameService(serviceName.getName());
  }

  /**
   * Adds a new {@link GameService}.
   * Corresponds to <code>PUT '/api/gameservices/{gameService}'</code>.
   *
   * @param gameService Game service to add.
   * @param accessToken Access token ({@link UserRole#ROLE_SERVICE} required).
   * @throws NullPointerException if <code>gameService</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void addGameService(GameService gameService, String accessToken) {
    LsChecker.allNotNull(gameService, accessToken);
    LsRequest
        .put("/api/gameservices/" + gameService.getName())
        .addJsonAppHeader()
        .addAccessToken(accessToken)
        .addBody(gameService)
        .execute();
  }

  /**
   * Adds a new {@link GameService}.
   * Corresponds to <code>PUT '/api/gameservices/{gameService}'</code>.
   *
   * @param gameService Game service to add.
   * @param token       Access token ({@link UserRole#ROLE_SERVICE} required).
   * @throws NullPointerException if <code>gameService</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void addGameService(GameService gameService, OauthToken token) {
    LsChecker.allNotNull(gameService, token);
    addGameService(gameService, token.getAccessToken());
  }

  /**
   * Deletes a {@link GameService} from its name.
   * Corresponds to <code>DELETE '/api/gameservices/{serviceName}'</code>.
   *
   * @param serviceName Game service to delete.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} or matching
   *                    {@link UserRole#ROLE_SERVICE} required).
   * @throws NullPointerException if <code>serviceName</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void deleteGameService(String serviceName, String accessToken) {
    LsChecker.allNotNull(serviceName, accessToken);
    LsRequest
        .delete("/api/gameservices/" + serviceName)
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Deletes a {@link GameService} from a {@link GameServiceBasicInfo}.
   * Corresponds to <code>DELETE '/api/gameservices/{serviceName}'</code>.
   *
   * @param serviceName Game service to delete.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} or matching
   *                    {@link UserRole#ROLE_SERVICE} required).
   * @throws NullPointerException if <code>serviceName</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void deleteGameService(GameServiceBasicInfo serviceName, String accessToken) {
    LsChecker.allNotNull(serviceName, accessToken);
    deleteGameService(serviceName.getName(), accessToken);
  }

  /**
   * Deletes a {@link GameService} from its name.
   * Corresponds to <code>DELETE '/api/gameservices/{serviceName}'</code>.
   *
   * @param serviceName Game service to delete.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN} or matching
   *                    {@link UserRole#ROLE_SERVICE} required).
   * @throws NullPointerException if <code>serviceName</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void deleteGameService(String serviceName, OauthToken token) {
    LsChecker.allNotNull(serviceName, token);
    deleteGameService(serviceName, token.getAccessToken());
  }

  /**
   * Deletes a {@link GameService} from a {@link GameServiceBasicInfo}.
   * Corresponds to <code>DELETE '/api/gameservices/{serviceName}'</code>.
   *
   * @param serviceName Game service to delete.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN} or matching
   *                    {@link UserRole#ROLE_SERVICE} required).
   * @throws NullPointerException if <code>serviceName</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void deleteGameService(GameServiceBasicInfo serviceName, OauthToken token) {
    LsChecker.allNotNull(serviceName, token);
    deleteGameService(serviceName.getName(), token.getAccessToken());
  }

  /**
   * Gets the list of {@link SaveGame} for a particular game service name.
   * Corresponds to <code>GET '/api/gameservices/{gameServiceName}/savegames'</code>.
   *
   * @param gameServiceName Game service name.
   * @param accessToken     Access token ({@link UserRole#ROLE_ADMIN} or
   *                        {@link UserRole#ROLE_PLAYER}).
   * @return Array of {@link SaveGame}.
   * @throws NullPointerException if <code>gameServiceName</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static SaveGame[] getAllSaveGames(String gameServiceName, String accessToken) {
    LsChecker.allNotNull(gameServiceName, accessToken);
    return LsRequest
        .get("/api/gameservices/" + gameServiceName + "/savegames")
        .addAccessToken(accessToken)
        .asObject(SaveGame[].class);
  }

  /**
   * Gets the list of {@link SaveGame} for a particular {@link GameServiceBasicInfo}.
   * Corresponds to <code>GET '/api/gameservices/{gameService}/savegames'</code>.
   *
   * @param gameService Game service.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} or {@link UserRole#ROLE_PLAYER}).
   * @return Array of {@link SaveGame}.
   * @throws NullPointerException if <code>gameService</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static SaveGame[] getAllSaveGames(GameServiceBasicInfo gameService, String accessToken) {
    LsChecker.allNotNull(gameService, accessToken);
    return getAllSaveGames(gameService.getName(), accessToken);
  }

  /**
   * Gets the list of {@link SaveGame} for a particular game service name.
   * Corresponds to <code>GET '/api/gameservices/{gameServiceName}/savegames'</code>.
   *
   * @param gameServiceName Game service name.
   * @param token           Access token ({@link UserRole#ROLE_ADMIN} or
   *                        {@link UserRole#ROLE_PLAYER}).
   * @return Array of {@link SaveGame}.
   * @throws NullPointerException if <code>gameServiceName</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static SaveGame[] getAllSaveGames(String gameServiceName, OauthToken token) {
    LsChecker.allNotNull(gameServiceName, token);
    return getAllSaveGames(gameServiceName, token.getAccessToken());
  }

  /**
   * Gets the list of {@link SaveGame} for a particular {@link GameServiceBasicInfo}.
   * Corresponds to <code>GET '/api/gameservices/{gameService}/savegames'</code>.
   *
   * @param gameService Game service.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN} or {@link UserRole#ROLE_PLAYER}).
   * @return Array of {@link SaveGame}.
   * @throws NullPointerException if <code>gameService</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static SaveGame[] getAllSaveGames(GameServiceBasicInfo gameService, OauthToken token) {
    LsChecker.allNotNull(gameService, token);
    return getAllSaveGames(gameService.getName(), token.getAccessToken());
  }

  /**
   * Deletes all the {@link SaveGame} for a particular game service name.
   * Corresponds to <code>DELETE '/api/gameservices/{gameServiceName}/savegames'</code>.
   *
   * @param gameServiceName Game service name.
   * @param accessToken     Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                        registered the game service).
   * @throws NullPointerException if <code>gameServiceName</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void deleteAllSaveGames(String gameServiceName, String accessToken) {
    LsChecker.allNotNull(gameServiceName, accessToken);
    LsRequest
        .delete("/api/gameservices/" + gameServiceName + "/savegames")
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Deletes all the {@link SaveGame} for a particular {@link GameServiceBasicInfo}.
   * Corresponds to <code>DELETE '/api/gameservices/{gameService}/savegames'</code>.
   *
   * @param gameService Game service name.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>gameService</code> or <code>accessToken</code> is
   *                              <code>null</code>.
   */
  public static void deleteAllSaveGames(GameServiceBasicInfo gameService, String accessToken) {
    LsChecker.allNotNull(gameService, accessToken);
    deleteAllSaveGames(gameService.getName(), accessToken);
  }

  /**
   * Deletes all the {@link SaveGame} for a particular game service name.
   * Corresponds to <code>DELETE '/api/gameservices/{gameServiceName}/savegames'</code>.
   *
   * @param gameServiceName Game service name.
   * @param token           Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                        registered the game service).
   * @throws NullPointerException if <code>gameServiceName</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void deleteAllSaveGames(String gameServiceName, OauthToken token) {
    LsChecker.allNotNull(gameServiceName, token);
    deleteAllSaveGames(gameServiceName, token.getAccessToken());
  }

  /**
   * Deletes all the {@link SaveGame} for a particular {@link GameServiceBasicInfo}.
   * Corresponds to <code>DELETE '/api/gameservices/{gameService}/savegames'</code>.
   *
   * @param gameService Game service name.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>gameService</code> or <code>token</code> is
   *                              <code>null</code>.
   */
  public static void deleteAllSaveGames(GameServiceBasicInfo gameService, OauthToken token) {
    LsChecker.allNotNull(gameService, token);
    deleteAllSaveGames(gameService.getName(), token.getAccessToken());
  }

  /**
   * Gets the {@link SaveGame} data for a particular game service name and save game id.
   * Corresponds to <code>GET '/api/gameservices/{serviceName}/savegames/{saveGameId}'</code>.
   *
   * @param serviceName Game service name.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} or
   *                    {@link UserRole#ROLE_PLAYER}).
   * @return Save game data.
   * @throws NullPointerException if <code>gameServiceName</code>, <code>saveGameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static SaveGame getSaveGame(String serviceName, String saveGameId, String accessToken) {
    LsChecker.allNotNull(serviceName, saveGameId, accessToken);
    return LsRequest
        .get("/api/gameservices/" + serviceName + "/savegames/" + saveGameId)
        .addAccessToken(accessToken)
        .asObject(SaveGame.class);
  }

  /**
   * Gets the {@link SaveGame} data for a particular {@link GameServiceBasicInfo} and save game id.
   * Corresponds to <code>GET '/api/gameservices/{gameService}/savegames/{saveGameId}'</code>.
   *
   * @param gameService Game service basic information.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN} or {@link UserRole#ROLE_PLAYER}).
   * @return Save game data.
   * @throws NullPointerException if <code>gameService</code>, <code>saveGameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static SaveGame getSaveGame(
      GameServiceBasicInfo gameService, String saveGameId, String accessToken) {
    LsChecker.allNotNull(gameService, saveGameId, accessToken);
    return getSaveGame(gameService.getName(), saveGameId, accessToken);
  }

  /**
   * Gets the {@link SaveGame} data for a particular game service name and save game id.
   * Corresponds to <code>GET '/api/gameservices/{gameServiceName}/savegames/{saveGameId}'</code>.
   *
   * @param gameServiceName Game service name.
   * @param saveGameId      Save game id.
   * @param token           Access token ({@link UserRole#ROLE_ADMIN} or
   *                        {@link UserRole#ROLE_PLAYER}).
   * @return Save game data.
   * @throws NullPointerException if <code>gameServiceName</code>, <code>saveGameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static SaveGame getSaveGame(
      String gameServiceName, String saveGameId, OauthToken token) {
    LsChecker.allNotNull(gameServiceName, saveGameId, token);
    return getSaveGame(gameServiceName, saveGameId, token.getAccessToken());
  }

  /**
   * Gets the {@link SaveGame} data for a particular {@link GameServiceBasicInfo} and save game id.
   * Corresponds to <code>GET '/api/gameservices/{gameService}/savegames/{saveGameId}'</code>.
   *
   * @param gameService Game service basic information.
   * @param saveGameId  Save game id.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN} or {@link UserRole#ROLE_PLAYER}).
   * @return Save game data.
   * @throws NullPointerException if <code>gameService</code>, <code>saveGameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static SaveGame getSaveGame(
      GameServiceBasicInfo gameService, String saveGameId, OauthToken token) {
    LsChecker.allNotNull(gameService, saveGameId, token);
    return getSaveGame(gameService.getName(), saveGameId, token.getAccessToken());
  }

  /**
   * Adds a particular {@link SaveGame}.
   * Corresponds to <code>PUT '/api/gameservices/{saveGame.name}/savegames/{saveGame}'</code>.
   *
   * @param saveGame        Save game data.
   * @param accessToken     Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                        registered the game service).
   * @throws NullPointerException if <code>gameServiceName</code>, <code>saveGame</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void addSaveGame(SaveGame saveGame, String accessToken) {
    LsChecker.allNotNull(saveGame, accessToken);
    LsRequest
        .put("/api/gameservices/" + saveGame.getGameName() + "/savegames/" + saveGame.getId())
        .addJsonAppHeader()
        .addAccessToken(accessToken)
        .addBody(saveGame)
        .execute();
  }

  /**
   * Adds a particular {@link SaveGame}.
   * Corresponds to <code>PUT '/api/gameservices/{saveGame.name}/savegames/{saveGame}'</code>.
   *
   * @param saveGame        Save game data.
   * @param token           Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                        registered the game service).
   * @throws NullPointerException if <code>gameServiceName</code>, <code>saveGame</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void addSaveGame(SaveGame saveGame, OauthToken token) {
    LsChecker.allNotNull(saveGame, token);
    addSaveGame(saveGame, token.getAccessToken());
  }

  /**
   * Deletes a particular {@link SaveGame}.
   * Corresponds to <code>DELETE '/api/gameservices/{serviceName}/savegames/{saveGameId}'</code>.
   *
   * @param serviceName Game service name.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>serviceName</code>, <code>saveGameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void deleteSaveGame(String serviceName, String saveGameId, String accessToken) {
    LsChecker.allNotNull(serviceName, saveGameId, accessToken);
    LsRequest
        .delete("/api/gameservices/" + serviceName + "/savegames/" + saveGameId)
        .addAccessToken(accessToken)
        .execute();
  }

  /**
   * Deletes a particular {@link SaveGame}.
   * Corresponds to <code>DELETE '/api/gameservices/{gameService}/savegames/{saveGameId}'</code>.
   *
   * @param gameService Game service basic information.
   * @param saveGameId  Save game id.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>gameService</code>, <code>saveGameId</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void deleteSaveGame(
      GameServiceBasicInfo gameService, String saveGameId, String accessToken) {
    LsChecker.allNotNull(gameService, saveGameId, accessToken);
    deleteSaveGame(gameService.getName(), saveGameId, accessToken);
  }

  /**
   * Deletes a particular {@link SaveGame}.
   * Corresponds to <code>DELETE '/api/gameservices/{saveGame.name}/savegames/{saveGame}'</code>.
   *
   * @param saveGame    Save game data.
   * @param accessToken Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>serviceName</code>, <code>saveGame</code> or
   *                              <code>accessToken</code> is <code>null</code>.
   */
  public static void deleteSaveGame(SaveGame saveGame, String accessToken) {
    LsChecker.allNotNull(saveGame, accessToken);
    deleteSaveGame(saveGame.getGameName(), saveGame.getId(), accessToken);
  }

  /**
   * Deletes a particular {@link SaveGame}.
   * Corresponds to <code>DELETE '/api/gameservices/{serviceName}/savegames/{saveGameId}'</code>.
   *
   * @param serviceName Game service name.
   * @param saveGameId  Save game id.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>serviceName</code>, <code>saveGameId</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void deleteSaveGame(String serviceName, String saveGameId, OauthToken token) {
    LsChecker.allNotNull(serviceName, saveGameId, token);
    deleteSaveGame(serviceName, saveGameId, token.getAccessToken());
  }

  /**
   * Deletes a particular {@link SaveGame}.
   * Corresponds to <code>DELETE '/api/gameservices/{gameService}/savegames/{saveGameId}'</code>.
   *
   * @param gameService Game service basic information.
   * @param saveGameId  Save game id.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>gameService</code>, <code>saveGameId</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void deleteSaveGame(
      GameServiceBasicInfo gameService, String saveGameId, OauthToken token) {
    LsChecker.allNotNull(gameService, saveGameId, token);
    deleteSaveGame(gameService.getName(), saveGameId, token.getAccessToken());
  }

  /**
   * Deletes a particular {@link SaveGame}.
   * Corresponds to <code>DELETE '/api/gameservices/{saveGame.name}/savegames/{saveGame}'</code>.
   *
   * @param saveGame    Save game data.
   * @param token       Access token ({@link UserRole#ROLE_ADMIN}, must be the admin who
   *                    registered the game service).
   * @throws NullPointerException if <code>serviceName</code>, <code>saveGame</code> or
   *                              <code>token</code> is <code>null</code>.
   */
  public static void deleteSaveGame(SaveGame saveGame, OauthToken token) {
    LsChecker.allNotNull(saveGame, token);
    deleteSaveGame(saveGame.getGameName(), saveGame.getId(), token.getAccessToken());
  }
}
