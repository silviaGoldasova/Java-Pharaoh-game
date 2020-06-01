package com.goldasil.pjv.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Represents a communication channel used only for initial listening on port 5555 and finding and connecting to the players willing to play a game.
 * The channel runs in its own separate thread, and creates a new thread for communication with every player that connects.
 */
public class ChannelGetClients implements Runnable {

    private ServerSocket listeningSocket;
    private int numOfClients;
    private int listeningPort;
    private ComResource resource;

    private static final Logger logger = LoggerFactory.getLogger(ChannelGetClients.class);


    //Thread getClients = new Thread(new ChannelGetClients(numOfClients, clients_list));
    // getClients.start();
    // Thread client0 = new Thread(new ChannelClientHandler(listOfSockets.get(i));
    // client0.start();

    /**
     * Creates a new channel for listenning.
     * @param numOfClients number of clients for which to wait to connect
     */
    public ChannelGetClients(int numOfClients, int port, ComResource resource) {
        this.numOfClients = numOfClients;
        this.listeningPort = port;
        this.resource = resource;
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

    /**
     * Listens for any connection, until all players connect.
     * Adds any new client to a list of clients.
     */
    @Override
    public void run() {
        try {
            listeningSocket = new ServerSocket(listeningPort);
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

        stopConnection();
    }



    /*public void startConnection(int listeningPort){
        initializeListening(listeningPort);
    }
    protected void initializeListening(int listeningPort) {
        try {
            listeningSocket = new ServerSocket(listeningPort);
            System.out.println("Server Started. Waiting for connection ...");

            sender = listeningSocket.accept();
            System.out.println("Got connection from client.");

            // set streams for communication
            in = new DataInputStream(new BufferedInputStream(sender.getInputStream()));
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String args[]) {

        ChannelGetClients server = new ChannelFromClient();
        server.startConnection(4444);
        server.stopConnection();
    }*/

}
