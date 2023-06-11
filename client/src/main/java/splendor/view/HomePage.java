package splendor.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import splendor.controller.ResourceManager;
import splendor.model.ClientConstants;

/**
 * Represents the home page.
 */
public final class HomePage extends GridPane {
  private final Button toLoginButton = new Button("Log In");
  private final Button toHowToButton = new Button("How to Play");

  /**
   * Constructor.
   */
  public HomePage() {
    // Create new labels
    ImageView logo = new ImageView();
    logo.setImage(ResourceManager.getLogoImage());
    logo.setPreserveRatio(true);
    logo.setFitWidth(575);

    // Create new buttons
    HBox buttonBox = new HBox(360, toLoginButton, toHowToButton);

    // Set pane
    setMinSize(ClientConstants.MIN_WIDTH, ClientConstants.MIN_HEIGHT);
    setPadding(new Insets(10, 10, 10, 10));
    VBox box = new VBox(logo, buttonBox);
    box.setSpacing(5);
    getChildren().add(box);
    setAlignment(Pos.CENTER);

    // Set styles
    setStyle("-fx-background-color: black;");
    buttonBox.setStyle("-fx-font: normal bold 20px 'serif';");
    toLoginButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);
    toHowToButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);
  }

  /**
   * Set action when the login button is clicked.
   *
   * @param event Event to trigger.
   */
  public void setWhenLoginClick(EventHandler<ActionEvent> event) {
    toLoginButton.setOnAction(event);
  }

  /**
   * Set action when the how to play button is clicked.
   *
   * @param event Event to trigger.
   */
  public void setWhenHowToPlayClick(EventHandler<ActionEvent> event) {
    toHowToButton.setOnAction(event);
  }
}
