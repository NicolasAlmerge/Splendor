package splendor.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import splendor.controller.ResourceManager;
import splendor.model.ClientConstants;

/**
 * Represents the how to play page.
 */
public final class HowToPlayPage extends GridPane {
  private int pageIndex = 0;

  /**
   * Constructor.
   *
   * @param bp   Border pane to use.
   * @param pane Pane to use.
   * @throws java.lang.RuntimeException if file access failed.
   */
  public HowToPlayPage(BorderPane bp, Pane pane) throws RuntimeException {

    // Set Styles
    Style darkBackground = new Style().setBackgroundColor("black");
    setStyle(darkBackground.get());

    Label title = new Label("Game Rules");
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);

    // Header text style
    String labelText = darkBackground
        .copy()
        .setFont("normal", "bold", "40px", "'serif'")
        .setTextFill("#FFD700")
        .get();
    title.setStyle(labelText);

    // Create back button
    Button backButton = new Button("Back");
    backButton.setStyle(ClientConstants.GOlDEN_BUTTON_STYLE);
    backButton.setOnAction(e -> bp.setCenter(pane));

    // Get images
    final List<ImageView> imageViews = new ArrayList<>();

    // Get all rules images
    for (Image image : ResourceManager.getAllRules()) {
      ImageView imageView = new ImageView(image);
      imageView.setPreserveRatio(true);
      imageView.setFitWidth(575);
      imageViews.add(imageView);
    }

    // Pane
    GridPane grid = new GridPane();
    grid.setMinWidth(ClientConstants.MIN_WIDTH);
    grid.setMinHeight(ClientConstants.MIN_HEIGHT);
    grid.setAlignment(Pos.CENTER);

    ColumnConstraints button = new ColumnConstraints();
    button.setMinWidth(100);
    button.setHalignment(HPos.CENTER);

    final ColumnConstraints image = new ColumnConstraints();

    // Buttons
    Button previous = new Button("<");
    Button next = new Button(">");
    String style = "-fx-background-color: gold; -fx-padding: 10px 15px; -fx-border-color: black;";
    previous.setStyle(style);
    next.setStyle(style);

    previous.setOnAction(e -> {
      if (pageIndex == 0) {
        return;
      }
      --pageIndex;

      grid.getChildren().clear();
      if (pageIndex > 0) {
        grid.add(previous, 0, 0, 1, 1);
      }

      grid.add(imageViews.get(pageIndex), 1, 0, 1, 1);
      grid.add(next, 2, 0, 1, 1);
    });

    next.setOnAction(e -> {
      if (pageIndex == imageViews.size() - 1) {
        return;
      }
      ++pageIndex;

      grid.getChildren().clear();
      grid.add(previous, 0, 0, 1, 1);
      grid.add(imageViews.get(pageIndex), 1, 0, 1, 1);
      if (pageIndex < imageViews.size() - 1) {
        grid.add(next, 2, 0, 1, 1);
      }
    });

    grid.add(imageViews.get(pageIndex), 1, 0, 1, 1);
    grid.add(next, 2, 0, 1, 1);
    grid.getColumnConstraints().addAll(button, image, button);

    final HBox hbox = new HBox(450);
    hbox.getChildren().addAll(backButton, title);
    final VBox vbox = new VBox(10);
    vbox.getChildren().addAll(hbox, grid);
    getChildren().addAll(vbox);
  }
}
