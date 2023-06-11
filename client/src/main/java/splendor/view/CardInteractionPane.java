package splendor.view;

import ca.mcgill.nalmer.splendormodels.Card;
import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import java.util.EnumMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import splendor.controller.ResourceManager;

/**
 * Represents the prompt that will allow a player to reserve or purchase a card, if they can.
 */
final class CardInteractionPane extends GridPane {
  private final CardView cardView;
  private final HBox tokenCostRow;
  private final Map<ResourceColor, NumberedPane<ImageView>> tokenCost;
  private final Button reserveButton;
  private final Button purchaseButton;
  private final Button cancelButton;

  /**
   * Initialize the prompt.
   */
  public CardInteractionPane() {
    super();
    cardView = new CardView();
    cardView.setFitWidth(GameBoardConstants.PROMPT_CARD_WIDTH);

    reserveButton = new Button();
    Image reserveButtonImage = ResourceManager.getReserveButtonImage();
    ImageView reserveButtonView = new ImageView(reserveButtonImage);
    reserveButtonView.setFitHeight(60);
    reserveButtonView.setPreserveRatio(true);
    reserveButton.setGraphic(reserveButtonView);

    purchaseButton = new Button();
    Image purchaseButtonImage = ResourceManager.getPurcharseButtonImage();
    ImageView purchaseButtonView = new ImageView(purchaseButtonImage);
    purchaseButtonView.setFitHeight(70);
    purchaseButtonView.setPreserveRatio(true);
    purchaseButton.setGraphic(purchaseButtonView);

    cancelButton = new Button();
    Image cancelButtonImage = ResourceManager.getCancelButtonImage();
    ImageView cancelButtonView = new ImageView(cancelButtonImage);
    cancelButtonView.setFitHeight(50);
    cancelButtonView.setPreserveRatio(true);
    cancelButton.setGraphic(cancelButtonView);

    tokenCost = new EnumMap<>(ResourceColor.class);
    tokenCostRow = new HBox(5);
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      tokenCost.put(color, new NumberedPane<>(new ImageView()));
      tokenCost.get(color).getRoot().setImage(ResourceManager.getTokenImage(color));
      tokenCost.get(color).getRoot().setFitWidth(GameBoardConstants.MIN_BOARD_TOKEN_SIZE);
      tokenCost.get(color).getRoot().setPreserveRatio(true);
      tokenCostRow.getChildren().add(tokenCost.get(color));
    }

    this.add(cardView, 0, 0, 1, 4);
    this.addColumn(1,
        tokenCostRow,
        reserveButton,
        purchaseButton,
        cancelButton
    );

    Background bg = new Background(
        new BackgroundFill(
            Color.DARKGRAY,
            new CornerRadii(5.0),
            new Insets(10, 10, 10, 10)
        )
    );
    this.setBackground(bg);
    setPrefSize(600, 400);
    setPadding(new Insets(20, 20, 20, 20));
    setVgap(50);
    setHgap(80);

    cancelButton.setOnAction(e -> {
      this.setVisible(false);
    });
  }

  /**
   * Set the card to be interacted with.
   * Buttons will be displayed depending on if its corresponding action exists.
   * The cancel button is always visible.
   *
   * @param card            Card to be displayed
   * @param canReserveCard  True if the card can be reserved by the player
   * @param canPurchaseCard True if the card can be purchased by the player
   */
  public void setCard(Card card,
                      boolean canReserveCard,
                      boolean canPurchaseCard) {
    cardView.setCard(card);
    Cost cardCost = card.getCost();
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      tokenCost.get(color).setValue(cardCost.getCost(color));
    }
    reserveButton.setVisible(canReserveCard);
    purchaseButton.setVisible(canPurchaseCard);
    tokenCostRow.setVisible(canPurchaseCard);
  }

  /**
   * Set the event that occurs when the Cancel button is pressed.
   * Prompt will always disappear after a button is pressed.
   *
   * @param e EventHandler of the action.
   */
  public void setCancelButtonEvent(EventHandler<ActionEvent> e) {
    cancelButton.setOnAction(actionEvent -> {
      e.handle(actionEvent);
      setVisible(false);
    });
  }

  /**
   * Set the event that occurs when the Reserve button is pressed.
   * Prompt will always disappear after a button is pressed.
   *
   * @param e EventHandler of the action.
   */
  public void setReserveButtonAction(EventHandler<ActionEvent> e) {
    reserveButton.setOnAction(actionEvent -> {
      e.handle(actionEvent);
      setVisible(false);
    });
  }

  /**
   * Set the event that occurs when the Purchase button is pressed.
   * Prompt will always disappear after a button is pressed.
   *
   * @param e EventHandler of the action.
   */
  public void setPurchaseButtonAction(EventHandler<ActionEvent> e) {
    purchaseButton.setOnAction(actionEvent -> {
      e.handle(actionEvent);
      setVisible(false);
    });
  }

}
