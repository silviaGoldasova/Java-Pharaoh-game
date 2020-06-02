package com.goldasil.pjv.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Represents a communication channel used only for initial listening, finding and connecting to the players willing to play a game.
 * The channel runs in its own separate thread and puts all the players that have connected to a list of sockets.
 */
public class ChannelGetClients implements Runnable {

    private ServerSocket listeningSocket;
    private int numOfClients;
    private ComResource resource;

    private static final Logger logger = LoggerFactory.getLogger(ChannelGetClients.class);

    /**
     * Creates a new channel for listenning.
     * @param numOfClients number of clients for which to wait to connect
     */
    public ChannelGetClients(int numOfClients, ServerSocket listeningSocket, ComResource resource) {
        this.numOfClients = numOfClients;
        this.listeningSocket = listeningSocket;
        this.resource = resource;
    }

    /**
     * Stops listening on the socket and close it.
     */
    public void stopConnection() {
        try {
            listeningSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Listens for any connection, until all players connect.
     * Adds any new client to a list of clients.
     */
    @Override
    public void run() {
        try {
            logger.debug("Listening for clients has started. Waiting for connection ...");

            int numOfFoundClients = 0;
            ArrayList<ClientComObj> clientList = new ArrayList<>();

            synchronized (resource) {
                while (numOfFoundClients != numOfClients) {
                    Socket newClient = listeningSocket.accept();
                    clientList.add(new ClientComObj(newClient, numOfFoundClients+1));
                    logger.debug("Match");
                    numOfFoundClients++;
                }

                resource.setClientsList(clientList);
                logger.debug(clientList.toString());
            }

        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //stopConnection();
    }

}
