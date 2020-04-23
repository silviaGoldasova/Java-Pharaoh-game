package com.goldasil.pjv.views;

import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.models.GameModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages the game view of the cards of the players, stock, waste and other entities.
 */
public class GameView extends Application implements Observer, Runnable {

    private GameModel game;
    private volatile boolean isUnprocessedUpdate;

    public GameView(GameModel game) {
        this.game = game;
        isUnprocessedUpdate = true;
    }


    @Override
    public void run() {
        launch();

        while (game.getCurrentState() != GameState.DONE) {
            if(isUnprocessedUpdate){
                isUnprocessedUpdate = false;
                updateScene();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.getMessage();
                    break;
                }
            }
        }
    }

    /**
     * Updates the scene to the newly set scene (a result of a change in the game model).
     */
    private void updateScene() {
        /*switch(game.getCurrentState()) {
            case FIRST_MOVE:
                break;
            case QUARTET_PLAYED:
                break;
            case
        }*/
    }


    @Override
    public void update(Observable o, Object arg) {
        isUnprocessedUpdate = true;
    }

    /**
     * Generates a new FXML application
     * @param stage Stage stage
     * @throws Exception e
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("game.fxml"));
        VBox vbox = loader.<VBox>load();

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }


}
