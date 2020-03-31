package com.goldasil.pjv.dto;
import com.goldasil.pjv.Card;
import com.goldasil.pjv.enums.Suit;
import java.util.ArrayList;

public class OpponentMoveDTO {

    Card upcard;
    boolean isNewUpcard;
    Suit requestedSuit;
    ArrayList<Card> opponentMove;
    int opponentCardCount;

    public OpponentMoveDTO(Card upcard, boolean isNewUpcard, Suit requestedSuit, ArrayList<Card> opponentMove, int opponentCardCount) {
        this.upcard = upcard;
        this.isNewUpcard = isNewUpcard;
        this.requestedSuit = requestedSuit;
        this.opponentMove = opponentMove;
        this.opponentCardCount = opponentCardCount;
    }

    public Card getUpcard() {
        return upcard;
    }

    public void setUpcard(Card upcard) {
        this.upcard = upcard;
    }

    public boolean isNewUpcard() {
        return isNewUpcard;
    }

    public void setNewUpcard(boolean newUpcard) {
        isNewUpcard = newUpcard;
    }

    public Suit getRequestedSuit() {
        return requestedSuit;
    }

    public void setRequestedSuit(Suit requestedSuit) {
        this.requestedSuit = requestedSuit;
    }

    public ArrayList<Card> getOpponentMove() {
        return opponentMove;
    }

    public void setOpponentMove(ArrayList<Card> opponentMove) {
        this.opponentMove = opponentMove;
    }

    public int getOpponentCardCount() {
        return opponentCardCount;
    }

    public void setOpponentCardCount(int opponentCardCount) {
        this.opponentCardCount = opponentCardCount;
    }
}
