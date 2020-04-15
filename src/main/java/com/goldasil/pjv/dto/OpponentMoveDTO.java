package com.goldasil.pjv.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goldasil.pjv.Card;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Suit;
import java.util.ArrayList;


public class OpponentMoveDTO extends MoveDTO {


    private int opponentCardCount;
    private Card upcard;

    public OpponentMoveDTO(){
    }

    public OpponentMoveDTO(MoveType moveType, ArrayList<Card> move, int drawCards, Suit requestedSuit, int opponentCardCount, Card upcard) {
        super(moveType, move, drawCards, requestedSuit);
        this.opponentCardCount = opponentCardCount;
        this.upcard = upcard;
    }

    public OpponentMoveDTO(MoveDTO move, int opponentCardCount, Card upcard) {
        super(move.getMoveType(), move.getMove(), move.getDrawCards(), move.getRequestedSuit());
        this.opponentCardCount = opponentCardCount;
        this.upcard = upcard;
    }

    @Override
    public String toString() {
        return super.toString() + ", opponentCardCount: " + opponentCardCount + ", upcard: " + upcard;
    }

    @JsonIgnore
    public boolean isNewUpcard() {
        if (moveType == MoveType.PLAY || moveType == MoveType.DOUBLE_PLAY) {
            return true;
        }
        return false;
    }

    public int getOpponentCardCount() {
        return opponentCardCount;
    }

    public void setOpponentCardCount(int opponentCardCount) {
        this.opponentCardCount = opponentCardCount;
    }

    public Card getUpcard() {
        return upcard;
    }

    public void setUpcard(Card upcard) {
        this.upcard = upcard;
    }

    /*private Card upcard;
    private boolean isNewUpcard;
    private Suit requestedSuit;
    private ArrayList<Card> opponentMove;
    private int opponentCardCount;*/

}
