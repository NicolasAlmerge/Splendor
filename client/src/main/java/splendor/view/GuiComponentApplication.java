package splendor.view;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import splendor.controller.NetworkParameters;
import splendor.controller.ResourceManager;

/**
 * Class with a main to test Gui objects.
 */
public final class GuiComponentApplication extends Application {
  /**
   * Create new Application.
   */

  PopUpError popUpGameOver;

  /**
   * Constructor.
   *
   * @param popUpGameOver Launch arguments.
   */
  public GuiComponentApplication(PopUpError popUpGameOver) {
    this.popUpGameOver = popUpGameOver;
  }

  /**
   * Main.
   *
   * @param args Launch arguments.
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    ResourceManager.loadAllResources();
    NetworkParameters.load();
    try {
      new GameBoardPage(popUpGameOver);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Parent node = new TradingPostsView();
    stage.setScene(new Scene(node));
    stage.show();
  }

  @Override
  public void stop() {
    System.exit(0);
  }
}
