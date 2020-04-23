package com.goldasil.pjv.dto;
import com.goldasil.pjv.enums.MoveState;
import java.util.ArrayList;

/**
 * A helper class for getting data about the move states from JSON file.
 */
public class MoveStateDTO {

    private MoveState state;
    private ArrayList<MoveState> neighbours;

    public MoveStateDTO(){
    }

    public MoveStateDTO(MoveState state, ArrayList<MoveState> neighbours) {
        this.state = state;
        this.neighbours = neighbours;
    }

    /**
     * Gets the fields moveStateDTO object into a String form.
     * @return a String with moveStateDTO object fields
     */
    @Override
    public String toString() {
        return "MoveStateDTO{" +
                "state=" + state +
                ", neighbours=" + neighbours +
                '}';
    }

    /**
     * Gets the state of the object.
     * @return MoveState state
     */
    public MoveState getState() {
        return state;
    }

    /**
     * Sets the state of the MoveState object
     * @param state MoveState state
     */
    public void setState(MoveState state) {
        this.state = state;
    }

    /**
     * Gets a list of the states that can follow the state
     * @return a list of MoveState states
     */
    public ArrayList<MoveState> getNeighbours() {
        return neighbours;
    }

    /**
     * Sets a list of the states that can follow the state
     * @param neighbours a list of the states that can follow
     */
    public void setNeighbours(ArrayList<MoveState> neighbours) {
        this.neighbours = neighbours;
    }
}
