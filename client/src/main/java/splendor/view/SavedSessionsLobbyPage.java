package splendor.view;

import ca.mcgill.nalmer.lsutilities.controller.LsGameServicesController;
import ca.mcgill.nalmer.lsutilities.controller.LsSessionController;
import ca.mcgill.nalmer.lsutilities.model.SaveGame;
import ch.qos.logback.core.net.server.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import splendor.controller.ClientServerController;
import splendor.model.ClientConstants;
import splendor.model.UserData;

/**
 * Represents the saved sessions lobby page.
 */
public final class SavedSessionsLobbyPage extends GridPane {

  final ObservableList<SavedGameModel> listOfSessions = FXCollections.observableArrayList();
  private final Button availableSessionsButton = new Button("Back To Available Sessions");

  /**
   * Constructor.
   *
   */
  public SavedSessionsLobbyPage() {
    setMinHeight(ClientConstants.MIN_HEIGHT);
    setMinWidth(ClientConstants.MIN_WIDTH);

    final HBox container = new HBox(25);

    final VBox containerVbox = new VBox(8); // spacing = 8

    // Lobby Label
    Label title = new Label("Saved Games");
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);

    // Join Game VBox
    final VBox joinGameVbox = new VBox(10); // spacing = 8
    availableSessionsButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);
    HBox header = new HBox();
    header.setSpacing(500);
    header.setPadding(new Insets(10));
    header.getChildren().addAll(title, availableSessionsButton);

    // Table of Save Games Vbox
    StackPane stackPane = new StackPane();
    stackPane.setAlignment(Pos.CENTER);
    final Label tableOfGamesTitle = new Label("Choose a Saved Game to make into a Session");
    final TableView<SavedGameModel> tableOfGames = new TableView<>();
    tableOfGames.setMaxWidth(785);
    TableColumn<SavedGameModel, SimpleStringProperty> column1 = new TableColumn<>("Session ID");
    TableColumn<SavedGameModel, SimpleStringProperty> column3 = new TableColumn<>("Players");
    TableColumn<SavedGameModel, SimpleStringProperty> column4 = new TableColumn<>("Player Count");
    column1.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_SMALL);
    column3.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_BIG);
    column4.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_EXTRA_SMALL);
    column1.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
    column3.setCellValueFactory(new PropertyValueFactory<>("players"));
    column4.setCellValueFactory(new PropertyValueFactory<>("playerCount"));
    TableColumn<SavedGameModel, Void> launchColumn = new TableColumn<>("Create Game");
    launchColumn.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_SMALL);
    Callback<TableColumn<SavedGameModel, Void>, TableCell<SavedGameModel, Void>> launchCellFactory =
        new Callback<>() {
          public TableCell<SavedGameModel,
              Void> call(final TableColumn<SavedGameModel, Void> param) {
            return new TableCell<>() {
              private final Button createButton = new Button();
              {
                createButton.setOnAction((ActionEvent event) -> {
                  SavedGameModel sessionModel = getTableView().getItems().get(getIndex());
                  ClientServerController.launchSessionFromSave(UserData.getUsername(),
                          sessionModel.getSaveGame(),
                          UserData.getToken());
                });
              }

              @Override
              public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  createButton.setText("Create Session");
                  createButton.setStyle("-fx-background-color: green;");
                  setGraphic(createButton);
                }
              }
            };
          }
        };

    launchColumn.setCellFactory(launchCellFactory);

    tableOfGames.setItems(listOfSessions);
    tableOfGames.getColumns().addAll(launchColumn, column1, column3, column4);


    listOfSessions.clear();
    tableOfGames.setItems(listOfSessions);

    stackPane.getChildren().addAll(tableOfGamesTitle, tableOfGames);

    // Add separate components to container
    containerVbox.getChildren().addAll(header, joinGameVbox, stackPane);
    container.getChildren().addAll(containerVbox);

    setAlignment(Pos.CENTER);
    add(container, 1, 0);

    // Set Styles
    Style darkBackground = new Style().setBackgroundColor("black");
    setStyle(darkBackground.get());

    // Header text style
    String labelText = darkBackground
        .copy()
        .setFont("normal", "bold", "40px", "'serif'")
        .setTextFill("#FFD700")
        .get();
    title.setStyle(labelText);


    // Set the column header style using CSS
    column1.setStyle("-fx-text-fill: white;");
    column3.setStyle("-fx-text-fill: white;");
    column4.setStyle("-fx-text-fill: white;");
    launchColumn.setStyle("-fx-text-fill: white;");

    tableOfGamesTitle.setStyle("-fx-control-inner-background: #333333; "
        + "-fx-text-background-color: white;");

    // Set the row style using CSS
    tableOfGames.setRowFactory(tv -> {
      TableRow<SavedGameModel> row = new TableRow<>();
      row.setStyle("-fx-background-color: #444444;");
      return row;
    });
  }


  /**
   * Set action when the back to available sessions button is clicked.
   *
   * @param event Event to trigger.
   */
  public void setWhenCreateButtonClicked(EventHandler<ActionEvent> event) {
    availableSessionsButton.setOnAction(event);
  }

  /**
   * Fill table with saved games.
   */
  public void getSavedSessions() {
    SaveGame[] savedGames = LsGameServicesController.getAllSaveGames(
            LsGameServicesController.getGameService("splendor"),
            UserData.getToken());

    listOfSessions.clear();
    // Add each saved game as a sessionModel with no creator name and false as launched
    for (SaveGame savedGame : savedGames) {
      listOfSessions.add(
          new SavedGameModel(
              savedGame.getId(), savedGame,
              LobbyPage.playersArrayToString(savedGame.getPlayerNames()), false
          )
      );
    }
  }
}
