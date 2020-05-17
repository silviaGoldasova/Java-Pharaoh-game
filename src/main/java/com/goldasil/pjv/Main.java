package com.goldasil.pjv;

import com.goldasil.pjv.controllers.gameControllers.GameController;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.views.GameView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.debug("Main method has started.");

        //set model
        GameModel gameModel = new GameModel();
        //GameView view = new GameView();
        //view.setGame(gameModel);

        // start view gui
        Thread guiLaunchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                javafx.application.Application.launch(GameView.class);
            }
        });
        guiLaunchThread.setName("guiLaunchThread");
        guiLaunchThread.start();
        GameView gameView = GameView.getGameViewAppInstance();
        gameView.setGame(gameModel);
        logger.debug("View has been launched.");

        // set controller
        GameController controller = new GameController(gameModel, gameView);
        gameView.setGameController(controller);
        controller.initializeGame();
    }


    private static void getRunningThreadsInfo() {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread t : threads) {
            String name = t.getName();
            Thread.State state = t.getState();
            int priority = t.getPriority();
            String type = t.isDaemon() ? "Daemon" : "Normal";
            System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
        }
    }


}


