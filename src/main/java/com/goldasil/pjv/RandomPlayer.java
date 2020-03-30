package com.goldasil.pjv;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.SpecialCardCase;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;

public class RandomPlayer extends Player {

    ArrayList<Card> chooseMove(Card upcard, boolean isNewUpcard, int opponentCardCount, Suit newSuit, int numOfAcesPlayed){
        SpecialCardCase specialCase = specialCardCaseCheck(isNewUpcard, upcard, opponentCardCount);
        if (specialCase == SpecialCardCase.NO_SPECIAL_CARD_CASE) {
            return chooseMoveFromHand(upcard);
        }
        else {
            return chooseSpecialCaseMove(specialCase, upcard, isNewUpcard, opponentCardCount, newSuit,numOfAcesPlayed);
        }
    }

    private ArrayList<Card> chooseSpecialCaseMove(SpecialCardCase specialCase, Card upcard, boolean isNewUpcard, int opponentCardCount, Suit newSuit, int numOfAcesPlayed) {
        switch(specialCase) {
            case OVER_KNAVE_PLAYED:
                upcard.setRank(Rank.UNSPECIFIED);
                upcard.setSuit(newSuit);
                return chooseMoveFromHand(upcard);
            case ACES_PLAYED:
                ArrayList<Card> aces = getCardsOnHandOfRank(upcard);
                if (aces.size() == 0) {
                    return null;
                }
                else{
                    return aces;
                    /*if ((numOfAcesPlayed + aces.size())% 2 == 1) { return aces; }
                    else{ ArrayList<Card> additionalMove = chooseMove(aces.get(aces.size()-1), false, int opponentCardCount, Suit newSuit, int numOfAcesPlayed);*/
                }
            case SEVENS_PLAYED:
                Card cardSeven = new Card(Rank.SEVEN, Suit.UNSPECIFIED);
                ArrayList<Card> sevens = getCardsOnHandOfRank(cardSeven);
                if (sevens.size() != 0) {
                    return sevens;
                } else if (isUnderKnaveLeavesInHand()) {
                    ArrayList<Card> move = new ArrayList<Card>();
                    move.add(new Card(Rank.UNDERKNAVE, Suit.LEAVES));
                    return move;
                } else {

                }
            case RETURN_TO_GAME:
                break;
            case OPPONENT_HAS_NO_CARDS:
                break;
            case UNDER_KNAVE_LEAVES_PLAYED:
                break;
            default:
                return null;
        }
        return null;
    }

    private ArrayList<Card> chooseMoveFromHand(Card upcard) {
        ArrayList<ArrayList<Card>> possibleMoves = new ArrayList<ArrayList<Card>>();

        for (Card card : cards) {
            if (isValidCard(upcard, card)) {
                ArrayList<Card> posMove = getCardsOnHandOfRank(card);
                possibleMoves.add(posMove);
            }
        }

        int numOfPosMoves = possibleMoves.size();
        if (numOfPosMoves == 0) {
            return null;
        }
        else if (numOfPosMoves == 1) {
            return possibleMoves.get(0);
        }
        else {
            return getLargestArrayList(possibleMoves);
        }
    }

    private ArrayList<Card> getCardsOnHandOfRank(Card origCard) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(origCard);
        Rank origCardRank = origCard.getRank();
        Suit origCardSuit = origCard.getSuit();
        for (Card card : cards) {
            if (card.getRank() == origCardRank && card.getSuit() != origCardSuit)
                cards.add(card);
        }
        return cards;
    }

    private ArrayList<Card> getLargestArrayList(ArrayList<ArrayList<Card>> list) {
        int max_size = 0;
        int max_index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).size() > max_size) {
                max_size = list.get(i).size();
                max_index = i;
            }
        }
        return list.get(max_index);
    }

}
