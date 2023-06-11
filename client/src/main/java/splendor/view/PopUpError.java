package splendor.view;

import ca.mcgill.nalmer.splendormodels.GameExtension;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import splendor.controller.ClientServerController;

/**
 * Pop up window class for extension selection.
 */
public class PopUpError extends Application {

  private String message;

  /**
   * close button.
   */
  public Button closeButton =  new Button("Close");

  /**
   * Constructs the PopUpWindow with a session ID.
   *
   * @param message session ID
   */
  public PopUpError(String message) {
    this.message = message;
  }

  @Override
  public void start(Stage primaryStage) {

    final Label label = new Label("Warning!");
    final Label messageToUser = new Label(message);
    closeButton.setDisable(false);


    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(label, messageToUser, closeButton);

    Stage popupStage = new Stage();
    popupStage.initModality(Modality.APPLICATION_MODAL);
    popupStage.setScene(new Scene(vbox, 300, 200));
    popupStage.setTitle("Extension Selection");

    popupStage.showAndWait();
  }

  /**
   * Set on close button.
   *
   * @param e event.
   */
  public void setOnExit(EventHandler<ActionEvent> e) {
    closeButton.setOnAction(e);
  }

  /**
   * main.
   *
   * @param args args
   */
  public static void main(String[] args) {
    launch(args);
  }

}
