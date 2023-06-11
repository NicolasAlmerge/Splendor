package splendor.view;

import ca.mcgill.nalmer.splendormodels.Action;
import ca.mcgill.nalmer.splendormodels.Card;
import ca.mcgill.nalmer.splendormodels.CardLevel;
import ca.mcgill.nalmer.splendormodels.City;
import ca.mcgill.nalmer.splendormodels.Constants;
import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.DevelopmentCard;
import ca.mcgill.nalmer.splendormodels.DevelopmentCardType;
import ca.mcgill.nalmer.splendormodels.Game;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import ca.mcgill.nalmer.splendormodels.GamePhase;
import ca.mcgill.nalmer.splendormodels.GameStatus;
import ca.mcgill.nalmer.splendormodels.Noble;
import ca.mcgill.nalmer.splendormodels.OrientAbility;
import ca.mcgill.nalmer.splendormodels.OrientDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.Player;
import ca.mcgill.nalmer.splendormodels.PrestigeCardType;
import ca.mcgill.nalmer.splendormodels.PurchaseCardAction;
import ca.mcgill.nalmer.splendormodels.ReserveCardAction;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import ca.mcgill.nalmer.splendormodels.TakeTokensAction;
import ca.mcgill.nalmer.splendormodels.TradingPostAbility;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import splendor.controller.ClientServerController;
import splendor.controller.ResourceManager;
import splendor.model.ClientConstants;
import splendor.model.UserData;
//todo highlight selection area

/**
 * Graphical interface that shows the state of one game of Splendor and allows
 * the player to select actions to perform.
 */

public final class GameBoardPage extends GridPane {
  private final Button exitButton;
  private final Label turnLabel;
  // Parts of the GUI that can be interacted with or that can be changed dynamically
  private final Label gameIdLabel;
  private final Label extensionLabel;
  private final Map<ResourceColor, NumberedPane<ImageView>> tokenViews;
  private final Button takeTokenConfirmButton;
  private final Button takeTokenCancelButton;
  private HBox nobleRow;
  private final List<CardView> nobleViews;
  private final Map<CardLevel, List<CardView>> baseCardViews;
  private final Map<CardLevel, List<CardView>> orientCardViews;
  private final List<CardView> allDevCardViews;

  private final Map<CardLevel, NumberedPane<ImageView>> baseDeckViews;
  private final Map<CardLevel, NumberedPane<ImageView>> orientDeckViews;

  private final PlayerView mainPlayerView;
  private final List<PlayerView> otherPlayerViews;

  private final CardInteractionPane cardPrompt;
  private final BonusMatchingSelectionPrompt bonusMatchingPrompt;

  // Expansions
  private final HBox extensionsRow;
  private List<CardView> citiesViews;
  private TradingPostsView tradingPostsView;

  // Action handling
  private final Map<Cost, Integer> takeTokenActions;
  private final Map<String, Integer> stringTakeTokenActions;
  private final Map<Integer, Integer> reserveCardActions;
  private final HashMap<PurchaseCardActionInfo, Integer> purchaseCardActions;
  private final PurchaseCardActionInfo purchaseCardBuffer;
  private final Map<ResourceColor, Integer> tokenSelectionBuffer;
  private final Logger logger = LoggerFactory.getLogger(GameBoardPage.class);
  private final List<Integer> purchaseCardOne;
  private int tokenSelectionCount = 0;

  // Misc
  private boolean gameHasStarted;
  private Game game;
  private Player mainPlayer;
  private Thread thread;
  private static final String GRAY_STYLE = "-fx-background-color: gray;";
  private PopUpError popUpGameOver;


