package splendor.view;

/**
 * Contains constants for the sizing of nodes in the game board specifically.
 */
final class GameBoardConstants {

  /**
   * Minimum width of development cards and noble cards on the game area.
   */
  static final double MIN_BOARD_CARD_WIDTH = 80.0;
  /**
   * Minimum size/diameter of tokens on the game area.
   */
  static final double MIN_BOARD_TOKEN_SIZE = 50.0;
  /**
   * Minimum width of development cards and noble cards in the Other Players area.
   */
  static final double MIN_PLAYER_OTHER_CARD_WIDTH = 30.0;
  /**
   * Minimum width of development cards and noble cards in Main Player area.
   */
  static final double MIN_PLAYER_MAIN_CARD_WIDTH = 60.0;
  /**
   * Minimum size of the tokens and bonus shapes for Players.
   */
  static final double MIN_PLAYER_SHAPE_SIZE = 40.0;
  /**
   * Size of a card that is displayed in the reserve/buy card prompt.
   */
  static final double PROMPT_CARD_WIDTH = 200.0;

  static final double EXTENSION_CARD_HEIGHT = 120.0;

  /**
   * Private constructor.
   */
  private GameBoardConstants() {
  }
}
