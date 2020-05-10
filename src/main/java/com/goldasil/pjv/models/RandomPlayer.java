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
        logger.debug("prevstates based on the arg MoveDTO prevMove: {}", prevStates);
        List<Move> posssibleMoves = getPossibleMoves(prevMove, prevStates);
        logger.debug("list of pos moves: {}", posssibleMoves);
        return getBestPosssibleMove(posssibleMoves, prevMove);
    }



    private List<Move> getPossibleMoves(MoveDTO prevMove, List<MoveState> prevStates) {
        List<Move> possibleMoves = new ArrayList<>();

        //priority states control
        for (MoveState prevState : prevStates) {
            switch(prevState) {
                case LOOKING_FOR_SEVEN_HEARTS_RETURN:
                    Card sevenHearts = new Card(Rank.SEVEN, Suit.HEARTS);
                    if (isCardInHand(sevenHearts)) {
                        ArrayList<Card> moveCardList = getCardsOnHandOfRank(sevenHearts);
                        possibleMoves.add(new Move(moveCardList));
                        return possibleMoves;
                    }
                    break;
                case AM_I_WITHOUT_CARDS:
                    if (stateHandler.isStateInList(MoveState.SEVEN_HEARTS_RETURN_PLAYED, prevStates)){
                        possibleMoves.add(new Move(prevMove.getPenaltyForSevens()));
                    }
                    else {
                        possibleMoves.add(new Move(MoveType.WIN));
                    }
                    return possibleMoves;
                default:
                    break;
            }
        }


        for (MoveState prevState : prevStates) {

            switch(prevState) {
                case SEVENS_PLAYED:
                    if (isCardOfRankInHand(Rank.SEVEN)) {
                        ArrayList<Card> moveCards = getCardsOnHandOfRank(Rank.SEVEN);
                        possibleMoves.add(new Move(moveCards));
                    }
                    else if (isUnderKnaveLeavesInHand()) {
                        ArrayList<Card> moveCardList = new ArrayList<>();
                        moveCardList.add(new Card(Rank.UNDERKNAVE, Suit.LEAVES));
                        possibleMoves.add(new Move(moveCardList));
                    }
                    else {
                        possibleMoves.add(new Move(prevMove.getPenaltyForSevens()));
                    }
                    break;
                case ACES_ONLY:
                    if (isCardOfRankInHand(Rank.ACE)) {
                        ArrayList<Card> moveCards = getCardsOnHandOfRank(Rank.ACE);
                        possibleMoves.add(new Move(moveCards));
                        return possibleMoves;
                    }
                    possibleMoves.add(new Move(MoveType.PASS));
                    return possibleMoves;
                case UNDERKNAVE_LEAVES_PLAYED:
                    ArrayList<ArrayList<Card>> posMovesForUnderknave = new ArrayList<ArrayList<Card>>();
                    for (Card cardInHand : cards) {
                        ArrayList<Card> posMoveCards = getCardsOnHandOfRank(cardInHand);
                        posMovesForUnderknave.add(posMoveCards);
                    }
                    possibleMoves.add(new Move(getLargestArrayList(posMovesForUnderknave)));
                    break;
                case NONSPECIAL_SITUATION:
                    Card upcard = prevMove.getUpcard();
                    possibleMoves.add(chooseMoveFromHand(new Card(upcard.getRank(), upcard.getSuit())));
                    break;
                case OVERKNAVE_HEARTS:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.HEARTS)));
                    break;
                case OVERKNAVE_LEAVES:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.LEAVES)));
                    break;
                case OVERKNAVE_ACORNS:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.ACORNS)));
                    break;
                case OVERKNAVE_BELLS:
                    possibleMoves.add(chooseMoveFromHand(new Card(Rank.UNSPECIFIED, Suit.BELLS)));
                    break;
            }
        }
        return possibleMoves;
    }

    private Move getBestPosssibleMove(List<Move> posMoves, MoveDTO prevMove){
        if (posMoves.size() == 1) {
            return posMoves.get(0);
        }
        /*if (prevMove.wasAnyoneWithoutCards()) {
            for (Move move : posMoves) {
                if (move.getMove().get(0).isSevenHearts()){
                    return move;
                }
            }
        }*/
        for (Move move : posMoves) {
            if (move.getMoveType() == MoveType.PLAY){
                return move;
            }
        }
        return posMoves.get(0);
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
        if (card.getRank() == Rank.UNDERKNAVE && card.getSuit() == Suit.LEAVES) {
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

    private boolean isValidCard(Card upcard, Card moveCard){
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
        ArrayList<Card> cardsMove = new ArrayList<Card>();
        for (Card card : cards) {
            if (card.getRank() == rank) {
                cardsMove.add(card);
            }
        }
        return cardsMove;
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

   /*public boolean isValidMoveAccCards(Card upcard, ArrayList<Card> move){
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
    }*/

}
