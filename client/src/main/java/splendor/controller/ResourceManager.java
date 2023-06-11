package splendor.controller;

import ca.mcgill.nalmer.splendormodels.Card;
import ca.mcgill.nalmer.splendormodels.CardLevel;
import ca.mcgill.nalmer.splendormodels.DevelopmentCard;
import ca.mcgill.nalmer.splendormodels.Noble;
import ca.mcgill.nalmer.splendormodels.PrestigeCardType;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import ca.mcgill.nalmer.splendormodels.ShieldColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;

/**
 * Represents the resource manager.
 */
public final class ResourceManager {
  // Configuration
  private static final String RESOURCE_DIR = pathJoin("src", "main", "resources");
  private static final String BASE_CARDS_DIR = pathJoin(RESOURCE_DIR, "basecards");
  private static final String EXPANSIONS_DIR = pathJoin(RESOURCE_DIR, "expansions");
  private static final String ORIENT_CARDS_DIR = pathJoin(EXPANSIONS_DIR, "orient");
  private static final String CITIES_CARDS_DIR = pathJoin(EXPANSIONS_DIR, "cities");
  private static final String TRADING_POSTS_DIR = pathJoin(EXPANSIONS_DIR, "tradingposts");
  private static final String NOBLE_CARDS_DIR = pathJoin(RESOURCE_DIR, "nobles");
  private static final String RULES_DIR = pathJoin(RESOURCE_DIR, "rules");
  private static final String TOKENS_DIR = pathJoin(RESOURCE_DIR, "tokens");
  private static final String SHIELDS_DIR = pathJoin(RESOURCE_DIR, "shields");
  private static final String BUTTONS_DIR = pathJoin(RESOURCE_DIR, "buttons");

  // Data
  private static Map<Integer, Image> cardImages = new HashMap<>();
  private static Map<ResourceColor, Image> tokenImages = new EnumMap<>(ResourceColor.class);
  private static Map<ShieldColor, Image> playerShieldImages = new EnumMap<>(ShieldColor.class);
  private static Map<ShieldColor, Image> tradingPostShieldImages = new EnumMap<>(ShieldColor.class);
  private static Map<CardLevel, Image> baseCardsBack = new EnumMap<>(CardLevel.class);
  private static Map<CardLevel, Image> orientCardsBack = new EnumMap<>(CardLevel.class);
  private static Image nobleCardsBack;
  private static Image[] rulesCards = new Image[0];
  private static Image backGroundImage;
  private static Image emptyCardImage;
  private static Image emptyNobleImage;
  private static Image emptyCitiesImage;
  private static Image errorImage;
  private static Image logoImage;
  private static Image tradingPostBoardImage;
  private static Image confirmButtonImage;
  private static Image cancelButtonImage;

  private static Image saveButtonImage;

  private static Image exitButtonImage;

  private static Image reserveButtonImage;

  private static Image purcharseButtonImage;
  private static boolean loaded = false;

  /**
   * Private constructor.
   */
  private ResourceManager() {
  }

  /**
   * Loads all resources. This has no effect if the resource manager has already been loaded.
   *
   * @throws RuntimeException if the resource loading failed.
   */
  public static void loadAllResources() {
    // If everything has been loaded, don't reload
    if (loaded) {
      return;
    }

    // Load visible components
    cardImages = loadAllCards();
    tokenImages = loadAllTokens();
    playerShieldImages = loadAllPlayerShields();
    tradingPostShieldImages = loadAllTradingPostShields();
    tradingPostBoardImage = readImage(pathJoin(TRADING_POSTS_DIR, "tradingposts.png"));

    // Load back cards
    baseCardsBack = loadAllBackBaseCards();
    orientCardsBack = loadAllBackOrientCards();
    nobleCardsBack = readImage(pathJoin(RESOURCE_DIR, "noble back.jpg"));

    // Load empty slots
    emptyCardImage = readImage(pathJoin(RESOURCE_DIR, "card empty.png"));
    emptyNobleImage = readImage(pathJoin(RESOURCE_DIR, "noble empty.png"));
    emptyCitiesImage = readImage(pathJoin(RESOURCE_DIR, "cities empty.png"));

    // Load other resources
    rulesCards = loadAllRules();
    errorImage = readImage(pathJoin(RESOURCE_DIR, "error.png"));
    logoImage = readImage(pathJoin(RESOURCE_DIR, "splendor.png"));
    backGroundImage = readImage(pathJoin(RESOURCE_DIR, "background.png"));

    confirmButtonImage = loadConfirmButtonImage();
    cancelButtonImage = loadCancelButtonImage();
    saveButtonImage = loadSaveButtonImage();
    exitButtonImage = loadExitButtonImage();
    reserveButtonImage = loadReserveButtonImage();
    purcharseButtonImage = loadPurchaseButtonImage();

    // Everything has been loaded
    loaded = true;
  }

