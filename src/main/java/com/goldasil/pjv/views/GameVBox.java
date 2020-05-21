package com.goldasil.pjv.views;

import ch.qos.logback.core.Layout;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    public GameVBox(int playerId, Label nameAndCardsCountLabel, Label lastMoveLabel) {
        super();
        this.playerId = playerId;
        this.lastMoveLabel = lastMoveLabel;
        this.nameAndCardsCountLabel = nameAndCardsCountLabel;

        ButtonCard backgroundCard = new ButtonCard(Rank.UNSPECIFIED, Suit.UNSPECIFIED);
        backgroundCard.setGraphic(GameLayout.getCardBackgroundImageView("background.jpg", 120.0));

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(nameAndCardsCountLabel, backgroundCard, lastMoveLabel);
        setSpacing(10);
    }

    public void setNameCountLabelText(int cardsCount){
        nameAndCardsCountLabel.setText("player #" + playerId + ". Cards count: " + cardsCount);
    }

    public void setLastMoveLabelText(MoveDTO moveDTO) {
        lastMoveLabel.setVisible(true);
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
