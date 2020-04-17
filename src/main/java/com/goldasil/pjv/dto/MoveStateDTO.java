package com.goldasil.pjv.dto;

import com.goldasil.pjv.enums.MoveState;

import java.util.ArrayList;
import java.util.List;

public class MoveStateDTO {

    private MoveState state;
    private ArrayList<MoveState> neighbours;

    public MoveStateDTO(){
    }

    public MoveStateDTO(MoveState state, ArrayList<MoveState> neighbours) {
        this.state = state;
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return "MoveStateDTO{" +
                "state=" + state +
                ", neighbours=" + neighbours +
                '}';
    }

    public MoveState getState() {
        return state;
    }

    public void setState(MoveState state) {
        this.state = state;
    }

    public ArrayList<MoveState> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(ArrayList<MoveState> neighbours) {
        this.neighbours = neighbours;
    }
}