  /**
   * Checks whether the resource manager has been loaded (useful for debugging).
   *
   * @return True if the resource manager has been loaded, false otherwise.
   */
  public static boolean isLoaded() {
    return loaded;
  }

  /**
   * Retrieves the image from a card id.
   *
   * @param id Card to retrieve image from.
   * @return Card image.
   * @throws RuntimeException if the resource manager has not been loaded or if the id is invalid.
   */
  public static Image getImage(int id) {
    assertLoaded();
    Image image = cardImages.get(id);
    if (image == null) {
      throw new RuntimeException("card id is invalid");
    }
    return image;
  }

  /**
   * Retrieves the image of a development card.
   *
   * @param card Card to retrieve image from.
   * @return Card image, or empty card slot if card is null.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getImage(DevelopmentCard card) {
    assertLoaded();
    if (card == null) {
      return getEmptyDevelopmentCardImage();
    }
    return getImage(card.getId());
  }

  /**
   * Retrieves the image of a noble.
   *
   * @param card Noble to retrieve image from.
   * @return Noble image, or empty noble slot if card is null.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getImage(Noble card) {
    assertLoaded();
    if (card == null) {
      return getEmptyNobleImage();
    }
    return getImage(card.getId());
  }

  /**
   * Retrieves the image of a card (development or noble).
   *
   * @param card Card to retrieve image from.
   * @return Card image.
   * @throws RuntimeException if the resource manager has not been loaded or if the card is null.
   */
  public static Image getImage(Card card) {
    assertLoaded();
    if (card == null) {
      throw new RuntimeException("card cannot be null");
    }
    return getImage(card.getId());
  }

  /**
   * Returns the back image of a card.
   *
   * @param type  Type of card.
   * @param level Level of card.
   * @return Image of the back of this card.
   * @throws RuntimeException if the resource manager has not been loaded or if the level is null.
   */
  public static Image getCardBack(PrestigeCardType type, CardLevel level) {
    assertLoaded();
    if (type == null) {
      throw new RuntimeException("type cannot be null");
    }

    return switch (type) {
      case BASE -> getBackBaseDevelopmentCard(level);
      case ORIENT -> getBackOrientDevelopmentCard(level);
      case NOBLE -> getNobleCardsBack();
    };
  }

  /**
   * Returns the back of base development basecards.
   *
   * @param level Corresponding card level.
   * @return Image representing the back of the card.
   * @throws RuntimeException if the resource manager has not been loaded or if the level is null.
   */
  public static Image getBackBaseDevelopmentCard(CardLevel level) {
    return getDevelopmentCardBackImage(level, baseCardsBack);
  }

  /**
   * Returns the back of orient development basecards.
   *
   * @param level Corresponding card level.
   * @return Image representing the back of the card.
   * @throws RuntimeException if the resource manager has not been loaded or if the level is null.
   */
  public static Image getBackOrientDevelopmentCard(CardLevel level) {
    return getDevelopmentCardBackImage(level, orientCardsBack);
  }

  /**
   * Returns the back of a noble card.
   *
   * @return Image representing the back of the noble.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getNobleCardsBack() {
    assertLoaded();
    return nobleCardsBack;
  }

  /**
   * Returns the image representing the slot of an empty card.
   *
   * @param type Type of card.
   * @return Image of the back of this card.
   * @throws RuntimeException if the resource manager has not been loaded or if the type is null.
   */
  public static Image getCardEmptyImage(PrestigeCardType type) {
    assertLoaded();
    if (type == null) {
      throw new RuntimeException("type cannot be null");
    }

    return switch (type) {
      case BASE, ORIENT -> getEmptyDevelopmentCardImage();
      case NOBLE -> getEmptyNobleImage();
    };
  }

