package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.views.ButtonCard;
import com.goldasil.pjv.views.GameView;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Represents a Game Controller, operates based on the Finite State Machine concept.
 */
public class GameController {

    GameModel game;
    GameView view;

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    public GameController(GameModel game, GameView view) {
        this.game = game;
        this.view = view;
    }

    /**
     * Sets the players, their order of the game, their playing cards.
     */
    public void initializeGame() {
        game.initGame();
        view.updateGameScene();
        //view.updateGameScene(game.getCurrentState(), game.getPlayers());
    }

    /**
     * Runs the game play based on the Finite State Machine concept.
     * Starts in the Initial State.
     * Calls the chooseMove() on the players in order, verifies the move, updates the model, updates the view, and changes states according to the move, all in a loop.
     * The game ends when it gets to the Final State.
     */
    public void runGame() {
        /*while() {

            switch(){
            }

        }*/
    }

    public ArrayList<Card> getSelectedCards(List<Node> cardButtons){
        ArrayList<Card> cards = new ArrayList<>();
        for (Node node : cardButtons) {
            ButtonCard card = (ButtonCard) node;
            cards.add(new Card((Rank) card.getButtonRank(), (Suit) card.getButtonSuit()));
        }
        return cards;
    }



    public void submitMove(List<Node> cardButtons) {
        ArrayList<Card> moveCards = getSelectedCards(cardButtons);
        MoveDTO moveDTO = new MoveDTO(new Move(moveCards));
        game.playMove(1, moveDTO);
        view.updateGameScene();
    }

    public void submitMove(int numberOfCardsDrawn) {
        MoveDTO moveDTO = new MoveDTO(new Move(numberOfCardsDrawn));
        game.playMove(1, moveDTO);
        view.updateGameScene();
    }

    public LinkedList<Card> getWaste() {
        return game.getWaste();
    }

    public GameModel getGame() {
        return game;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }

}
