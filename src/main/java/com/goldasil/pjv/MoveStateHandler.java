package com.goldasil.pjv;

import com.goldasil.pjv.models.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.dto.MoveStateDTO;
import com.goldasil.pjv.enums.MoveState;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Rank;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MoveStateHandler {

    /*MoveState state;
    List<MoveState> posFollowUpStates;*/

    private static final Logger logger = LoggerFactory.getLogger(MoveStateHandler.class);
    private static Map<MoveState, List<MoveState>> neighbours = new HashMap<MoveState, List<MoveState>>();
    private static Collection<MoveState> allSuitMoveStates;

    public MoveStateHandler() {
        initNeighbours();
        allSuitMoveStates = new ArrayList<>();
        allSuitMoveStates.add(MoveState.HEARTS_PLAYED);
        allSuitMoveStates.add(MoveState.LEAVES_PLAYED);
        allSuitMoveStates.add(MoveState.ACORNS_PLAYED);
        allSuitMoveStates.add(MoveState.BELLS_PLAYED);
    }

    public void initNeighbours() {
        ArrayList<MoveStateDTO> statesObj = null;
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("moveStates/testUpdated.txt"));
            Type founderListType = new TypeToken<ArrayList<MoveStateDTO>>(){}.getType();
            statesObj = gson.fromJson(reader, founderListType);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //logger.info(statesObj.toString());

        for (MoveStateDTO stateDTO: statesObj) {
            if (stateDTO.getState() != null) {
                neighbours.put(stateDTO.getState(), stateDTO.getNeighbours());
            }
        }
        //logger.debug("Initialization of neighbours successful.");
    }

    public boolean isValidMove(MoveDTO prevMove, MoveDTO desiredMove) {
        List<MoveState> previousStates = getMoveStatesPrev(prevMove);
        List<MoveState> desiredStates = getMoveStatesDesired(desiredMove);

        if (isValidTransition(previousStates, desiredStates) && Card.arrAllCardsSameRank(desiredMove.getMove()) && checkSevensPenalty(prevMove, desiredMove) ) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the transition from the state after the previous move to the state that would be caused by the desired move is valid.
     * That means, whether each of the states caused by the desired move are possible to be performed after each one of the current states caused by the last performed move.
     * @param previousStates list of all states left after the previous move
     * @param desiredStates list of all states that would be caused by the desired move
     * @return true is the move is valid to be performed
     */
    public static boolean isValidTransition(List<MoveState> previousStates, List<MoveState> desiredStates) {
        logger.debug("going from {} to {}", previousStates.toString(), desiredStates.toString());

        for (MoveState previousMoveState : previousStates) {
            if (previousMoveState.getPriority() == 2) {
                for (MoveState desiredMoveState : desiredStates) {
                    if (isBetweenNeighbours(previousMoveState, desiredMoveState)) {
                        logger.info("Special high priority transition from {} to {} -> valid transition.\n", previousStates, desiredStates);
                        return true;
                    }
                }
            }
        }

        if (shouldCheckRankORSuit(desiredStates)) {
            boolean returnValue = false;
            for (MoveState previousMoveState : previousStates) {
                if (previousMoveState.getPriority() == 1) {
                    for (MoveState desiredMoveState : desiredStates) {
                        if (isBetweenNeighbours(previousMoveState, desiredMoveState)) {
                            //logger.info("Same rank or suit from {} to {}.\n", previousStates, desiredStates);
                            returnValue = true;
                        }
                    }
                }
            }
            if (!returnValue) {
                logger.info("Not the same rank or suit from {} to {}.\n", previousStates, desiredStates);
                return false;
            }
        }

        for (MoveState previousMoveState : previousStates) {
            if (previousMoveState.getPriority() == 0) {
                for (MoveState desiredMoveState : desiredStates) {
                    if (desiredMoveState.getPriority() == 0 && !isBetweenNeighbours(previousMoveState, desiredMoveState)) {
                        logger.info("Not a valid trasition from {} to {}.\n", previousStates, desiredStates);
                        logger.info("Not a valid trasition from {} to {}.\n", previousMoveState, desiredMoveState);
                        return false;
                    }
                }
            }
        }
        logger.info("Valid trasition from {} to {}.\n", previousStates, desiredStates);
        return true;

    }

    /**
     * Checks whether a (desired) state A is among the neighbours of the (previous) state B,
     * that is, whether we can move from state B to the state A.
     * @param previousState previous, the source, state
     * @param desiredState the desired state
     * @return
     */
    private static boolean isBetweenNeighbours(MoveState previousState, MoveState desiredState) {
        List<MoveState> prev_neighbours = neighbours.get(previousState);
        for (MoveState state : prev_neighbours) {
            if (state == desiredState) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the list of all states activated by the previous moves.
     * @param moveDTO data about the previous moves
     * @return list of all states currently active
     */
    public static List<MoveState> getMoveStatesPrev(MoveDTO moveDTO) {
        List<MoveState> states = new LinkedList<MoveState>();

        if (moveDTO.getUpcard().isUnderKnaveLeaves()) {
            states.add(MoveState.UNDERKNAVE_LEAVES_PLAYED);
            states.add(MoveState.UNDERKNAVE_PLAYED);
            states.addAll(allSuitMoveStates);
        }

        if (moveDTO.getUpcard().getRank() == Rank.OVERKNAVE) {
            states.add(moveDTO.getMoveStateForSuit(moveDTO.getRequestedSuit()));
            states.add(MoveState.OVERKNAVE_PLAYED);
            states.add(moveDTO.getMoveStateForOverknave(moveDTO.getRequestedSuit()));
        }

        if (moveDTO.wasNonspecialMove(getUpperCard(moveDTO))) {
            states.add(MoveState.NONSPECIAL_SITUATION);
        }

        if (moveDTO.wasSevenHeartsPlayed()) {
            states.add(MoveState.SEVEN_HEARTS_RETURN_PLAYED);
        }

        if (moveDTO.wasAnyoneWithoutCards()) {
            states.add(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN);
        }

        if (moveDTO.wasSevenPlayed()) {
            states.add(MoveState.SEVENS_PLAYED);
        }

        if (moveDTO.isAcesOnlyTurn()) {
            states.add(MoveState.ACES_ONLY);
        }

        if (!(isStateInList(MoveState.UNDERKNAVE_LEAVES_PLAYED, states) || getUpperCard(moveDTO).getRank() == Rank.OVERKNAVE)) {
            states.add(moveDTO.getMoveStateForSuit(getUpperCard(moveDTO).getSuit()));
            states.add(moveDTO.getMoveStateFromRank(getUpperCard(moveDTO).getRank()));
        }

        return states;
    }

    private static Card getUpperCard(MoveDTO moveDTO) {
        if(moveDTO.getMoveType() == MoveType.PLAY) {
            return moveDTO.getMove().get(moveDTO.getMove().size()-1);
        }
        return moveDTO.getUpcard();
    }

    /**
     * Gets the list of all states that would be activated a the desired move would be performed.
     * @param moveDTO the desired move
     * @return list of all states that would be active after having performed the desired move
     */
    public static List<MoveState> getMoveStatesDesired(MoveDTO moveDTO) {
        List<MoveState> states = new LinkedList<MoveState>();

        if (moveDTO.getMoveType() == MoveType.PLAY && moveDTO.getMove().get(0).isUnderKnaveLeaves()) {
            states.add(MoveState.UNDERKNAVE_LEAVES_PLAYED);
            states.add(MoveState.UNDERKNAVE_PLAYED);
            states.addAll(allSuitMoveStates);
        }

        if (moveDTO.getMoveType() == MoveType.PLAY && moveDTO.getMove().get(0).getRank() == Rank.OVERKNAVE) {
            states.add(MoveState.OVERKNAVE);
            states.addAll(allSuitMoveStates);
            states.add(MoveState.OVERKNAVE_PLAYED);
        }

        if (moveDTO.isNonspecialMove()) {
            states.add(MoveState.NONSPECIAL_SITUATION);
        }

        if (moveDTO.isSevenHeartsPlayed()) {
            states.add(MoveState.SEVEN_HEARTS_RETURN_PLAYED);
        }

        if (moveDTO.isSevenPlayed()) {
            states.add(MoveState.SEVENS_PLAYED);
        }

        if (moveDTO.isAcePlayed()) {
            states.add(MoveState.ACES_PLAYED);
        }

        if (moveDTO.getMoveType() == MoveType.PASS) {
            states.add(MoveState.PASS);
        }

        if (moveDTO.getMoveType() == MoveType.PLAY){
            states.add(moveDTO.getSuitMoveState());
        }

        if (moveDTO.getMoveType() == MoveType.DRAW && moveDTO.getDrawCards() == 1) {
            states.add(MoveState.DRAW);
        }

        if (moveDTO.getMoveType() == MoveType.DRAW && moveDTO.getDrawCards() > 1){
            states.add(MoveState.DRAW_PENALTY);
        }

        if (moveDTO.getMoveType() == MoveType.PLAY && !(isStateInList(MoveState.UNDERKNAVE_LEAVES_PLAYED, states) || moveDTO.getUpcard().getRank() == Rank.OVERKNAVE)) {
            states.add(moveDTO.getMoveStateForSuit(moveDTO.getMove().get(0).getSuit()));
            states.add(moveDTO.getMoveStateFromRank(moveDTO.getMove().get(0).getRank()));
        }

        return states;
    }


    public static boolean isStateInList(MoveState seekedState, List<MoveState> statesList) {
        for (MoveState state : statesList) {
            if (state == seekedState) {
                return true;
            }
        }
        return false;
    }

    private static boolean shouldCheckRankORSuit(List<MoveState> desiredStates)  {
        int priorityOnes = 0;

        for (MoveState state : desiredStates) {
            if (state.getPriority() == 1) {
                priorityOnes++;
            }
        }
        if (priorityOnes >= 2) {
            return true;
        }
        return false;
    }

    private boolean checkSevensPenalty(MoveDTO prevMoveDTO, MoveDTO desiredMoveDTO){
        if (prevMoveDTO.wasSevenPlayed() && desiredMoveDTO.getMoveType() == MoveType.DRAW) {
            if (prevMoveDTO.getPenaltyForSevens() == desiredMoveDTO.getDrawCards()) {
                return true;
            }
            logger.debug("Wrong penalty set.");
            return false;
        }
        return true;
    }


}
