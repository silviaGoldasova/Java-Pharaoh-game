package com.goldasil.pjv.communication;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Represents a network task: either to send data or to process received data
 */
public class ComTask {

    private int playerID;
    private String messageType;
    private String messageBody;

    /**
     * Create a new task
     * @param playerID ID of the initiator of the task
     * @param messageType
     * @param messageBody
     */
    public ComTask(int playerID, String messageType, String messageBody) {
        this.playerID = playerID;
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
