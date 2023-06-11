package splendor.view;

import ca.mcgill.nalmer.lsutilities.controller.LsUsersController;
import ca.mcgill.nalmer.lsutilities.model.User;
import ca.mcgill.nalmer.lsutilities.model.UserRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import splendor.model.ClientConstants;
import splendor.model.UserData;

/**
 * Represents the AdminZone Page.
 */

public final class AdminZonePage extends GridPane {

  private final Button backToLobbyButton = new Button("Back");
  private final Button createNew = new Button("Create");
  private final Text errorText = new Text("Username or password is invalid.");
  private final TextField usernameField = new TextField();
  private final PasswordField passwordField = new PasswordField();
  private final ChoiceBox<String> choiceBox = new ChoiceBox<>();
  private final ColorPicker colorPicker = new ColorPicker();
  private final TableView<User> tableOfUsers = new TableView<>();
  final ObservableList<User> listOfUsers = FXCollections.observableArrayList();
  private String selectedOption;

  /**
   * Constructor.
   *
   * @param bp Border pane to use.
   */
  public AdminZonePage(BorderPane bp) {
    setMinHeight(ClientConstants.MIN_HEIGHT);
    setMinWidth(ClientConstants.MIN_WIDTH);

    final Text text1 = new Text("Username:");
    final Text text2 = new Text("Password:");
    final Text text3 = new Text("Type:");
    final Text text4 = new Text("Colour:");

    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    choiceBox.setItems(FXCollections.observableArrayList("Player", "Admin", "Service"));
    colorPicker.setValue(Color.RED); // Set the initial color to red

    Label title = new Label("Admin Zone");
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);


    // Setting the padding
    setPadding(new Insets(10, 10, 10, 10));
    setVgap(20);
    setHgap(50);
    //setAlignment(Pos.CENTER);

    TableColumn<User, String> column1 = new TableColumn<>("Username");
    TableColumn<User, String> column2 = new TableColumn<>("Colour");
    TableColumn<User, String> column3 = new TableColumn<>("Type");

    column1.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_SMALL);
    column2.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_SMALL);
    column3.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_SMALL);

    column1.setCellValueFactory(new PropertyValueFactory<>("name"));
    column2.setCellValueFactory(new PropertyValueFactory<>("preferredColor"));
    column3.setCellValueFactory(new PropertyValueFactory<>("role"));

    tableOfUsers.setItems(listOfUsers);
    tableOfUsers.getColumns().addAll(column1, column2, column3);
    VBox usersVbox = new VBox(tableOfUsers);

    // Add username input
    VBox usernameVbox = new VBox(text1, usernameField);
    // Add password input
    VBox passwordVbox = new VBox(text2, passwordField);
    // Add drop down menu
    StackPane root = new StackPane(choiceBox);
    VBox choiceVbox = new VBox(text3, root);
    // Add colour chooser
    VBox colourVbox = new VBox(text4, colorPicker);

    HBox options = new HBox(usernameVbox, passwordVbox, choiceVbox, colourVbox, createNew);
    options.setSpacing(40);
    // Login button
    HBox backButton = new HBox(backToLobbyButton);
    // Add top label
    HBox titleBox = new HBox(title);

    VBox all = new VBox(backButton, titleBox, options, usersVbox);
    all.setSpacing(20);
    add(all, 1, 4);

    // Define general styles
    Style textStyle = new Style().setFont("normal", "bold", "20px", "'serif'");
    Style darkBackground = new Style().setBackgroundColor("black");

    // Define text styles
    String whiteText = textStyle.copy().setFillColor("white").get();
    String labelText = darkBackground
        .copy()
        .setFont("normal", "bold", "40px", "'serif'")
        .setTextFill("white")
        .get();

    // apple style to the title
    title.setStyle(labelText);

    // Apply style to background
    setStyle(darkBackground.get());

    // Apply styles to text and labels
    text1.setStyle(whiteText);
    text2.setStyle(whiteText);
    text3.setStyle(whiteText);
    text4.setStyle(whiteText);



    // Apply styles to buttons
    backToLobbyButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);

    // Apply style to create button
    createNew.setStyle((ClientConstants.STANDARD_BUTTON_STYLE));

    usernameField.setOnKeyTyped(e -> {
      newUsernameInput();
      //refresh(createNew);
    });

    passwordField.setOnKeyTyped(e -> {
      //refresh(createNew);
      newPasswordInput();
    });

    createNew.setOnAction(e -> {
      setWhenCreateButtonClicked();
    });

    choiceBox.setOnAction(event -> {
      selectedOption = choiceBox.getValue();
    });
  }

  /**
   * Set action when the back button is clicked.
   *
   * @param event Event to trigger.
   */
  public void setWhenBackButtonClicked(EventHandler<ActionEvent> event) {
    backToLobbyButton.setOnAction(event);
  }

  /**
   * Set action when the Create button is clicked.
   */
  public void setWhenCreateButtonClicked() {
    if (passwordField.getText().isEmpty() || usernameField.getText().isEmpty()) {
      System.out.println("ALL empty ???????");
      return;
    } // Find the selected type
    UserRole userSelectedRole;
    if ("Player".equals(selectedOption)) {
      userSelectedRole = UserRole.ROLE_PLAYER;
    } else if ("Admin".equals(selectedOption)) {
      userSelectedRole = UserRole.ROLE_ADMIN;
    } else if ("Service".equals(selectedOption)) {
      userSelectedRole = UserRole.ROLE_SERVICE;
    } else {
      System.out.println("role not working whattup with that" + selectedOption);
      return;
    }
    // Find the selected colour
    Color selectedColour = colorPicker.getValue();
    String hexColour = selectedColour.toString().substring(2, 8).toUpperCase();
    System.out.println(hexColour);
    // Add the new user
    User newUser = new User(usernameField.getText(),
        passwordField.getText(), hexColour, userSelectedRole);
    LsUsersController.addUser(usernameField.getText(),
        passwordField.getText(), hexColour, userSelectedRole, UserData.getToken());
    listOfUsers.add(newUser);
  }

  /**
   * Gets all users.
   */
  public void getUsers() {
    User[] allUsers = LsUsersController.getAllUsers(UserData.getToken());
    listOfUsers.clear();
    for (User user : allUsers) {
      listOfUsers.add(user);
    }
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
      newUsername.append(c);
    }
    usernameField.setText(newUsername.toString());
    System.out.println(newUsername);
    usernameField.positionCaret(newUsername.length());
    usernameField.requestFocus();
  }

  private void newPasswordInput() {
    String password = passwordField.getText();
    StringBuilder newPassword = new StringBuilder();
    for (int i = 0; i < password.length(); ++i) {
      char c = password.charAt(i);
      newPassword.append(c);
    }
    passwordField.setText(newPassword.toString());
    System.out.println(newPassword);
    passwordField.positionCaret(newPassword.length());
    passwordField.requestFocus();
  }
}
