package splendor.controller;

import ca.mcgill.nalmer.lsutilities.controller.LsGameServicesController;
import ca.mcgill.nalmer.lsutilities.controller.LsSessionController;
import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import ca.mcgill.nalmer.lsutilities.model.Session;
import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import ca.mcgill.nalmer.splendormodels.Action;
import ca.mcgill.nalmer.splendormodels.ActionGeneratorInterface;
import ca.mcgill.nalmer.splendormodels.Game;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import ca.mcgill.nalmer.splendormodels.Player;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import ca.mcgill.nalmer.splendormodels.Utils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.gson.Gson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import splendor.model.GameManager;

/**
 * Manages game actions.
 */
@RestController
public class SplendorManager {
  private static final long LONG_POLL_TIMEOUT = 30000;

  private final GameManager gameManager = new GameManager();
  private final Map<String, BroadcastContentManager<Game>> gameBroadCastManagers = new HashMap<>();
  private final Gson gson;

  private final Map<String, Game> savedGames = new HashMap<>();

  /**
   * Constructor.
   *
   * @param gsonBean Gson bean.
   * @hidden
   */
  public SplendorManager(@Autowired Gson gsonBean) {
    gson = gsonBean;
  }

  /**
   * Checks whether the server is online.
   *
   * @return <code>"Server is online!"</code>.
   */
  @GetMapping("/online")
  public String testOnline() {
    return "Server is online!";
  }

  /**
   * Gets a {@link Game} asynchronously.
   *
   * @param gameId {@link Game} id.
   * @param hash   {@link Game} hash.
   * @return Updated {@link Game}.
   */
  @GetMapping("/games/async/{gameId}")
  public DeferredResult<ResponseEntity<String>> getGameAsync(
      @PathVariable String gameId, @RequestParam String hash
  ) {
    return ResponseGenerator.getHashBasedUpdate(
        LONG_POLL_TIMEOUT,
        gameBroadCastManagers.get(gameId),
        hash
    );
  }

  /**
   * Get all {@link Game}.
   *
   * @return Map of all id with their associated {@link Game}.
   */
  @GetMapping("/games")
  public Map<String, Game> getAllGames() {
    return gameManager.getAllGames();
  }

  /**
   * Get all {@link Game}.
   *
   * @return Map of all id with their associated {@link Game}.
   */
  @GetMapping("/savegames")
  public Map<String, Game> getAllSaveGames() {
    return gameManager.getAllSaveGames();
  }

  /**
   * Get a board game.
   *
   * @param gameId The id of the game to get the board game from.
   * @return Board game for this game.
   */
  @GetMapping("/games/{gameId}")
  public Game getGame(@PathVariable("gameId") String gameId) {
    return gameManager.getGame(gameId);
  }

  /**
   * Creates a game from a list of players.
   *
   * @param players   List of players.
   * @param sessionId Session id.
   * @param extension {@link GameExtension} to use.
   * @return Game id.
   */
  String createGame(String[] players, String sessionId, GameExtension extension) {
    List<User> users = new ArrayList<>();
    for (String username : players) {
      users.add(new User(username, "", "", UserRole.ROLE_PLAYER));
    }

    // Create and initialise a new game
    Game game = new Game(users, extension, new CardGenerator(), new ActionGenerator(), sessionId);

    // Custom serializer
    StdSerializer<Game> jsonSerializer = new StdSerializer<>(Game.class) {
      @Override
      public void serialize(
          Game game, JsonGenerator jsonGenerator,
          SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeRaw(gson.toJson(game));
        jsonGenerator.close();
      }
    };

    // Customer mapper
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(Game.class, jsonSerializer);
    mapper.registerModule(module);

    // Add game and return id
    gameManager.addGame(game);
    gameBroadCastManagers.put(sessionId, new BroadcastContentManager<>(mapper, game));
    return game.getId();
  }

  /**
   * Creates a new game.
   *
   * @param sessionId Session id to create the game from.
   * @param extension {@link GameExtension} to use.
   * @return Game id.
   * @throws IOException if file doesn't exist.
   */
  @PostMapping("/games/{sessionId}")
  public String createGame(@PathVariable String sessionId,
                           @RequestParam GameExtension extension) throws IOException {
    Session session = LsSessionController.getSession(sessionId);
    if (session.getSaveGameId().equals("")) {
      return createGame(session.getPlayers(), sessionId, extension);
    } else {
      return createGame(session.getPlayers(), sessionId);
    }
  }

  /**
   * Creates a game by loading a saved game.
   *
   * @param players   List of players.
   * @param sessionId Session id.
   * @return Game id.
   */
  String createGame(String[] players, String sessionId) throws IOException {
    System.out.println(Arrays.toString(players) + ", " + sessionId);
    File file = new File(sessionId + ".json");

    Game newGame = null;

    if (file.exists()) {
      FileReader reader = new FileReader(file);
      StringBuilder sb = new StringBuilder();

      Scanner myReader = new Scanner(file);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        sb.append(data);
      }

      newGame = Utils.getGameGson().fromJson(sb.toString(), Game.class);
      reader.close();
    }



