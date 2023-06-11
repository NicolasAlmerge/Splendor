package splendor.controller;

import ca.mcgill.nalmer.lsutilities.controller.LsGameServicesController;
import ca.mcgill.nalmer.lsutilities.model.AutoRefreshOauthToken;
import ca.mcgill.nalmer.lsutilities.model.GameService;
import ca.mcgill.nalmer.lsutilities.model.OauthToken;
import ca.mcgill.nalmer.splendormodels.Constants;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Splendor game registrator.
 *
 * @author Nicolas Almerge
 * @hidden
 * @since 1.0
 */
@Component
public final class GameServiceRegistrator {
  private static final String SERVICE_USERNAME = "xox";
  private static final String SERVICE_PASSWORD = "laaPhie*aiN0";
  private static final GameService service = new GameService(
      "splendor", "Splendor", Constants.MIN_NUMBER_OF_PLAYERS, Constants.MAX_NUMBER_OF_PLAYERS
  );
  private static AutoRefreshOauthToken refreshOauthToken;
  private final Logger logger = LoggerFactory.getLogger(GameServiceRegistrator.class);

  /**
   * Constructor.
   */
  public GameServiceRegistrator() {
  }

  /**
   * Gets the splendor game service.
   *
   * @return Splendor game service.
   */
  public static GameService getSplendorGameService() {
    return service;
  }

  /**
   * Registers the splendor game service.
   */
  @PostConstruct
  public void registerSplendor() {
    refreshOauthToken = new AutoRefreshOauthToken(SERVICE_USERNAME, SERVICE_PASSWORD);
    LsGameServicesController.addGameService(service, refreshOauthToken.getOauthToken());
    logger.info("Registered splendor game service.");
  }

  /**
   * Unregisters the splendor game service.
   */
  @PreDestroy
  public void unregisterSplendor() {
    if (refreshOauthToken == null) {
      logger.warn("Could not unregister the splendor game service as the oauth token was null.");
    } else {
      LsGameServicesController.deleteGameService(service, refreshOauthToken.getOauthToken());
      logger.info("Unregistered splendor game service.");
    }
  }

  /**
   * Gets the service account {@link OauthToken}.
   *
   * @return Service account {@link OauthToken}.
   */
  static OauthToken getServiceToken() {
    return refreshOauthToken.getOauthToken();
  }
}
