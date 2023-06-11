package splendor.view;

import ca.mcgill.nalmer.splendormodels.Card;
import ca.mcgill.nalmer.splendormodels.Game;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import ca.mcgill.nalmer.splendormodels.Player;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import splendor.controller.ResourceManager;

/**
 * Contains all the scene objects needed to display a player's state.
 */
final class PlayerView {
  private final Label pointCountLabel;
  private final Label nameLabel;
  private final Label tokenCountlabel;
  private final Map<ResourceColor, NumberedPane<ImageView>> tokenViews;
  private final Map<ResourceColor, NumberedPane<Rectangle>> bonusViews;
  private final Label reservedCardCountLabel;
  private final List<CardView> reservedDevCardViews;
  private final List<CardView> reservedNobleCardViews;
  /**
   * The view that has all scene elements properly arranged to represent a player's state.
   */
  public Pane view;
  private Player player = null;
  private Game game;
  private final PlayerViewMode playerViewMode;

  /**
   * Create a new PlayerView. Is not associated with a player. Gives a view with "empty" scenes.
   * "Main player view" is the view of the player controlling the computer.
   * "Other player view" is a view of one other player.
   *
   * @param mode Whether a "main player view" or an "other player view" is to be created.
   */
  public PlayerView(PlayerViewMode mode) {
    playerViewMode = mode;
    pointCountLabel = new Label("#");
    tokenCountlabel = new Label("Tokens (#/10)");
    nameLabel = new Label("Missing Player");

    tokenViews = new EnumMap<>(ResourceColor.class);
    bonusViews = new EnumMap<>(ResourceColor.class);
    for (ResourceColor color : ResourceColor.values()) {
      tokenViews.put(color,
          new NumberedPane<>(new ImageView(ResourceManager.getTokenImage(color))));

      tokenViews.get(color).getRoot().setPreserveRatio(true);
      tokenViews.get(color).getRoot().setFitWidth(GameBoardConstants.MIN_PLAYER_SHAPE_SIZE);

      Color rectangleColor = switch (color) {
        case WHITE -> Color.WHITE;
        case BLUE -> Color.BLUE;
        case GREEN -> Color.GREEN;
        case RED -> Color.RED;
        case BLACK -> Color.BLACK;
        case GOLD -> Color.GOLD;
      };
      double rectangleSize = GameBoardConstants.MIN_PLAYER_SHAPE_SIZE;
      bonusViews.put(color,
          new NumberedPane<>(new Rectangle(rectangleSize, rectangleSize, rectangleColor)));
    }

    reservedCardCountLabel = new Label("Reserved Cards (#/3)");
    reservedDevCardViews = new ArrayList<>();
    reservedNobleCardViews = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      reservedDevCardViews.add(new CardView());
      reservedNobleCardViews.add(new CardView());

      reservedDevCardViews.get(i).setEmptyDevCard();
      reservedNobleCardViews.get(i).setEmptyNobleCard();

      if (mode == PlayerViewMode.MAIN) {
        reservedDevCardViews.get(i).setFitWidth(GameBoardConstants.MIN_PLAYER_MAIN_CARD_WIDTH);
        reservedNobleCardViews.get(i).setFitWidth(GameBoardConstants.MIN_PLAYER_MAIN_CARD_WIDTH);
      } else {
        reservedDevCardViews.get(i).setFitWidth(GameBoardConstants.MIN_PLAYER_OTHER_CARD_WIDTH);
        reservedNobleCardViews.get(i).setFitWidth(GameBoardConstants.MIN_PLAYER_OTHER_CARD_WIDTH);
      }
    }

    GridPane tokensAndBonusesGrid = new GridPane();

    int col = 0;
    for (ResourceColor color : ResourceColor.values()) {
      tokensAndBonusesGrid.addColumn(col,
          tokenViews.get(color),
          bonusViews.get(color)
      );
      col++;
    }

    HBox reservedCardRow = new HBox();
    HBox reservedNobleRow = new HBox();
    for (int i = 0; i < 3; i++) {
      reservedCardRow.getChildren().add(reservedDevCardViews.get(i));
      reservedNobleRow.getChildren().add(reservedNobleCardViews.get(i));
    }

