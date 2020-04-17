package com.goldasil.pjv.Game;

import com.goldasil.pjv.dto.MoveStateDTO;
import com.goldasil.pjv.enums.MoveState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveStateHandler {

    /*MoveState state;
    List<MoveState> posFollowUpStates;
    /*public MoveStateHandler(MoveState state, List<MoveState> possibleFollowUpStates) {
        this.state = state;
        this.possibleFollowUpStates = possibleFollowUpStates;
    }*/

    private static Map<MoveState, List<MoveState>> neighbours = new HashMap<MoveState, List<MoveState>>();

    public void initNeighbours() {
        ArrayList<MoveStateDTO> statesObj = null;
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("test.txt"));
            Type founderListType = new TypeToken<ArrayList<MoveStateDTO>>(){}.getType();
            statesObj = gson.fromJson(reader, founderListType);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(statesObj.toString());

        for (MoveStateDTO stateDTO: statesObj) {
            neighbours.put(stateDTO.getState(), stateDTO.getNeighbours());
        }
    }

    public static boolean isValidTransition(List<MoveState> previousStates, MoveState desiredState) {
        for (MoveState state : previousStates) {
            if (! isBetweenNeighbours(state, desiredState)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBetweenNeighbours(MoveState previousState, MoveState desiredState) {
        List<MoveState> prev_neighbours = neighbours.get(previousState);
        for (MoveState state : prev_neighbours) {
            if (state == desiredState) {
                return true;
            }
        }
        return false;
    }

}
