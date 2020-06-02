package com.goldasil.pjv.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listener for server.
 */
public class ServerListener implements Runnable {

    private ServerSocket listeningSocket;
    private int port;
    private DataInputStream in;
    private ComResource resource;
    private Socket sender;
    private ClientComObj client;

    private static final Logger logger = LoggerFactory.getLogger(ServerListener.class);


    /**
     * Create a new listener
     * @param listeningSocket
     * @param client socket object of the sender
     * @param resource shared resource
     */
    public ServerListener(ServerSocket listeningSocket, ClientComObj client, ComResource resource) {
        logger.debug("ServerListener class");
        this.listeningSocket = listeningSocket;
        this.client = client;
        this.resource = resource;
    }

    /**
     * Listen to any incomming messages and add them to the shared resourcde.
     */
    @Override
    public void run() {

        try {
            in = new DataInputStream(client.getClientSocket().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (resource.isGameOn()) {

            try {
                //logger.debug("sender accepted, from port {}", senderSock.getPort());

                if (in.available() > 0) {
                    String messageRaw = in.readUTF();

                    logger.debug("{}", messageRaw);

                    //handleReceived(messageRaw);
                    String messageType = decodeMessageType(messageRaw);
                    String messageBody = decodeMessageBody(messageRaw);
                    resource.addMessage(new ComTask(client.getPlayerID(), messageType, messageBody));
                    resource.newReceivedProperty().setValue(true);
                }

                //stopConnection();
            }
            catch(IOException e){
                logger.debug(e.getMessage());
            }

        }
        stopConnection();
    }

    private int getPlayerIdFromSocketPort(int portNumber) {
        for (ClientComObj client : resource.getClientsList()) {
            if (client.getPort() == portNumber) {
                return client.getPlayerID();
            }
        }
        return -1;
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
            listeningSocket.close();
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
