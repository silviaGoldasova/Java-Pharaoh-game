package com.goldasil.pjv.models;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;

/**
 * Represents a move played by a player.
 */
public class Move {

    protected MoveType moveType;
    protected ArrayList<Card> move;
    protected int drawCards;
    protected Suit requestedSuit;

    public Move(){
    }

    /**
     * Creates a new move with the specified list of cards to be played in the move
     * @param move list of cards to be played in the move
     */
    public Move(ArrayList<Card> move) {
        moveType = MoveType.PLAY;
        this.move = move;
        drawCards = 0;
        requestedSuit = Suit.UNSPECIFIED;
    }

    public Move(MoveType movetype) {
        this.moveType = movetype;
        this.move = null;
        drawCards = 0;
        requestedSuit = Suit.UNSPECIFIED;
    }

    /**
     * Creates a new move - a player draws the specified number of cards
     * @param drawCards number of cards to draw if applies
     */
    public Move(int drawCards) {
        this.moveType = MoveType.DRAW;
        this.move = null;
        this.drawCards = drawCards;
        this.requestedSuit = Suit.UNSPECIFIED;
    }

    /**
     * Creates a new move with the specified list of cards to be played in the move and suit requested as an OVERKNAVE was played.
     * @param move list of cards to be played in the move
     * @param requestedSuit suit requested as an OVERKNAVE was played
     */
    public Move(ArrayList<Card> move, Suit requestedSuit) {
        this.moveType = moveType;
        this.move = move;
        this.drawCards = 0;
        this.requestedSuit = requestedSuit;
    }

    public Move(MoveType movetype, ArrayList<Card> move, int drawCards, Suit requestedSuit) {
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
