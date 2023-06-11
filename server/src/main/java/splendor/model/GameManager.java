package splendor.model;

import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import ca.mcgill.nalmer.splendormodels.Game;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the collection of all games.
 */
public final class GameManager implements BroadcastContent {
  private final Map<String, Game> gamesMap = new HashMap<>();

  private final Map<String, Game> saveGamesMap = new HashMap<>();



  /**
   * Constructor.
   */
  public GameManager() {
  }

  /**
   * Gets a {@link Game} from an id.
   *
   * @param id {@link Game} id.
   * @return {@link Game} with corresponding id.
   * @throws NullPointerException if <code>id</code> is <code>null</code>.
   * @throws RuntimeException     if game not found.
   */
  public Game getGame(String id) throws RuntimeException {
    if (id == null) {
      throw new NullPointerException("game id cannot be null");
    }

    Game game = gamesMap.get(id);
    if (game == null) {
      throw new RuntimeException("cannot get game with specified id");
    }

    return game;
  }

  /**
   * Get all {@link Game}.
   *
   * @return All games as a {@link Map} from {@link String} (ids) to {@link Game}.
   */
  public Map<String, Game> getAllGames() {
    return gamesMap;
  }


  /**
   * Checks if we have a {@link Game} with a specific id.
   *
   * @param id {@link Game} id.
   * @return <code>true</code> if such {@link Game} exists, <code>false</code> otherwise.
   * @throws NullPointerException if <code>id</code> is <code>null</code>.
   */
  public boolean hasGame(String id) {
    if (id == null) {
      throw new NullPointerException("id cannot be null");
    }
    return gamesMap.containsKey(id);
  }

  /**
   * Sets a {@link Game} with a special id.
   *
   * @param game {@link Game} to add.
   * @throws NullPointerException if <code>game</code> is <code>null</code>.
   */
  public void addGame(Game game) {
    if (game == null) {
      throw new NullPointerException("game cannot be null");
    }
    gamesMap.put(game.getId(), game);
  }

  /**
   * Removes a {@link Game} with a certain id. This has no effects if the {@link Game} was not
   * present.
   *
   * @param id Game id.
   * @throws NullPointerException if <code>id</code> is <code>null</code>.
   */
  public void removeGame(String id) {
    if (id == null) {
      throw new NullPointerException("id cannot be null");
    }
    gamesMap.remove(id);
  }

  /**
   * Removes a {@link Game}. This has no effects if the {@link Game} was not present.
   *
   * @param game {@link Game} to remove.
   * @throws NullPointerException if <code>game</code> is <code>null</code>.
   */
  public void removeGame(Game game) {
    if (game == null) {
      throw new NullPointerException("game cannot be null");
    }
    removeGame(game.getId());
  }

  /**
   * Clears all games.
   */
  public void clear() {
    gamesMap.clear();
  }

  /**
   * Checks whether the game manager is empty or not.
   *
   * @return <code>true</code> if there are no games registered, <code>false</code> otherwise.
   */
  @Override
  public boolean isEmpty() {
    return gamesMap.isEmpty();
  }

  /**
   * Checks whether the game manager for saved games is empty or not.
   *
   * @return <code>true</code> if there are no saved games, <code>false</code> otherwise.
   */
  public boolean isSaveGameEmpty() {
    return saveGamesMap.isEmpty();
  }

  /**
   * Gets a {@link SaveGame} from an id.
   *
   * @param id {@link SaveGame} id.
   * @return {@link SaveGame} with corresponding id.
   * @throws NullPointerException if <code>id</code> is <code>null</code>.
   * @throws RuntimeException     if game not found.
   */
  public Game getSaveGame(String id) throws RuntimeException {
    if (id == null) {
      throw new NullPointerException("game id cannot be null");
    }

    Game game = saveGamesMap.get(id);
    if (game == null) {
      throw new RuntimeException("cannot get save game with specified id : " + id);
    }

    return game;
  }

  /**
   * Get all {@link SaveGame}.
   *
   * @return All games as a {@link Map} from {@link String} (ids) to {@link SaveGame}.
   */
  public Map<String, Game> getAllSaveGames() {
    return saveGamesMap;
  }

  /**
   * Sets a {@link SaveGame} with a special id.
   *
   * @param game {@link SaveGame} to add.
   * @throws NullPointerException if <code>save game</code> is <code>null</code>.
   */
  public void addSaveGame(String id, Game game) {
    if (game == null) {
      throw new NullPointerException("save game cannot be null");
    }
    // use session id as key not the id of the game
    saveGamesMap.put(id, game);
  }

  /**
   * Checks if we have a {@link SaveGame} with a specific id.
   *
   * @param id {@link SaveGame} id.
   * @return <code>true</code> if such {@link SaveGame} exists, <code>false</code> otherwise.
   * @throws NullPointerException if <code>id</code> is <code>null</code>.
   */
  public boolean hasSaveGame(String id) {
    if (id == null) {
      throw new NullPointerException("id cannot be null");
    }
    return saveGamesMap.containsKey(id);
  }
}
