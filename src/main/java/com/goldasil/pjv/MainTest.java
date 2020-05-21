package com.goldasil.pjv;

import com.goldasil.pjv.entity.GameEntity;
import com.goldasil.pjv.entity.GameService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.ServerSocket;

@Controller
public class MainTest {

    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    @Autowired
    GameService service;

    @GetMapping("/home")
    @ResponseBody
    public String example() {
        service.save();
        System.out.println("saved");
        return "Hello";
    }

    /*public static void main(String[] args) {
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

    }*/


}