  /**
   * Constructs the Game Board with an empty game state.
   * Use only when first launching the application.
   *
   * @param popUpGameOver launch args.
   */
  public GameBoardPage(PopUpError popUpGameOver) {
    this.popUpGameOver = popUpGameOver;
    // Initialize field objects
    takeTokenActions = new HashMap<>();
    stringTakeTokenActions = new HashMap<>();
    reserveCardActions = new HashMap<>();
    purchaseCardActions = new HashMap<>();
    purchaseCardOne = new ArrayList<>();
    purchaseCardBuffer = new PurchaseCardActionInfo();
    tokenSelectionBuffer = new EnumMap<>(ResourceColor.class);

    cardPrompt = new CardInteractionPane();
    cardPrompt.setVisible(false);
    gameHasStarted = false;

    bonusMatchingPrompt = new BonusMatchingSelectionPrompt();
    bonusMatchingPrompt.setVisible(false);

    exitButton = new Button();
    Image exitButtonImage = ResourceManager.getExitButtonImage();
    ImageView exitButtonView = new ImageView(exitButtonImage);
    exitButtonView.setFitHeight(30);
    exitButtonView.setPreserveRatio(true);
    exitButton.setGraphic(exitButtonView);

    final Button saveButton = new Button();
    Image saveButtonImage = ResourceManager.getSaveButtonImage();
    ImageView saveButtonView = new ImageView(saveButtonImage);
    saveButtonView.setFitHeight(30);
    saveButtonView.setPreserveRatio(true);
    saveButton.setGraphic(saveButtonView);

    turnLabel = new Label("Game hasn't started");
    turnLabel.setStyle("-fx-text-fill: #0000ff");
    gameIdLabel = new Label("####");
    extensionLabel = new Label("extensionLabel");

    takeTokenConfirmButton = new Button();
    Image confirmButton = ResourceManager.getConfirmButtonImage();
    ImageView confirmButtonView = new ImageView(confirmButton);
    confirmButtonView.setFitHeight(30);
    confirmButtonView.setPreserveRatio(true);
    takeTokenConfirmButton.setGraphic(confirmButtonView);

    takeTokenCancelButton = new Button();
    Image cancelButton = ResourceManager.getCancelButtonImage();
    ImageView cancelButtonView = new ImageView(cancelButton);
    cancelButtonView.setFitHeight(30);
    cancelButtonView.setPreserveRatio(true);
    takeTokenCancelButton.setGraphic(cancelButtonView);

    tokenViews = new EnumMap<>(ResourceColor.class);
    for (ResourceColor color : ResourceColor.values()) {
      tokenViews.put(color, new NumberedPane<>(new ImageView()));
      tokenViews.get(color).getRoot().setPreserveRatio(true);
      tokenViews.get(color).getRoot().setImage(ResourceManager.getTokenImage(color));
      tokenViews.get(color).getRoot().setFitWidth(GameBoardConstants.MIN_BOARD_TOKEN_SIZE);
    }

    nobleViews = new ArrayList<>(5);
    baseCardViews = new EnumMap<>(CardLevel.class);
    orientCardViews = new EnumMap<>(CardLevel.class);
    allDevCardViews = new ArrayList<>();
    baseDeckViews = new EnumMap<>(CardLevel.class);
    orientDeckViews = new EnumMap<>(CardLevel.class);

    for (CardLevel level : CardLevel.values()) {
      ArrayList<CardView> baseCardsList = new ArrayList<>(4);
      baseCardViews.put(level, baseCardsList);
      for (int i = 0; i < Constants.NUMBER_BASE_CARDS_PER_ROW; i++) {
        baseCardsList.add(new CardView());
        baseCardsList.get(i).setFitWidth(GameBoardConstants.MIN_BOARD_CARD_WIDTH);
        allDevCardViews.add(baseCardsList.get(i));
      }

      ArrayList<CardView> orientCardsList = new ArrayList<>(2);
      orientCardViews.put(level, orientCardsList);
      for (int i = 0; i < Constants.NUMBER_ORIENT_CARDS_PER_ROW; i++) {
        orientCardsList.add(new CardView());
        orientCardsList.get(i).setFitWidth(GameBoardConstants.MIN_BOARD_CARD_WIDTH);
        allDevCardViews.add(orientCardsList.get(i));
      }

      ImageView baseDeckImgView = new ImageView();
      baseDeckImgView.setImage(ResourceManager.getCardBack(PrestigeCardType.BASE, level));
      baseDeckImgView.setPreserveRatio(true);
      baseDeckImgView.setFitWidth(GameBoardConstants.MIN_BOARD_CARD_WIDTH);
      baseDeckViews.put(level, new NumberedPane<>(baseDeckImgView));

      ImageView orientDeckImgView = new ImageView();
      orientDeckImgView.setImage(ResourceManager.getCardBack(PrestigeCardType.ORIENT, level));
      orientDeckImgView.setPreserveRatio(true);
      orientDeckImgView.setFitWidth(GameBoardConstants.MIN_BOARD_CARD_WIDTH);
      orientDeckViews.put(level, new NumberedPane<>(orientDeckImgView));
    }

    // Structure of the GUI
    // Developments cards and decks (base + orient)
    GridPane baseCardGrid = new GridPane();
    GridPane orientCardGrid = new GridPane();
    for (CardLevel level : CardLevel.values()) {
      int row = CardLevel.values().length - level.ordinal() - 1;

      baseCardGrid.add(baseDeckViews.get(level), 0, row);
      for (int col = 0; col < 4; col++) {
        baseCardGrid.add(baseCardViews.get(level).get(col), col + 1, row);
      }
      for (int col = 0; col < 2; col++) {
        orientCardGrid.add(orientCardViews.get(level).get(col), col, row);
      }
      orientCardGrid.add(orientDeckViews.get(level), 2, row);
    }

    // Nobles
    nobleRow = new HBox();

    // Tokens
    HBox tokenRow = new HBox();
    for (ResourceColor color : ResourceColor.values()) {
      tokenRow.getChildren().add(tokenViews.get(color));
    }
    VBox tokenButtons = new VBox(takeTokenConfirmButton, takeTokenCancelButton);
    tokenRow.getChildren().add(tokenButtons);

    // Players
    mainPlayerView = new PlayerView(PlayerView.PlayerViewMode.MAIN);
    VBox otherPlayersColumn = new VBox();
    otherPlayerViews = new ArrayList<>(3);
    for (int i = 0; i < 3; i++) {
      otherPlayerViews.add(new PlayerView(PlayerView.PlayerViewMode.OTHER));
      otherPlayerViews.get(i).view.setVisible(false);
      otherPlayersColumn.getChildren().add(otherPlayerViews.get(i).view);
    }

    // Extensions
    citiesViews = new ArrayList<>(3);
    for (int i = 0; i < 3; i++) {
      CardView cityView = new CardView();
      cityView.setEmptyCityCard();
      cityView.setFitHeight(GameBoardConstants.EXTENSION_CARD_HEIGHT);
      citiesViews.add(cityView);
    }
    tradingPostsView = new TradingPostsView();

    VBox gameBoardMiddle = new VBox(
        nobleRow, new HBox(50.0, baseCardGrid, orientCardGrid), tokenRow
    );

    StackPane gameBoardMiddleAndPrompts = new StackPane();
    gameBoardMiddleAndPrompts.getChildren()
        .addAll(gameBoardMiddle, cardPrompt, bonusMatchingPrompt);

    Image backGround = ResourceManager.getBackGroundImage();
    BackgroundImage backgroundImage = new BackgroundImage(
            backGround,
            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
            new BackgroundSize(1.0, 1.0, true, true, false, false));
    gameBoardMiddleAndPrompts.setBackground(new Background(backgroundImage));

    extensionsRow = new HBox(10);
    HBox gameBoardTopRow = new HBox(new HBox(exitButton, saveButton), turnLabel,
        gameIdLabel, extensionLabel);
    super.add(gameBoardTopRow, 0, 0);
    super.add(extensionsRow, 0, 1);
    super.add(gameBoardMiddleAndPrompts, 0, 2);
    super.add(mainPlayerView.view, 0, 3);
    super.add(otherPlayersColumn, 1, 0, 1, 4);

    // Actions
    assignDefaultActions();

    cardPrompt.setCancelButtonEvent(e -> {
      clearActionBuffers();
      assignDefaultActions();
      cardPrompt.setVisible(false);
    });

    takeTokenCancelButton.setOnAction(e -> {
      clearActionBuffers();
      assignDefaultActions();
    });

    takeTokenConfirmButton.setOnAction(e -> {
      String selection = stringCost(new Cost(tokenSelectionBuffer));
      if (stringTakeTokenActions.containsKey(selection)) {
        logger.debug("Sending game action with selected cost to server");
        ClientServerController.sendGameAction(
            game.getId(),
            stringTakeTokenActions.get(selection)
        );
        logger.debug("Sending token taking action finished");
        takeTokenConfirmButton.setVisible(false);
        takeTokenCancelButton.setVisible(false);
      } else {
        logger.debug("Not a valid token taking action");
        clearActionBuffers();
      }
    });

    // Styling
    setVgap(10.0);
    baseCardGrid.setHgap(5.0);
    baseCardGrid.setVgap(5.0);
    orientCardGrid.setHgap(5.0);
    orientCardGrid.setVgap(5.0);
    nobleRow.setSpacing(5.0);

    tokenButtons.setMaxHeight(GameBoardConstants.MIN_BOARD_TOKEN_SIZE);

    otherPlayersColumn.setSpacing(10.0);

    setAlignment(Pos.CENTER);

    // Set sizes
    setMinSize(ClientConstants.MIN_WIDTH, ClientConstants.MIN_HEIGHT);
    setPrefSize(ClientConstants.MIN_WIDTH, ClientConstants.MIN_HEIGHT);
    setMaxSize(ClientConstants.MIN_WIDTH, ClientConstants.MIN_HEIGHT);

    double leftWidth = ClientConstants.MIN_WIDTH * 0.75;
    double topRowHeight = 30.0;
    gameBoardTopRow.setMinSize(leftWidth, topRowHeight);
    gameBoardTopRow.setPrefSize(leftWidth, topRowHeight);
    gameBoardTopRow.setMaxSize(leftWidth, topRowHeight);

    double middleHeight = 500;
    gameBoardMiddle.setMinSize(leftWidth, middleHeight);
    gameBoardMiddleAndPrompts.setMinSize(leftWidth, middleHeight);
    cardPrompt.setMinSize(leftWidth, middleHeight);
    bonusMatchingPrompt.setMinSize(leftWidth, middleHeight);

    double mainPlayerViewHeight = 100;
    mainPlayerView.view.setMinSize(leftWidth, mainPlayerViewHeight);
    otherPlayersColumn.setMaxSize(
        ClientConstants.MIN_WIDTH - leftWidth,
        ClientConstants.MIN_HEIGHT
    );

    saveButton.setOnAction(event -> setOnSaveGame());
  }

