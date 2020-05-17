package com.goldasil.pjv.views;

import com.goldasil.pjv.controllers.gameControllers.GameController;
import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.models.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.Label;
import javafx.scene.text.*;


import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

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

    private static volatile GridPane grid = new GridPane();
    private int numDrawnCards = 0;
    HBox selectedCardsBox, p1CardsBox, wasteCards;
    Button drawnCardsButton = new Button();

    public GameView() {
        setGameView(this);
        logger.debug("game view constructor");
        //this.gameController = gameController;
        //isUnprocessedUpdate = true;
    }

    public void printText(String text) {
        System.out.println(text);
        logger.debug("printing");
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

    /*@Override
    public void update(Observable o, Object arg) {
        // take the gameState of Controller
        // ser the view with the info from model
        // => gameScene = gameSate + players' info

        updateGameScene(gameController.getGameState());
    }*/

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

        logger.debug("Block start() entered.");

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(25);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label p1Label = new Label("P1 cards:");
        Label p1SelectedCards = new Label("P1 selected cards:");
        Label p2Label = new Label("P2 cards:");
        Label waste = new Label("Waste:");

        // set stock
        Button stock = new Button("Stock (draw a card)");
        stock.setOnAction(drawCardsHandler);
        drawnCardsButton.setOnAction(drawCardsSubstractHandler);

        Button submit = new Button("Submit Move");
        submit.setOnAction(submitSelectCardHandler);

        // set waste
        /*Image image = new Image(getClass().getResourceAsStream("/card.jpg"));
        Label wasteContent = new Label("Search");
        wasteContent.setGraphic(new ImageView(image));*/
        wasteCards = new HBox();
        grid.add(wasteCards, 2, 6);



        //String waste = game.getWaste();
        //Text text = new Text("Some Text");

        // column, row
        grid.add(p1Label, 0, 0);
        grid.add(p1SelectedCards, 0, 2);
        grid.add(submit, 0, 3);
        grid.add(p2Label, 0, 4);
        grid.add(waste, 0, 6);
        grid.add(stock, 0, 7);

        p1CardsBox = new HBox();
        HBox p2CardsBox = new HBox();
        grid.add(p1CardsBox, 1, 0);
        grid.add(p2CardsBox, 1, 4);

        selectedCardsBox = new HBox();
        selectedCardsBox.setSpacing(10);
        grid.add(selectedCardsBox, 1, 2);

        //p1Cards.setText("text other");

        Scene scene = new Scene(grid, 1200, 700);
        stage.setScene(scene);
        stage.show();
        logger.debug("Showing scene.");

    }

    private HBox getCardsBox(Player player) {
        HBox cardsBox = new HBox();
        cardsBox.setSpacing(10);
        int playerID = player.getPlayerID();
        for (Card card : player.getCards()) {
            ButtonCard buttonCard = new ButtonCard(card);
            cardsBox.getChildren().add(buttonCard);
            //logger.debug("card suit: {} vs suit {}", buttonCard.getButtonSuit(), card.getSuit());
            //selectedCardsBox.getChildren().add(buttonCard);
            if (playerID == 1) {
                buttonCard.setOnAction(selectCardToPlayHandler);
            }
        }
        return cardsBox;

    }


    EventHandler<ActionEvent> selectCardToPlayHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ButtonCard butSelectedCard = (ButtonCard) event.getSource();
            //logger.debug("button: {}, rank: {}, suit: {}", butSelectedCard, butSelectedCard.getButtonRank(), butSelectedCard.getButtonSuit());

            //ButtonCard newSelectedCardBut = new ButtonCard(butSelectedCard);
            selectedCardsBox.getChildren().add(butSelectedCard);
            butSelectedCard.setOnAction(unSelectCardHandler);
            //grid.getChildren().remove(butSelectedCard);

            event.consume();
            logger.debug("Selected a card to play.");
        }
    };

    EventHandler<ActionEvent> unSelectCardHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            ButtonCard butSelectedCard = (ButtonCard) event.getSource();
            //logger.debug("button: {}, rank: {}, suit: {}", butSelectedCard, butSelectedCard.getButtonRank(), butSelectedCard.getButtonSuit());

            p1CardsBox.getChildren().add(butSelectedCard);
            butSelectedCard.setOnAction(selectCardToPlayHandler);

            event.consume();
            logger.debug("Unselected a card.");
        }
    };

    EventHandler<ActionEvent> submitSelectCardHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            if (numDrawnCards != 0 && selectedCardsBox.getChildren().size() != 0) {
                // error
                return;
            }

            if (numDrawnCards != 0) {
                gameController.submitMove(numDrawnCards);
            }

            if (selectedCardsBox.getChildren().size() != 0) {
                gameController.submitMove(selectedCardsBox.getChildren());
            }

            event.consume();
            logger.debug("Move submitted.");
        }
    };

    EventHandler<ActionEvent> drawCardsHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            if (numDrawnCards == 0) {
                numDrawnCards++;
                drawnCardsButton.setText("Num of cards to draw: 1");
                grid.add(drawnCardsButton, 1, 7);
            }
            else {
                numDrawnCards++;
                drawnCardsButton.setText("Num of cards to draw: " + numDrawnCards );
            }

        }
    };

    EventHandler<ActionEvent> drawCardsSubstractHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            numDrawnCards--;
            drawnCardsButton.setText("Num of cards to draw: " + numDrawnCards );

            if (numDrawnCards == 0) {
                grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 7 && GridPane.getColumnIndex(node) == 1);
            }

        }
    };

    /**
     * Updates the scene to the newly set scene (a result of a change in the game model).
     */
    public void updateGameScene(/*GameState gameState, List<Player> players*/) {
        GameState gameState = game.getCurrentState();
        List<Player> players = game.getPlayers();

        logger.debug("View-Update method called => Updating scene with new gameState: {}.", gameState.toString());

        Platform.runLater(() -> {
            logger.debug("platform run later");
            selectedCardsBox.getChildren().clear();
            // cards to draw button
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 7 && GridPane.getColumnIndex(node) == 1);
            numDrawnCards = 0;


            p1CardsBox.getChildren().clear();
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1);
            grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 5);

            p1CardsBox = getCardsBox(players.get(0));
            grid.add(p1CardsBox, 1, 0);
            grid.add(getCardsBox(players.get(1)), 1, 4);

            /*for (Player player : players) {
                HBox cardsBox = new HBox();
                for (Card card : player.getCards()) {
                    Button buttonCard = new Button(card.toString());
                    grid.add(buttonCard);
                }
            }*/

            switch(gameState) {
                case MY_TURN:
                    break;
                case OPP_TURN:
                    break;
            }
        });
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

}
