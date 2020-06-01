package com.goldasil.pjv.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class ServerSender implements Runnable  {

    private Socket clientSocket;
    private DataOutputStream out;
    private final ComResource resource;
    private int localPort;

    private static final Logger logger = LoggerFactory.getLogger(ServerSender.class);

    public ServerSender(int localPort, ComResource resource) {
        this.resource = resource;
        this.localPort = localPort;
    }

    /**
     * Starts connection to the specified IP address and Port.
     */
    private void startConnection(String destIpAddress, int destPort) {
        try {
            InetAddress addr = InetAddress.getByName(destIpAddress);
            clientSocket = new Socket(addr, destPort, addr, localPort);
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Starts connection to the specified socket.
     */
    private void startConnection(Socket socket) {
        try {
            clientSocket = socket;
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void run() {

        while (resource.isGameOn()) {

            if (resource.getTaskList().size() != 0) {
                synchronized (resource) {

                    LinkedList<ComTask> tasks = resource.getTaskList();
                    for (ComTask task : new LinkedList<ComTask>(tasks)) {

                        for (ClientComObj socketObj : task.getReceivers()) {
                            startConnection(socketObj.getClientSocket());

                            sendMessage(task.getMessageType(), task.getMessageBody());
                            logger.debug("sent from server sender: {}", task.getMessageBody());

                            stopConnection();
                        }
                        tasks.remove();
                    }
                    //resource.getTaskList().clear();
                }
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
     * Decomposes the String data (message type and body of the message) to be sent to bytes (using UTF_8).
     * Sends data byte by byte to the recepient.
     * @param messageType type of the message to be sent
     * @param message message to be sent
     */
    private void sendMessage(String messageType, String message) {
        String messageToSend = messageType + message;

        try {
            out.writeUTF(messageToSend);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
