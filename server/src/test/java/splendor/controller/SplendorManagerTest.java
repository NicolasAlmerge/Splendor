package splendor.controller;

import ca.mcgill.nalmer.splendormodels.Action;
import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.Game;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import ca.mcgill.nalmer.splendormodels.Player;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import ca.mcgill.nalmer.splendormodels.TakeTokensAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import splendor.config.AppConfigTest;

public final class SplendorManagerTest {
  private static final int NUMBER_INITIAL_TAKE_TOKENS_ACTIONS = 15;
  private static final int NUMBER_INITIAL_RESERVE_CARD_ACTIONS = 24;
  private final SplendorManager manager = new SplendorManager(AppConfigTest.getGsonBean());

  @After
  public void clearAllSessionsAfter() {
    manager.getGameManager().clear();
  }

  @Test
  public void getAllGamesEmpty() {
    Assert.assertEquals(0, manager.getAllGames().size());
  }

  @Test
  public void createGame() {
    String gameId = newGame();
    Assert.assertEquals(1, manager.getGameManager().getAllGames().size());
    Assert.assertTrue(manager.getGameManager().hasGame(gameId));
    Assert.assertEquals(gameId, manager.getGameManager().getGame(gameId).getId());
    Assert.assertEquals(gameId, manager.getGame(gameId).getId());
  }

  @Test
  public void getGameAsyncTest() {
    String id = newGame();
    Game game = manager.getGame(id);
    String hex = DigestUtils.md5Hex(AppConfigTest.getGsonBean().toJson(game));
    Assert.assertFalse(manager.getGameAsync(id, hex).hasResult());
  }

  @Test
  public void testGetActionsInitialBoard() {
    String gameId = newGame();
    Action[] actions = manager.getAllActions(gameId);

    Map<Integer, Action> takeTokens = new HashMap<>();
    Map<Integer, Action> reserveCard = new HashMap<>();
    Map<Integer, Action> purchaseCard = new HashMap<>();

    for (Action action : actions) {
      switch (action.getType()) {
        case TAKE_TOKENS -> takeTokens.put(action.getId(), action);
        case RESERVE_CARD -> reserveCard.put(action.getId(), action);
        case PURCHASE_CARD -> purchaseCard.put(action.getId(), action);
        default -> throw new RuntimeException("invalid type");
      }
    }

    // 5 dual tokens + (5 choose 3) combinations
    Assert.assertEquals(NUMBER_INITIAL_TAKE_TOKENS_ACTIONS, takeTokens.size());

    // 4 base + 2 orient cards per row, 3 rows + we can reserve the top card of any deck
    Assert.assertEquals(NUMBER_INITIAL_RESERVE_CARD_ACTIONS, reserveCard.size());

    // We cannot buy anything yet
    Assert.assertEquals(0, purchaseCard.size());

    // It should be the first player's index
    Assert.assertEquals(0, manager.getGame(gameId).getCurrentPlayerIndex());
  }

  @Test
  public void testGameSwitch() {
    String gameId = newGame();
    Action[] firstActions = manager.getAllActions(gameId);

    // Check action size
    final int actionSize = NUMBER_INITIAL_TAKE_TOKENS_ACTIONS + NUMBER_INITIAL_RESERVE_CARD_ACTIONS;
    Assert.assertEquals(actionSize, firstActions.length);

    // Post first action
    manager.postAction(gameId, 0);

    // Check that it is the next player's turn
    Assert.assertEquals(1, manager.getGame(gameId).getCurrentPlayerIndex());

    // Check action length is the same for the next player
    Action[] secondActions = manager.getAllActions(gameId);
    Assert.assertEquals(firstActions.length, secondActions.length);

    // Post 25th action
    manager.postAction(gameId, 25);

    // Check that it is the next player's turn
    Assert.assertEquals(2, manager.getGame(gameId).getCurrentPlayerIndex());

    // Check action length is the same for the next player
    Action[] thirdActions = manager.getAllActions(gameId);
    Assert.assertEquals(secondActions.length, thirdActions.length);
  }

  @Test
  public void testTakeTokensWhenUserHasEight() {
    Game game = manager.getGame(newGame());

    Player currentPlayer = game.getCurrentPlayer();
    currentPlayer.addTokens(Map.of(
        ResourceColor.RED, 2,
        ResourceColor.GREEN, 3,
        ResourceColor.BLACK, 3
    ));

    List<TakeTokensAction> actions = new ActionGenerator().generateTokenTakingActions(game);
    for (TakeTokensAction takeTokensAction : actions) {
      int number = 0;
      for (ResourceColor color : Cost.getAllPossibleColors()) {
        int costColor = takeTokensAction.getCost().getCost(color);
        Assert.assertTrue(costColor == 0 || costColor == 1 || costColor == 2);
        number += costColor;
      }

      Assert.assertEquals(2, number);
    }
  }

  @Test
  public void testTakeTokensWhenUserHasNine() {
    Game game = manager.getGame(newGame());

    Player currentPlayer = game.getCurrentPlayer();
    currentPlayer.addTokens(Map.of(
        ResourceColor.BLUE, 3,
        ResourceColor.WHITE, 3,
        ResourceColor.RED, 3
    ));

    List<TakeTokensAction> actions = new ActionGenerator().generateTokenTakingActions(game);
    for (TakeTokensAction takeTokensAction : actions) {
      int number = 0;
      for (ResourceColor color : Cost.getAllPossibleColors()) {
        int costColor = takeTokensAction.getCost().getCost(color);
        Assert.assertTrue(costColor == 0 || costColor == 1);
        number += costColor;
      }

      Assert.assertEquals(1, number);
    }
  }

  @Test
  public void testTakeTokensWhenUserHasTen() {
    Game game = manager.getGame(newGame());

    Player currentPlayer = game.getCurrentPlayer();
    currentPlayer.addTokens(Map.of(
        ResourceColor.WHITE, 3,
        ResourceColor.BLUE, 1,
        ResourceColor.GREEN, 3,
        ResourceColor.BLACK, 3
    ));

    List<TakeTokensAction> actions = new ActionGenerator().generateTokenTakingActions(game);
    Assert.assertEquals(0, actions.size());
  }

  @Test
  public void testPostActionInitialBoard() {
    String gameId = newGame();
    Game game = manager.getGame(gameId);

    Action[] actions = manager.getAllActions(gameId);
    Assert.assertNotEquals(0, actions.length);

    Player currentPlayer = game.getCurrentPlayer();
    manager.postAction(gameId, 0);

    Map<ResourceColor, Integer> expected = Map.of(
        ResourceColor.WHITE, 1,
        ResourceColor.BLUE, 1,
        ResourceColor.GREEN, 1,
        ResourceColor.RED, 0,
        ResourceColor.BLACK, 0,
        ResourceColor.GOLD, 0
    );

    Assert.assertEquals(expected, currentPlayer.getTokens());
  }

  private String newGame() {
    return manager.createGame(new String[] {"maex", "linus", "a", "b"}, "0",
        GameExtension.BASE_ORIENT);
  }

  @Test
  public void putSavedGame() {
    String gameId = newGame();
    manager.putSavedGame(gameId, new String[] {"maex", "linus"});
    Assert.assertEquals(1, manager.getGameManager().getAllSaveGames().size());
    Assert.assertTrue(manager.getGameManager().hasSaveGame(gameId));
    Assert.assertEquals(gameId, manager.getGameManager().getSaveGame(gameId).getId());
    Assert.assertEquals(gameId, manager.getSaveGame(gameId).getId());
  }

}
