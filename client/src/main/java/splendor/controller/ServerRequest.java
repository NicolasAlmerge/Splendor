package splendor.controller;

import ca.mcgill.nalmer.lsutilities.model.RequestType;
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
import com.mashape.unirest.http.HttpResponse;
import splendor.model.ServerFetchException;

/**
 * Utility class for server requests.
 */
abstract class ServerRequest {
  private final String subPath;
  private final RequestType reqType;
  private static final Gson gson;

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

    // Create the GSON object
    gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(factory1)
        .registerTypeAdapterFactory(factory2)
        .registerTypeAdapterFactory(factory3)
        .registerTypeAdapterFactory(factory4)
        .registerTypeAdapterFactory(factory5)
        .create();
  }

  /**
   * Constructor.
   *
   * @param path Path to get, starting with '/'.
   * @param type Request type.
   */
  protected ServerRequest(String path, RequestType type) {
    subPath = path;
    reqType = type;
  }

  /**
   * Creates a new get request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static ServerGetRequest get(String subPath) {
    return new ServerGetRequest(subPath);
  }

  /**
   * Creates a new put request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static ServerNonGetRequest put(String subPath) {
    return ServerNonGetRequest.putRequest(subPath);
  }

  /**
   * Creates a new post request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static ServerNonGetRequest post(String subPath) {
    return ServerNonGetRequest.postRequest(subPath);
  }

  /**
   * Creates a new delete request.
   *
   * @param subPath Sub-path, starting with /
   * @return New request object.
   */
  public static ServerNonGetRequest delete(String subPath) {
    return ServerNonGetRequest.deleteRequest(subPath);
  }

  /**
   * Gets the server base location, with '/splendor' included.
   *
   * @return Server location url.
   */
  public static String getServerLocation() {
    return NetworkParameters.getServerLocation() + "/splendor";
  }

  /**
   * Get the specified sub path.
   *
   * @return Specified sub path.
   */
  public final String getSubPath() {
    return subPath;
  }

  /**
   * Get the specified request type.
   *
   * @return Specified request type.
   */
  public final RequestType getRequestType() {
    return reqType;
  }

  /**
   * Get response as string. This method checks the error code to make sure it is 200 or 201.
   *
   * @return Response string.
   */
  public final String asString() throws RuntimeException {
    HttpResponse<String> request = getStringRequest();
    checkErrorCode(request);
    return request.getBody();
  }

  /**
   * Get response as an object.
   *
   * @param <T>           Type parameter.
   * @param responseClass Class to model data for.
   * @return Response as object.
   */
  public abstract <T> T asObject(Class<? extends T> responseClass) throws RuntimeException;

  /**
   * Execute the request without returning anything.
   */
  public final void execute() throws RuntimeException {
    checkErrorCode(getStringRequest());
  }

  /**
   * Sets the game id.
   *
   * @param gameId Game id.
   * @return Self-object for chaining.
   */
  public abstract ServerRequest setGameId(String gameId);

  /**
   * Get string request. This method does not check the return code.
   *
   * @return Http response containing string data.
   * @throws RuntimeException if request failed.
   */
  public abstract HttpResponse<String> getStringRequest() throws RuntimeException;

  /**
   * Check the error code for a response.
   *
   * @param <T>      Type parameter.
   * @param response Response to check.
   * @throws RuntimeException if error code is not 200 or 201.
   */
  protected final <T> void checkErrorCode(HttpResponse<T> response) throws RuntimeException {
    if (response.getStatus() != 200 && response.getStatus() != 201) {
      throw new ServerFetchException(
              reqType, getSubPath(), response.getStatus(),
              response.getBody().toString(), response.getStatusText()
      );
    }
  }

  /**
   * Gets the gson object.
   *
   * @return Gson object.
   */
  public static Gson getGson() {
    return gson;
  }
}