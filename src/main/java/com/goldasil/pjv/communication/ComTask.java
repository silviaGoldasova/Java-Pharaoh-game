package com.goldasil.pjv.communication;

import java.net.Socket;
import java.util.ArrayList;

public class ComTask {

    private int playerID;
    private String messageType;
    private String messageBody;

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
