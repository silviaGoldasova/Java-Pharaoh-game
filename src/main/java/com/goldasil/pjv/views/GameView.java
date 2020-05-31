package com.goldasil.pjv.views;

import com.goldasil.pjv.ApplicationContextProvider;
import com.goldasil.pjv.controllers.gameControllers.GameController;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.dto.NewGameDTO;
import com.goldasil.pjv.entity.GameEntity;
import com.goldasil.pjv.entity.GameService;
import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.enums.GameType;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.models.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * Manages the game view of the cards of the players, stock, waste and other entity.
 */
public class GameView extends Application {

    public static final CountDownLatch latch = new CountDownLatch(1);
    public static GameView gameView = null;
    private GameModel game;
    private GameController gameController;
    //public static volatile boolean needUpdate = false;
    private static final Logger logger = LoggerFactory.getLogger(GameView.class);
    private GameLayout layout;
    private BooleanProperty changedSuit = new SimpleBooleanProperty();
    private BooleanProperty newUpdate = new SimpleBooleanProperty();

    private static BorderPane pane = null;
    private int numDrawnCards = 0;
    private Suit requestedSuit = Suit.UNSPECIFIED;
    private VBox optionsBox;
    private NewGameDTO newGame = new NewGameDTO();
    private Stage stage;
    private Scene playingBoardScene, menuScene;


    public GameView() {
        setGameView(this);
    }

