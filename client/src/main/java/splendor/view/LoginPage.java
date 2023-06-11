package splendor.view;

import ca.mcgill.nalmer.lsutilities.controller.LsOauthController;
import ca.mcgill.nalmer.lsutilities.model.LobbyServiceFetchException;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import splendor.model.ClientConstants;
import splendor.model.UserData;

/**
 * Represents the login page.
 */
public final class LoginPage extends GridPane {
  private final Button backButton = new Button("Back");
  private final Text errorText = new Text("Username or password is invalid.");
  private final TextField usernameField = new TextField();
  private final PasswordField passwordField = new PasswordField();

  /**
   * Constructor.
   *
   * @param bp        Border pane to use.
   * @param lobbyPage Lobby page to connect to after login.
   */
  public LoginPage(BorderPane bp, Pane lobbyPage) {
    setMinHeight(ClientConstants.MIN_HEIGHT);
    setMinWidth(ClientConstants.MIN_WIDTH);

    final Text text1 = new Text("Username");
    final Text text2 = new Text("Password");

    Button logInButton = new Button("Log In");

    usernameField.setOnKeyTyped(e -> {
      newUsernameInput();
      refresh(bp, lobbyPage, logInButton);
    });

    passwordField.setOnKeyTyped(e -> refresh(bp, lobbyPage, logInButton));

    // Create label
    Label label = new Label("Log In");
    label.setAlignment(Pos.CENTER);
    label.setTextAlignment(TextAlignment.CENTER);

    // Setting the padding
    setPadding(new Insets(10, 10, 10, 10));
    setVgap(10);
    setHgap(10);
    setAlignment(Pos.CENTER);

    // Add top label
    add(label, 1, 0);

    // Add username input
    add(createSubPane(text1, usernameField), 1, 1);

    // Add password input
    add(createSubPane(text2, passwordField), 1, 2);

    // Error text
    add(errorText, 1, 3);

    // Login button
    add(logInButton, 2, 4);
    add(backButton, 0, 4);

    // Define general styles
    Style textStyle = new Style().setFont("normal", "bold", "20px", "'serif'");
    Style darkBackground = new Style().setBackgroundColor("black");

    // Define text styles
    String whiteText = textStyle.copy().setFillColor("white").get();
    String labelText = darkBackground
        .copy()
        .setFont("normal", "bold", "40px", "'serif'")
        .setTextFill("#FFD700")
        .get();

    // Apply style to background
    setStyle(darkBackground.get());

    // Apply styles to text and labels
    label.setStyle(labelText);
    text1.setStyle(whiteText);
    text2.setStyle(whiteText);

    // Define error text style and apply
    String redText = textStyle.copy().setFillColor("red").get();
    errorText.setStyle(redText);

    // Apply styles to buttons
    logInButton.setStyle(ClientConstants.FADED_BUTTON_STYLE);
    backButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);
  }

  /**
   * Set action when the back button is clicked.
   *
   * @param event Event to trigger.
   */
  public void setWhenBackButtonClicked(EventHandler<ActionEvent> event) {
    backButton.setOnAction(event);
  }

  /**
   * Clears previous sensitive data.
   */
  public void clearData() {
    clearData(false);
  }

  private void clearData(boolean errorMessage) {
    errorText.setVisible(errorMessage);
    passwordField.clear();
  }

  private GridPane createSubPane(Text node1, TextField node2) {
    GridPane pane = new GridPane();
    pane.setVgap(10);
    pane.setHgap(10);
    pane.setAlignment(Pos.CENTER_RIGHT);
    pane.add(node1, 0, 0);
    pane.add(node2, 1, 0);
    return pane;
  }

  private void newUsernameInput() {
    String username = usernameField.getText();
    StringBuilder newUsername = new StringBuilder();
    for (int i = 0; i < username.length(); ++i) {
      char c = username.charAt(i);
      if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
        newUsername.append(c);
      }
    }
    usernameField.setText(newUsername.toString());
    usernameField.positionCaret(newUsername.length());
    usernameField.requestFocus();
  }

  /**
   * Set action when the login button is clicked.
   *
   * @param bp          Border pane to use.
   * @param lobbyPage   Lobby page to connect to after login.
   * @param logInButton Login button to click on to switch to Lobby page
   */
  private void refresh(BorderPane bp, Pane lobbyPage, Button logInButton) {
    if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
      logInButton.setOnAction(f -> {
      });
      logInButton.setStyle(ClientConstants.FADED_BUTTON_STYLE);
      return;
    }

    logInButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);
    logInButton.setOnAction(actionEvent -> {
      logInButton.setOnAction(f -> {
      });
      logInButton.setStyle(ClientConstants.FADED_BUTTON_STYLE);

      try {
        UserData.connect(usernameField.getText(), passwordField.getText());
        LobbyPage.getAdminZone().setVisible(
            LsOauthController.getRole(UserData.getToken()).equals(UserRole.ROLE_ADMIN)
        );
      } catch (LobbyServiceFetchException e) {
        e.printStackTrace();
        clearData(true);
        return;
      }
      clearData();
      bp.setCenter(lobbyPage);
    });
  }
}
