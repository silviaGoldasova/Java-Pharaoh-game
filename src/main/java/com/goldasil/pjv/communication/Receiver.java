package com.goldasil.pjv.communication;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.Thread.sleep;

/**
 * Receiver for a client
 */
public class Receiver implements Runnable {

    private ServerSocket listeningSocket;
    private Socket socket;
    private int port;
    private DataInputStream in;
    private ComResource resource;
    private Socket sender;

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    /**
     * Create a new Receiver
     * @param clientSocket socket of the sender
     * @param portNumber port number of the sender
     * @param resource shared resource
     */
    public Receiver(Socket clientSocket, int portNumber, ComResource resource) {
        this.socket = clientSocket;
        this.port = portNumber;
        this.resource = resource;
    }

    /**
     * Listen to any incomming messages and add them to the shared resourcde.
     */
    @Override
    public void run() {
        try {
            //sender = listeningSocket.accept();
            while (resource.isGameOn()) {

                in = new DataInputStream(socket.getInputStream());
                logger.debug("Listening");

                if (in.available() > 0) {
                    String messageRaw = in.readUTF();
                    //handleReceived(messageRaw);

                    String messageType = decodeMessageType(messageRaw);
                    String messageBody = decodeMessageBody(messageRaw);
                    resource.addMessage(new ComTask(0, messageType, messageBody));
                    resource.setNewReceived(true);

                    logger.debug("Message received: {}", messageBody);
                }

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
     * Decodes the received message type.
     * @return type of the message received as a String
     */
    private String decodeMessageType(String messageRaw) {
        return messageRaw.substring(0, 4);
    }

    /**
     * Decodes the received message body.
     * @param messageRaw
     * @return message body
     */
    private String decodeMessageBody(String messageRaw) {
        return messageRaw.substring(4);
    }


    /**
     * Stops listening on the socket.
     */
    public void stopConnection() {
        try {
            in.close();
            socket.close();
            //listeningSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
