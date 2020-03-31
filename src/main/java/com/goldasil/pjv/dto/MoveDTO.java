package com.goldasil.pjv.dto;
import com.goldasil.pjv.Card;
import com.goldasil.pjv.enums.MoveType;

import java.util.ArrayList;

public class MoveDTO {

    private MoveType moveType;
    private ArrayList<Card> move;
    private int drawCards;

    public MoveDTO(MoveType moveType, ArrayList<Card> move, int drawCards) {
        this.moveType = moveType;
        this.move = move;
        this.drawCards = drawCards;
    }


    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public ArrayList<Card> getMove() {
        return move;
    }

    public void setMove(ArrayList<Card> move) {
        this.move = move;
    }

    public int getDrawCards() {
        return drawCards;
    }

    public void setDrawCards(int drawCards) {
        this.drawCards = drawCards;
    }
}