  /**
   * Set the underlying game state to the game specified by the gameId and update the view.
   *
   * @param gameId GameID of the game to be displayed
   */
  public void setGame(String gameId) {
    gameIdLabel.setText("    Game ID:  " + gameId);
    game = ClientServerController.getGame(gameId);
    extensionLabel.setText("    Extension:   " + formatExtension(game.getExtension()));

    nobleViews.clear();
    for (int i = 0; i < game.getInitialNumberOfNobles(); ++i) {
      nobleViews.add(new CardView());
      nobleViews.get(i).setEmptyNobleCard();
      nobleViews.get(i).setFitWidth(GameBoardConstants.MIN_BOARD_CARD_WIDTH);
    }

    nobleRow.getChildren().clear();
    for (CardView view : nobleViews) {
      nobleRow.getChildren().add(view);
    }

    updateState();
    updateView();

    if (thread != null) {
      thread.interrupt();
    }

    thread = new Thread(() -> {
      while (true) {
        logger.info("Long polling started at " + LocalTime.now());
        game = ClientServerController.getGameAsync(game);
        logger.info("Long polling received at " + LocalTime.now());
        Platform.runLater(() -> {
          updateView();
          loadGameActions();
        });
      }
    });

    thread.start(); // Start the thread
  }

  /**
   * Synchronize the game state with the server's game state.
   */
  private void updateState() {
    game = ClientServerController.getGame(game.getId());
    updateView();
    loadGameActions();
  }

