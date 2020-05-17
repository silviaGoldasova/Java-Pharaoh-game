package com.goldasil.pjv.models;

import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * Represents a card from the German playing cards pack.
 * Contains a basic set of function for operations on cards.
 */
public class Card {

    private Rank rank;
    private Suit suit;

    /**
     * Creates a new card with UNSPECIFIED rank and UNSPECIFIED suit.
     */
    public Card(){
        this.rank = Rank.UNSPECIFIED;
        this.suit = Suit.UNSPECIFIED;
    }

    /**
     * Creates a card with the specified rank and suit.
     * @param rank the rank to be set for the card
     * @param suit the suit to be set for the card
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        //return "Card obj: rank: " + rank + ", suit: " + suit;
        return "Card: " + StringUtils.capitalize(rank.toString().toLowerCase()) + ", " + StringUtils.capitalize(suit.toString().toLowerCase());
    }

    public String toStringForGUI(){
        return StringUtils.capitalize(rank.toString().toLowerCase()) + " " + StringUtils.capitalize(suit.toString().toLowerCase());
    }

    /**
     * Compares the rank value  of 2 cards - the card on which the method is called and the card passed as a parameter.
     * Possible values for the rank are: SEVEN/EIGHT/NINE/TEN/UNDERKNAVE/OVERKNAVE/KING/ACE/UNSPECIFIED.
     * Returns true if the rank is the same.
     * @param otherCard card whose rank is to be compared
     * @return true if the cards have the same rank other than UNSPECIFIED
     */
    public boolean isSameRank(Card otherCard){
        if (this.getRank() == Rank.UNSPECIFIED) {
            return false;
        }
        return (this.getRank() == otherCard.getRank());
    }

    /**
     * Compares the suit values of 2 cards - the card on which the method is called and the card passed as a parameter.
     * Possible values for the suit are: HEARTS, BELLSS, ACORNS, LEAVES, UNSPECIFIED.
     * @param otherCard card whose suit is to be compared
     * @return true if the 2 compared cards have the same suit other than UNSPECIFIED
     */
    public boolean isSameSuit(Card otherCard){
        if (this.getSuit() == Suit.UNSPECIFIED) {
            return false;
        }
        return (this.getSuit() == otherCard.getSuit());
    }

    /**
     * Compares 2 cards whether they are of the same rank and same suit.
     * @param otherCard a card to be compared
     * @return true it the cards have the same rank
     */
    public boolean compareCards(Card otherCard) {
        if (otherCard.getRank() == this.getRank() && otherCard.getSuit() == this.getSuit()) {
            return true;
        }
        return false;
    }

    public static boolean arrAllCardsSameRank(Rank rank, ArrayList<Card> cardList){
        if (cardList == null) {
            return true;
        }
        for (Card card : cardList) {
            if (card.getRank() != rank) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the card is of rank UNDERKNAVE and suit LEAVES
     * @return true if the card is of rank UNDERKNAVE and of suit LEAVES
     */
    public boolean isUnderKnaveLeaves() {
        if (this.getRank() == Rank.OVERKNAVE && this.getSuit() == Suit.LEAVES) {
            return true;
        }
        return false;
    }

    public boolean isSevenHearts() {
        if (this.getRank() == Rank.SEVEN && this.getSuit() == Suit.HEARTS) {
            return true;
        }
        return false;
    }

    /**
     * Gets the suit of the card.
     * @return suit of the card
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Sets the suit of the card.
     * @param suit suit to be set to the card
     */
    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    /**
     * Gets the rank of a card.
     * @return the rank of the card
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Sets the rank of the card.
     * @param rank rank to be set as the rank of the card
     */
    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
