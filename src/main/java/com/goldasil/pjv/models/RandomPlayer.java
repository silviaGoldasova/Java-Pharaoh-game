package com.goldasil.pjv.models;

import com.goldasil.pjv.MoveStateHandler;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.*;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Random;

/**
 * Represents a random player for the mode of the gameControllers Player Vs Computer
 * The random player can generate possible moves, evaluate them to some extend, but chooses randomly from the pool of possible moves narrowed down to the best ones.
 * Eg., the random player prefers moves when he plays the most cards.
 *
 * TO DO - rewrite the class with the MoveStates instead of SpecialCaseMove
 */
public class RandomPlayer extends Player {

    private static final Logger logger = LoggerFactory.getLogger(RandomPlayer.class);

    static final int CARDS_TO_DRAW_PER_SEVEN = 3;
    static final int NUM_OF_SUITS = 4;
    MoveStateHandler stateHandler;

    /**
     * Generates a player with the specified ID and playing cards in hand.
     * @param playerID ID number of the player
     */
    public RandomPlayer(int playerID) {
        super(playerID);
        stateHandler = new MoveStateHandler();
    }

    public Move getMove(MoveDTO prevMove){
        //SpecialCardCase specialCase = specialCardCaseCheck(oppMove.isNewUpcard(), oppMove.getUpcard(), 1);
        List<MoveState> prevStates = MoveStateHandler.getMoveStatesPrev(prevMove);
        List<Move> posssibleMoves = getPossibleMoves(prevMove, prevStates);
        return posssibleMoves.get(0);
    }

    private List<Move> getPossibleMoves(MoveDTO prevMove, List<MoveState> prevStates) {
        List<Move> possibleMoves = new ArrayList<>();
        for (MoveState prevState : prevStates) {

            switch(prevState) {
                case LOOKING_FOR_SEVEN_HEARTS_RETURN:
                    Card sevenHearts = new Card(Rank.SEVEN, Suit.HEARTS);
                    if (isCardInHand(sevenHearts)) {
                        ArrayList<Card> moveCardList = new ArrayList<>();
                        moveCardList.add(sevenHearts);
                        possibleMoves.add(new Move(moveCardList));
                        return possibleMoves;
                    }
                    break;
                case SEVENS_PLAYED:
                    if (isCardOfRankInHand(Rank.SEVEN)) {
                        ArrayList<Card> moveCards = getCardsOnHandOfRank(Rank.SEVEN);
                        possibleMoves.add(new Move(moveCards));
                        return possibleMoves;
                    }
                    if (isUnderKnaveLeavesInHand()) {
                        ArrayList<Card> moveCardList = new ArrayList<>();
                        moveCardList.add(new Card(Rank.UNDERKNAVE, Suit.LEAVES));
                        possibleMoves.add(new Move(moveCardList));
                        return possibleMoves;
                    }
                    possibleMoves.add(new Move(3));
                    return possibleMoves;
                case ACES_ONLY:
                    if (isCardOfRankInHand(Rank.ACE)) {
                        ArrayList<Card> moveCards = getCardsOnHandOfRank(Rank.ACE);
                        possibleMoves.add(new Move(moveCards));
                        return possibleMoves;
                    }
                    possibleMoves.add(new Move(MoveType.PASS));
                    return possibleMoves;
                case UNDERKNAVE_LEAVES_PLAYED:
                    break;
                case NONSPECIAL_SITUATION:
                    Card upcard = prevMove.getUpcard();
                    possibleMoves.add(chooseMoveFromHand(new Card(upcard.getRank(), upcard.getSuit())));
                    return possibleMoves;
                case OVERKNAVE_HEARTS:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.HEARTS)));
                    return possibleMoves;
                case OVERKNAVE_LEAVES:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.LEAVES)));
                    return possibleMoves;
                case OVERKNAVE_ACORNS:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.ACORNS)));
                    return possibleMoves;
                case OVERKNAVE_BELLS:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.BELLSS)));
                    return possibleMoves;
            }
        }
        return possibleMoves;
    }

    private boolean isCardInHand(Card seekedCard) {
        for (Card card : cards) {
            if (card.getRank() == seekedCard.getRank() && card.getSuit() == seekedCard.getSuit()) {
                return true;
            }
        }
        return false;
    }

    private boolean isCardOfRankInHand(Rank seekedRank) {
        for (Card card : cards) {
            if (card.getRank() == seekedRank) {
                return true;
            }
        }
        return false;
    }

    private boolean isUnderKnaveLeavesInHand(){
        for (Card card : cards) {
            if (isUnderKnaveLeaves(card)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUnderKnaveLeaves(Card card) {
        if (card.getRank() == Rank.OVERKNAVE && card.getSuit() == Suit.LEAVES) {
            return true;
        }
        return false;
    }

    private SpecialCardCase specialCardCaseCheck(boolean isNewUpcard, Card upcard, int opponentCardCount){

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

    private boolean isMyTurnAfterAces(int myAcesCount, int oppAcesCount) {
        if ((myAcesCount + oppAcesCount)% 2 == 1) {
            return false;
        }
        return true;
    }

    /**
     * Generates a move just based on the same rank of the card on the top of the stock and rank of the cards in hand.
     * @param upcard the current card on the top of the stock
     * @return an option when most cards would be played if such exists
     */
    public Move chooseMoveFromHand(Card upcard) {
        ArrayList<ArrayList<Card>> possibleMoves = new ArrayList<ArrayList<Card>>();

        for (Card card : cards) {
            if (isValidCard(upcard, card)) {
                ArrayList<Card> posMove = getCardsOnHandOfRank(card);
                possibleMoves.add(posMove);
            }
        }

        int numOfPosMoves = possibleMoves.size();
        if (numOfPosMoves == 0) {
            return new Move(1);
        }
        else if (numOfPosMoves == 1) {
            return new Move(possibleMoves.get(0));
        }
        else {
            return new Move(getLargestArrayList(possibleMoves));
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
            if (card.getRank() != Rank.OVERKNAVE && !card.isUnderKnaveLeaves()) {
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
        ArrayList<Card> moveCards = new ArrayList<>();
        moveCards .add(origCard);
        Rank origCardRank = origCard.getRank();
        Suit origCardSuit = origCard.getSuit();
        for (Card card : cards) {
            if (card.getRank() == origCardRank && card.getSuit() != origCardSuit)
                moveCards.add(card);
        }
        return moveCards ;
    }

    private ArrayList<Card> getCardsOnHandOfRank(Rank rank) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (Card card : cards) {
            if (card.getRank() == rank)
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

    public boolean compareMoves(Move moveA, Move moveB) {
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
        return false;
    }

    /*private Move chooseSpecialCaseMove(SpecialCardCase specialCase, MoveDTO oppMove) {
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
                        return new Move(MoveType.PLAY, aces, 0);
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
                return null;

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
    }*/

}
