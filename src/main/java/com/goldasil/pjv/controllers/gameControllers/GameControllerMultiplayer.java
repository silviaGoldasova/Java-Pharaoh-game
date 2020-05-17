package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.views.GameView;

/**
 * Represents a Game Controller for the Person Vs Person mode, operates based on the Finite State Machine concept.
 * Extends the basic game controller. The added functionality lies in the connection of all the players, setup of network communication, propagating information about every move to all players and error handling connected to the communication.
 */
public class GameControllerMultiplayer extends GameController {

    public GameControllerMultiplayer(GameModel game, GameView view) {
        super(game, view);
    }


}
