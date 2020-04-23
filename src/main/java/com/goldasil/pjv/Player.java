package com.goldasil.pjv;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.SpecialCardCase;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;
import java.util.List;


/**
 * Represends a general player in a card gameControllers.
 * The class contains a basic pack of functions necessary for handling cards a player has in the hand.
 */
public class Player {

    protected ArrayList<Card> cards;
    protected int cardsCount;
    protected int playerID;

    public Player(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Creates a player with the specified cards in hand.
     * @param cards cards that the player has in hand
     */
    public Player(ArrayList<Card> cards, int playerID) {
        this.cards = cards;
        this.playerID = playerID;
        this.cardsCount = cards.size();
    }

    /** Adds cards passes in in an array into the hand of the player.
     * @param move list of cards to be added to the hand of the player
     */
    public void addCards(ArrayList<Card> move) {
        for (Card card : move) {
            cards.add(card);
        }
    }

    /**
     * Adds the card passed as a parameter to the list of cards a player has in the hand.
     * @param card card to be added
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes the cards passed in in an array from the cards in a player's hand.
     * @param move
     */
    public void removeCards(ArrayList<Card> move) {
        for (Card card : move) {
            cards.remove(card);
        }
        cardsCount = cards.size();
    }

    /**
     * Check whether the player has a specified card in hand.
     * @param card card to be checked whether is at the moment in the player's hand
     * @return true if the player has the specified card
     */
    protected boolean hasTheCard(Card card){
        for (Card handCard : cards){
            if (handCard.getRank() == card.getRank() && handCard.getSuit() == card.getSuit()) {
                return true;
            }
        }
        return false;
    }

    /*


    protected boolean isUnderKnaveLeavesInHand(){
        for (Card card : cards) {
            if (isUnderKnaveLeaves(card)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isUnderKnaveLeaves(Card card) {
        if (card.getRank() == Rank.OVERKNAVE && card.getSuit() == Suit.LEAVES) {
            return true;
        }
        return false;
    }


    protected SpecialCardCase specialCardCaseCheck(boolean isNewUpcard, Card upcard, int opponentCardCount){

        if (upcard.getRank() == Rank.UNDERKNAVE && upcard.getSuit() == Suit.LEAVES) {
            return SpecialCardCase.UNDER_KNAVE_LEAVES_PLAYED;
        }

        if (upcard.getRank() == Rank.OVERKNAVE) {
            return SpecialCardCase.OVER_KNAVE_PLAYED;
        }

        if (!isNewUpcard) {
            return SpecialCardCase.NO_SPECIAL_CARD_CASE;
        }

        if (opponentCardCount == 0){
            return SpecialCardCase.OPPONENT_HAS_NO_CARDS;
        }

        if (upcard.getRank() == Rank.SEVEN && upcard.getSuit() == Suit.HEARTS && cardsCount == 0) {
            return SpecialCardCase.RETURN_TO_GAME;
        }

        if (upcard.getRank() == Rank.SEVEN) {
            return SpecialCardCase.SEVENS_PLAYED;
        }

        return SpecialCardCase.NO_SPECIAL_CARD_CASE;
    }

    protected boolean isMyTurnAfterAces(int myAcesCount, int oppAcesCount) {
        if ((myAcesCount + oppAcesCount)% 2 == 1) {
            return false;
        }
        return true;
    }
    */

    /**
     * Gets the cards that the player has in hand.
     * @return the cards in player's hand
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Sets the cards to held in the player's hand.
     * @param cards to be held in the player's hand
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
        this.cardsCount = cards.size();
    }

    /**
     * Gets the number of cards in the player's hand.
     * @return the number of cards in the player's hand
     */
    public int getCardsCount() {
        return cardsCount;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
