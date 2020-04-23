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
     * @param gameType a String, either a new game or loaded game
     */
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    /**
     * Gets game mode - vsPerson or vsComputer
     * @return game mode as a String
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * Sets the game mode - vsPerson or vsComputer
     * @param gameMode a String game mode
     */
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Gets the number of players
     * @return the number of players
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Sets the number of players
     * @param numOfPlayers the number of players
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Gets a list of IP addresses of players
     * @return list of IP addresses as Strings
     */
    public List<String> getIPadresses() {
        return IPadresses;
    }

    /**
     * Sets a list of IP addresses of players
     * @param IPadresses a list of IP addresses
     */
    public void setIPadresses(List<String> IPadresses) {
        this.IPadresses = IPadresses;
    }
}
