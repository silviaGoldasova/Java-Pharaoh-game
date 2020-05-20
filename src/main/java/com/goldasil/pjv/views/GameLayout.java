package com.goldasil.pjv.views;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

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
    private Button drawnCardsButton;

    private Label setPenalty;
    private Label turnLabel;

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
        playersBox.setSpacing(30);
    }

    public void initializePlayersBox(List<Player> players, int thisPlayerId){
        logger.debug("initialize PlayersBox");
        for (Player player : players) {
            if (player.getPlayerID() == thisPlayerId) {
                continue;
            }
            Label playerLabel = new Label("player #" + player.getPlayerID() + ". Cards count: " + player.getCards().size());
            Label lastMove = new Label("-");

            GameVBox playerBox = new GameVBox(player.getPlayerID(), playerLabel, lastMove);

            playersBox.getChildren().add(playerBox);
        }
    }

    public void updatePlayersBox(Player player, MoveDTO moveDTO) {
        for (Node node : playersBox.getChildren()){
            GameVBox playerBox = (GameVBox) node;
            if ( playerBox.getPlayerId() == player.getPlayerID()){
                logger.debug("Found VBox for the player");
                playerBox.setNameCountLabelText(player.getCards().size());
                playerBox.setLastMoveLabelText(moveDTO);
            }
        }
    }

    private void setThisPlayerBox(){
        thisPlayerBox = new GridPane();
        //thisPlayerBox.setPadding(new Insets(25, 25, 25, 25));
        thisPlayerBox.setHgap(10);
        thisPlayerBox.setVgap(10);

        Label selectedCardsLabel = new Label("Selected Cards ");
        thisPlayerBox.add(selectedCardsLabel, 0, 0);

        selectedCardsBox = new HBox();
        selectedCardsBox.setSpacing(10);
        thisPlayerBox.add(selectedCardsBox, 1, 0);

        Label myCardsLabel = new Label("My Cards: ");
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
              ButtonCard buttonCard = new ButtonCard(card);
              wasteBox.getChildren().add(buttonCard);
          }
        } else {
            int index = waste.size() - WASTE_DISPLAY_SIZE;
            for (int i = index; i < waste.size(); i++) {
                ButtonCard buttonCard = new ButtonCard(waste.get(i));
                wasteBox.getChildren().add(buttonCard);
            }
        }
    }

    private void setMoveControlBox() {
        moveControlBox = new VBox();
        moveControlBox.setSpacing(10);

        Button submit = new Button("Submit Move");
        submit.setOnAction(view.submitSelectCardHandler);

        Button winGame = new Button("Win Game");
        winGame.setOnAction(view.winGameButtonHandler);

        setPenalty = new Label("Set Penalty: ");
        turnLabel = new Label("Turn of the player #: ");

        moveControlBox.getChildren().addAll(submit, winGame, setPenalty, turnLabel, suitsBox);
    }

    private void setStockBox(){
        stockBox = new VBox();
        stockBox.setSpacing(10);

        Button stockButton = new Button("Stock (draw a card)");
        stockButton.setOnAction(drawCardsHandler);

        drawnCardsButton = new Button();
        drawnCardsButton.setOnAction(drawCardsSubstractHandler);
        drawnCardsButton.setVisible(false);

        stockBox.getChildren().addAll(stockButton, drawnCardsButton);
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
                    colorButton(requestedSuitBut);
                }
                else {
                    requestedSuitBut.setStyle("");
                }
            }
        }
    };

    private HBox getRequestedSuitsBox(){
        HBox box = new HBox();
        box.setSpacing(10);

        ButtonCard hearts = new ButtonCard("Hearts");
        ButtonCard leaves = new ButtonCard("Leaves");
        ButtonCard acorns = new ButtonCard("Acorns");
        ButtonCard bells = new ButtonCard("Bells");

        hearts.setButtonSuit(Suit.HEARTS);
        leaves.setButtonSuit(Suit.LEAVES);
        acorns.setButtonSuit(Suit.ACORNS);
        bells.setButtonSuit(Suit.BELLS);

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

            event.consume();
            //logger.debug("Move submitted.");
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

    public void setDrawnCardsButton(Button drawnCardsButton) {
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

    public Label getSetPenalty() {
        return setPenalty;
    }

    public void setPenaltyText(String penalty) {
        this.setPenalty.setText(penalty);
    }

    public Label getTurnLabel() {
        return turnLabel;
    }

    public void setTurnLabelText(String text) {
        turnLabel.setText(text);
    }
}