  /**
   * Get the empty development card image.
   *
   * @return Empty development card image.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getEmptyDevelopmentCardImage() {
    assertLoaded();
    return emptyCardImage;
  }

  /**
   * Get the empty noble image.
   *
   * @return Empty noble image.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getEmptyNobleImage() {
    assertLoaded();
    return emptyNobleImage;
  }

  /**
   * Get the empty cities image.
   *
   * @return Empty cities image.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getEmptyCitiesImage() {
    assertLoaded();
    return emptyCitiesImage;
  }

  /**
   * Get the list of all rules.
   *
   * @return List of all image rules.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static List<Image> getAllRules() {
    assertLoaded();
    return new ArrayList<>(List.of(rulesCards));
  }

  /**
   * Get the error image.
   *
   * @return Error image.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getErrorImage() {
    assertLoaded();
    return errorImage;
  }

  /**
   * Get the logo image.
   *
   * @return Logo image.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Image getLogoImage() {
    assertLoaded();
    return logoImage;
  }

  /**
   * Get the background image.
   *
   * @return Background image.
   */
  public static Image getBackGroundImage() {
    assertLoaded();
    return backGroundImage;
  }

  /**
   * Get all token images.
   *
   * @return Map of all token images.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Map<ResourceColor, Image> getAllTokenImages() {
    assertLoaded();
    return tokenImages;
  }

  /**
   * Get all shield images.
   *
   * @return Map of all shield images.
   * @throws RuntimeException if the resource manager has not been loaded.
   */
  public static Map<ShieldColor, Image> getAllShieldImages() {
    assertLoaded();
    return playerShieldImages;
  }

  /**
   * Get the token image of one token.
   *
   * @param color Color of the token.
   * @return Image of the token.
   * @throws RuntimeException if the resource manager has not been loaded or if the color is null.
   */
  public static Image getTokenImage(ResourceColor color) {
    assertLoaded();
    if (color == null) {
      throw new RuntimeException("color cannot be null");
    }

    Image image = tokenImages.get(color);
    if (image == null) {
      throw new RuntimeException("could not get the requested image");
    }

    return image;
  }

  /**
   * Get the shield image of one shield for use in PlayerView.
   *
   * @param color Color of the shield.
   * @return Image of the shield.
   * @throws RuntimeException if the resource manager has not been loaded or if the color is null.
   */
  public static Image getPlayerShieldImage(ShieldColor color) {
    assertLoaded();
    if (color == null) {
      throw new RuntimeException("color cannot be null");
    }

    Image image = playerShieldImages.get(color);
    if (image == null) {
      throw new RuntimeException("could not get the requested image");
    }

    return image;
  }

  /**
   * Get the shield image of one shield for use on the game board.
   * These images have a transparent background that sets them at the correct height position.
   *
   * @param color Color of the shield.
   * @return Image of the shield.
   * @throws RuntimeException if the resource manager has not been loaded or if the color is null.
   */
  public static Image getTradingPostShieldImage(ShieldColor color) {
    assertLoaded();
    if (color == null) {
      throw new RuntimeException("color cannot be null");
    }

    Image image = tradingPostShieldImages.get(color);
    if (image == null) {
      throw new RuntimeException("could not get the requested image");
    }

    return image;
  }

  /**
   * Get the image of the Trading Posts board.
   *
   * @return Image of the Trading Posts board
   */
  public static Image getTradingPostBoardImage() {
    assertLoaded();
    return tradingPostBoardImage;
  }

  ////////////////////////////// PRIVATE HELPER FUNCTIONS //////////////////////////////

  private static void assertLoaded() {
    if (!loaded) {
      throw new RuntimeException("Resource manager should be loaded before being used");
    }
  }

  private static Image getDevelopmentCardBackImage(CardLevel level, Map<CardLevel, Image> images) {
    assertLoaded();
    if (level == null) {
      throw new RuntimeException("level cannot be null");
    }

    Image image = images.get(level);
    if (image == null) {
      throw new RuntimeException("could not get the card image");
    }

    return image;
  }

