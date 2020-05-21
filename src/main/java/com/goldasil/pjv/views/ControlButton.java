package com.goldasil.pjv.views;


import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ControlButton extends Button {

    public ControlButton() {
        super();
        setAlignment(Pos.CENTER);
        setStyle("-fx-font-size: 1.5em; -fx-background-color: #faf0e6; -fx-border-width: 0px; -fx-background-radius: 10;");
    }

    public ControlButton(String label) {
        super(label);
        setStyle("-fx-font-size: 1.5em; -fx-background-color: #faf0e6; -fx-border-width: 0px; -fx-background-radius: 10;");
        setMinWidth(180);
        setAlignment(Pos.CENTER);
        //setPrefWidth(200);
        //setPrefHeight(150);
    }


}
