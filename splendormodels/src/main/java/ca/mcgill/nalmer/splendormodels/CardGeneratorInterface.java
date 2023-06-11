package ca.mcgill.nalmer.splendormodels;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a card generator interface.
 */
public interface CardGeneratorInterface {
  /**
   * Gets all {@link BaseDevelopmentCard} at a certain {@link CardLevel}.
   *
   * @param level {@link BaseDevelopmentCard} level.
   * @return List of all {@link BaseDevelopmentCard} at <code>level</code>.
   * @throws NullPointerException if <code>level</code> is <code>null</code>.
   */
  Deck<BaseDevelopmentCard> getBaseDevelopmentCardAtLevel(CardLevel level);

  /**
   * Gets all {@link OrientDevelopmentCard} at a certain {@link CardLevel}.
   *
   * @param level {@link OrientDevelopmentCard} level.
   * @return List of all {@link OrientDevelopmentCard} at <code>level</code>.
   * @throws NullPointerException if <code>level</code> is <code>null</code>.
   */
  Deck<OrientDevelopmentCard> getOrientDevelopmentCardAtLevel(CardLevel level);

  /**
   * Gets all {@link Noble}.
   *
   * @return List of {@link Noble}.
   */
  List<Noble> getAllNobles();

  /**
   * Gets all {@link City}.
   *
   * @return List of {@link City}.
   */
  List<City> getAllCities();

  /**
   * Gets all {@link TradingPost}.
   *
   * @return List of {@link TradingPost}.
   */
  List<TradingPost> getAllTradingPosts();
}