  private static Map<Integer, Image> loadAllCards() {
    Map<Integer, Image> values = new HashMap<>();

    // Load all base and orient development cards
    for (CardLevel level : CardLevel.values()) {
      values.putAll(loadAllBaseDevelopmentCards(level));
      values.putAll(loadAllOrientDevelopmentCards(level));
    }

    // Load the rest
    values.putAll(loadAllNobles());
    values.putAll(loadAllCities());
    //values.putAll(loadAllTradingPosts());
    loadConfirmButtonImage();
    return values;
  }

  private static Map<CardLevel, Image> loadAllBackBaseCards() {
    Map<CardLevel, Image> values = new EnumMap<>(CardLevel.class);
    for (CardLevel level : CardLevel.values()) {
      values.put(level, loadBackBaseDevelopmentCard(level));
    }

    return values;
  }

  private static Map<CardLevel, Image> loadAllBackOrientCards() {
    Map<CardLevel, Image> values = new EnumMap<>(CardLevel.class);
    for (CardLevel level : CardLevel.values()) {
      values.put(level, loadBackOrientDevelopmentCard(level));
    }

    return values;
  }

  private static Map<Integer, Image> loadAllBaseDevelopmentCards(CardLevel level) {
    return loadAllCardFiles(BASE_CARDS_DIR, level);
  }

  private static Image loadBackBaseDevelopmentCard(CardLevel level) {
    return loadBackOfCard(BASE_CARDS_DIR, level);
  }

  private static Map<Integer, Image> loadAllOrientDevelopmentCards(CardLevel level) {
    return loadAllCardFiles(ORIENT_CARDS_DIR, level);
  }

  private static Image loadBackOrientDevelopmentCard(CardLevel level) {
    return loadBackOfCard(ORIENT_CARDS_DIR, level);
  }

  private static Map<Integer, Image> loadAllNobles() {
    return getIdImageMap(NOBLE_CARDS_DIR);
  }

  private static Map<Integer, Image> loadAllCities() {
    return getIdImageMap(CITIES_CARDS_DIR);
  }

  /*
  private static Map<Integer, Image> loadAllTradingPosts() {
    return getIdImageMap(TRADING_POSTS_DIR);
  }
   */

  private static Image loadConfirmButtonImage() {
    return readImage(pathJoin(BUTTONS_DIR, "confirm.png"));
  }

  /**
   * Load confirm button image.
   *
   * @return image of confirm button
   */
  public static Image getConfirmButtonImage() {
    assertLoaded();
    return confirmButtonImage;
  }

  /**
   * Load cancel button image.
   *
   * @return image of cancel button
   */
  private static Image loadCancelButtonImage() {
    return readImage(pathJoin(BUTTONS_DIR, "cancel.png"));
  }

  /**
   * Get cancel button image.
   *
   * @return image of cancel button
   */
  public static Image getCancelButtonImage() {
    assertLoaded();
    return cancelButtonImage;
  }

  /**
   * Load save button image.
   *
   * @return image of save button
   */
  private static Image loadSaveButtonImage() {
    return readImage(pathJoin(BUTTONS_DIR, "save.png"));
  }

  /**
   * Load save button image.
   *
   * @return image of save button
   */
  public static Image getSaveButtonImage() {
    assertLoaded();
    return saveButtonImage;
  }

  private static Image loadExitButtonImage() {
    return readImage(pathJoin(BUTTONS_DIR, "exit.png"));
  }

  /**
   * Get exit button image.
   *
   * @return image of exit button
   */
  public static Image getExitButtonImage() {
    assertLoaded();
    return exitButtonImage;
  }

  /**
   * Load reserve button image.
   *
   * @return image of reserve button
   */
  private static Image loadReserveButtonImage() {
    return readImage(pathJoin(BUTTONS_DIR, "reserve.png"));
  }

  /**
   * Get reserve button image.
   *
   * @return image of reserve button
   */
  public static Image getReserveButtonImage() {
    assertLoaded();
    return reserveButtonImage;
  }

  private static Image loadPurchaseButtonImage() {
    return readImage(pathJoin(BUTTONS_DIR, "purchase.png"));
  }

