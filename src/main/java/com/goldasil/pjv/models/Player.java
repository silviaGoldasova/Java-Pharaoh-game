package com.goldasil.pjv.models;
import com.goldasil.pjv.models.Card;

import java.util.ArrayList;


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
