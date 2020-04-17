package com.goldasil.pjv.communication;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChannelClientHandler implements Runnable {

    private ServerSocket listeningSocket;
    private Socket sender;
    private DataInputStream in;
    private boolean isStopped = false;
    private int ID;

    public int getID() {
        return ID;
    }

    private ChannelClientHandler(Socket sender) {
        this.sender = sender;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    private String handleReceived() throws IOException {
        String messageType = "MOVE";

        messageType = decodeReceivedMessageType();
        switch (messageType) {
            case "MOVE":
                System.out.println(decodeReceivedMessage());
                return "Move";
            case "ERRO":
                System.out.println("Error switch");
                return "Error";
            case "OVER":
                System.out.println("Game over");
                return "Game over";
            default:
                System.out.println("Error switch");
                // request new communication
                return "Error";
        }
    }

    @Override
    public void run() {
        try {
            listeningSocket = new ServerSocket(4444);
            in = new DataInputStream(new BufferedInputStream(sender.getInputStream()));
            System.out.println("Listening to a client.");

             // process the client's data
            while (!isStopped) {
                handleReceived();
            }

            listeningSocket.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String decodeReceivedMessage(){
        int dataLength = 0;
        try {
            dataLength = in.readInt();
            byte[] messageByte = new byte[dataLength];
            boolean end = false;
            StringBuilder dataString = new StringBuilder(dataLength);
            int totalBytesRead = 0;

            while(!end) {
                int currentBytesRead = in.read(messageByte);
                totalBytesRead = currentBytesRead + totalBytesRead;
                if(totalBytesRead <= dataLength) {
                    dataString
                            .append(new String(messageByte, 0, currentBytesRead));
                } else {
                    dataString
                            .append(new String(messageByte, 0, dataLength - totalBytesRead + currentBytesRead));
                }
                if(dataString.length() >= dataLength) {
                    end = true;
                }
            }
            return dataString.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String decodeReceivedMessageType() throws IOException {
        byte[] messageType = new byte[4];
        if (in.read(messageType, 0, 4) != 4){
            System.out.println("Communication failed");;
        }
        String messageTypeStr = new String(messageType, StandardCharsets.UTF_8);
        return messageTypeStr;
    }

}
