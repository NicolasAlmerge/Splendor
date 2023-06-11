package splendor.view;

import ca.mcgill.nalmer.splendormodels.ShieldColor;
import ca.mcgill.nalmer.splendormodels.TradingPostAbility;
import java.util.EnumMap;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import splendor.controller.ResourceManager;

final class TradingPostsView extends AnchorPane {
  private final EnumMap<TradingPostAbility, EnumMap<ShieldColor, ImageView>> allShields;

  TradingPostsView() {
    ImageView tradingPostBoardView = new ImageView(ResourceManager.getTradingPostBoardImage());
    tradingPostBoardView.setPreserveRatio(true);
    tradingPostBoardView.setFitHeight(GameBoardConstants.EXTENSION_CARD_HEIGHT);
    getChildren().add(tradingPostBoardView);

    // Load the shields, 4 shields for each ability
    allShields = new EnumMap<>(TradingPostAbility.class);
    for (TradingPostAbility tpa : TradingPostAbility.values()) {
      EnumMap<ShieldColor, ImageView> shieldViews = new EnumMap<>(ShieldColor.class);
      allShields.put(tpa, shieldViews);
      StackPane paneAllShields = new StackPane();
      for (ShieldColor shieldColor : ShieldColor.values()) {
        ImageView shieldView =
            new ImageView(ResourceManager.getTradingPostShieldImage(shieldColor));
        shieldView.setVisible(false);
        shieldView.setPreserveRatio(true);
        shieldView.setFitHeight(GameBoardConstants.EXTENSION_CARD_HEIGHT);
        shieldViews.put(shieldColor, shieldView);
        paneAllShields.getChildren().add(shieldView);
      }
      AnchorPane.setTopAnchor(paneAllShields, 0.0);
      AnchorPane.setLeftAnchor(paneAllShields, getShieldOffset(tpa));
      getChildren().add(paneAllShields);
    }

    // Set the background to be the trading post board
    Background bg = new Background(
        new BackgroundImage(
            ResourceManager.getTradingPostBoardImage(),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(
                BackgroundSize.AUTO,
                GameBoardConstants.EXTENSION_CARD_HEIGHT,
                false, false, false, false
            )
        )
    );

    setMinHeight(GameBoardConstants.EXTENSION_CARD_HEIGHT);
    setPrefHeight(GameBoardConstants.EXTENSION_CARD_HEIGHT);
    setMaxHeight(GameBoardConstants.EXTENSION_CARD_HEIGHT);
  }

  void setShieldVisible(TradingPostAbility tradingPost, ShieldColor shield, boolean b) {
    allShields.get(tradingPost).get(shield).setVisible(b);
  }

  /**
   * Get the offset of any shield from the left edge of the Trading Post board image.
   *
   * @param ability Ability of the Trading Post
   * @return Offset from the left edge in pixels
   */
  private double getShieldOffset(TradingPostAbility ability) {
    return GameBoardConstants.EXTENSION_CARD_HEIGHT
        * switch (ability) {
      case TAKE_TOKEN_AFTER_TAKING_CARD -> 0.176;
      case TAKE_TOKEN_AFTER_TWO_TOKENS -> 1.016;
      case GOLD_TOKEN_WORTH_TWO_TOKENS -> 1.872;
      case FIVE_POINTS -> 2.728;
      case ONE_POINT_FOR_EACH_SHIELD -> 3.536;
    };
  }
}
