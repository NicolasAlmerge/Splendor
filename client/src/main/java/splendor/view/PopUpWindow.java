package splendor.view;

import ca.mcgill.nalmer.splendormodels.GameExtension;
import javafx.application.Application;
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
public class PopUpWindow extends Application {
  // private static String extension;
  private String sessionId;

  /**
   * Constructs the PopUpWindow with a session ID.
   *
   * @param extension extension
   * @param sessionId session ID
   */
  public PopUpWindow(String extension, String sessionId) {
    // this.extension = extension;
    this.sessionId = sessionId;
  }

  @Override
  public void start(Stage primaryStage) {

    final Label label = new Label("Please choose an extension to play this game with!");
    Button closeButton = new Button("Launch");
    closeButton.setDisable(true); // disable close button until a radio button is selected

    RadioButton citiesRadioButton = new RadioButton("Cities");
    RadioButton tradingPostsRadioButton = new RadioButton("Trading Posts");
    RadioButton baseGameRadioButton = new RadioButton("Base Game + Orient");

    ToggleGroup extensionsToggleGroup = new ToggleGroup();
    citiesRadioButton.setToggleGroup(extensionsToggleGroup);
    tradingPostsRadioButton.setToggleGroup(extensionsToggleGroup);
    baseGameRadioButton.setToggleGroup(extensionsToggleGroup);

    extensionsToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      closeButton.setDisable(false);
    });

    closeButton.setOnAction(event -> {
      if (citiesRadioButton.isSelected()) {
        ClientServerController.createNewGame(
                sessionId, GameExtension.BASE_ORIENT_CITIES
        );
      } else if (tradingPostsRadioButton.isSelected()) {
        ClientServerController.createNewGame(
                sessionId, GameExtension.BASE_ORIENT_TRADING_POSTS
        );
      } else if (baseGameRadioButton.isSelected()) {
        ClientServerController.createNewGame(
                sessionId, GameExtension.BASE_ORIENT
        );
      } else {
        System.out.println("No Extension selected");
        return;
      }
      Stage popupStage = (Stage) closeButton.getScene().getWindow();
      popupStage.close();
      extensionsToggleGroup.selectToggle(null); // clear the selection
    });

    VBox vbox = new VBox(10);
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().addAll(label, citiesRadioButton,
            tradingPostsRadioButton, baseGameRadioButton, closeButton);

    Stage popupStage = new Stage();
    popupStage.initModality(Modality.APPLICATION_MODAL);
    popupStage.setScene(new Scene(vbox, 300, 200));
    popupStage.setTitle("Extension Selection");

    popupStage.showAndWait();
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
