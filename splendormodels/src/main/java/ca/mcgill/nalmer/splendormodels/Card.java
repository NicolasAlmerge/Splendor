package ca.mcgill.nalmer.splendormodels;

import java.util.Objects;

/**
 * Represents a card.
 */
public abstract class Card {
  private final Cost cost;
  private final int id;

  /**
   * Constructor.
   *
   * @param cardCost     Cost of the card.
   * @param cardId       Card id.
   * @throws NullPointerException if <code>cardCost</code> is <code>null</code>.
   */
  protected Card(Cost cardCost, int cardId) {
    if (cardCost == null) {
      throw new NullPointerException("cost cannot be null");
    }
    id = cardId;
    cost = cardCost;
  }

  /**
   * Returns the {@link CardType} of the card.
   *
   * @return {@link CardType} of the card.
   */
  public abstract CardType getCardType();

  /**
   * Returns the cost of the card.
   *
   * @return PrestigeCard cost.
   */
  public final Cost getCost() {
    return cost;
  }

  /**
   * Returns the card id.
   *
   * @return PrestigeCard id.
   */
  public final int getId() {
    return id;
  }

  /**
   * Checks if two {@link Card} are equal. This only compares the card's ids.
   *
   * @param obj Other object two compare.
   * @return <code>true</code> if cards are equal, <code>false</code> otherwise.
   */
  @Override
  public final boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (obj instanceof Card cardObj) {
      return id == cardObj.id;
    }
    return false;
  }

  /**
   * Returns the hash code of the {@link Card}. This is equal to <code>Objects.hash(getId())</code>.
   *
   * @return Hash of the {@link Card}.
   */
  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}
