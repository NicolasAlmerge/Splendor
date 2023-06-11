package splendor.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Allows for easy creation of a StackPane with a label that displays integer values,
 * a circle to provide contrast for the text, and a root node that will be on the bottom
 * of the stack.
 *
 * @param <T> Class of the Node that will be the root.
 */
final class NumberedPane<T extends Node> extends StackPane {
  private final Label valueLabel;
  private final T root;

  /**
   * Put a label on top of the given root.
   *
   * @param node Node to be at the bottom of the StackPane
   */
  public NumberedPane(T node) {
    root = node;
    valueLabel = new Label("0");
    valueLabel.setTextFill(Color.BEIGE);
    Circle bg = new Circle(10);
    bg.setFill(Color.GRAY);
    StackPane labelWithCircle = new StackPane(bg, valueLabel);
    getChildren().addAll(root, labelWithCircle);
  }

  /**
   * Get the root node that was used.
   *
   * @return Root node at the bottom of the StackPane
   */
  public T getRoot() {
    return root;
  }

  /**
   * Set the value of the label.
   *
   * @param v Integer that will be the new value shown by the label.
   */
  public void setValue(int v) {
    valueLabel.setText(String.valueOf(v));
  }
}
