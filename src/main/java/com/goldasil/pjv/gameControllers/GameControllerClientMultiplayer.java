package com.goldasil.pjv.gameControllers;

import com.goldasil.pjv.GameModel;

/**
 * Represents a Game Controller for the Person Vs Person mode from the perspective of a player playing on the device in the client role, operates based on the Finite State Machine concept.
 * Extends the Game multiplayer mode controller. The added functionality lies in the handling of the communication about the moves and other matters.
 *
 * The player in the role of a client is an outer node of the communication - sends all information to the server = the central node of the communication, the server propages the message as deemed necessary.
 *
 */
public class GameControllerClientMultiplayer extends GameControllerMultiplayer {

    public GameControllerClientMultiplayer(GameModel game) {
        super(game);
    }


}
