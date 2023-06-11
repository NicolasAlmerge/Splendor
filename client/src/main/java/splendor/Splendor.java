package splendor;

import ca.mcgill.nalmer.lsutilities.data.LobbyServiceData;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import splendor.controller.NetworkParameters;
import splendor.controller.ResourceManager;
import splendor.model.ClientConstants;
import splendor.view.AdminZonePage;
import splendor.view.GameBoardPage;
import splendor.view.HomePage;
import splendor.view.HowToPlayPage;
import splendor.view.LobbyPage;
import splendor.view.LoginPage;
import splendor.view.PopUpError;
import splendor.view.SavedSessionsLobbyPage;

/**
 * Represents the application.
 *
 * @author Nicolas Almerge
 * @hidden
 * @since 1.0
 */
public final class Splendor extends Application {

  /**
   * Creates a new application.
   * This constructor should not be called manually.
   */
  public Splendor() {
  }

  /**
   * Start the program.
   *
   * @param args Not used.
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    // Load all resources
    ResourceManager.loadAllResources();
    NetworkParameters.load();

    // Set the networking parameters
    LobbyServiceData.setLsLocation(NetworkParameters.getLobbyLocation());

    // Set title
    stage.setTitle("Splendor");

    // Set minimum size
    stage.setMinWidth(ClientConstants.MIN_WIDTH);
    stage.setMinHeight(ClientConstants.MIN_HEIGHT);

    // Start with minimum size window
    stage.setWidth(ClientConstants.MIN_WIDTH);
    stage.setHeight(ClientConstants.MIN_HEIGHT);

    // Create all pages
    final BorderPane bp = new BorderPane();
    final HomePage homePage = new HomePage();
    final HowToPlayPage howToPage = new HowToPlayPage(bp, homePage);
    final PopUpError popUpGameOver = new PopUpError("Game is over. Thanks for playing!");
    final GameBoardPage gamePage = new GameBoardPage(popUpGameOver);
    final LobbyPage lobbyPage = new LobbyPage(bp, gamePage);
    final SavedSessionsLobbyPage savedSessionsLobbyPage = new SavedSessionsLobbyPage();
    final LoginPage loginPage = new LoginPage(bp, lobbyPage);
    final AdminZonePage adminZonePage = new AdminZonePage(bp);


    // Set home page as center
    bp.setCenter(homePage);

    // Assign page-changing to appropriate buttons
    homePage.setWhenLoginClick(e -> {
      loginPage.clearData();
      bp.setCenter(loginPage);
    });
    homePage.setWhenHowToPlayClick(e -> bp.setCenter(howToPage));
    loginPage.setWhenBackButtonClicked(e -> bp.setCenter(homePage));
    lobbyPage.setWhenBackButtonClicked(e -> {
      loginPage.clearData();
      bp.setCenter(loginPage);
    });
    lobbyPage.setWhenSavedSessionsClicked(e -> {
      savedSessionsLobbyPage.getSavedSessions();
      bp.setCenter(savedSessionsLobbyPage);
    });
    lobbyPage.setWhenAdminButtonClicked(e -> {
      adminZonePage.getUsers();
      bp.setCenter(adminZonePage);
    });
    savedSessionsLobbyPage.setWhenCreateButtonClicked(e -> {
      bp.setCenter(lobbyPage);
    });
    adminZonePage.setWhenBackButtonClicked(e -> {
      bp.setCenter(lobbyPage);
    });


    gamePage.setOnExit(e -> {
      bp.setCenter(lobbyPage);
    });

    popUpGameOver.setOnExit(e -> {
      Stage popupStage = (Stage) popUpGameOver.closeButton.getScene().getWindow();
      popupStage.close();
      bp.setCenter(lobbyPage);
    });

    // Display home page
    stage.setScene(new Scene(bp));
    stage.show();
  }

  @Override
  public void stop() {
    System.exit(0);
  }
}