  /**
   * Update the GUI to reflect the game state.
   */
  private void updateView() {
    // Set number of available tokens
    for (ResourceColor color : ResourceColor.values()) {
      tokenViews.get(color).setValue(game.getNumberOfTokens(color));
    }

    // Set images of available nobles
    for (int i = 0; i < game.getInitialNumberOfNobles(); ++i) {
      if (i >= game.getAvailableNobles().size()) {
        nobleViews.get(i).setEmptyNobleCard();
      } else {
        nobleViews.get(i).setCard(game.getAvailableNobles().get(i));
      }
    }

    // Set images of dev cards and decks
    for (CardLevel level : CardLevel.values()) {
      // Update deck numbers
      baseDeckViews.get(level).setValue(game.getBaseDeck(level).getSize());
      orientDeckViews.get(level).setValue(game.getOrientDeck(level).getSize());

      // Set images of available development cards
      for (int i = 0; i < 4; i++) {
        if (i >= game.getBaseCards(level).size()) {
          baseCardViews.get(level).get(i).setEmptyDevCard();
          break;
        }
        baseCardViews.get(level).get(i).setCard(game.getBaseCards(level).get(i));
      }
      for (int i = 0; i < 2; i++) {
        if (i >= game.getOrientCards(level).size()) {
          orientCardViews.get(level).get(i).setEmptyDevCard();
          break;
        }
        orientCardViews.get(level).get(i).setCard(game.getOrientCards(level).get(i));
      }

      // Set images of card decks
      if (game.getBaseDeck(level).isEmpty()) {
        baseDeckViews
            .get(level)
            .getRoot().setImage(ResourceManager.getEmptyDevelopmentCardImage());
      } else {
        baseDeckViews
            .get(level)
            .getRoot().setImage(ResourceManager.getCardBack(PrestigeCardType.BASE, level));
      }

      if (game.getOrientDeck(level).isEmpty()) {
        orientDeckViews
            .get(level)
            .getRoot().setImage(ResourceManager.getEmptyDevelopmentCardImage());
      } else {
        orientDeckViews
            .get(level)
            .getRoot().setImage(ResourceManager.getCardBack(PrestigeCardType.ORIENT, level));
      }
    }

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.8), evt -> turnLabel.setVisible(false)),
            new KeyFrame(Duration.seconds(0.9), evt -> turnLabel.setVisible(true))
    );
    timeline.setCycleCount(Animation.INDEFINITE);

    // Update players information & Trading post
    int playerCounter = 0;
    for (Player player : game.getPlayers()) {
      if (player.getUser().getName().equals(UserData.getUsername())) {
        mainPlayer = player;
        mainPlayerView.setPlayer(mainPlayer);
        mainPlayerView.setGame(game);
      } else {
        otherPlayerViews.get(playerCounter).setPlayer(player);
        otherPlayerViews.get(playerCounter).setGame(game);
        playerCounter++;
      }

      if (game.getCurrentPlayerIndex() == -1 || game.getStatus() == GameStatus.FINISHED) {
        List<Player> players = game.getWinners();
        if (players == null || players.isEmpty()) {
          turnLabel.setText("  GAME OVER! It's a draw!  ");
        } else {
          String wins = String.join(", ", players.stream().map(Player::getUsername).toList());
          turnLabel.setText("  GAME OVER! " + wins + " won!  ");
        }
        timeline.stop();
      } else {
        turnLabel.setText("  It's " + game.getCurrentPlayer().getUser().getName() + "'s turn! ");
        timeline.playFromStart();
      }

      if (game.getExtension() == GameExtension.BASE_ORIENT_TRADING_POSTS) {
        for (TradingPostAbility tpAbility : TradingPostAbility.values()) {
          tradingPostsView.setShieldVisible(tpAbility, player.getShieldColor(),
              player.hasAbility(tpAbility));
        }
      }
    }

    // Cities
    if (game.getExtension() == GameExtension.BASE_ORIENT_CITIES) {
      List<City> availCities = game.getAllCities();
      for (int i = 0; i < citiesViews.size(); i++) {
        if (i < availCities.size()) {
          citiesViews.get(i).setCard(availCities.get(i));
        } else {
          citiesViews.get(i).setEmptyCityCard();
        }
      }
    }

    // Set extension
    if (game.getExtension() == GameExtension.BASE_ORIENT) {
      extensionsRow.getChildren().clear();
    } else if (game.getExtension() == GameExtension.BASE_ORIENT_TRADING_POSTS) {
      extensionsRow.getChildren().setAll(tradingPostsView);
    } else if (game.getExtension() == GameExtension.BASE_ORIENT_CITIES) {
      extensionsRow.getChildren().setAll(citiesViews);
    }
  }

  private void clearActionBuffers() {
    tokenSelectionBuffer.clear();
    for (ResourceColor color : ResourceColor.values()) {
      tokenSelectionBuffer.put(color, 0);
    }

    tokenSelectionCount = 0;
    takeTokenConfirmButton.setVisible(false);
    takeTokenCancelButton.setVisible(false);
    cardPrompt.setVisible(false);
    bonusMatchingPrompt.setVisible(false);
    purchaseCardBuffer.clear();
  }

  /**
   * Loads possible game actions into their respective collections.
   */
  private void loadGameActions() {
    takeTokenActions.clear();
    stringTakeTokenActions.clear();
    reserveCardActions.clear();
    purchaseCardActions.clear();
    purchaseCardOne.clear();
    clearActionBuffers();

    // only get the game actions if it's your turn
    if (game.getCurrentPhase() == GamePhase.GAME_ENDED) {
      popUpGameOver.start(new Stage());
    } else if (game.getCurrentPlayer().getUser().getName().equals(UserData.getUsername())) {
      Action[] allActions = ClientServerController.getGameActions(game.getId());
      for (Action action : allActions) {
        switch (action.getType()) {
          case TAKE_TOKENS -> {
            TakeTokensAction a = (TakeTokensAction) action;
            takeTokenActions.put(a.getCost(), a.getId());
            stringTakeTokenActions.put(stringCost(a.getCost()), a.getId());
          }
          case RESERVE_CARD -> {
            ReserveCardAction a = (ReserveCardAction) action;
            reserveCardActions.put(a.getCard().getId(), a.getId());
          }
          case PURCHASE_CARD -> {
            PurchaseCardAction a = (PurchaseCardAction) action;
            purchaseCardActions.put(new PurchaseCardActionInfo(a), a.getId());
            purchaseCardOne.add(a.getFirstCard().getId());
          }
          default -> throw new RuntimeException("Unsupported Action Type");
        }
      }

      logger.debug("Reservable: " + reserveCardActions);
      logger.debug("Purchasable: " + purchaseCardOne);

      if (game.getCurrentPhase() == GamePhase.CITY_SELECTION) {
        clearActionAssignments();
        handleCitySelection();
      } else if (game.getCurrentPhase() == GamePhase.TOKEN_SELECTION) {
        clearActionAssignments();
        handleTradingPostTokenSelection();
      }
    }
    assignDefaultActions();
  }

  private void assignDefaultActions() {
    clearActionAssignments();
    assignActionToTokens();
    assignActionsToDecks();
    assignActionToDevCards();
    assignActionToReservedDevCards();
  }

  /**
   * Sets all game object's event handlers to do nothing.
   */
  private void clearActionAssignments() {
    EventHandler<MouseEvent> noAction = mouseEvent -> {
    };

    for (ResourceColor color : ResourceColor.values()) {
      tokenViews.get(color).setOnMouseClicked(noAction);
    }
    for (CardView nobleView : nobleViews) {
      nobleView.setOnMouseClicked(noAction);
    }
    for (CardView devCardView : allDevCardViews) {
      devCardView.setOnMouseClicked(noAction);
    }
    for (CardLevel deckLevel : CardLevel.values()) {
      baseDeckViews.get(deckLevel).setOnMouseClicked(noAction);
      orientDeckViews.get(deckLevel).setOnMouseClicked(noAction);
    }
    for (int i = 0; i < 2; i++) {
      mainPlayerView.setReserveCardAction(i, noAction);
    }
    for (CardView cityView : citiesViews) {
      cityView.setOnMouseClicked(noAction);
    }

  }

  private void assignActionToTokens() {
    for (ResourceColor color : ResourceColor.values()) {
      tokenViews.get(color).setOnMouseClicked(e -> {
        logger.debug("Clicked on token of color " + color);

        if (color.equals(ResourceColor.GOLD)) {
          return;
        }

        // Don't show the cancel button if it's not your turn!
        if (!game.getCurrentPlayer().getUser().getName().equals(UserData.getUsername())) {
          return;
        }

        logger.debug("Getting colour of selected button...");
        // Number of tokens of this color already selected
        int alreadySelected = tokenSelectionBuffer.get(color);
        logger.debug("Selected:" + alreadySelected);
        logger.debug("Available:" + game.getAvailableTokens().get(color));
        if (game.getAvailableTokens().get(color) - alreadySelected < 1) {
          return;
        }

        logger.debug("Setting cancel visible now...");
        takeTokenCancelButton.setVisible(true);

        // Only allow selection if less than 3 tokens have been selected
        if (tokenSelectionCount < 3) {
          logger.debug("Adding one to tokenSelectionCount and to the tokenSelectionBuffer for "
              + color);
          tokenSelectionBuffer.replace(color, alreadySelected + 1);
          tokenSelectionCount++;
        }

        Cost cost = new Cost(tokenSelectionBuffer);
        printCost(cost);
        boolean isValidCost = false;

        for (String c : stringTakeTokenActions.keySet()) {
          if (c.equals(stringCost(cost))) {
            logger.debug("Found a match : " + c);
            printCost(cost);
            isValidCost = true;
          }
        }
        if (alreadySelected == 1 && game.getAvailableTokens().get(color) < 4) {
          isValidCost = false;
        }

        if (isValidCost) {
          logger.debug("Found a matching action for the cost of the tokenSelectionBuffer which is "
              + tokenSelectionBuffer);
          takeTokenConfirmButton.setVisible(true);
        } else {
          logger.debug("Couldn't find a matching action by the cost key: " + cost);
        }
      });
    }
  }

  private void assignActionsToDecks() {
    for (CardLevel level : CardLevel.values()) {
      baseDeckViews.get(level).setOnMouseClicked(b -> {
        if (game.getBaseDeck(level).isEmpty()) {
          return;
        }

        Card topCard = game.getBaseDeck(level).peek();
        if (topCard != null && reserveCardActions.containsKey(topCard.getId())) {
          ClientServerController.sendGameAction(
              game.getId(),
              reserveCardActions.get(topCard.getId())
          );
        }
      });

      orientDeckViews.get(level).setOnMouseClicked(o -> {
        if (game.getOrientDeck(level).isEmpty()) {
          return;
        }

        Card topCard = game.getOrientDeck(level).peek();
        if (topCard != null && reserveCardActions.containsKey(topCard.getId())) {
          ClientServerController.sendGameAction(
              game.getId(),
              reserveCardActions.get(topCard.getId())
          );
        }
      });
    }
  }

  private void assignActionToDevCards() {
    for (CardView devCardView : allDevCardViews) {
      devCardView.setOnMouseClicked(new DevCardMouseEventHandler(devCardView));
    }
  }

  private void assignActionToReservedDevCards() {
    if (mainPlayer == null) {
      return;
    }
    for (int i = 0; i < 3; i++) {
      CardView cardView = mainPlayerView.getReservedDevCardView(i);
      mainPlayerView.setReserveCardAction(i, new DevCardMouseEventHandler(cardView));
    }
  }

  private void purchaseCardEndOfTurn() {
    List<Noble> selectableNobles = getSelectableNobles();


    if (!selectableNobles.isEmpty()) {
      clearActionAssignments();
      if (selectableNobles.size() == 1) {
        purchaseCardBuffer.nobleToTakeId = selectableNobles.get(0).getId();
        sendPurchaseCardAction();
      } else {
        for (CardView nobleView : nobleViews) {
          nobleView.setOnMouseClicked(e -> {
            if (selectableNobles.contains((Noble) nobleView.getCard())) {
              purchaseCardBuffer.nobleToTakeId = nobleView.getCard().getId();
              sendPurchaseCardAction();
            }
          });
        }
      }
    } else {
      sendPurchaseCardAction();
    }
  }

  private void sendPurchaseCardAction() {
    if (purchaseCardActions.containsKey(purchaseCardBuffer)) {
      logger.debug("Sending purchase card action...");
      ClientServerController.sendGameAction(
          game.getId(),
          purchaseCardActions.get(purchaseCardBuffer)
      );
    } else {
      logger.debug("Invalid purchase card action attempted with buffer: \n" + purchaseCardBuffer);
      clearActionBuffers();
      assignDefaultActions();
    }
  }

  private List<Noble> getSelectableNobles() {
    Map<ResourceColor, Integer> bonuses = new EnumMap<>(mainPlayer.getBonusesCount());
    Map<ResourceColor, Integer> futureBonuses = purchaseCardBuffer.getNewBonuses();
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      bonuses.put(color, bonuses.get(color) + futureBonuses.get(color));
    }

    List<Noble> availableNobles = game.getAvailableNobles();

    // List of nobles whose cost is satisfied after the cards are purchased
    List<Noble> noblesCostMet = new ArrayList<>();

    for (Noble noble : availableNobles) {
      boolean canGet = true;
      Cost nobleCost = noble.getCost();

      for (ResourceColor color : Cost.getAllPossibleColors()) {
        canGet &= (nobleCost.getCost(color) <= bonuses.get(color));
      }
      if (canGet) {
        noblesCostMet.add(noble);
      }
    }
    return noblesCostMet;
  }

  private void handleAbilityReserveNoble() {
    logger.debug("Handling Reserve Noble");
    cardPrompt.setVisible(false);
    clearActionAssignments();
    for (CardView view : nobleViews) {
      view.setOnMouseClicked(e -> {
        if (view.isEmpty()) {
          return;
        }
        purchaseCardBuffer.reservedNobleId = view.getCard().getId();
        purchaseCardEndOfTurn();
      });
    }
  }

  private void handleAbilityBonusMatching(int cascadeLevel, boolean endTurn) {
    logger.debug("Handling Bonus Matching");
    cardPrompt.setVisible(false);
    clearActionAssignments();
    List<ResourceColor> selectableColors = game.getCurrentPlayer().getNonEmptyColorBonuses();
    bonusMatchingPrompt.setAvailableColors(selectableColors);
    bonusMatchingPrompt.setVisible(true);
    bonusMatchingPrompt.setText("Select bonus matching for card " + cascadeLevel);
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      if (!selectableColors.contains(color)) {
        bonusMatchingPrompt.setColorMouseAction(color, e -> {
        });
      }
      bonusMatchingPrompt.setColorMouseAction(color, e -> {
        if (cascadeLevel == 1) {
          purchaseCardBuffer.firstMatchingColor = color;
        } else if (cascadeLevel == 2) {
          purchaseCardBuffer.secondMatchingColor = color;
        } else if (cascadeLevel == 3) {
          purchaseCardBuffer.thirdMatchingColor = color;
        } else {
          throw new RuntimeException("Invalid cascade level provided for bonus matching");
        }
        bonusMatchingPrompt.setVisible(false);
        if (endTurn) {
          purchaseCardEndOfTurn();
        }
      });
    }
  }

  private void handleAbilityTakeCard(int cascadeLevel, CardLevel level) {
    logger.debug("Handling Take Card");
    clearActionAssignments();
    cardPrompt.setVisible(false);
    ArrayList<CardView> cardViews = new ArrayList<>(6);
    cardViews.addAll(baseCardViews.get(level));
    cardViews.addAll(orientCardViews.get(level));

    for (CardView cv : cardViews) {
      cv.setOnMouseClicked(e -> {
        if (cv.isEmpty()) {
          return;
        }

        DevelopmentCard card = (DevelopmentCard) cv.getCard();
        if (cascadeLevel == 1) {
          purchaseCardBuffer.card2 = card;
        } else if (cascadeLevel == 2) {
          purchaseCardBuffer.card3 = card;
        } else {
          throw new RuntimeException("Invalid cascade level provided for take card ability");
        }

        if (card.getDevelopmentCardType().equals(DevelopmentCardType.ORIENT)) {
          OrientDevelopmentCard orientCard = (OrientDevelopmentCard) card;
          switch (orientCard.getAbility()) {
            case RESERVE_NOBLE -> handleAbilityReserveNoble();
            case BONUS_MATCHING_TAKE_LEVEL_ONE -> {
              handleAbilityBonusMatching(cascadeLevel + 1, false);
              handleAbilityTakeCard(cascadeLevel + 1, CardLevel.ONE);
            }
            case BONUS_MATCHING -> handleAbilityBonusMatching(cascadeLevel + 1, true);
            default -> purchaseCardEndOfTurn();
          }
        } else {
          purchaseCardEndOfTurn();
        }
      });
    }
  }

  private final class DevCardMouseEventHandler implements EventHandler<MouseEvent> {
    private final CardView cardView;

    private DevCardMouseEventHandler(CardView cardView) {
      this.cardView = cardView;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
      DevelopmentCard card = (DevelopmentCard) cardView.getCard();
      if (card == null) {
        cardPrompt.setVisible(false);
        return;
      }

      boolean hasReserveAction = reserveCardActions.containsKey(card.getId());
      boolean hasPurchaseAction = purchaseCardOne.contains(card.getId());
      cardPrompt.setCard(card, hasReserveAction, hasPurchaseAction);
      cardPrompt.setVisible(true);

      logger.debug(String.format("Card Id: %d, Reservable: %s, Purchasable: %s",
          card.getId(),
          hasReserveAction,
          hasPurchaseAction
      ));

      if (!hasReserveAction) {
        cardPrompt.setReserveButtonAction(r -> {
        });
      } else {
        cardPrompt.setReserveButtonAction(r ->
            ClientServerController.sendGameAction(
                game.getId(),
                reserveCardActions.get(card.getId())
            )
        );
      }

      if (!hasPurchaseAction) {
        cardPrompt.setPurchaseButtonAction(p -> {
        });
      } else {
        purchaseCardBuffer.card1 = card;
        cardPrompt.setPurchaseButtonAction(p -> {
          if (card.getDevelopmentCardType() == DevelopmentCardType.BASE) {
            purchaseCardEndOfTurn();
          } else {
            logger.debug("Orient Ability flow started");
            OrientDevelopmentCard orientCard = (OrientDevelopmentCard) card;
            switch (orientCard.getAbility()) {
              case RESERVE_NOBLE -> handleAbilityReserveNoble();
              case BONUS_MATCHING -> handleAbilityBonusMatching(1, true);
              case BONUS_MATCHING_TAKE_LEVEL_ONE -> {
                handleAbilityBonusMatching(1, false);
                handleAbilityTakeCard(1, CardLevel.ONE);
              }
              case TAKE_LEVEL_TWO -> handleAbilityTakeCard(1, CardLevel.TWO);
              default -> purchaseCardEndOfTurn();
            }
          }
        });
      }
    }
  }

  private void handleCitySelection() {
    if (game.getCurrentPhase() != GamePhase.CITY_SELECTION) {
      logger.debug("City Selection called when it shouldn't be");
      return;
    }

    List<City> unlockedCities = game.unlockedCities();
    for (CardView cv : citiesViews) {
      if (cv.isEmpty()) {
        continue;
      }
      City city = (City) cv.getCard();
      for (int i = 0; i < unlockedCities.size(); i++) {
        if (city.equals(unlockedCities.get(i))) {
          int cityIndex = i;
          cv.setOnMouseClicked(e -> {
            ClientServerController.sendCitySelection(game.getId(), cityIndex);
          });
        }
      }
    }
  }

  private void handleTradingPostTokenSelection() {
    if (game.getCurrentPhase() != GamePhase.TOKEN_SELECTION) {
      logger.debug("Trading Post token selection ");
      return;
    }
    Map<ResourceColor, Integer> availTokens = game.getAvailableTokens();
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      tokenViews.get(color).setOnMouseClicked(e -> {
        if (availTokens.get(color) > 0) {
          ClientServerController.sendTradingPostTokenSelection(game.getId(), color.toString());
        }
      });
    }
  }

  /**
   * Set the action of the game board's Exit button.
   *
   * @param e EventHandler to be assigned to the Exit button
   */
  public void setOnExit(EventHandler<ActionEvent> e) {
    exitButton.setOnAction(e);
    //    LsSessionController.removePlayerFromSession(
    //            game.getId(),
    //            (LsUsersController.getUser(UserData.getUsername(), UserData.getToken())),
    //            UserData.getToken());
  }

  /**
   * Set the label.
   *
   * @param e game extension.
   * @return string with the fornatted extension.
   */
  public String formatExtension(GameExtension e) {
    return switch (e) {
      case BASE_ORIENT -> "Orient + Base";
      case BASE_ORIENT_TRADING_POSTS -> "Trading Posts";
      case BASE_ORIENT_CITIES -> "Cities";
    };
  }

  /**
   * Prints a cost.
   *
   * @param cost to be printed
   */
  public void printCost(Cost cost) {
    logger.debug("Cost begin");
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      Integer value = cost.getCost(color);
      logger.debug(value + " " + color.name() + " ");
    }
    logger.debug("Cost end");
  }

  /**
   * Returns a string representing a cost.
   *
   * @param cost to be converted to string
   * @return cost as a string
   */

  public String stringCost(Cost cost) {
    StringBuilder stb = new StringBuilder();
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      int value = cost.getCost(color);
      if (value > 0) {
        stb.append(value);
        stb.append(" ");
        stb.append(color.name());
        stb.append(" ");
      }
    }
    return stb.toString();
  }

  /**
   * Set on save game.
   */
  private void setOnSaveGame() {
    ClientServerController.sendSaveGame(game.getId());
  }

  /**
   * Sets the flag for checking if game has started.
   *
   * @param b true/false
   */
  public void setStart(Boolean b) {
    gameHasStarted = b;
  }

  /**
   * Returns the value of this flag.
   *
   * @return Whether the game has started.
   */
  public boolean getStartBoolean() {
    return gameHasStarted;
  }

  private static final class PurchaseCardActionInfo {
    private DevelopmentCard card1;
    private DevelopmentCard card2;
    private DevelopmentCard card3;
    private ResourceColor firstMatchingColor;
    private ResourceColor secondMatchingColor;
    private ResourceColor thirdMatchingColor;
    private Integer reservedNobleId;
    private Integer nobleToTakeId;

    private PurchaseCardActionInfo() {
      card1 = card2 = card3 = null;
      firstMatchingColor = secondMatchingColor = thirdMatchingColor = null;
      reservedNobleId = nobleToTakeId = null;
    }

    private PurchaseCardActionInfo(PurchaseCardAction a) {
      card1 = a.getFirstCard();
      card2 = a.getSecondCard();
      card3 = a.getThirdCard();
      firstMatchingColor = a.getFirstMatchingColor();
      secondMatchingColor = a.getSecondMatchingColor();
      thirdMatchingColor = a.getThirdMatchingColor();
      reservedNobleId = a.getReservedNoble() == null ? null : a.getReservedNoble().getId();
      nobleToTakeId = a.getNobleToTake() == null ? null : a.getNobleToTake().getId();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      PurchaseCardActionInfo that = (PurchaseCardActionInfo) o;
      return Objects.equals(card1, that.card1)
          && Objects.equals(card2, that.card2)
          && Objects.equals(card3, that.card3)
          && Objects.equals(firstMatchingColor, that.firstMatchingColor)
          && Objects.equals(secondMatchingColor, that.secondMatchingColor)
          && Objects.equals(thirdMatchingColor, that.thirdMatchingColor)
          && Objects.equals(reservedNobleId, that.reservedNobleId)
          && Objects.equals(nobleToTakeId, that.nobleToTakeId);
    }

    @Override
    public int hashCode() {
      return Integer.parseInt(
          cardHash(card1) + cardHash(card2) + cardHash(card3)
      );
    }

    private String cardHash(DevelopmentCard card) {
      return card == null ? String.valueOf(999) : String.valueOf(card.getId());
    }

    /**
     * Sets all values to null.
     */
    private void clear() {
      card1 = card2 = card3 = null;
      firstMatchingColor = secondMatchingColor = thirdMatchingColor = null;
      reservedNobleId = nobleToTakeId = null;
    }

    /**
     * Computes the bonus that the player will receive were they to perform this purchase.
     * The sum of the player's current bonus and the bonus return by this method should
     * be equal to the player's bonuses after the purchase.
     *
     * @return Map of number of bonuses for each colour.
     */
    private Map<ResourceColor, Integer> getNewBonuses() {
      Map<ResourceColor, Integer> bonuses = new EnumMap<>(ResourceColor.class);
      for (ResourceColor color : Cost.getAllPossibleColors()) {
        int sum = 0;
        if (card1.getColor() == color || firstMatchingColor == color) {
          sum += getBonusValueOf(card1);
        }
        if (card2 != null && (card2.getColor() == color || secondMatchingColor == color)) {
          sum += getBonusValueOf(card2);
        }
        if (card3 != null && (card3.getColor() == color || thirdMatchingColor == color)) {
          sum += getBonusValueOf(card3);
        }
        bonuses.put(color, sum);
      }
      return bonuses;
    }

    private int getBonusValueOf(DevelopmentCard card) {
      if (card == null) {
        return 0;
      }
      if (card.getDevelopmentCardType() == DevelopmentCardType.ORIENT) {
        if (((OrientDevelopmentCard) card).getAbility() == OrientAbility.TWO_BONUSES) {
          return 2;
        }
      }
      return 1;
    }

    @Override
    public String toString() {
      return String.format("Info: {card1: %s, card2: %s, card3: %s, "
              + "color1: %s, color2: %s, color3: %s, "
              + "rNoble: %d, tNoble: %d}",
          cardToString(card1), cardToString(card2), cardToString(card3),
          firstMatchingColor, secondMatchingColor, thirdMatchingColor,
          reservedNobleId, nobleToTakeId
      );
    }

    private String cardToString(Card card) {
      return card == null ? "null" : String.valueOf(card.getId());
    }
  }

}