    switch (mode) {
      case MAIN -> {
        GridPane grid = new GridPane();
        grid.addRow(0,
            new Label(" Player "), new Label(" Points "),
            tokenCountlabel, reservedCardCountLabel
        );
        grid.addRow(1,
            nameLabel, pointCountLabel, tokensAndBonusesGrid,
            new HBox(reservedCardRow, reservedNobleRow)
        );
        grid.setGridLinesVisible(true);
        view = grid;
      }
      case OTHER -> {
        VBox pbox = new VBox();
        Label prestigePoints = new Label("Prestige Points: ");
        pbox.getChildren().addAll(
            new HBox(10, nameLabel, new HBox(3, prestigePoints, pointCountLabel)),
            tokenCountlabel,
            tokensAndBonusesGrid,
            reservedCardCountLabel,
            new HBox(10, reservedCardRow, reservedNobleRow)
        );

        view = pbox;
      }
      default -> {
      }
    }
  }

  public void setGame(Game game) {
    if (this.game != null && this.game.getId().equals(game.getId())) {
      return;
    }
    this.game = game;

    final ImageView shieldImage;
    if (game != null && player != null
        && game.getExtension() == GameExtension.BASE_ORIENT_TRADING_POSTS
        && player.getShieldColor() != null) {
      Image image = ResourceManager.getPlayerShieldImage(player.getShieldColor());
      shieldImage = new ImageView(image);
      shieldImage.setFitHeight((playerViewMode == PlayerViewMode.MAIN) ? 25 : 20);
      shieldImage.setPreserveRatio(true);
    } else {
      return;
    }

    if (playerViewMode == PlayerViewMode.MAIN) {
      ((GridPane) view).add(shieldImage, 4, 0);
    } else {
      ((HBox) view.getChildren().get(0)).getChildren().add(shieldImage);
    }
  }

  /**
   * Update the view to reflect the player's state.
   */
  public void updateView() {
    nameLabel.setText(player.getUser().getName());
    pointCountLabel.setText(String.valueOf(player.getPrestigePts()));
    tokenCountlabel.setText(
        "Tokens ("
            + player.getNumberOfTokens()
            + "/10)");
    reservedCardCountLabel.setText(
        "Reserved Cards ("
            + player.getNumberOfReservedCards()
            + "/3)"
    );

    for (ResourceColor color : ResourceColor.values()) {
      tokenViews.get(color).setValue(player.getTokens().get(color));
      bonusViews.get(color).setValue(player.getNumberOfBonusesOfColor(color));
    }

    for (int i = 0; i < 3; i++) {
      if (i >= player.getNumberOfReservedCards()) {
        reservedDevCardViews.get(i).setEmptyDevCard();
      } else {
        Card card = player.getReservedCards().get(i);
        reservedDevCardViews.get(i).setCard(card);
      }
    }

    for (int i = 0; i < 3; i++) {
      if (i >= player.getReservedNobles().size()) {
        reservedNobleCardViews.get(i).setEmptyNobleCard();
      } else {
        Card card = player.getReservedNobles().get(i);
        reservedNobleCardViews.get(i).setCard(card);
      }
    }
    view.setVisible(true);
  }

  /**
   * Get the CardView of the reserved card at the given index. Index must be 0-2.
   *
   * @param index Index of the CardView
   * @return CardView of the reserved card
   */
  public CardView getReservedDevCardView(int index) {
    return reservedDevCardViews.get(index);
  }

  /**
   * Set a player to be represented by this PlayerView.
   *
   * @param player Player to be represented.
   */
  public void setPlayer(Player player) {
    this.player = player;
    updateView();
  }

  /**
   * Allows to set the action of the reserve card CardViews by another class.
   *
   * @param index Index of the reserved card CardView. Range 0-2
   * @param e     Action to be associated with CardView on click.
   */
  public void setReserveCardAction(int index, EventHandler<MouseEvent> e) {
    reservedDevCardViews.get(index).setOnMouseClicked(e);
  }

  /**
   * The valid options for type of PlayerView.
   */
  enum PlayerViewMode {
    /**
     * Main player view.
     */
    MAIN,
    /**
     * Other player view.
     */
    OTHER
  }
}
