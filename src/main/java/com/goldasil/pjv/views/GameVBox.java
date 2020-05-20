package com.goldasil.pjv.views;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.MoveType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameVBox extends VBox {

    private int playerId;
    private Label lastMoveLabel;
    private Label nameAndCardsCountLabel;

    public GameVBox() {
        super();
    }

    public GameVBox(int playerId) {
        super();
        this.playerId = playerId;
    }

    public GameVBox(int playerId, Label lastMoveLabel, Label nameAndCardsCountLabel) {
        super();
        this.playerId = playerId;
        this.lastMoveLabel = lastMoveLabel;
        this.nameAndCardsCountLabel = nameAndCardsCountLabel;
        Button button = new Button("-");
        this.getChildren().addAll(lastMoveLabel, button, nameAndCardsCountLabel);
        setSpacing(10);
    }

    public void setNameCountLabelText(int cardsCount){
        nameAndCardsCountLabel.setText("player #" + playerId + ". Cards count: " + cardsCount);
    }

    public void setLastMoveLabelText(MoveDTO moveDTO) {
        String moveText;
        if (moveDTO.getMoveType() == MoveType.PLAY) {
            moveText = moveDTO.getMove().toString();
        }
        else if (moveDTO.getMoveType() == MoveType.DRAW) {
            moveText = "Drew " + moveDTO.getDrawCards();
        }
        else {
            moveText = moveDTO.getMoveType().toString();
        }
        lastMoveLabel.setText(moveText);
    }

    public Label getLastMoveLabel() {
        return lastMoveLabel;
    }

    public void setLastMoveLabel(Label lastMoveLabel) {
        this.lastMoveLabel = lastMoveLabel;
    }

    public Label getNameAndCardsCountLabel() {
        return nameAndCardsCountLabel;
    }

    public void setNameAndCardsCountLabel(Label nameAndCardsCountLabel) {
        this.nameAndCardsCountLabel = nameAndCardsCountLabel;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
