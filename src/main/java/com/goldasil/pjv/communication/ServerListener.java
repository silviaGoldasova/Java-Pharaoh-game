package com.goldasil.pjv.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ServerListener implements Runnable {

    private ServerSocket listeningSocket;
    private int port;
    private DataInputStream in;
    private ComResource resource;
    private Socket sender;

    private static final Logger logger = LoggerFactory.getLogger(ServerListener.class);


    public ServerListener(int port, ComResource resource) {
        this.port = port;
        this.resource = resource;

        try {
            listeningSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (resource.isGameOn()) {

            try {
                sender = listeningSocket.accept();
                in = new DataInputStream(sender.getInputStream());

                String messageRaw = in.readUTF();

                //handleReceived(messageRaw);
                String messageType = decodeMessageType(messageRaw);
                String messageBody = decodeMessageBody(messageRaw);
                resource.addMessage(new ComTask(getPlayerIdFromSocketPort(sender.getPort()), messageType, messageBody));
                resource.setNewReceived(true);

                stopConnection();
            }
            catch(IOException e){
                logger.debug(e.getMessage());
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
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
