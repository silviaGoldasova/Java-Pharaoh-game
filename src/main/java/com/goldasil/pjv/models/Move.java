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
     * Creates a new move with specified type of the move, list of cards to be played in the move if applies, number of cards to draw if applies.
     * @param moveType type of the move
     * @param move list of cards to be played in the move if applies
     * @param drawCards number of cards to draw if applies
     */
    public Move(MoveType moveType, ArrayList<Card> move, int drawCards) {
        this.moveType = moveType;
        this.move = move;
        this.drawCards = drawCards;
        this.requestedSuit = Suit.UNSPECIFIED;
    }

    /**
     * Creates a new move with specified type of the move, list of cards to be played in the move if applies, number of cards to draw if applies, suit requested if an OVERKNAVE was played.
     * @param moveType type of the move
     * @param move list of cards to be played in the move if applies to the move
     * @param drawCards number of cards to draw if applies to the move
     * @param requestedSuit suit requested if an OVERKNAVE was played
     */
    public Move(MoveType moveType, ArrayList<Card> move, int drawCards, Suit requestedSuit) {
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
