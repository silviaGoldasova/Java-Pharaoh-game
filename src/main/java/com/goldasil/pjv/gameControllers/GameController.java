package com.goldasil.pjv.gameControllers;

import com.goldasil.pjv.GameModel;
import com.goldasil.pjv.dto.MoveDTO;

/**
 * Represents a Game Controller, operates based on the Finite State Machine concept.
 */
public class GameController {

    GameModel game;
    MoveDTO lastMoveDTO;
    MoveDTO currentMoveDTO;

    public GameController(GameModel game) {
        this.game = game;
    }

    /**
     * Sets the players, their order of the game, their playing cards.
     */
    private void initializeGame() {
    }

    /**
     * Runs the game play based on the Finite State Machine concept.
     * Starts in the Initial State.
     * Calls the getMove() on the players in order, verifies the move, updates the model, updates the view, and changes states according to the move, all in a loop.
     * The game ends when it gets to the Final State.
     */
    public void runGame() {
        /*while() {

            switch(){

            }

        }*/
    }



}
