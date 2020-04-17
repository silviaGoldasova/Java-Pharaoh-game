package com.goldasil.pjv.communication;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChannelGetClients implements Runnable{

    private ServerSocket listeningSocket;
    private ArrayList<Socket> clients;
    private int numOfClients;
    private int listeningPort = 4444;

    //Thread getClients = new Thread(new ChannelGetClients(numOfClients, clients_list));
    // getClients.start();
    // Thread client0 = new Thread(new ChannelClientHandler(listOfSockets.get(i));
    // client0.start();
    public ChannelGetClients(int numOfClients, ArrayList<Socket> clients) {
        this.numOfClients = numOfClients;
        this.clients = clients;
    }

    public void stopConnection() {
        try {
            listeningSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            listeningSocket = new ServerSocket(listeningPort);
            System.out.println("Server Started. Waiting for connection ...");
            int numOfFoundClients = 0;

            while (numOfFoundClients != numOfClients) {
                clients.add(listeningSocket.accept());
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
    }*/

    public static void main(String args[]) {
        /*
        ChannelGetClients server = new ChannelFromClient();
        server.startConnection(4444);
        server.stopConnection();
        */
    }

}