    public static GameView getGameViewAppInstance() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return gameView;
    }

    public static void setGameView(GameView gameViewInst) {
        gameView = gameViewInst;
        latch.countDown();
    }

    /**
     * Generates a new FXML application
     * @param stage Stage stage
     * @throws Exception e
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setChooseGameTypeScene();
    }

    private void setChooseGameTypeScene(){

        BorderPane menuPane = new BorderPane();
        setBackground(menuPane);

        //HBox labelBox = new HBox();
        GameLabel label = new GameLabel("Set Up a Game");
        label.setStyle("-fx-background-color: #faf0e6; -fx-border-width: 0px; -fx-background-radius: 10; margin: 100px;");
        label.setMinWidth(180);
        //labelBox.getChildren().add(label);

        optionsBox = new VBox();
        optionsBox.setSpacing(30);
        optionsBox.setAlignment(Pos.CENTER);
        ControlButton butVsComputer = new ControlButton("vs Computer");
        ControlButton butVsPlayer = new ControlButton("vs Player");
        ControlButton loadGame = new ControlButton("Load a game");
        butVsComputer.setOnAction(chooseOptionGameType);
        butVsPlayer.setOnAction(chooseOptionGameType);
        loadGame.setOnAction(chooseOptionGameType);
        optionsBox.getChildren().addAll(butVsComputer, butVsPlayer, loadGame);

        VBox container = new VBox();
        container.setSpacing(100);
        menuPane.setCenter(container);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(label, optionsBox);

        Scene scene = new Scene(menuPane, 1400, 900);
        stage.setScene(scene);
        stage.show();
    }

    EventHandler<ActionEvent> chooseOptionGameType = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ControlButton butSelected = (ControlButton) event.getSource();
            String selectedText = butSelected.getText();
            optionsBox.getChildren().clear();

            if (selectedText.equals("vs Computer")) {
                newGame.setGametype(GameType.VS_COMPUTER);

                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
                hbox.setSpacing(10);
                GameLabel chooseNumOppLabel = new GameLabel("Choose the number of opponents:");

                ComboBox numOfOppBox = new ComboBox();
                numOfOppBox.setPrefHeight(40);
                numOfOppBox.getItems().addAll("1", "2", "3");
                numOfOppBox.setValue("1");
                newGame.setNumOfOpp(1);
                numOfOppBox.valueProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue ov, String oldValue, String newValue) {
                        newGame.setNumOfOpp(Integer.parseInt(newValue));;
                        logger.info("setNumOfOpp: {}", newValue);
                    }
                });
                hbox.getChildren().addAll(chooseNumOppLabel, numOfOppBox);

                ControlButton submit = new ControlButton("Start Game");
                submit.setOnAction(startGameButtonHandler);
                optionsBox.getChildren().addAll(hbox, submit);
            } else if (selectedText.equals("vs Player")) {
                newGame.setGametype(GameType.VS_PLAYER);
            } else {    // load a new game
                logger.info("loading a new game");
                newGame.setGametype(GameType.LOAD_GAME);
                GameService service  = ApplicationContextProvider.getBean(GameService.class);
                List<GameEntity> savedGames = service.getSavedGames();

                VBox savedGamesBox = new VBox();
                optionsBox.getChildren().add(savedGamesBox);
                savedGamesBox.setAlignment(Pos.CENTER);
                savedGamesBox.setSpacing(20);
                for (GameEntity savedGame : savedGames){
                    ControlButton butOption = new ControlButton(savedGame.getMainPlayerName() + ": " + getDateInProperFormat(savedGame.getPlayed_at()));
                    butOption.setSavedGameInfo(savedGame);
                    butOption.setOnAction(loadGameButtonHandler);
                    savedGamesBox.getChildren().add(butOption);
                }

            }

            event.consume();
            //logger.debug("Selected a card to play.");
        }
    };

    private String getDateInProperFormat(Date date){
        SimpleDateFormat dateFormatDate = new SimpleDateFormat("E, dd.MM.yyyy");
        String dateToStr = dateFormatDate.format(date);
        dateToStr = dateToStr + " at ";

        SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
        dateToStr = dateToStr + dateFormatTime.format(date);

        return dateToStr;
    }

    EventHandler<ActionEvent> loadGameButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            logger.info("loading a game");

            ControlButton button = (ControlButton) event.getSource();
            GameEntity savedGame = button.getSavedGameInfo();

            // get players' info from String to List<Player>
            Gson gson = new Gson();
            Type playerType = new TypeToken<ArrayList<Player>>(){}.getType();
            List<Player> players = gson.fromJson(savedGame.getPlayersInfo(), playerType);

            Type cardListType = new TypeToken<LinkedList<Card>>(){}.getType();
            LinkedList<Card> stock = gson.fromJson(savedGame.getStock(), cardListType);
            LinkedList<Card> waste = gson.fromJson(savedGame.getWaste(), cardListType);

            Type cardType = new TypeToken<Card>(){}.getType();
            Card upcard = gson.fromJson(savedGame.getUpcard(), cardType);

            Type moveDTOType = new TypeToken<MoveDTO>(){}.getType();
            MoveDTO lastMove = gson.fromJson(savedGame.getLastMoveDTO(), moveDTOType);

            gameController.initializeGame(players, stock, waste, upcard, lastMove, savedGame.getCurrentPlayerToPlay());
            requestedSuit = lastMove.getRequestedSuit();

            setPlayingBoardGui();

            event.consume();
            //logger.debug("Selected a card to play.");
        }
    };

    private void setPlayingBoardGui() {
        if (pane == null) {
            logger.debug("pane is null");
            setGameScene();

            playingBoardScene = new Scene(pane, 1400, 900);
            stage.setScene(playingBoardScene);
            stage.show();
        } else {
            stage.setScene(playingBoardScene);
            stage.show();
            setPenaltyAndTurnLabel();
        }

        while (game.getCurrentMoveDTO() == null) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.debug("Waiting for game.getCurrentMoveDTO() to be set.");
        }

        logger.debug("initializePlayersSection: players: {}, this player id: {}", game.getPlayers().toString(), game.getThisPlayerId());
        layout.initializePlayersBox(game.getPlayers(), game.getThisPlayerId());
        updatePlayersBoxFromView();

        updateGameScene();
        logger.debug("Showing scene.");

    }

    EventHandler<ActionEvent> startGameButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            logger.info("number of  opponents: {}", newGame.getNumOfOpp());
            gameController.initializeGame(newGame.getNumOfOpp());

            setPlayingBoardGui();

            event.consume();
        }
    };

    private void setBackground(BorderPane pane){
        String path = "/cards/table5.jpg";
        Image backgroundImage = new Image(GameLayout.class.getResourceAsStream(path));
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        pane.setBackground(new Background(background));
    }

    private void setGameScene(){
        logger.debug("Block start() entered.");
        layout = new GameLayout(gameView);

        this.newUpdate.set(false);
        newUpdateProperty().addListener(displayUpdatedGUIHandler);

        game.winnerIDProperty().addListener(winnerHandler);

        pane = new BorderPane();
        pane.setPadding(new Insets(30, 30, 30, 30) );

        //set background
        setBackground(pane);

        pane.setTop(layout.getPlayersBox());
        layout.getPlayersBox().setAlignment(Pos.CENTER);

        pane.setRight(layout.getMoveControlBox());
        layout.getMoveControlBox().setAlignment(Pos.CENTER);

        pane.setBottom(layout.getThisPlayerBox());
        layout.getThisPlayerBox().setAlignment(Pos.CENTER);

        pane.setLeft(layout.getStockBox());
        layout.getStockBox().setAlignment(Pos.CENTER);

        pane.setCenter(layout.getWasteBox());
        layout.getWasteBox().setAlignment(Pos.CENTER);

    }

    private void setThisPlayerCardsBox(Player player) {
        for (Card card : player.getCards()) {
            ButtonCard buttonCard = new ButtonCard(card.getRank(), card.getSuit());
            buttonCard.setGraphic(layout.getCardImageView(card.getRank(), card.getSuit()));
            layout.getThisPlayerCards().getChildren().add(buttonCard);
            //logger.debug("card suit: {} vs suit {}", buttonCard.getButtonSuit(), card.getSuit());
            buttonCard.setOnAction(selectCardToPlayHandler);
        }
    }

    private void colorButton(Button button){
        button.setStyle("-fx-background-color: #d7efef; -fx-border-width: 1px; -fx-border-color: #acbfbf;");
    }

    EventHandler<ActionEvent> selectCardToPlayHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ButtonCard butSelectedCard = (ButtonCard) event.getSource();
            //logger.debug("button: {}, rank: {}, suit: {}", butSelectedCard, butSelectedCard.getButtonRank(), butSelectedCard.getButtonSuit());

            //colorButton(butSelectedCard);

            layout.getSelectedCardsLabel().setVisible(true);

            layout.getSelectedCardsBox().getChildren().add(butSelectedCard);
            butSelectedCard.setOnAction(unSelectCardHandler);

            event.consume();
            //logger.debug("Selected a card to play.");
        }
    };

    EventHandler<ActionEvent> unSelectCardHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ButtonCard butSelectedCard = (ButtonCard) event.getSource();
            //logger.debug("button: {}, rank: {}, suit: {}", butSelectedCard, butSelectedCard.getButtonRank(), butSelectedCard.getButtonSuit());

            layout.getThisPlayerCards().getChildren().add(butSelectedCard);
            butSelectedCard.setOnAction(selectCardToPlayHandler);
            //butSelectedCard.setStyle("");

            if (layout.getSelectedCardsBox().getChildren().size() == 0) {
                layout.getSelectedCardsLabel().setVisible(false);
            }

            event.consume();
            //logger.debug("Unselected a card.");
        }
    };

    EventHandler<ActionEvent> submitSelectCardHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            if (numDrawnCards != 0 && layout.getSelectedCardsBox().getChildren().size() != 0) {
                // error
                return;
            }

            if (numDrawnCards != 0) {
                gameController.submitMoveFromView(numDrawnCards);
            }

            if (requestedSuit == Suit.UNSPECIFIED && ButtonCard.isOverKnaveSubmitted(layout.getSelectedCardsBox().getChildren())) {
                return;
            }

            if (layout.getSelectedCardsBox().getChildren().size() != 0) {
                gameController.submitMoveFromView(layout.getSelectedCardsBox().getChildren(), requestedSuit);
            }

            /*List<ButtonCard> selectedCards = new ArrayList<>();
            for (Node card : p1CardsBox.getChildren()) {
                if ( ((ButtonCard)card).isSelected() ) {
                    selectedCards.add((ButtonCard)card);
                }
            }
            if (selectedCards.size() != 0) {
                gameController.submitMoveFromView(selectedCards, requestedSuit);
            }*/

            event.consume();
            //logger.debug("Move submitted.");
        }
    };

    EventHandler<ActionEvent> winGameButtonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (numDrawnCards != 0) {
                // error
                return;
            }
            gameController.submitMoveFromView(new MoveDTO(new Move(MoveType.WIN)));
        }
    };

    ChangeListener displayUpdatedGUIHandler = new ChangeListener() {
        @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
            logger.debug("changedUpdateGUI has been changed!");
            updateGameScene();
        }
    };

    ChangeListener winnerHandler = new ChangeListener() {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object t1) {
            if (game.getWinnerID() != -1) {
                wonProcess();
            }
        }
    };

    public void updatePlayersBoxFromView() {
        logger.debug("Updating PlayersBox with data: {}, {}", game.getPlayerByID(game.getCurrentPlayerIdTurn()).toString(), game.getCurrentMoveDTO());
        layout.updatePlayersBox(game.getPlayerByID(game.getCurrentPlayerIdTurn()), game.getCurrentMoveDTO());
    }

    public void wonProcess(){

        String winnerAnnouncement;
        if (game.getWinnerID() == game.getThisPlayerId()) {
            winnerAnnouncement = "You have won. Congratulations!\nHow do you wish to proceed?";
        } else {
            winnerAnnouncement = "Player with ID " + game.getWinnerID() + " has won.\nHow do you wish to proceed?";
        }

        ButtonType optionNewGame = new ButtonType("New Game (vs Computer)");
        ButtonType optionGoToMenu = new ButtonType("Return to the menu");

        Platform.runLater( ()-> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("The end of the game.");
            alert.setHeaderText(winnerAnnouncement);
            alert.setContentText("Choose your option.");

            alert.getButtonTypes().setAll(optionNewGame, optionGoToMenu);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == optionNewGame) {
                //Platform.exit();
                //Main.startUpNewGameProcess(game.getPlayers().size()-1);
                logger.debug("New Game to play chosen");
                gameController.initializeGame(game.getPlayers().size()-1);
                setRequestedSuit(game.getCurrentMoveDTO().getRequestedSuit());
            }
            else if (result.get() == optionGoToMenu) {
                setChooseGameTypeScene();
            }
        });

    }

    EventHandler<ActionEvent> pauseGameDialogBoxHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ButtonType continueGame = new ButtonType("Continue");
            ButtonType saveAndContinue = new ButtonType("Save game and continue playing");
            ButtonType saveAndLeave = new ButtonType("Save game and leave the game");
            ButtonType leaveGame = new ButtonType("Leave the game");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("The game has been paused.");
            alert.setHeaderText("How do you wish to proceed?");

            alert.getButtonTypes().setAll(continueGame, saveAndContinue, saveAndLeave, leaveGame);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == continueGame) {
                logger.debug("continueGame was chosen");
            }
            else if (result.get() == saveAndContinue) {
                logger.debug("saveAndContinue was chosen");
                runSaveGameInputDialog();
            }
            else if(result.get() == saveAndLeave) {
                logger.debug("saveAndLeave was chosen");
                runSaveGameInputDialog();
                setChooseGameTypeScene();
            } else {    // leave the game
                setChooseGameTypeScene();
            }
        }
    };

    private void runSaveGameInputDialog() {
        // Create the custom dialog.
        Dialog <Pair <String, String> > dialog = new Dialog<>();
        dialog.setTitle("Save Game Dialog");
        dialog.setHeaderText("Please, input the requested information about the game.");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Name of the main player");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Name of the main player:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node saveButtonNode = dialog.getDialogPane().lookupButton(saveButton);
        saveButtonNode.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButtonNode.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> name.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                return new Pair<>(name.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            game.saveGame(usernamePassword.getKey(), usernamePassword.getValue(), requestedSuit);
        });
    }

    /**
     * Updates the scene to the newly set scene (a result of a change in the game model).
     */
    public void updateGameScene(/*GameState gameState, List<Player> players*/) {
        GameState gameState = game.getCurrentState();
        List<Player> players = game.getPlayers();

        // cards to draw button
        layout.getDrawnCardsButton().setVisible(false);
        numDrawnCards = 0;

        // set selected cards box
        layout.getSelectedCardsBox().getChildren().clear();
        layout.getSelectedCardsLabel().setVisible(false);

        // set this player's cards
        layout.getThisPlayerCards().getChildren().clear();
        setThisPlayerCardsBox(game.getPlayerByID(game.getThisPlayerId()));

        // set waste box
        layout.setWasteBox(game.getWaste());

        setPenaltyAndTurnLabel();

        switch (gameState) {
            case MY_TURN:
                break;
            case OPP_TURN:
                break;
        }
        logger.debug("end of the update GUI");
    }

    public void setPenaltyAndTurnLabel(){
        // set panalty counter:
        logger.info("game.getCurrentMoveDTO(): {}", game.getCurrentMoveDTO());

        int penalty;
        if (game.getCurrentMoveDTO() == null) {
            penalty = 0;
        } else {
           penalty = game.getCurrentMoveDTO().getPenaltyForSevens();
        }

        layout.setPenaltyText("Set Penalty: " + penalty);
        if (penalty != 0) {
            layout.getSetPenalty().highlightLabel();
        } else {
            layout.getSetPenalty().unHighlightLabel();
        }

        // set turn of the player #
        layout.setTurnLabelText("Turn of the player #" + game.getCurrentPlayerIdTurn());
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            layout.getTurnLabel().highlightLabel();
        } else {
            layout.getTurnLabel().unHighlightLabel();
        }
    }


    // getters and setters

    public GameModel getGame() {
        return game;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public boolean isChangedSuit() {
        return changedSuit.get();
    }

    public BooleanProperty changedSuitProperty() {
        return changedSuit;
    }

    public void setChangedSuit(boolean changedSuit) {
        this.changedSuit.set(changedSuit);
    }

    public Suit getRequestedSuit() {
        return requestedSuit;
    }

    public void setRequestedSuit(Suit requestedSuit) {
        this.requestedSuit = requestedSuit;
        setChangedSuit(!changedSuit.get());
    }

    public int getNumDrawnCards() {
        return numDrawnCards;
    }

    public void setNumDrawnCards(int numDrawnCards) {
        this.numDrawnCards = numDrawnCards;
    }

    public boolean isNewUpdate() {
        return newUpdate.get();
    }

    public BooleanProperty newUpdateProperty() {
        return newUpdate;
    }

    public void setNewUpdate() {
        this.newUpdate.set(!this.newUpdate.get());
    }
}
