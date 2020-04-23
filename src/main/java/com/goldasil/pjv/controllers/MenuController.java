package com.goldasil.pjv.controllers;

import com.goldasil.pjv.enums.MenuScene;
import com.goldasil.pjv.models.SceneModel;

/**
 * Manages all the menu options and steps taken by the user in order to play a game.
 * Sets up appropriate views and manages information transfer between the application model and the view (eg. login data, scoreboard) that correspond with the user' actions while going through the menu options.
 */
public class MenuController {

    SceneModel sceneModelObj;

    /**
     * Creates a new controller managing a SceneModel model which holds the state of the current sceneModel.
     * @param sceneModel SceneModel object keeping track of the scenes
     */
    public MenuController(SceneModel sceneModel) {
        sceneModelObj = sceneModel;
    }


    public void run() {
        while (sceneModelObj.getScene() != MenuScene.DONE) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.getMessage();
                break;
            }
        }
    }

    

}
