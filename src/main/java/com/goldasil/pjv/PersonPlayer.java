package com.goldasil.pjv;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.dto.OpponentMoveDTO;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.SpecialCardCase;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;


public class PersonPlayer extends Player{

    public boolean isValidMove(MoveDTO plannedMove, OpponentMoveDTO oppMove){

        SpecialCardCase specialCase = specialCardCaseCheck(oppMove.isNewUpcard(), oppMove.getUpcard(), oppMove.getOpponentCardCount());

        if (specialCase == SpecialCardCase.NO_SPECIAL_CARD_CASE) {
            if (plannedMove.getMoveType() == MoveType.PLAY) {
                if (!areValidCards(oppMove.getUpcard(), plannedMove.getMove())) {
                    return false;
                }
            }
        }
        else {
            if (!isSpecialCaseValid(specialCase, plannedMove, oppMove)){
                return false;
            }
            if (plannedMove.getMoveType() == MoveType.PLAY) {
                if (!isMoveSameRank(plannedMove.getMove())){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSpecialCaseValid(SpecialCardCase specialCase, MoveDTO plannedMove, OpponentMoveDTO oppMove) {

        switch(specialCase) {
            case OVER_KNAVE_PLAYED:
                if (plannedMove.getMoveType() == MoveType.PLAY) {
                    Card plannedCard = plannedMove.getMove().get(0);
                    if (oppMove.getRequestedSuit() == plannedCard.getSuit() || isOfRank(plannedCard, Rank.OVERKNAVE) || isUnderKnaveLeaves(plannedCard)) {
                        return true;
                    }
                    return false;
                } else {  // MoveType.DRAW
                    return true;
                }

            case ACES_PLAYED:

                if (plannedMove.getMoveType() == MoveType.PLAY) {
                    for (Card card : plannedMove.getMove()) {
                        if (!isOfRank(card, Rank.ACE)) {
                            return false;
                        }
                    }
                    if (isMyTurnAfterAces(plannedMove.getMove().size(), oppMove.getMove().size())) {
                        plannedMove.setMoveType(MoveType.DOUBLE_PLAY);
                    }
                } else {  // MoveType.PASS
                    if (isMyTurnAfterAces(0, oppMove.getMove().size())) {
                        plannedMove.setMoveType(MoveType.DOUBLE_PLAY);
                    }
                }
                return true;


            case SEVENS_PLAYED:

                if (plannedMove.getMoveType() == MoveType.PLAY) {
                    if (isUnderKnaveLeaves(plannedMove.getMove().get(0))) {
                        return true;
                    }
                    for (Card card : plannedMove.getMove()) {
                        if (!isOfRank(card, Rank.SEVEN)) {
                            return false;
                        }
                    }
                }
                if (plannedMove.getMoveType() == MoveType.DRAW) {
                    return true;
                }
                return false;

            case RETURN_TO_GAME:
                if (plannedMove.getMoveType() == MoveType.DRAW) {
                    return true;
                }
                return false;

            case OPPONENT_HAS_NO_CARDS:

                if (plannedMove.getMove().get(0).getRank() == Rank.SEVEN && plannedMove.getMove().get(0).getSuit() == Suit.HEARTS) {
                    return true;
                }
                if (plannedMove.getMoveType() == MoveType.DRAW) {
                    return true;
                }
                return false;

            case UNDER_KNAVE_LEAVES_PLAYED:
                return true;
        }
        return false;
    }

    private boolean isMoveSameRank(ArrayList<Card> move) {
        Rank orig_rank = move.get(0).getRank();
        for (int i = 1; i < move.size(); i++) {
            if (move.get(i).getRank() != orig_rank) {
                return false;
            }
        }
        return true;
    }

    private boolean isOfRank(Card card, Rank rank){
        if (card.getRank() == rank) {
            return true;
        }
        return false;
    }

}
