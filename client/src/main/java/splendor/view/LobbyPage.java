package splendor.view;

import ca.mcgill.nalmer.lsutilities.controller.LsSessionController;
import ca.mcgill.nalmer.lsutilities.model.Session;
import ca.mcgill.nalmer.splendormodels.GameExtension;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import splendor.controller.ClientServerController;
import splendor.model.ClientConstants;
import splendor.model.UserData;




/**
 * Represents the lobby page.
 */
public final class LobbyPage extends GridPane {
  private static final Button adminZone = new Button("Admin Zone");

  static {
    adminZone.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20;");
  }

  private final Button backButton = new Button("Log Out");
  private final Button createButton = new Button("Create");
  private final Button savedSessions = new Button("See Saved Sessions");
  private Map<String, Session> allSessions;


  /**
   * Constructor.
   *
   * @param bp            Border pane to use.
   * @param gameBoardPage Lobby page to connect to after login.
   */
  public LobbyPage(BorderPane bp, GameBoardPage gameBoardPage) {
    setMinHeight(ClientConstants.MIN_HEIGHT);
    setMinWidth(ClientConstants.MIN_WIDTH);

    final HBox containerHbox = new HBox(25);

    // The back button is not centered
    backButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);

    final VBox containerVbox = new VBox(8); // Spacing = 8

    // Lobby Label
    Label title = new Label("Lobby");
    title.setAlignment(Pos.CENTER);
    title.setTextAlignment(TextAlignment.CENTER);
    Label name = new Label("Welcome");
    name.setAlignment(Pos.BOTTOM_RIGHT);
    name.setTextAlignment(TextAlignment.CENTER);
    name.setStyle("-fx-text-fill: white; -fx-font: normal bold 25px 'serif'");

