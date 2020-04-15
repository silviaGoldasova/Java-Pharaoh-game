package com.goldasil.pjv;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.SpecialCardCase;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;
import java.util.List;

public class Player {

    protected ArrayList<Card> cards;
    protected int cardsCount;

    public void addCards(ArrayList<Card> move) {
        for (Card card : move) {
            cards.add(card);
        }
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCards(ArrayList<Card> move) {
        for (Card card : move) {
            cards.remove(card);
        }
    }

    private boolean isSameRank(Card card, Card otherCard){
        if (card.getRank() == otherCard.getRank()) {
            return true;
        }
        return false;
    }

    private boolean isSameSuit(Card card, Card otherCard){
        if (card.getSuit() == otherCard.getSuit()) {
            return true;
        }
        return false;
    }

    protected boolean areValidCards(Card upcard, ArrayList<Card> move){
        if (!(isSameRank(upcard, move.get(0)) || isSameSuit(upcard, move.get(0)) || move.get(0).getRank() == Rank.OVERKNAVE || isUnderKnaveLeaves(move.get(0)) )) {
            return false;
        }

        Rank rank = move.get(0).getRank();
        for (int i = 1; i < move.size(); i++) {
            if (move.get(i).getRank() != rank) {
                return false;
            }
        }
        return true;
    }

    protected boolean isValidCard(Card upcard, Card move){
        if (isSameRank(upcard, move) || isSameSuit(upcard, move) || move.getRank() == Rank.OVERKNAVE || isUnderKnaveLeaves(move)) {
            return true;
        }
        return false;
    }

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

    protected boolean hasTheCard(Card card){
        for (Card handCard : cards){
            if (handCard.getRank() == card.getRank() && handCard.getSuit() == card.getSuit()) {
                return true;
            }
        }
        return false;
    }

    protected boolean compareItems(Card cardA, Card cardB) {
        if (cardA.getRank() == cardB.getRank() && cardA.getSuit() == cardB.getSuit()) {
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

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public int getCardsCount() {
        return cardsCount;
    }

    public void setCardsCount(int cardsCount) {
        this.cardsCount = cardsCount;
    }


}
