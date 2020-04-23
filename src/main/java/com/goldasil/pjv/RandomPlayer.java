package com.goldasil.pjv;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.SpecialCardCase;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a random player for the mode of the gameControllers Player Vs Computer
 * The random player can generate possible moves, evaluate them to some extend, but chooses randomly from the pool of possible moves narrowed down to the best ones.
 * Eg., the random player prefers moves when he plays the most cards.
 */
public class RandomPlayer extends Player {

    static final int CARDS_TO_DRAW_PER_SEVEN = 3;
    static final int NUM_OF_SUITS = 4;

    /**
     * Generates a player with the specified ID and playing cards in hand.
     * @param playerID
     */
    public RandomPlayer(int playerID) {
        super(playerID);
    }

    public Move chooseMove(MoveDTO oppMove){
        SpecialCardCase specialCase = specialCardCaseCheck(oppMove.isNewUpcard(), oppMove.getUpcard(), oppMove.getOpponentCardCount());
        if (specialCase == SpecialCardCase.NO_SPECIAL_CARD_CASE) {
            Move move = chooseMoveFromHand(oppMove.getUpcard());
            checkOverKnaveGetSuit(move.getMove());
            return move;
        }
        else {
            return chooseSpecialCaseMove(specialCase, oppMove);
        }
    }

    private Move chooseSpecialCaseMove(SpecialCardCase specialCase, MoveDTO oppMove) {
        switch(specialCase) {
            case OVER_KNAVE_PLAYED:
                return chooseMoveFromHand(new Card(Rank.UNSPECIFIED, oppMove.getRequestedSuit()));

            case ACES_PLAYED:
                ArrayList<Card> aces = getCardsOnHandOfRank(oppMove.getUpcard());
                if (aces.size() == 0) {
                    if (isMyTurnAfterAces(0, oppMove.getMove().size())) {
                        return chooseMoveFromHand(oppMove.getUpcard());
                    }
                    else {
                        return new Move(MoveType.PASS, null, 0);
                    }
                }
                else {
                    if (isMyTurnAfterAces(aces.size(), oppMove.getMove().size())) {
                        return new Move(MoveType.DOUBLE_PLAY, aces, 0);
                    }
                    else {
                        return new Move(MoveType.PLAY, aces, 0);
                    }
                }

            case SEVENS_PLAYED:
                Card cardSeven = new Card(Rank.SEVEN, Suit.UNSPECIFIED);
                ArrayList<Card> sevens = getCardsOnHandOfRank(cardSeven);
                if (sevens.size() != 0) {
                    Move move = new Move(MoveType.PLAY, sevens, 0);
                    return move;
                } else if (isUnderKnaveLeavesInHand()) {
                    ArrayList<Card> move = getCardsOnHandOfRank(new Card(Rank.UNDERKNAVE, Suit.LEAVES));
                    return new Move(MoveType.PLAY, move, 0);
                } else {
                    return new Move(MoveType.DRAW, null, CARDS_TO_DRAW_PER_SEVEN*oppMove.getMove().size());
                }

            case RETURN_TO_GAME:
                return (new Move(MoveType.DRAW, null, CARDS_TO_DRAW_PER_SEVEN * oppMove.getMove().size()));

            case OPPONENT_HAS_NO_CARDS:
                Card sevenHearts = new Card(Rank.SEVEN, Suit.HEARTS);
                if (hasTheCard(sevenHearts)) {
                    ArrayList<Card> move = getCardsOnHandOfRank(sevenHearts);
                    return new Move(MoveType.PLAY, move, 0);
                }
                return new Move(MoveType.LOSE, null, 0);

            case UNDER_KNAVE_LEAVES_PLAYED:
                ArrayList<ArrayList<Card>> possibleMoves = new ArrayList<ArrayList<Card>>();
                for (Card card : cards) {
                    ArrayList<Card> posMove = getCardsOnHandOfRank(card);
                    possibleMoves.add(posMove);
                }
                if (possibleMoves.size() == 1) {
                    return new Move(MoveType.PLAY, possibleMoves.get(0), 0);
                }
                else {
                    return new Move(MoveType.PLAY, getLargestArrayList(possibleMoves), 0);
                }
        }
        return null;
    }

    private Move chooseMoveFromHand(Card upcard) {
        ArrayList<ArrayList<Card>> possibleMoves = new ArrayList<ArrayList<Card>>();

        for (Card card : cards) {
            if (isValidCard(upcard, card)) {
                ArrayList<Card> posMove = getCardsOnHandOfRank(card);
                possibleMoves.add(posMove);
            }
        }

        int numOfPosMoves = possibleMoves.size();
        if (numOfPosMoves == 0) {
            return new Move(MoveType.DRAW, null, 1);
        }
        else if (numOfPosMoves == 1) {
            return new Move(MoveType.PLAY, possibleMoves.get(0), 0);
        }
        else {
            return new Move(MoveType.PLAY, getLargestArrayList(possibleMoves), 0);
        }
    }

    public boolean isValidMoveAccCards(Card upcard, ArrayList<Card> move){
        if (!(upcard.isSameRank(move.get(0)) || upcard.isSameSuit(move.get(0)) || move.get(0).getRank() == Rank.OVERKNAVE || move.get(0).isUnderKnaveLeaves() )) {
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

    public boolean isValidCard(Card upcard, Card moveCard){
        if (upcard.isSameRank(moveCard) || upcard.isSameSuit(moveCard) || moveCard.getRank() == Rank.OVERKNAVE || moveCard.isUnderKnaveLeaves()) {
            return true;
        }
        return false;
    }


    private Suit checkOverKnaveGetSuit(ArrayList<Card> move){
        if (move.get(0).getRank() == Rank.OVERKNAVE) {
            return getSuitForKnave();
        }
        return Suit.UNSPECIFIED;
    }

    private Suit getSuitForKnave() {
        ArrayList<Suit> suits = new ArrayList<Suit>();
        for (Card card : cards) {
            if (card.getRank() != Rank.OVERKNAVE) {
                suits.add(card.getSuit());
            }
        }
        Random random = new Random();
        if (suits.size() == 0) {
            int randomNumber = random.nextInt(NUM_OF_SUITS);
            return Suit.values()[randomNumber];
        }
        int randomNumber = random.nextInt(suits.size());
        return suits.get(randomNumber);
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