    // Join Game VBox
    final VBox joinGameVbox = new VBox(10); // spacing = 8
    final Label subtitle = new Label("Create New Game Session");
    final HBox innerJoinGameHbox = new HBox();
    createButton.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);
    savedSessions.setStyle(ClientConstants.STANDARD_BUTTON_STYLE);
    adminZone.setStyle(ClientConstants.RED_BUTTON_STYLE);

    innerJoinGameHbox.setSpacing(400);
    innerJoinGameHbox.getChildren().addAll(createButton, savedSessions);

    joinGameVbox.getChildren().addAll(subtitle, name, innerJoinGameHbox);

    // Table of Games Vbox
    final VBox tableOfGamesVbox = new VBox(30); // Spacing = 8
    final Label tableOfGamesTitle = new Label("Join Existing Session");
    final TableView<SessionModel> tableOfGames = new TableView<>();

    TableColumn<SessionModel, SimpleStringProperty> column1 = new TableColumn<>("Session ID");
    column1.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_SMALL);

    TableColumn<SessionModel, SimpleStringProperty> column2 = new TableColumn<>("Creator");
    column2.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_SMALL);

    TableColumn<SessionModel, SimpleStringProperty> column3 = new TableColumn<>("Players");
    column3.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_BIG);

    TableColumn<SessionModel, SimpleStringProperty> column4 = new TableColumn<>("Max Player Count");
    column4.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_EXTRA_SMALL);


    final ObservableList<SessionModel> listOfSessions = FXCollections.observableArrayList();

    allSessions = LsSessionController.getAllSessions();
    allSessions.forEach((k, v) ->
        listOfSessions.add(
            new SessionModel(k, v.getCreator(),
                playersArrayToString(v.getPlayers()), v.isLaunched(),
                    (!v.getSaveGameId().equalsIgnoreCase("")), ((v.getSaveGameId().equals("") ? 4
                    : LsSessionController.getSession(v.getSaveGameId()).getPlayers().length)))
        )
    );

    column1.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
    column2.setCellValueFactory(new PropertyValueFactory<>("creatorName"));
    column3.setCellValueFactory(new PropertyValueFactory<>("players"));
    column4.setCellValueFactory(new PropertyValueFactory<>("playerCount"));


    TableColumn<SessionModel, Void> buttonColumn = new TableColumn<>("Join");
    TableColumn<SessionModel, Void> launchColumn = new TableColumn<>("Launch");
    buttonColumn.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_EXTRA_SMALL);
    launchColumn.setMinWidth(ClientConstants.LOBBY_PAGE_TABLE_CELL_EXTRA_SMALL);

    Callback<TableColumn<SessionModel, Void>, TableCell<SessionModel, Void>> cellFactory =
        new Callback<>() {
          public TableCell<SessionModel,
              Void> call(final TableColumn<SessionModel, Void> param) {
            return new TableCell<>() {
              private final Button joinButton = new Button();

              {
                joinButton.setOnAction((ActionEvent event) -> {
                  SessionModel sessionModel = getTableView().getItems().get(getIndex());
                  if (!sessionModel.getIsLaunched()) {
                    if (sessionModel.getCreatorName().equals(UserData.getUsername())) {
                      LsSessionController.removeSession(
                          (sessionModel.getSessionId()).get(),
                          UserData.getToken()
                      );
                    } else if (
                        Arrays.asList(sessionModel.splitPlayerNames()).contains(
                            UserData.getUsername()
                        )
                    ) {
                      LsSessionController.removePlayerFromSession(
                          sessionModel.getSessionId().get(),
                          UserData.getUsername(),
                          UserData.getToken()
                      );
                      joinButton.setText("Join");
                      joinButton.setStyle("-fx-background-color: green;");
                    } else {
                      LsSessionController.addPlayerToSession(
                          (sessionModel.getSessionId()).get(),
                          UserData.getUsername(),
                          UserData.getToken()
                      );
                      joinButton.setText("Leave");
                      joinButton.setStyle("-fx-background-color: orange;");
                    }
                  }
                });
              }

              @Override
              public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  SessionModel sessionModel = getTableView().getItems().get(getIndex());
                  if (!sessionModel.getIsLaunched()) {
                    if (sessionModel.getCreatorName().equals(UserData.getUsername())) {
                      joinButton.setText("Delete");
                      joinButton.setStyle("-fx-background-color: red;");
                    } else if (Arrays.asList(sessionModel.splitPlayerNames())
                        .contains(UserData.getUsername())) {
                      joinButton.setText("Leave");
                      joinButton.setStyle("-fx-background-color: orange;");
                    } else {
                      joinButton.setText("Join");
                      joinButton.setStyle("");
                      joinButton.setDisable(false);
                      joinButton.setStyle("-fx-background-color: green;");
                    }
                    setGraphic(joinButton);
                  }
                }
              }
            };
          }
        };

    Callback<TableColumn<SessionModel, Void>, TableCell<SessionModel, Void>> launchCellFactory =
        new Callback<>() {
          public TableCell<SessionModel, Void> call(final TableColumn<SessionModel, Void> param) {
            return new TableCell<>() {
              private final Button launchButton = new Button();

              {
                launchButton.setOnAction((ActionEvent event) -> {
                  SessionModel sessionModel = getTableView().getItems().get(getIndex());
                  if (
                      sessionModel.getIsLaunched()
                          && sessionModel.getPlayers().contains(UserData.getUsername())
                  ) {
                    gameBoardPage.setGame(sessionModel.getSessionId().get());
                    bp.setCenter(gameBoardPage);
                  } else if (
                      ((sessionModel.isSaveGame()
                              && (sessionModel.splitPlayerNames().length
                              == sessionModel.getPlayerCount()))
                              || (sessionModel.splitPlayerNames().length > 1)
                          && sessionModel.getCreatorName().equals(UserData.getUsername())
                      )) {
                    LsSessionController.launchSession(
                            sessionModel.getSessionId().get(),
                            UserData.getToken()
                    );
                    if (!sessionModel.isSaveGame()) {
                      PopUpWindow popup = new PopUpWindow("",
                              sessionModel.getSessionId().get());
                      popup.start(new Stage());
                      sessionModel.setIsLaunched(true);
                      launchButton.setText("Play");
                      launchButton.setStyle("-fx-background-color: magenta;");
                    } else {
                      // if not enough players have joined
                      if (sessionModel.splitPlayerNames().length < sessionModel.getPlayerCount()) {
                        PopUpError p = new PopUpError("You have not joined enough users to "
                                + "launch this session");
                        p.start(new Stage());
                      } else {
                        ClientServerController.createNewGame(
                                sessionModel.getSessionId().get(), GameExtension.BASE_ORIENT);
                        sessionModel.setIsLaunched(true);
                        launchButton.setText("Play");
                        launchButton.setStyle("-fx-background-color: magenta;");
                      }
                    }
                  }
                });
              }

              @Override
              public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  SessionModel sessionModel = getTableView().getItems().get(getIndex());
                  if (
                      sessionModel.getIsLaunched()
                          && sessionModel.getPlayers().contains(UserData.getUsername())
                  ) {
                    launchButton.setText("Play");
                    launchButton.setStyle("");
                    launchButton.setDisable(false);
                    launchButton.setStyle("-fx-background-color: magenta;");
                    setGraphic(launchButton);
                  } else if (
                      ((sessionModel.isSaveGame()
                                  && (sessionModel.splitPlayerNames().length
                                  == sessionModel.getPlayerCount())
                                  && sessionModel.getCreatorName().equals(UserData.getUsername()))
                                  || ((sessionModel.splitPlayerNames().length > 1)
                                  && !sessionModel.isSaveGame())
                                  && sessionModel.getCreatorName().equals(UserData.getUsername())
                          )) {
                    launchButton.setText("Launch");
                    launchButton.setStyle("");
                    launchButton.setDisable(false);
                    launchButton.setStyle("-fx-background-color: gold;");
                    setGraphic(launchButton);
                  }
                }
              }
            };
          }
        };

    buttonColumn.setCellFactory(cellFactory);
    launchColumn.setCellFactory(launchCellFactory);

    tableOfGames.getColumns().add(buttonColumn);
    tableOfGames.getColumns().add(launchColumn);
    tableOfGames.setItems(listOfSessions);
    tableOfGames.getColumns().addAll(column1, column2, column3, column4);

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    executor.scheduleWithFixedDelay(() -> {
      allSessions = LsSessionController.fetchAllSessionsAsync(allSessions);
      listOfSessions.clear();
      allSessions.forEach((k, v) -> {
        if (v.getSaveGameId().equals("")
                && ((!v.isLaunched() && ((v.getPlayers().length < ClientConstants.MAX_PLAYERS_AMT)))
                || (playersArrayToString(v.getPlayers())).contains(UserData.getUsername()))) {
          listOfSessions.add(new SessionModel(k, v.getCreator(),
                  playersArrayToString(v.getPlayers()), v.isLaunched(),
                  false, 4)
          );
        } else if ((!v.isLaunched() && ((v.getPlayers().length < ClientConstants.MAX_PLAYERS_AMT)))
                || (playersArrayToString(v.getPlayers())).contains(UserData.getUsername())) {
          listOfSessions.add(new SessionModel(k, v.getCreator(),
                  playersArrayToString(v.getPlayers()), v.isLaunched(),
                  true,
                  LsSessionController.getSession(v.getSaveGameId()).getPlayers().length)
          );
        }
      });

      Platform.runLater(() -> {
        tableOfGames.setItems(listOfSessions);
        tableOfGames.refresh();
      });
    }, 0, 1, TimeUnit.SECONDS); // Update as soon as possible

    listOfSessions.clear();
    tableOfGames.setItems(listOfSessions);

    tableOfGamesVbox.getChildren().addAll(tableOfGamesTitle, tableOfGames);

    HBox header = new HBox();
    header.setSpacing(500);
    header.setPadding(new Insets(10));
    header.getChildren().addAll(title, adminZone);

    // Add separate components to container
    containerVbox.getChildren().addAll(header, joinGameVbox, tableOfGamesVbox);
    containerHbox.getChildren().addAll(backButton, containerVbox);

    setAlignment(Pos.CENTER);
    add(containerHbox, 1, 0);

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

    // Subtitle styling
    subtitle.setStyle("-fx-text-fill: white; -fx-font: normal bold 25px 'serif'");

    // Set the column header style using CSS
    column1.setStyle("-fx-text-fill: white;");
    column2.setStyle("-fx-text-fill: white;");
    column3.setStyle("-fx-text-fill: white;");
    column4.setStyle("-fx-text-fill: white;");
    buttonColumn.setStyle("-fx-text-fill: white;");
    launchColumn.setStyle("-fx-text-fill: white;");

    tableOfGamesTitle.setStyle("-fx-control-inner-background: #333333; "
        + "-fx-text-background-color: white;");

    // Set the row style using CSS
    tableOfGames.setRowFactory(tv -> {
      TableRow<SessionModel> row = new TableRow<>();
      row.setStyle("-fx-background-color: #444444;");
      return row;
    });
    setWhenCreateButtonClicked();
  }

  /**
   * Gets admin zone {@link Button}.
   *
   * @return Admin zone {@link Button}.
   */
  public static Button getAdminZone() {
    return adminZone;
  }

  /**
   * Returns a single string of the players' names seperated by commas.
   *
   * @param playerNames String list of player names.
   */
  static String playersArrayToString(String[] playerNames) {
    StringBuilder stb = new StringBuilder();
    for (int i = 0; (i < playerNames.length); i++) {
      stb.append(playerNames[i]);
      if (i < playerNames.length - 1) {
        stb.append(", ");
      }
    }
    return stb.toString();
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
   * Set action when the admin zone button is clicked.
   *
   * @param event Event to trigger.
   */
  public void setWhenAdminButtonClicked(EventHandler<ActionEvent> event) {
    adminZone.setOnAction(event);
  }

  /**
   * Set action when the saved sessions button is clicked.
   *
   * @param event Event to trigger.
   */
  public void setWhenSavedSessionsClicked(EventHandler<ActionEvent> event) {
    savedSessions.setOnAction(event);
  }


  /**
   * Set action when the create button is clicked.
   */
  public void setWhenCreateButtonClicked() {
    createButton.setOnAction(actionEvent -> LsSessionController.addSession(
        UserData.getUsername(), "splendor", UserData.getToken()));
  }
}
