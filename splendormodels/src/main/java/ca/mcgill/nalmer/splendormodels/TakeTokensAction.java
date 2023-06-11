package ca.mcgill.nalmer.splendormodels;

import java.util.HashMap;
import java.util.Map;

/**
 * Action that represents token selection.
 */
public final class TakeTokensAction extends Action {
  private final Cost cost;

  /**
   * Constructor - specify a cost.
   *
   * @param cost Cost of tokens to take.
   * @param id   Action id.
   * @throws NullPointerException if <code>cost</code> is <code>null</code>.
   */
  public TakeTokensAction(Cost cost, int id) {
    super(id);
    if (cost == null) {
      throw new NullPointerException("cost cannot be null");
    }
    this.cost = cost;
  }

  /**
   * Gets the amount of each chosen token.
   *
   * @return The amount of each chosen token.
   */
  public Cost getCost() {
    return cost;
  }

  @Override
  public void execute(Game game, Player player) {
    Map<ResourceColor, Integer> tokensToAdd = new HashMap<>();
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      tokensToAdd.put(color, cost.getCost(color));
    }
    player.addTokens(tokensToAdd);
    game.removeTokens(tokensToAdd);
  }

  @Override
  public ActionType getType() {
    return ActionType.TAKE_TOKENS;
  }
}
