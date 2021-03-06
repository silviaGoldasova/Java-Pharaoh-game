package com.goldasil.pjv.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

/**
 * A sender that sends messages to the given socket whenever a new one is available in the shared resource.
 */
public class Sender implements Runnable {

    private Socket clientSocket;
    private DataOutputStream out;
    private ComResource resource;
    private String destIpAddress;
    private int destPort;
    private int localPort;

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    /**
     * Create a new sender
     * @param socket destination socket
     * @param destIpAddress
     * @param destPort
     * @param localPort
     * @param resource shared resource
     */
    public Sender(Socket socket, String destIpAddress, int destPort, int localPort, ComResource resource) {
        clientSocket = socket;
        this.destIpAddress = destIpAddress;
        this.destPort = destPort;
        this.localPort = localPort;
        this.resource = resource;
    }

    /**
     * Starts connection to the specified IP address and Port.
     */
    private void startConnection() {
        try {
            // connect to the client

            //InetAddress addr = InetAddress.getByName(destIpAddress);
            //InetAddress localAddr = InetAddress.getByName("192.168.0.104");
            //clientSocket = new Socket(addr, destPort, addr, localPort);
            //clientSocket = new Socket(destIpAddress, destPort);
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sends all the messages in the task list in a shared resource.
     */
    @Override
    public void run() {
        startConnection();

        while (resource.isGameOn()) {
            //logger.debug("resource: {}", resource.toString());

            if (resource.getTaskList().size() != 0) {
                synchronized (resource) {

                    logger.debug("Sending to: {}, {}, from {}, {}", clientSocket.getInetAddress().toString(), clientSocket.getPort(), clientSocket.getLocalAddress(), clientSocket.getLocalPort());

                    LinkedList<ComTask> tasks = resource.getTaskList();
                    for (ComTask task : new LinkedList<ComTask>(tasks)) {
                        logger.debug("Message {} has been sent", task.getMessageBody());
                        sendMessage(task.getMessageType(), task.getMessageBody());
                        tasks.remove();
                    }

                }
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopConnection();
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
