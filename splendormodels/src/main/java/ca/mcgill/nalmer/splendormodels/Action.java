package ca.mcgill.nalmer.splendormodels;

/**
 * Represents an action to execute.
 */
public abstract class Action {
  private final int id;

  /**
   * Constructor.
   *
   * @param actionId Action id.
   */
  protected Action(int actionId) {
    id = actionId;
  }

  /**
   * Executes the action.
   *
   * @param game   {@link Game} the action belongs to.
   * @param player {@link Player} to execute the action for.
   */
  public abstract void execute(Game game, Player player);

  /**
   * Gets the action id.
   *
   * @return Action id.
   */
  public final int getId() {
    return id;
  }

  /**
   * Gets the action type.
   *
   * @return Action type.
   */
  public abstract ActionType getType();
}