  /**
   * Load purchase button image.
   *
   * @return image of purchase button
   */
  public static Image getPurcharseButtonImage() {
    assertLoaded();
    return purcharseButtonImage;
  }



  private static Image[] loadAllRules() {
    Map<Integer, Image> rules = getIdImageMap(RULES_DIR);
    Image[] images = new Image[rules.size()];

    // Add every image to the list
    for (int key : rules.keySet()) {
      if (key <= 0 || key > rules.size()) {
        throw new RuntimeException("invalid ordering of the rules");
      }

      // Image index start at 1, so we remove 1
      images[key - 1] = rules.get(key);
    }

    // Return list of images
    return images;
  }

  private static Map<ResourceColor, Image> loadAllTokens() {
    Map<ResourceColor, Image> tokens = new EnumMap<>(ResourceColor.class);
    for (ResourceColor value : ResourceColor.values()) {
      tokens.put(value, getColorImage(TOKENS_DIR, value));
    }

    return tokens;
  }

  private static Map<ShieldColor, Image> loadAllPlayerShields() {
    Map<ShieldColor, Image> shields = new EnumMap<>(ShieldColor.class);
    for (ShieldColor value : ShieldColor.values()) {
      shields.put(value, getColorImage(SHIELDS_DIR, value));
    }

    return shields;
  }

  private static Map<ShieldColor, Image> loadAllTradingPostShields() {
    Map<ShieldColor, Image> shields = new EnumMap<>(ShieldColor.class);
    for (ShieldColor value : ShieldColor.values()) {
      shields.put(value, getColorImage(TRADING_POSTS_DIR, value));
    }

    return shields;
  }

  private static Map<Integer, Image> loadAllCardFiles(String baseDir, CardLevel level) {
    if (level == null) {
      throw new RuntimeException("level cannot be null");
    }

    return switch (level) {
      case ONE -> getIdImageMap(pathJoin(baseDir, "lvl1"));
      case TWO -> getIdImageMap(pathJoin(baseDir, "lvl2"));
      case THREE -> getIdImageMap(pathJoin(baseDir, "lvl3"));
    };
  }

  private static Image loadBackOfCard(String baseDir, CardLevel level) {
    if (level == null) {
      throw new RuntimeException("level cannot be null");
    }
    int value = level.ordinal() + 1;
    return readImage(pathJoin(baseDir, "lvl" + value + " back.png"));
  }

  private static Map<Integer, Image> getIdImageMap(String path) {
    File[] listOfFiles = new File(path).listFiles();
    if (listOfFiles == null) {
      throw new RuntimeException("unable to access " + path);
    }

    Map<Integer, Image> images = new HashMap<>();
    for (File file : listOfFiles) {
      Integer id = getIdFromFile(file);
      if (id != null) {
        images.put(id, readImage(file));
      }
    }

    return images;
  }

  private static Image getColorImage(String baseDir, Object color) {
    if (color == null) {
      throw new RuntimeException("color cannot be null");
    }

    return readImage(pathJoin(baseDir, color.toString().toLowerCase() + ".png"));
  }

  private static Integer getIdFromFile(String fileName) {
    // Safety check
    if (fileName == null || fileName.isEmpty()) {
      throw new RuntimeException("file name is empty or null");
    }

    // Get the index of the first occurrence of '.'
    final int extSep = fileName.indexOf('.');

    // Ignore hidden files
    if (extSep == 0) {
      return null;
    }

    // Parse integer value before dot if it exists
    return parseInt((extSep == -1) ? fileName : fileName.substring(0, extSep));
  }

  private static Integer getIdFromFile(File file) {
    return getIdFromFile(file.getName());
  }

  private static Image readImage(File file) {
    try {
      return new Image(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("could not read file " + file.getPath());
    }
  }

  private static Image readImage(String filePath) {
    return readImage(new File(filePath));
  }

  private static int parseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new RuntimeException("string doesn't have an integer format");
    }
  }

  private static String pathJoin(String first, String... others) {
    StringBuilder fullPath = new StringBuilder(first);
    for (String other : others) {
      fullPath.append(File.separator).append(other);
    }
    return fullPath.toString();
  }
}
