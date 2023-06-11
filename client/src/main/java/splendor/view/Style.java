package splendor.view;

/**
 * Represents a particular JavaFX style.
 */
final class Style {
  private final StringBuilder stringBuilder;

  /**
   * Create a new style.
   */
  public Style() {
    stringBuilder = new StringBuilder();
  }

  private Style(StringBuilder sb) {
    stringBuilder = new StringBuilder(sb);
  }

  /**
   * Creates a copy of the style.
   *
   * @return Copy of the style.
   */
  public Style copy() {
    return new Style(stringBuilder);
  }

  /**
   * Sets the fill color.
   *
   * @param color Color (any valid format).
   * @return Self object for chaining.
   */
  public Style setFillColor(String color) {
    return add("-fx-fill", color);
  }

  /**
   * Sets the font.
   *
   * @param style   Font style.
   * @param variant Font variant.
   * @param weight  Font weight.
   * @param stretch Font stretch.
   * @return Self object for chaining.
   */
  public Style setFont(String style, String variant, String weight, String stretch) {
    return add("-fx-font", style, variant, weight, stretch);
  }

  /**
   * Sets the background color.
   *
   * @param color Color (any valid format).
   * @return Self object for chaining.
   */
  public Style setBackgroundColor(String color) {
    return add("-fx-background-color", color);
  }

  /**
   * Sets the text fill color.
   *
   * @param color Color (any valid format).
   * @return Self object for chaining.
   */
  public Style setTextFill(String color) {
    return add("-fx-text-fill", color);
  }

  /**
   * Gets the compiled CSS string.
   *
   * @return CSS string.
   */
  public String get() {
    return stringBuilder.toString();
  }

  private Style add(String key, String... values) {
    stringBuilder.append(key).append(':');
    for (String value : values) {
      stringBuilder.append(' ').append(value);
    }
    stringBuilder.append(';');
    return this;
  }
}
