package com.goldasil.pjv.views;


import com.goldasil.pjv.entity.GameEntity;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ControlButton extends Button {

    String info;
    GameEntity savedGameInfo;

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

    public GameEntity getSavedGameInfo() {
        return savedGameInfo;
    }

    public void setSavedGameInfo(GameEntity savedGameInfo) {
        this.savedGameInfo = savedGameInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
