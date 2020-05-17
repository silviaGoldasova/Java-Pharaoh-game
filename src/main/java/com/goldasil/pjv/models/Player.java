package com.goldasil.pjv.models;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.models.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Represends a general player in a card gameControllers.
 * The class contains a basic pack of functions necessary for handling cards a player has in the hand.
 */
public class Player {

    protected volatile ArrayList<Card> cards = new ArrayList<Card>();
    protected int cardsCount;
    protected int playerID;

    private static final Logger logger = LoggerFactory.getLogger(Player.class);


    public Player(int playerID) {
        this.playerID = playerID;
    }

    public Player(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Creates a player with the specified cards in hand and player ID number.
     * @param cards cards that the player has in hand
     * @param playerID ID number of the player
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

    public void addCards(Card card1, Card card2, Card card3) {
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
    }

    /**
     * Removes the cards passed in in an array from the cards in a player's hand.
     * @param move list of cards played in the move
     */
     public void removeCards(ArrayList<Card> move) {
        for (Card cardInMove : move) {
            for (Card cardInHand : new ArrayList<>(cards) ) {
                if (cardInHand.compareCards(cardInMove)) {
                    cards.remove(cardInHand);
                }
            }
        }
        logger.debug("rest of the cards: {}", cards);
        cardsCount = cards.size();

    }

    public Move chooseMove(MoveDTO prevMove) {
        return null;
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


    public ArrayList<Card> addCardsFromArgs(ArrayList<Card> list, Card card1, Card card2) {
        list.add(card1);
        list.add(card2);
        return list;
    }

    public static boolean compareMoves(Move moveA, Move moveB) {
        if (moveA.moveType != moveB.moveType || moveA.drawCards != moveB.drawCards) {
            return false;
        }

        if (moveA.moveType == MoveType.PLAY) {
            int matches = 0;
            for (Card cardFromBmove : moveB.move) {
                for (Card cardFromAmove : moveA.move) {
                    if (cardFromAmove.compareCards(cardFromBmove)) {
                        matches++;
                    }
                }
            }
            return matches == moveA.move.size();
        }
        return true;
    }

    public static boolean isMoveBetweenGeneratedMoves(List<Move> generatedPosMoves, Move move){
        for (Move posMove : generatedPosMoves) {
            if (compareMoves(move, posMove))
                return true;
        }
        return false;
    }

    // to delete
    public Move chooseMoveFromHand(Card upcard) {
       return null;
    }


}
