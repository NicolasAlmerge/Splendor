package splendor.view;

import ca.mcgill.nalmer.splendormodels.Card;
import ca.mcgill.nalmer.splendormodels.PrestigeCardType;
import javafx.scene.image.ImageView;
import splendor.controller.ResourceManager;

/**
 * Class to make representing a game development card or a noble card easier.
 * Preserves the aspect ratio of the card images.
 */
final class CardView extends ImageView {
  private Card card;

  /**
   * Create an empty CardView. The image will the error card image.
   */
  public CardView() {
    super();
    setPreserveRatio(true);
    setEmptyDevCard();
  }

  /**
   * Get the Card object of the card being represented.
   *
   * @return The card displayed by this CardView.
   */
  public Card getCard() {
    return card;
  }

  /**
   * Set the image shown to be an image of the card represented by the given Card object.
   * Null represents no card.
   *
   * @param card Card to be displayed.
   */
  public void setCard(Card card) {
    this.card = card;
    if (this.card == null) {
      setEmptyDevCard();
    } else {
      this.setImage(ResourceManager.getImage(card.getId()));
    }
  }

  /**
   * Set the image shown to be the image representing no development card.
   */
  public void setEmptyDevCard() {
    this.card = null;
    this.setImage(ResourceManager.getCardEmptyImage(PrestigeCardType.BASE));
  }

  /**
   * Set the image shown to be the image representing no noble card.
   */
  public void setEmptyNobleCard() {
    this.card = null;
    this.setImage(ResourceManager.getCardEmptyImage(PrestigeCardType.NOBLE));
  }

  /**
   * Set the image shown to be the image representing no cities card.
   */
  public void setEmptyCityCard() {
    this.card = null;
    this.setImage(ResourceManager.getEmptyCitiesImage());
  }

  /**
   * Returns true if there is no card assigned to the CardView.
   *
   * @return True if the CardView does not have a card i.e. the card is null
   */
  public boolean isEmpty() {
    return card == null;
  }
}
