package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.models.GameModel;

/**
 * Represents a Game Controller for the Person Vs Person mode from the perspective of a player playing on the device in the server role, operates based on the Finite State Machine concept.
 * Extends the Game multiplayer mode controller. The added functionality lies in the connection of the players and management of all the communication taking place throughout the game.
 *
 * The player in the role of a server is a central node of the communication - all other players send information to him, then he propagates the information to the others.
 */
public class GameControllerServerMultiplayer extends GameControllerMultiplayer {

    public GameControllerServerMultiplayer(GameModel game) {
        super(game);
    }




}
