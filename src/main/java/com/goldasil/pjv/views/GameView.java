package com.goldasil.pjv.views;

import com.goldasil.pjv.controllers.gameControllers.GameController;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.models.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
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
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.Label;
import javafx.scene.text.*;


import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * Manages the game view of the cards of the players, stock, waste and other entities.
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


    private static volatile GridPane grid = new GridPane();
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

        pane = new BorderPane();
        pane.setPadding(new Insets(30, 30, 30, 30) );
        //layout.getMoveControlBox(), layout.getStockBox(), layout.getThisPlayerCards(), layout.getSuitsBox(), layout.getWasteBox()

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

        Scene scene = new Scene(pane, 1200, 700);
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

        logger.debug("Showing scene.");

    }

    private void setThisPlayerCardsBox(Player player) {
        int playerID = player.getPlayerID();
        for (Card card : player.getCards()) {
            ButtonCard buttonCard = new ButtonCard(card);
            layout.getThisPlayerCards().getChildren().add(buttonCard);
            //logger.debug("card suit: {} vs suit {}", buttonCard.getButtonSuit(), card.getSuit());
            if (playerID == game.getThisPlayerId()) {
                buttonCard.setOnAction(selectCardToPlayHandler);
            }
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

            layout.getSelectedCardsBox().getChildren().add(butSelectedCard);
            butSelectedCard.setOnAction(unSelectCardHandler);

            event.consume();
            logger.debug("Selected a card to play.");
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

            event.consume();
            logger.debug("Unselected a card.");
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

    public void updatePlayersBoxFromView(){
        logger.debug("Updating PlayersBox with data: {}, {}", game.getPlayerByID(game.getCurrentPlayerIdTurn()).toString(), game.getCurrentMoveDTO());
        layout.updatePlayersBox(game.getPlayerByID(game.getCurrentPlayerIdTurn()), game.getCurrentMoveDTO());
    }

    /**
     * Updates the scene to the newly set scene (a result of a change in the game model).
     */
    public void updateGameScene(/*GameState gameState, List<Player> players*/) {
        GameState gameState = game.getCurrentState();
        List<Player> players = game.getPlayers();

        logger.debug("View-Update method called => Updating scene with new gameState: {}.\n\n\n", gameState.toString());

        if (layout.getPlayersBox().getChildren().size() == 0) {
            //logger.debug("initializePlayersSection: players: {}, this player id: {}", players.toString(), game.getThisPlayerId());
            //layout.initializePlayersBox(players, game.getThisPlayerId());
        }

        // cards to draw button
        layout.getDrawnCardsButton().setVisible(false);
        numDrawnCards = 0;

        // set selected cards box
        layout.getSelectedCardsBox().getChildren().clear();

        // set this player's cards
        layout.getThisPlayerCards().getChildren().clear();
        setThisPlayerCardsBox(game.getPlayerByID(game.getThisPlayerId()));

        // set waste box
        layout.setWasteBox(game.getWaste());

        switch (gameState) {
            case MY_TURN:
                break;
            case OPP_TURN:
                break;
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
