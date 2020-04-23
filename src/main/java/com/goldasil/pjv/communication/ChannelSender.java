package com.goldasil.pjv.communication;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Represents a communication channel used only for sending String messages to a specified recipient.
 * The connection is established for every message to be sent anew, the socket is closed again after the message is sent.
 * The recipient is specified by an IP address and Port.
 */
public class ChannelSender {

    private ServerSocket listeningSocket;
    private Socket clientSocket;
    private DataOutputStream out;

    /**
     * Initializes a new connection to the recipient.
     * @param ipAddress
     * @param port
     */
    public ChannelSender(String ipAddress, int port) {
        startConnection(ipAddress, port);
    }

    /**
     * Starts connection to the specified IP address and Port.
     * @param ipAddress IP address of the recipient
     * @param port port of the recipient
     */
    private void startConnection(String ipAddress, int port) {
        try {
            // connect to the client
            clientSocket = new Socket(ipAddress, port);
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Stops connection with the recipient immediately after a message is sent, closes the socket.
     */
    private void stopConnection() {
        try {
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends a String message thought the established connection with the recipient.
     * @param ipClient IP address of the recipient
     * @param portClient port of the recipient
     * @param message message to be sent
     * @param messageType type of the message to be sent, e.g. about a move in the gameControllers, an error message, a request for data
     * @return true if the message is sends successfully
     */
    public boolean sendMessageTo(String ipClient, int portClient, String message, String messageType) {
        startConnection(ipClient, portClient);
        sendMessage(messageType, message);
        stopConnection();
        return true;
    }

    /**
     * Decomposes the String data (message type and body of the message) to be sent to bytes (using UTF_8).
     * Sends data byte by byte to the recepient.
     * @param messageType type of the message to be sent
     * @param message message to be sent
     */
    private void sendMessage(String messageType, String message) {
        try {
            byte[] messageTypeBytes = messageType.getBytes(StandardCharsets.UTF_8);
            out.write(messageTypeBytes);
            out.writeInt(message.length());

            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            out.write(messageBytes);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

    /*public static void main( String args[ ] ) throws Exception {
        ChannelSender server = new ChannelSender ();
        server.startConnection("127.0.0.1", 4444);

        // sending the OppMove JSON file to client
        String message = "{\"rank\":\"OVERKNAVE\",\"suit\":\"LEAVES\"}";

        server.sendMessageTo("127.0.0.1", 4444, "MOVE", message);
        server.sendMessageTo("127.0.0.1", 4444, "MOVE", message);
    }*/
