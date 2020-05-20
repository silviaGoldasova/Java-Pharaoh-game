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

import java.util.concurrent.CountDownLatch;

public class ViewTest extends Application {

    private static final Logger logger = LoggerFactory.getLogger(ViewTest.class);

    public static final CountDownLatch latch = new CountDownLatch(1);
    public static ViewTest viewTest = null;

    public static ViewTest waitForStartUpTest() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return viewTest;
    }

    public static void setViewTest(ViewTest startUpTest0) {
        viewTest = startUpTest0;
        latch.countDown();
    }

    public ViewTest() {
        setViewTest(this);
    }

    public void printSomething() {
        logger.debug("You called a method on the application");
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(25);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label p1Label = new Label("P1 cards:");
        Label p1SelectedCards = new Label("P1 selected cards:");
        Label p2Label = new Label("P2 cards:");
        Label waste = new Label("Waste:");

        Button stock = new Button("Stock (draw a card)");
        Button submit = new Button("Submit Move");

        //String waste = game.getWaste();
        //Text text = new Text("Some Text");

        // column, row
        grid.add(p1Label, 0, 0);
        grid.add(p1SelectedCards, 0, 2);
        grid.add(submit, 0, 3);
        grid.add(p2Label, 0, 4);
        grid.add(waste, 1, 6);
        grid.add(stock, 0, 6);

        HBox p1CardsBox = new HBox();
        HBox p2CardsBox = new HBox();
        grid.add(p1CardsBox, 0, 1);
        grid.add(p2CardsBox, 0, 5);

        //p1Cards.setText("text other");

        Scene scene = new Scene(grid, 300, 275);
        stage.setScene(scene);
        stage.show();

        logger.debug("Showing scene.");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
