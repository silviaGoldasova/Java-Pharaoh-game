package com.goldasil.pjv.views;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class GameLayout {

    private GameView view;

    private VBox moveControlBox;
    private HBox suitsBox;
    private HBox wasteBox;
    private VBox stockBox;

    private HBox playersBox;
    private GridPane thisPlayerBox;
    private HBox thisPlayerCardsBox;
    private HBox selectedCardsBox;
    private ControlButton drawnCardsButton;

    private GameLabel selectedCardsLabel;
    private GameLabel setPenalty;
    private GameLabel turnLabel;

    private static int WASTE_DISPLAY_SIZE = 4;

    private static final Logger logger = LoggerFactory.getLogger(GameLayout.class);


    public GameLayout(GameView view) {
        this.view = view;
        setSuitRequestBox();
        setStockBox();
        setMoveControlBox();
        setWasteBox();
        setThisPlayerBox();
        setPlayersBox();
    }

    private void setPlayersBox(){
        playersBox = new HBox();
        playersBox.setSpacing(70);
    }

    public void initializePlayersBox(List<Player> players, int thisPlayerId){
        logger.debug("initialize PlayersBox");
        playersBox.getChildren().clear();

        for (Player player : players) {
            if (player.getPlayerID() == thisPlayerId) {
                continue;
            }
            GameLabel playerLabel = new GameLabel("player #" + player.getPlayerID() + ". Cards count: " + player.getCards().size());
            GameLabel lastMove = new GameLabel("");
            lastMove.setVisible(false);

            GameVBox playerBox = new GameVBox(player.getPlayerID(), playerLabel, lastMove);

            playersBox.getChildren().add(playerBox);
        }
    }

    public void updatePlayersBox(Player player, MoveDTO moveDTO) {
        for (Node node : playersBox.getChildren()){
            GameVBox playerBox = (GameVBox) node;
            if ( playerBox.getPlayerId() == player.getPlayerID()){
                playerBox.setNameCountLabelText(player.getCards().size());
                playerBox.setLastMoveLabelText(moveDTO);
            }
        }
    }

    private void setThisPlayerBox(){
        thisPlayerBox = new GridPane();
        //thisPlayerBox.setPadding(new Insets(25, 25, 25, 25));
        thisPlayerBox.setHgap(25);
        thisPlayerBox.setVgap(10);
        thisPlayerBox.setAlignment(Pos.CENTER);

        selectedCardsLabel = new GameLabel("Selected Cards ");
        selectedCardsLabel.setVisible(false);
        thisPlayerBox.add(selectedCardsLabel, 0, 0);

        selectedCardsBox = new HBox();
        selectedCardsBox.setSpacing(10);
        thisPlayerBox.add(selectedCardsBox, 1, 0);

        GameLabel myCardsLabel = new GameLabel("My Cards: ");
        thisPlayerBox.add(myCardsLabel, 0, 1);

        thisPlayerCardsBox = new HBox();
        thisPlayerCardsBox.setSpacing(10);
        thisPlayerBox.add(thisPlayerCardsBox, 1, 1);

    }

    private void setWasteBox() {
        wasteBox = new HBox();
        wasteBox.setSpacing(10);
    }

    public void setWasteBox(LinkedList<Card> waste) {
        wasteBox.getChildren().clear();
        if (waste.size() <= WASTE_DISPLAY_SIZE) {
          for (Card card : waste) {
              ButtonCard buttonCard = new ButtonCard(card.getRank(), card.getSuit());
              buttonCard.setGraphic(getCardImageView(card.getRank(), card.getSuit()));
              wasteBox.getChildren().add(buttonCard);
          }
        } else {
            int index = waste.size() - WASTE_DISPLAY_SIZE;
            for (int i = index; i < waste.size(); i++) {
                Card card = waste.get(i);
                ButtonCard buttonCard = new ButtonCard(card.getRank(), card.getSuit());
                buttonCard.setGraphic(getCardImageView(card.getRank(), card.getSuit()));
                wasteBox.getChildren().add(buttonCard);
            }
        }
    }

    private void setMoveControlBox() {
        moveControlBox = new VBox();
        moveControlBox.setSpacing(20);

        ControlButton pauseButton = new ControlButton();
        pauseButton.setGraphic(getCardBackgroundImageView("home.png", 40));
        pauseButton.setOnAction(view.pauseGameDialogBoxHandler);

        ControlButton submit = new ControlButton("Submit Move");
        submit.setMinWidth(200);
        submit.setOnAction(view.submitSelectCardHandler);

        ControlButton winGame = new ControlButton("Win Game");
        winGame.setMinWidth(200);
        winGame.setOnAction(view.winGameButtonHandler);

        setPenalty = new GameLabel("Set Penalty: ");
        setPenalty.setMinWidth(200);
        turnLabel = new GameLabel("Turn of the player #");
        turnLabel.setMinWidth(200);

        moveControlBox.getChildren().addAll(pauseButton, submit, winGame, setPenalty, turnLabel, suitsBox);
    }

    private void setStockBox(){
        stockBox = new VBox();
        stockBox.setSpacing(10);

        ControlButton stockButton = new ControlButton("Stock (draw a card)");
        stockButton.setOnAction(drawCardsHandler);

        drawnCardsButton = new ControlButton();
        drawnCardsButton.setOnAction(drawCardsSubstractHandler);
        drawnCardsButton.setVisible(false);

        //Button button = new Button();
        //button.setGraphic(getCardImageView(Rank.KING, Suit.HEARTS));

        stockBox.getChildren().addAll(stockButton, drawnCardsButton);
    }

    public ImageView getCardImageView(Rank rank, Suit suit){

        String path = "/cards/" + rank.toString().toLowerCase() + "_" + suit.toString().toLowerCase() + ".jpg";
        Image image = new Image(GameLayout.class.getResourceAsStream(path));
        //Image image = new Image(this.getClass().getResourceAsStream("/cards/king_hearts.jpg"));
        ImageView imageView = new ImageView(image);
        //imageView.fitHeightProperty().bind(thisPlayerCardsBox.heightProperty());
        imageView.setFitHeight(210);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static ImageView getCardBackgroundImageView(String name, double size){
        String path = "/cards/" + name;
        Image image = new Image(GameLayout.class.getResourceAsStream(path));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    EventHandler<ActionEvent> drawCardsHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            int numDrawnCards = view.getNumDrawnCards();

            view.setNumDrawnCards(numDrawnCards+1);
            drawnCardsButton.setText("Num of cards to draw: " + ++numDrawnCards );
            drawnCardsButton.setVisible(true);

        }
    };

    EventHandler<ActionEvent> drawCardsSubstractHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {

            int numDrawnCards = view.getNumDrawnCards();
            view.setNumDrawnCards(numDrawnCards-1);
            drawnCardsButton.setText("Num of cards to draw: " + --numDrawnCards );

            if (numDrawnCards == 0) {
                drawnCardsButton.setVisible(false);
            }

        }
    };

    private void setSuitRequestBox(){
        suitsBox = getRequestedSuitsBox();
        view.setChangedSuit(false);

        // set change listener on requestSuit buttons
        view.changedSuitProperty().addListener(displayChangedRequestedSuitHandler);
    }

    ChangeListener displayChangedRequestedSuitHandler = new ChangeListener() {
        @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
            //logger.debug("changedSuitProperty() has been changed!");
            for (Node button : suitsBox.getChildren()) {
                ButtonCard requestedSuitBut = (ButtonCard) button;
                if (requestedSuitBut.getButtonSuit() == view.getRequestedSuit()) {
                    //colorButton(requestedSuitBut);
                    requestedSuitBut.highlightButton();
                    requestedSuitBut.setPadding(new Insets(10, 10, 10, 10));
                }
                else {
                    requestedSuitBut.unHighlightButton();
                    requestedSuitBut.setPadding(new Insets(4, 4, 4, 4));
                }
            }
        }
    };

    private HBox getRequestedSuitsBox(){
        HBox box = new HBox();
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        ButtonCard hearts = new ButtonCard(Suit.HEARTS);
        hearts.setGraphic(getCardBackgroundImageView("hearts.jpg", 40));
        ButtonCard leaves = new ButtonCard(Suit.LEAVES);
        leaves.setGraphic(getCardBackgroundImageView("leaves.jpg", 40));
        ButtonCard acorns = new ButtonCard(Suit.ACORNS);
        acorns.setGraphic(getCardBackgroundImageView("acorns.jpg", 40));
        ButtonCard bells = new ButtonCard(Suit.BELLS);
        bells.setGraphic(getCardBackgroundImageView("bells.jpg", 40));

        hearts.setOnAction(selectRequestedSuitHandler);
        leaves.setOnAction(selectRequestedSuitHandler);
        acorns.setOnAction(selectRequestedSuitHandler);
        bells.setOnAction(selectRequestedSuitHandler);

        box.getChildren().addAll(hearts, leaves, acorns, bells);
        return box;
    }

    EventHandler<ActionEvent> selectRequestedSuitHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            ButtonCard requestedSuitBut = (ButtonCard) event.getSource();
            view.setRequestedSuit( (Suit) requestedSuitBut.getButtonSuit() );
            logger.debug("Requested suit set: {}", view.getRequestedSuit());
            requestedSuitBut.setOnAction(unselectRequestedSuitHandler);

            event.consume();
        }
    };

    EventHandler<ActionEvent> unselectRequestedSuitHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            ButtonCard requestedSuitBut = (ButtonCard) event.getSource();
            view.setRequestedSuit(Suit.UNSPECIFIED);
            logger.debug("Requested suit set: {}", Suit.UNSPECIFIED);
            requestedSuitBut.setOnAction(selectRequestedSuitHandler);
            event.consume();
        }
    };

    private void colorButton(Button button){
        button.setStyle("-fx-background-color: #d7efef; -fx-border-width: 1px; -fx-border-color: #acbfbf;");
    }

    public HBox getPlayersBox() {
        return playersBox;
    }

    public void setPlayersBox(HBox playersBox) {
        this.playersBox = playersBox;
    }

    public VBox getMoveControlBox() {
        return moveControlBox;
    }

    public void setMoveControlBox(VBox moveControlBox) {
        this.moveControlBox = moveControlBox;
    }

    public HBox getSuitsBox() {
        return suitsBox;
    }

    public void setSuitsBox(HBox suitsBox) {
        this.suitsBox = suitsBox;
    }

    public HBox getWasteBox() {
        return wasteBox;
    }

    public void setWasteBox(HBox wasteBox) {
        this.wasteBox = wasteBox;
    }

    public VBox getStockBox() {
        return stockBox;
    }

    public void setStockBox(VBox stockBox) {
        this.stockBox = stockBox;
    }

    public GridPane getThisPlayerBox() {
        return thisPlayerBox;
    }

    public void setThisPlayerBox(GridPane thisPlayerBox) {
        this.thisPlayerBox = thisPlayerBox;
    }

    public Button getDrawnCardsButton() {
        return drawnCardsButton;
    }

    public void setDrawnCardsButton(ControlButton drawnCardsButton) {
        this.drawnCardsButton = drawnCardsButton;
    }

    public HBox getThisPlayerCards() {
        return thisPlayerCardsBox;
    }

    public void setThisPlayerCards(HBox thisPlayerCards) {
        this.thisPlayerCardsBox = thisPlayerCards;
    }

    public HBox getSelectedCardsBox() {
        return selectedCardsBox;
    }

    public void setSelectedCardsBox(HBox selectedCardsBox) {
        this.selectedCardsBox = selectedCardsBox;
    }

    public GameLabel getSetPenalty() {
        return setPenalty;
    }

    public void setPenaltyText(String penalty) {
        this.setPenalty.setText(penalty);
    }

    public GameLabel getTurnLabel() {
        return turnLabel;
    }

    public void setTurnLabelText(String text) {
        turnLabel.setText(text);
    }

    public GameLabel getSelectedCardsLabel() {
        return selectedCardsLabel;
    }

    public void setSelectedCardsLabel(GameLabel selectedCardsLabel) {
        this.selectedCardsLabel = selectedCardsLabel;
    }
}
