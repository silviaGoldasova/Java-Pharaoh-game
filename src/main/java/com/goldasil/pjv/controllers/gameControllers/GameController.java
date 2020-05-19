package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.models.Player;
import com.goldasil.pjv.views.ButtonCard;
import com.goldasil.pjv.views.GameView;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Thread.sleep;

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
    public void initializeGame(int numberOfRandomPlayers) {
        game.initGame(numberOfRandomPlayers);
        view.updateGameScene();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        view.setRequestedSuit(game.getCurrentMoveDTO().getRequestedSuit());
        //view.updateGameScene(game.getCurrentState(), game.getPlayers());
    }

    /**
     * Runs the game play based on the Finite State Machine concept.
     * Starts in the Initial State.
     * Calls the chooseMove() on the players in order, verifies the move, updates the model, updates the view, and changes states according to the move, all in a loop.
     * The game ends when it gets to the Final State.
     */

    public void playTurn(){
        if (game.getCurrentPlayerIdTurn() == game.getThisPlayerId()) {
            return;
        }

        logger.debug("\n\nStart of the randomplayer's move.");

        if (game.runOppTurn()) {

            view.updateGameScene();
            setChangedSuit();
            game.setNextPlayersTurn();
            logger.debug("Player's turn has been played (id = {}).", game.getCurrentPlayerIdTurn());
        }
        playTurn();

    }

    public ArrayList<Card> getSelectedCards(List<Node> cardButtons){
        ArrayList<Card> cards = new ArrayList<>();
        for (Node node : cardButtons) {
            ButtonCard card = (ButtonCard) node;
            cards.add(new Card((Rank) card.getButtonRank(), (Suit) card.getButtonSuit()));
        }
        return cards;
    }

    public void submitMoveFromView(List<Node> cardButtons, Suit requestedSuit) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        ArrayList<Card> moveCards = getSelectedCards(cardButtons);
        MoveDTO moveDTO = new MoveDTO(new Move(moveCards, requestedSuit));
        moveDTO.setUpcard(game.getUpcard());

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)){
            setChangedSuit();
            view.updateGameScene();
            game.setNextPlayersTurn();
            logger.debug("randomplayer's move follows");
            playTurn();
        }
    }

    public void submitMoveFromView(int numberOfCardsDrawn) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        MoveDTO moveDTO = new MoveDTO(new Move(numberOfCardsDrawn));
        moveDTO.setUpcard(game.getUpcard());

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)){
            setChangedSuit();
            view.updateGameScene();
            game.setNextPlayersTurn();
            playTurn();
        }
    }

    private void setChangedSuit(){
        view.setRequestedSuit(game.getCurrentMoveDTO().getRequestedSuit());
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
