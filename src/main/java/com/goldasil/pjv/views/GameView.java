package com.goldasil.pjv.views;

import com.goldasil.pjv.Main;
import com.goldasil.pjv.controllers.gameControllers.GameController;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.models.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.text.*;


import java.lang.reflect.Array;
import java.net.URL;
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

    private static BorderPane pane;
    private int numDrawnCards = 0;
    private Suit requestedSuit = Suit.UNSPECIFIED;

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
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("game.fxml"));
        VBox vbox = loader.<VBox>load();

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();*/

        /*Image image = new Image(getClass().getResourceAsStream("/card.jpg"));
        Label wasteContent = new Label("Search");
        wasteContent.setGraphic(new ImageView(image));*/

        logger.debug("Block start() entered.");
        layout = new GameLayout(gameView);

        this.newUpdate.set(false);
        newUpdateProperty().addListener(displayUpdatedGUIHandler);

        game.winnerIDProperty().addListener(winnerHandler);

        pane = new BorderPane();
        pane.setPadding(new Insets(30, 30, 30, 30) );

        //set background
        String path = "/cards/table5.jpg";
        Image backgroundImage = new Image(GameLayout.class.getResourceAsStream(path));
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        pane.setBackground(new Background(background));

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

        Scene scene = new Scene(pane, 1400, 900);
        stage.setScene(scene);
        stage.show();

        while (game.getCurrentMoveDTO() == null) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.debug("initializePlayersSection: players: {}, this player id: {}", game.getPlayers().toString(), game.getThisPlayerId());
        layout.initializePlayersBox(game.getPlayers(), game.getThisPlayerId());
        updateGameScene();

        /*new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGameScene();
            }
        }.start();*/
        /*new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Platform.runLater(()->{
                    setNewUpdate();
                });
            }
        }, 2000, 2000);*/

        logger.debug("Showing scene.");
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

    public void updatePlayersBoxFromView(){
        logger.debug("Updating PlayersBox with data: {}, {}", game.getPlayerByID(game.getCurrentPlayerIdTurn()).toString(), game.getCurrentMoveDTO());
        layout.updatePlayersBox(game.getPlayerByID(game.getCurrentPlayerIdTurn()), game.getCurrentMoveDTO());
    }

    public void wonProcess(){

        String winnerAnnouncement;
        if (game.getThisPlayerId() == game.getThisPlayerId()) {
            winnerAnnouncement = "You have won. Congratulations!\nHow do you wish to proceed?";
        } else {
            winnerAnnouncement = "Player with id " + game.getThisPlayerId() + "has won.\nHow do you wish to proceed?";
        }

        ButtonType optionNewGame = new ButtonType("New Game");
        ButtonType optionGoToMenu = new ButtonType("Return to the menu");

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
            game.initGame(game.getPlayers().size()-1);
            setRequestedSuit(game.getCurrentMoveDTO().getRequestedSuit());
        }
        else if (result.get() == optionGoToMenu) {
            //
        }

    }

    EventHandler<ActionEvent> pauseGameDialogBoxHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ButtonType continueGame = new ButtonType("Continue");
            ButtonType saveAndContinue = new ButtonType("Save game and continue playing");
            ButtonType saveAndLeave = new ButtonType("Save game and leave the game");

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("The game has been paused.");
            alert.setHeaderText("How do you wish to proceed?");

            alert.getButtonTypes().setAll(continueGame, saveAndContinue, saveAndLeave);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == continueGame) {
                logger.debug("continueGame was chosen");
            }
            else if (result.get() == saveAndContinue) {
                logger.debug("saveAndContinue was chosen");
                runSaveGameInputDialog();
            }
            else {
                logger.debug("saveAndLeave was chosen");
                runSaveGameInputDialog();
                
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
            game.saveGame(usernamePassword.getKey(), usernamePassword.getValue());
        });
    }

    /**
     * Updates the scene to the newly set scene (a result of a change in the game model).
     */
    public void updateGameScene(/*GameState gameState, List<Player> players*/) {
        GameState gameState = game.getCurrentState();
        List<Player> players = game.getPlayers();

        logger.debug("View-Update method called => Updating scene with new gameState: {}.\n", gameState.toString());

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
        int penalty = game.getCurrentMoveDTO().getPenaltyForSevens();
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
