package com.goldasil.pjv.dto;

import com.goldasil.pjv.enums.GameType;

/**
 * Represents a New Game DTO for carrying data
 */
public class NewGameDTO {

    private int numOfOpp = 1;
    private GameType gametype;


    public int getNumOfOpp() {
        return numOfOpp;
    }

    public void setNumOfOpp(int numOfOpp) {
        this.numOfOpp = numOfOpp;
    }

    public GameType getGametype() {
        return gametype;
    }

    public void setGametype(GameType gametype) {
        this.gametype = gametype;
    }
}
