package com.goldasil.pjv.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goldasil.pjv.Card;
import com.goldasil.pjv.Move;
import com.goldasil.pjv.enums.MoveState;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import java.util.ArrayList;

/**
 * A helper class encompassing all the necessary data for communicating the performed moves between the players, can be used to both send through the network in the JSON format, or passed as an object directly to another player.
 * Carries the last performed move, consequences of the move for the next player and a list of active states in the game that are to be taken into account.
 * Includes functions for analysis of the information about the move.
 */
public class MoveDTO extends Move {

    private Card upcard;
    private int penaltyForSevens;
    private ArrayList<MoveState> states;

    private static final int NUM_OF_CARDS_IN_QUARTET = 4;

    public MoveDTO(){
    }

    public MoveDTO(Move move) {
        super(move.getMoveType(), move.getMove(), move.getDrawCards(), move.getRequestedSuit());
    }

    public MoveDTO(Move move, Card upcard, int penaltyForSevens, ArrayList<MoveState> states) {
        super(move.getMoveType(), move.getMove(), move.getDrawCards(), move.getRequestedSuit());
        this.upcard = upcard;
        this.penaltyForSevens = penaltyForSevens;
        this.states = states;
    }

    /**
     * Check whether if a non-empty list of cards is played, all the cards have the same rank.
     * @return true all the the cards in the desired move are of the same rank
     */
    public boolean isValidMoveWithin(){
        if (moveType == MoveType.PLAY) {
            Rank rank = move.get(0).getRank();
            for (int i = 1; i < move.size(); i++) {
                if (move.get(i).getRank() != rank) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether there were 4 cards of the same rank played in the move.
     * @return true if the move is a quartet / 4 cards of the same rank were played
     */
    public boolean isQuartet() {
        if (moveType != MoveType.PLAY) {
            return false;
        }
        if (move.size() != NUM_OF_CARDS_IN_QUARTET) {
            return false;
        }
        Rank rank = move.get(0).getRank();
        for (int i = 1; i < 4; i++) {
            if (move.get(i).getRank() != rank) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a player has been returned to the gameControllers in the last round, meaning a Seven Hearts would have been played.
     * @return true is a player has been returned to the gameControllers
     */
    public boolean wasSevenHeartsPlayed(){
        if (isActiveState(MoveState.SEVEN_HEARTS_RETURN_PLAYED)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a player wants to return another player back to the gameControllers, meaning a player plays SEVEN HEARTS.
     * @return true is a player will be returned to the gameControllers
     */
    public boolean isSevenHeartsPlayed(){
        if (isActiveState(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN) && moveType == MoveType.PLAY && move.get(0).compareCards(new Card(Rank.SEVEN, Suit.HEARTS)) ) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a card of rank of seven has been played in the previous move and is still active.
     * @return true if a card of rank of seven is active
     */
    public boolean wasSevenPlayed() {
        if (upcard.getRank() == Rank.SEVEN && moveType == MoveType.PLAY && !isSevenHeartsPlayed()) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the desired move to play is to play cards of rank of SEVEN.
     * @return true if Sevens are played
     */
    public boolean isSevenPlayed(){
        if (moveType == MoveType.PLAY && move.get(0).getRank() == Rank.SEVEN && !isActiveState(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether in the previous move was any special card played.
     * @return true if no special card was played
     */
    public boolean wasNonspecialMove(){
        Rank upRank = upcard.getRank();
        if (upRank == Rank.EIGHT || upRank == Rank.NINE || upRank == Rank.TEN || upRank == Rank.KING) {
            return true;
        }
        if (upRank == Rank.UNDERKNAVE && upcard.getSuit() != Suit.LEAVES) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether in the desired move is any special card to be played.
     * @return true if no special card will be played
     */
    public boolean isNonspecialMove(){
        if (moveType != MoveType.PLAY) {
            return false;
        }
        Rank upRank = move.get(0).getRank();
        if (upRank == Rank.EIGHT || upRank == Rank.NINE || upRank == Rank.TEN || upRank == Rank.KING) {
            return true;
        }
        if (upRank == Rank.UNDERKNAVE && upcard.getSuit() != Suit.LEAVES) {
            return true;
        }
        return false;
    }

    /**
     * Gets the state specified for the situation when an OVERKNAVE has been played, and a suit is requested.
     * @return the state specified to the suit requested in the previous move
     */
    public MoveState getOverknaveState() {
        try {
            switch (requestedSuit) {
                case HEARTS:
                    return MoveState.OVERKNAVE_HEARTS;
                case LEAVES:
                    return MoveState.OVERKNAVE_LEAVES;
                case BELLSS:
                    return MoveState.OVERKNAVE_BELLS;
                case ACORNS:
                    return MoveState.OVERKNAVE_ACORNS;
            }
            throw new Exception("Undefined requested suit.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return MoveState.NONSPECIAL_SITUATION;
        }
    }

    /**
     * Checks whether any of the players is without cards.
     * @return true if there is a player in the gameControllers with no cards in hand since the beginning of the last round
     */
    public boolean wasWithoutCards(){
        if (isActiveState(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether during the current round, only aces are collected.
     * @return true if the round is dedicated to only the aces
     */
    public boolean isAcesOnlyTurn(){
        if (isActiveState(MoveState.ACES_ONLY)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether an ACE is desired to be played.
     * @return true if an ACE is desired to be played
     */
    public boolean isAcePlayed(){
        if (moveType != MoveType.PLAY) {
            return false;
        }
        if (move.get(0).getRank() == Rank.ACE) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a state is currently active (set active from the previous moves)
     * @param state the state to be checked whether active
     * @return true if the state is active
     */
    private boolean isActiveState(MoveState state) {
        for (MoveState moveState : states) {
            if (moveState == state) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the desired state based on the suit of the desired card.
     * @return the suit of the card desired to be played
     */
    public MoveState getSuitMoveState() {
        switch (move.get(0).getSuit()){
            case HEARTS:
                return MoveState.HEARTS_PLAYED;
            case LEAVES:
                return MoveState.LEAVES_PLAYED;
            case ACORNS:
                return MoveState.ACORNS_PLAYED;
            case BELLSS:
                return MoveState.BELLS_PLAYED;
        }
        return null;
    }


    @Override
    public String toString() {
        return "MoveDTO{" +
                "upcard=" + upcard +
                ", penaltyForSevens=" + penaltyForSevens +
                ", states=" + states +
                ", moveType=" + moveType +
                ", move=" + move +
                ", drawCards=" + drawCards +
                ", requestedSuit=" + requestedSuit +
                '}';
    }

    @JsonIgnore
    public boolean isNewUpcard() {
        if (moveType == MoveType.PLAY) {
            return true;
        }
        return false;
    }

    public Card getUpcard() {
        return upcard;
    }

    public void setUpcard(Card upcard) {
        this.upcard = upcard;
    }

    public int getPenaltyForSevens() {
        return penaltyForSevens;
    }

    public void setPenaltyForSevens(int penaltyForSevens) {
        this.penaltyForSevens = penaltyForSevens;
    }

    public ArrayList<MoveState> getStates() {
        return states;
    }

    public void setStates(ArrayList<MoveState> states) {
        this.states = states;
    }

}
