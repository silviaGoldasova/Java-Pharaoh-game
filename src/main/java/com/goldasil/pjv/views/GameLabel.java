package com.goldasil.pjv.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameLabel extends Label {

    public GameLabel(String labelText) {
        super(labelText);
        setPadding(new Insets(5, 5, 5, 5));
        setStyle("-fx-background-color: #faf0e6; -fx-border-width: 0px; -fx-background-radius: 10;");
        setTextFill(Color.web("#32302e")); //beb9b5
        setFont(new Font("Arial", 18));
        setAlignment(Pos.CENTER);
    }

    public void highlightLabel(){
        setPadding(new Insets(8, 8, 8, 8));
        setStyle("-fx-background-color: #d6aaaa; -fx-border-width: 0px; -fx-background-radius: 10;");
    }

    public void unHighlightLabel(){
        setPadding(new Insets(5, 5, 5, 5));
        setStyle("-fx-background-color: #faf0e6; -fx-border-width: 0px; -fx-background-radius: 10;");
    }

}