    for (int i = 0; i < newGame.getPlayers().size(); ++i) {
      newGame.getPlayers().get(i).setUser(
              new User(players[i], "", "", UserRole.ROLE_PLAYER)
      );
    }
    newGame.setNewId(sessionId);

    // Custom serializer
    StdSerializer<Game> jsonSerializer = new StdSerializer<>(Game.class) {
      @Override
      public void serialize(
              Game game, JsonGenerator jsonGenerator,
              SerializerProvider serializerProvider
      ) throws IOException {
        jsonGenerator.writeRaw(gson.toJson(game));
        jsonGenerator.close();
      }
    };

    // Customer mapper
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(Game.class, jsonSerializer);
    mapper.registerModule(module);

    // Add game and return id
    gameManager.addGame(newGame);
    gameBroadCastManagers.put(sessionId, new BroadcastContentManager<>(mapper, newGame));
    return newGame.getId();
  }

  /**
   * Get all the actions for a game.
   *
   * @param gameId Game id.
   * @return Array of all the actions the current player can do.
   */
  @GetMapping("/games/{gameId}/actions")
  public Action[] getAllActions(@PathVariable("gameId") String gameId) {
    return gameManager.getGame(gameId).getAllActions().toArray(Action[]::new);
  }

  /**
   * Request an action execution.
   *
   * @param gameId   The id of the game to execute the action to.
   * @param actionId The id of the action to execute.
   */
  @PutMapping("/games/{gameId}/actions/{actionId}")
  public void postAction(
      @PathVariable("gameId") String gameId,
      @PathVariable("actionId") int actionId
  ) {
    Game game = gameManager.getGame(gameId);
    game.executeAction(actionId, new ActionGenerator());
    gameBroadCastManagers.get(gameId).touch();
  }

  /**
   * Request a city selection.
   *
   * @param gameId The id of the game to execute the action to.
   * @param cityId The id of the action to execute.
   */
  @PutMapping("/games/{gameId}/cityselection/{cityId}")
  public void postCitySelection(
      @PathVariable("gameId") String gameId,
      @PathVariable("cityId") int cityId
  ) {
    Game game = gameManager.getGame(gameId);
    game.executeCityUnlockAction(cityId, new ActionGenerator());
    gameBroadCastManagers.get(gameId).touch();
  }

  /**
   * Request a token selection.
   *
   * @param gameId The id of the game to execute the action to.
   * @param color  The color of the token to take.
   */
  @PutMapping("/games/{gameId}/tokenselection/{color}")
  public void postCitySelection(
      @PathVariable("gameId") String gameId,
      @PathVariable("color") ResourceColor color
  ) {
    Game game = gameManager.getGame(gameId);
    game.executeTokenSelectionAction(color, new ActionGenerator());
    gameBroadCastManagers.get(gameId).touch();
  }

  /**
   * Creates a save game.
   *
   * @param gameId  The sessionId that we want to add.
   * @param players Usernames of players in the game.
   * @return SaveGame object
   */
  public SaveGame putSavedGame(String gameId, String[] players) {
    SaveGame savedGame = new SaveGame("splendor", players, gameId);
    gameManager.addSaveGame(gameId, gameManager.getGame(gameId));
    return savedGame;
  }

  /**
   * Sends a save game to the lobby service.
   *
   * @param gameId The saveGame that we want to add.
   */
  @PostMapping("/saveGames/{gameId}")
  public void putSavedGame(@PathVariable("gameId") String gameId) {
    SaveGame savedGame = putSavedGame(gameId, LsSessionController.getSession(gameId).getPlayers());
    LsGameServicesController.addSaveGame(savedGame, GameServiceRegistrator.getServiceToken());
  }

  /**
   * Get a save game.
   *
   * @param gameId The id of the game to get the board game from.
   * @return Board game for this game.
   */
  @GetMapping("/saveGames/{gameId}")
  public Game getSaveGame(@PathVariable("gameId") String gameId) {
    return gameManager.getSaveGame(gameId);
  }

  /**
   * Get a save game.
   *
   * @param gameId The id of the game to get the board game from.
   * @param oldId Old game id.
   */
  @PostMapping("/saveGames/map/{gameId}")
  public void createSessionFromSaveGame(@PathVariable("gameId") String gameId,
                                        @RequestParam String oldId) {
    savedGames.put(gameId, gameManager.getGame(oldId));

    String json = Utils.getGameGson().toJson(gameManager.getGame(oldId), Game.class);

    try {
      FileWriter writer = new FileWriter(gameId + ".json");

      // Write the JSON string to the file
      writer.write(json);

      // Close the file
      writer.close();

      System.out.println("Game object is written to game.json");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  GameManager getGameManager() {
    return gameManager;
  }
}
