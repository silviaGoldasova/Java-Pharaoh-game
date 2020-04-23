package com.goldasil.pjv.dto;

import java.util.List;

/**
 * Helper Data transfer Object class for propagating information from view through controller to the model.
 */
public class GameSettingsDTO {

    String gameType;
    String gameMode;
    int numOfPlayers;
    List<String> IPadresses;


    public GameSettingsDTO() {
    }

    /**
     * Gets the type of the game - new game or loaded game.
     * @return the type of the game
     */
    public String getGameType() {
        return gameType;
    }

    /**
     * Sets the game type - new game or loaded game.
     * @param gameType
     */
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    /**
     * Gets game mode - vsPerson or vsComputer
     * @return
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * Sets the game mode - vsPerson or vsComputer
     * @param gameMode
     */
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Gets the number of players
     * @return
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Sets the number of players
     * @param numOfPlayers
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Gets a list of IP addresses of players
     * @returna list of IP addresses as Strings
     */
    public List<String> getIPadresses() {
        return IPadresses;
    }

    /**
     * Sets a list of IP addresses of players
     * @param IPadresses
     */
    public void setIPadresses(List<String> IPadresses) {
        this.IPadresses = IPadresses;
    }
}
