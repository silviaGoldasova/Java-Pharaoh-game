package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.views.GameView;

/**
 * Represents a Game Controller for the Person Vs Computer mode, operates based on the Finite State Machine concept.
 * Extends the basic game controller. The added functionality lies in the initialization and handling of the random player.
 */
public class GameControllerVsComputer extends GameController {

    public GameControllerVsComputer(GameModel game, GameView view) {
        super(game, view);
    }



}
