package com.goldasil.pjv;

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

    private static Map<MoveState, List<MoveState>> neighbours = new HashMap<MoveState, List<MoveState>>();

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
        System.out.println(statesObj.toString());

        for (MoveStateDTO stateDTO: statesObj) {
            if (stateDTO.getState() != null) {
                neighbours.put(stateDTO.getState(), stateDTO.getNeighbours());
            }
        }
    }

    /**
     * Checks whether the transition from the state after the previous move to the state that would be caused by the desired move is valid.
     * That means, whether each of the states caused by the desired move are possible to be performed after each one of the current states caused by the last performed move.
     * @param previousStates list of all states left after the previous move
     * @param desiredStates list of all states that would be caused by the desired move
     * @return true is the move is valid to be performed
     */
    public static boolean isValidTransition(List<MoveState> previousStates, List<MoveState> desiredStates) {
        for (MoveState desiredMoveState : desiredStates) {
            for (MoveState previousState : previousStates) {
                if (!isBetweenNeighbours(previousState, desiredMoveState)) {
                    return false;
                }
            }
        }
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
    public List<MoveState> getMoveStatesPrev(MoveDTO moveDTO) {
        List<MoveState> states = new LinkedList<MoveState>();

        if (moveDTO.getUpcard().isUnderKnaveLeaves()) {
            states.add(MoveState.UNDERKNAVE_LEAVES_PLAYED);
        }

        if (moveDTO.getUpcard().getRank() == Rank.OVERKNAVE) {
            states.add(moveDTO.getOverknaveState());
        }

        if (moveDTO.wasNonspecialMove()) {
            states.add(MoveState.NONSPECIAL_SITUATION);
        }

        if (moveDTO.wasSevenHeartsPlayed()) {
            states.add(MoveState.SEVEN_HEARTS_RETURN_PLAYED);
        }

        if (moveDTO.wasWithoutCards()) {
            states.add(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN);
        }

        if (moveDTO.wasSevenPlayed()) {
            states.add(MoveState.SEVENS_PLAYED);
        }

        if (moveDTO.isAcesOnlyTurn()) {
            states.add(MoveState.ACES_ONLY);
        }

        return states;
    }

    /**
     * Gets the list of all states that would be activated a the desired move would be performed.
     * @param moveDTO the desired move
     * @return list of all states that would be active after having performed the desired move
     */
    public List<MoveState> getMoveStatesDesired(MoveDTO moveDTO) {
        List<MoveState> states = new LinkedList<MoveState>();

        if (moveDTO.getMoveType() == MoveType.PLAY && moveDTO.getMove().get(0).isUnderKnaveLeaves()) {
            states.add(MoveState.UNDERKNAVE_LEAVES_PLAYED);
        }

        if (moveDTO.getMoveType() == MoveType.PLAY && moveDTO.getMove().get(0).getRank() == Rank.OVERKNAVE) {
            states.add(MoveState.OVERKNAVE);
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

        if (moveDTO.getMoveType() == MoveType.DRAW) {
            states.add(MoveState.DRAW);
        }

        return states;
    }


}
