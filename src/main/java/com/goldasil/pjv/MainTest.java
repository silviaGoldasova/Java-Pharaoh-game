package com.goldasil.pjv;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainTest {

    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    public static void main(String[] args) {
        Thread guiLaunchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                javafx.application.Application.launch(ViewTest.class);
            }
        });
        guiLaunchThread.start();
        ViewTest viewTest = ViewTest.waitForStartUpTest();
        viewTest.printSomething();


        logger.debug("after launch");

    }


}