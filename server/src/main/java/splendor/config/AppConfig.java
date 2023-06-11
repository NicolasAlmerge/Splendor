package splendor.config;

import ca.mcgill.nalmer.splendormodels.Action;
import ca.mcgill.nalmer.splendormodels.ActionType;
import ca.mcgill.nalmer.splendormodels.BaseDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.Card;
import ca.mcgill.nalmer.splendormodels.CardType;
import ca.mcgill.nalmer.splendormodels.City;
import ca.mcgill.nalmer.splendormodels.DevelopmentCard;
import ca.mcgill.nalmer.splendormodels.DevelopmentCardType;
import ca.mcgill.nalmer.splendormodels.Noble;
import ca.mcgill.nalmer.splendormodels.NonPrestigeCard;
import ca.mcgill.nalmer.splendormodels.NonPrestigeCardType;
import ca.mcgill.nalmer.splendormodels.OrientDevelopmentCard;
import ca.mcgill.nalmer.splendormodels.PrestigeCard;
import ca.mcgill.nalmer.splendormodels.PrestigeCardType;
import ca.mcgill.nalmer.splendormodels.PurchaseCardAction;
import ca.mcgill.nalmer.splendormodels.ReserveCardAction;
import ca.mcgill.nalmer.splendormodels.TakeTokensAction;
import ca.mcgill.nalmer.splendormodels.TradingPost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class.
 */
@Configuration
class AppConfig implements WebMvcConfigurer {

  /**
   * Constructor.
   */
  public AppConfig() {
  }

  /**
   * Get gson bean for serialization and deserialization.
   *
   * @return Gson bean.
   */
  @Bean
  public Gson gson() {
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
    return new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory1)
        .registerTypeAdapterFactory(factory2)
        .registerTypeAdapterFactory(factory3)
        .registerTypeAdapterFactory(factory4)
        .registerTypeAdapterFactory(factory5)
        .create();
  }
}
