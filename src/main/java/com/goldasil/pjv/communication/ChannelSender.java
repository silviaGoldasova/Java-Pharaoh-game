package com.goldasil.pjv.communication;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class ChannelSender {

    private ServerSocket listeningSocket;
    private Socket clientSocket;
    private DataOutputStream out;

    private void startConnection(String ipClient, int portClient) {
        try {
            // connect to the client
            clientSocket = new Socket(ipClient, portClient);
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void stopConnection() {
        try {
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean sendMessageTo(String ipClient, int portClient, String message, String messageType) {
        startConnection(ipClient, portClient);
        sendMessage(messageType, message);
        stopConnection();
        return true;
    }


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

    public static void main( String args[ ] ) throws Exception
    {
        ChannelSender server = new ChannelSender ();
        server.startConnection("127.0.0.1", 4444);

        // sending the OppMove JSON file to client
        String message = "{\"rank\":\"OVERKNAVE\",\"suit\":\"LEAVES\"}";

        server.sendMessageTo("127.0.0.1", 4444, "MOVE", message);
        server.sendMessageTo("127.0.0.1", 4444, "MOVE", message);


    }



}
