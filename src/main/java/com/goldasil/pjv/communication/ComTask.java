package com.goldasil.pjv.communication;

import java.net.Socket;
import java.util.ArrayList;

public class ComTask {

    private ArrayList<ClientComObj> receivers = new ArrayList<>();
    private String messageType;
    private String messageBody;

    public ComTask(ArrayList<ClientComObj> receiversList, String messageType, String messageBody) {
        receivers = receiversList;
        this.messageType = messageType;
        this.messageBody = messageBody;
    }



    public ArrayList<ClientComObj> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<ClientComObj> receivers) {
        this.receivers = receivers;
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
