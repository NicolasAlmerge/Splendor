package splendor.view;

import ca.mcgill.nalmer.splendormodels.Cost;
import ca.mcgill.nalmer.splendormodels.ResourceColor;
import java.util.EnumMap;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * The prompt that will show to allow a player to pair a "matching bonus" card to a color.
 */
final class BonusMatchingSelectionPrompt extends StackPane {
  private final EnumMap<ResourceColor, Shape> colorViews = new EnumMap<>(ResourceColor.class);
  private final Text text = new Text("Select bonus matching");

  /**
   * Initialize the prompt.
   */
  BonusMatchingSelectionPrompt() {
    super();
    HBox rectRow = new HBox(5);
    rectRow.setAlignment(Pos.CENTER);
    getChildren().add(text);
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      Rectangle rect = new Rectangle(100, 100, Color.GRAY);
      rect.setArcHeight(5);
      rect.setArcWidth(5);
      rect.setStroke(Color.BEIGE);
      rect.setStrokeWidth(2);
      colorViews.put(color, rect);
      rectRow.getChildren().add(rect);
    }
    Rectangle bg = new Rectangle(550, 150, Color.DARKMAGENTA);
    bg.setArcWidth(10);
    bg.setArcHeight(10);
    getChildren().addAll(bg, rectRow);
  }

  public void setText(String newText) {
    text.setText(newText);
  }

  /**
   * Colors rectangle representing the ResourceColor to be colored in.
   * ResourceColors missing from the given List will be colored GRAY.
   *
   * @param availableBonuses A List of colors that are to be colored
   */
  void setAvailableColors(List<ResourceColor> availableBonuses) {
    for (ResourceColor color : Cost.getAllPossibleColors()) {
      if (!availableBonuses.contains(color) || color == ResourceColor.GOLD) {
        colorViews.get(color).setFill(Color.GRAY);
        continue;
      }
      colorViews.get(color).setFill(
          switch (color) {
            case WHITE -> Color.WHITE;
            case BLUE -> Color.BLUE;
            case GREEN -> Color.GREEN;
            case RED -> Color.RED;
            case BLACK -> Color.BLACK;
            default -> throw new IllegalStateException("Unexpected value: " + color);
          }
      );
    }
  }

  /**
   * Set the action to be performed when a rectangle of a color is clicked by the player.
   *
   * @param color ResourceColor of the rectangle we want to attach the EventHandler to
   * @param e     The actions that will occur when the rectangle is clicked
   */
  void setColorMouseAction(ResourceColor color, EventHandler<MouseEvent> e) {
    colorViews.get(color).setOnMouseClicked(e);
  }
}