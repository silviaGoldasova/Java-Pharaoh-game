package com.goldasil.pjv.models;

import com.goldasil.pjv.enums.MenuScene;

import java.util.Observable;

/**
 * Represents a scene in the view.
 */
public class SceneModel extends Observable {

    MenuScene currentScene;

    public SceneModel() {
        currentScene = MenuScene.WELCOME_WINDOW;
    }

    /**
     * Gets the current scene.
     * @return
     */
    public MenuScene getScene() {
        return currentScene;
    }

    /**
     * Sets a new current screen.
     * @param updatedScene a new current screen to be set
     */
    public synchronized void setScene(MenuScene updatedScene) {
        this.currentScene = updatedScene;
        notifyObservers();
    }
}
