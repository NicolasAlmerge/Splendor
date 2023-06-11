package ca.mcgill.nalmer.splendormodels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

/**
 * Utility class.
 */
public final class Utils {
  private static final Gson gsonGame;

  static {
    // Register action subtypes
    RuntimeTypeAdapterFactory<Action> factory1 = RuntimeTypeAdapterFactory
        .of(Action.class, "type")
        .registerSubtype(TakeTokensAction.class, ActionType.TAKE_TOKENS.toString())
        .registerSubtype(PurchaseCardAction.class, ActionType.PURCHASE_CARD.toString())
        .registerSubtype(ReserveCardAction.class, ActionType.RESERVE_CARD.toString());

    // Register card subtypes
    RuntimeTypeAdapterFactory<Card> factory2 = RuntimeTypeAdapterFactory
        .of(Card.class, "type")
        .registerSubtype(BaseDevelopmentCard.class, CardType.BASE.toString())
        .registerSubtype(OrientDevelopmentCard.class, CardType.ORIENT.toString())
        .registerSubtype(Noble.class, CardType.NOBLE.toString())
        .registerSubtype(City.class, CardType.CITY.toString())
        .registerSubtype(TradingPost.class, CardType.TRADING_POST.toString());

    // Register prestige card subtypes
    RuntimeTypeAdapterFactory<PrestigeCard> factory3 = RuntimeTypeAdapterFactory
        .of(PrestigeCard.class, "type")
        .registerSubtype(BaseDevelopmentCard.class, PrestigeCardType.BASE.toString())
        .registerSubtype(OrientDevelopmentCard.class, PrestigeCardType.ORIENT.toString())
        .registerSubtype(Noble.class, PrestigeCardType.NOBLE.toString());

    // Register non-prestige card subtypes
    RuntimeTypeAdapterFactory<NonPrestigeCard> factory4 = RuntimeTypeAdapterFactory
        .of(NonPrestigeCard.class, "type")
        .registerSubtype(City.class, NonPrestigeCardType.CITY.toString())
        .registerSubtype(TradingPost.class, NonPrestigeCardType.TRADING_POST.toString());

    // Register development card subtypes
    RuntimeTypeAdapterFactory<DevelopmentCard> factory5 = RuntimeTypeAdapterFactory
        .of(DevelopmentCard.class, "type")
        .registerSubtype(BaseDevelopmentCard.class, DevelopmentCardType.BASE.toString())
        .registerSubtype(OrientDevelopmentCard.class, DevelopmentCardType.ORIENT.toString());

    // Create the GSON bean
    gsonGame = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory1)
        .registerTypeAdapterFactory(factory2)
        .registerTypeAdapterFactory(factory3)
        .registerTypeAdapterFactory(factory4)
        .registerTypeAdapterFactory(factory5)
        .create();
  }

  /**
   * Private constructor.
   */
  private Utils() {
  }

  /**
   * Gets the {@link Gson} instance to serialize and deserialize a {@link Game}.
   *
   * @return {@link Gson} instance.
   */
  public static Gson getGameGson() {
    return gsonGame;
  }
}
