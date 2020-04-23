package com.goldasil.pjv.views;

import com.goldasil.pjv.models.SceneModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Generates the view for the menu part of the application based on the status of the SceneModel object managed by the MenuController.
 */
public class MenuView extends Application implements Observer, Runnable {

    private SceneModel currentScene;
    private volatile boolean isUnprocessedUpdate;

    /**
     * Creates a new MenuView based on the specified SceneModel.
     * @param currentSceneModel model carrying the current scene to be displayed
     */
    public MenuView(SceneModel currentSceneModel) {
        this.currentScene = currentSceneModel;
    }

    @Override
    public void run() {
        launch();

        while (true) {
            if(isUnprocessedUpdate){
                isUnprocessedUpdate = false;
                getNewScene();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    ex.getMessage();
                    break;
                }
            }
        }
    }

    /**
     * Sets the isUnprocessedUpdate instance variable to true, notifying of the change.
     * @param o scene
     * @param arg arg
     */
    @Override
    public void update(Observable o, Object arg) {
        isUnprocessedUpdate = true;
    }

    /**
     * Updates the view with the new scene based on the set scene model.
     */
    private void getNewScene(){
        switch(currentScene.getScene()) {
            case WELCOME_WINDOW:
                break;
            case MENU:
                break;
            case GUIDE:
                break;
            case SIGN_UP_IN:
                break;
            case SCOREBOARD:
                break;
            case GAME_NEW_OLD:
                break;
            case GAME_MODE:
                break;
            case GAME_MULTIPLAYER_SET_UP:
                break;
            case GAME_WIN_LOSE_STATUS:
                break;
            case DONE:
                break;
        }
    }


    /**
     * Generates a new FXML application
     * @param stage Stage stage
     * @throws Exception e
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("menu.fxml"));
        VBox vbox = loader.<VBox>load();

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

}
