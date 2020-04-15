package com.goldasil.pjv.dto;
import com.goldasil.pjv.Card;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;

public class MoveDTO {

    protected MoveType moveType;
    protected ArrayList<Card> move;
    protected int drawCards;
    protected Suit requestedSuit;


    public MoveDTO(){
    }

    public MoveDTO(MoveType moveType, ArrayList<Card> move, int drawCards) {
        this.moveType = moveType;
        this.move = move;
        this.drawCards = drawCards;
        this.requestedSuit = Suit.UNSPECIFIED;
    }

    public MoveDTO(MoveType moveType, ArrayList<Card> move, int drawCards, Suit requestedSuit) {
        this.moveType = moveType;
        this.move = move;
        this.drawCards = drawCards;
        this.requestedSuit = requestedSuit;
    }

    @Override
    public String toString() {
        return "MoveType" + moveType + ", move: " + move + ", drawCards: " + drawCards;
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

    public Suit getRequestedSuit() {
        return requestedSuit;
    }

    public void setRequestedSuit(Suit requestedSuit) {
        this.requestedSuit = requestedSuit;
    }
}
