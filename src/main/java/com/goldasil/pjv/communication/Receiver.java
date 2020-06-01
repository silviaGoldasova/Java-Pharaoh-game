package com.goldasil.pjv.communication;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Receiver implements Runnable {

    private ServerSocket listeningSocket;
    private int port;
    private DataInputStream in;
    private ComResource resource;
    private Socket sender;

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);


    public Receiver(int portNumber, ComResource resource) {
        this.port = portNumber;
        this.resource = resource;

        try {
            listeningSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            sender = listeningSocket.accept();
            in = new DataInputStream(sender.getInputStream());

            while (resource.isGameOn()) {

                String messageRaw = in.readUTF();
                //handleReceived(messageRaw);

                String messageType = decodeMessageType(messageRaw);
                String messageBody = decodeMessageBody(messageRaw);
                resource.addMessage(new ComTask(null, messageType, messageBody));
                resource.setNewReceived(true);

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //logger.debug("sender on port: {}", sender.getPort());

            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        stopConnection();
    }

    /**
     * Processes a received message based on the decoded message type.
     * @return message body
     * @throws IOException
     */
    /*private String handleReceived(String messageRaw) throws IOException {
        String messageType = decodeMessageType(messageRaw);
        String messageBody = decodeMessageBody(messageRaw);

        logger.debug("decoded type: {}", messageType);

        switch (messageType) {
            case "MOVE":
                logger.debug("messageBody: {}", messageBody);

                synchronized (resource) {
                    resource.setNewReceived(true);
                    resource.addMessage(new ComTask(null, messageType, messageBody));
                }

                return "Move";
            case "ERRO":
                System.out.println("Error switch");
                return "Error";
            case "OVER":
                System.out.println("gameControllers over");
                return "gameControllers over";
            case "INIT":



                System.out.println("INIT");



                return "";
            default:
                System.out.println("Error switch");
                // request new communication
                return "Error";
        }
    }*/


    /**
     * Decodes the received message type.
     * @return type of the message received as a String
     * @throws IOException
     */
    private String decodeMessageType(String messageRaw) {
        return messageRaw.substring(0, 4);
    }

    private String decodeMessageBody(String messageRaw) {
        return messageRaw.substring(4);
    }


    /**
     * Stops listening on the socket.
     */
    public void stopConnection() {
        try {
            listeningSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
