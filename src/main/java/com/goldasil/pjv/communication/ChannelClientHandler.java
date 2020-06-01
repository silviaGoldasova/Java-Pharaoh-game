package com.goldasil.pjv.communication;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * A handler for each client (= another player) a server (= the main player) has and services.
 * Each client is services in an own thread.
 */
public class ChannelClientHandler implements Runnable {

    private ServerSocket listeningSocket;
    private Socket sender;
    private DataInputStream in;
    private boolean isStopped = false;
    private int ID;
    private ComResource resource;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Creates a handler of a client.
     * @param sender specifies the sender's socket got by the ChannelGetClients
     */
    public ChannelClientHandler(Socket sender, ComResource resource) {
        this.sender = sender;
        this.resource = resource;
    }


    /**
     * Sets status variable isStopped to true to notify the thread that the listening is over.
     * @param stopped status variable isStopped
     */
    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    /**
     * Processes a received message based on the decoded message type.
     * @return message body
     * @throws IOException
     */
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
                System.out.println("gameControllers over");
                return "gameControllers over";
            default:
                System.out.println("Error switch");
                // request new communication
                return "Error";
        }
    }

    /**
     * Keeps listening for any new data from the specified client/player until isStopped is set to true;
     */
    @Override
    public void run() {
        try {
            listeningSocket = new ServerSocket(5555);
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

    /**
     * Decodes the body of the received messsage.
     * @return the body of the received message as a String
     */
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


    /**
     * Decodes the received message type.
     * @return type of the message received as a String
     * @throws IOException
     */
    private String decodeReceivedMessageType() throws IOException {
        byte[] messageType = new byte[4];
        if (in.read(messageType, 0, 4) != 4){
            System.out.println("Communication failed");;
        }
        String messageTypeStr = new String(messageType, StandardCharsets.UTF_8);
        return messageTypeStr;
    }

}
