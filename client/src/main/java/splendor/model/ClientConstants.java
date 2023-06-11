package splendor.model;

/**
 * Represents constants.
 */
public final class ClientConstants {
  /**
   * Game minimum height (in pixels).
   */
  public static final int MIN_HEIGHT = 800;

  /**
   * Game minimum width (in pixels).
   */
  public static final int MIN_WIDTH = 1200;

  /**
   * Lobby page small table cell size (in pixels).
   */
  public static final int LOBBY_PAGE_TABLE_CELL_SMALL = 200;

  /**
   * Lobby page very small table cell size (in pixels).
   */
  public static final int LOBBY_PAGE_TABLE_CELL_EXTRA_SMALL = 80;

  /**
   * Lobby page big table cell size (in pixels).
   */
  public static final int LOBBY_PAGE_TABLE_CELL_BIG = 300;

  /**
   * Golden button CSS style.
   */
  public static final String GOlDEN_BUTTON_STYLE =
          "-fx-background-color: gold; -fx-font-size: 20";

  /**
   * Red button CSS style.
   */
  public static final String RED_BUTTON_STYLE =
      "-fx-background-color: Red; -fx-font-size: 20";

  /**
   * Standard button CSS style.
   */
  public static final String STANDARD_BUTTON_STYLE =
      "-fx-background-color: #FFD700; -fx-font-size: 20";

  /**
   * Faded button CSS style.
   */
  public static final String FADED_BUTTON_STYLE =
      "-fx-text-fill: white; -fx-background-color: gray; -fx-font-size: 20";

  /**
   * Max number of players.
   */
  public static final int MAX_PLAYERS_AMT = 4;

  /**
   * Private constructor.
   */
  private ClientConstants() {
  }
}