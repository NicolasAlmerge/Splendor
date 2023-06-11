package ca.mcgill.nalmer.splendormodels;

import java.util.List;

/**
 * Represents an action generator interface.
 */
public interface ActionGeneratorInterface {

  /**
   * Generate all {@link Action} for the current {@link Player}.
   *
   * @param game {@link Game} to use.
   * @return All {@link Action} the current {@link Player} can do.
   */
  List<? extends Action> generateAllActions(Game game);
}
