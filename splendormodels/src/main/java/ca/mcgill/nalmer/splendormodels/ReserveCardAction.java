package ca.mcgill.nalmer.splendormodels;

/**
 * Action that represents a card reservation.
 */
public final class ReserveCardAction extends Action {
  private final DevelopmentCard card;

  /**
   * Constructor - reserves a card.
   *
   * @param card PrestigeCard to reserve.
   * @param id   Action id.
   * @throws NullPointerException if <code>card</code> is <code>null</code>.
   */
  public ReserveCardAction(DevelopmentCard card, int id) {
    super(id);
    if (card == null) {
      throw new NullPointerException("card cannot be null");
    }
    this.card = card;
  }

  /**
   * Gets reserved card.
   *
   * @return Reserved card.
   */
  public DevelopmentCard getCard() {
    return card;
  }

  @Override
  public void execute(Game game, Player player) {
    player.addReservedCard(card);
    game.removeDevelopmentCard(card);

    // Transfer one gold token from the board to the player if possible
    if (game.hasTokensOfColor(ResourceColor.GOLD)
        && player.getNumberOfTokens() < Constants.USER_MAX_NUMBER_OF_TOKENS) {
      game.removeOneGoldToken();
      player.addOneGoldToken();
    }
  }

  @Override
  public ActionType getType() {
    return ActionType.RESERVE_CARD;
  }
}
