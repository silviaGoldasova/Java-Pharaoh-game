package com.goldasil.pjv.communication;

import java.net.Socket;
import java.util.ArrayList;

/**
* Represents a client socket object (is player specific), encompasses it with additional information: playerId, ipAddress, port.
*/
public class ClientComObj {

    private Socket clientSocket;
    private String ipAddress;
    private int port;
    private int playerID;

    /**
     * Creates a new ClientComObj object
     * @param clientSocket clientSocket to be associated with the object
     * @param playerID ID of the player on the other side of the socket
     */
    public ClientComObj(Socket clientSocket, int playerID) {
        this.clientSocket = clientSocket;
        this.playerID = playerID;
        this.port = clientSocket.getPort();
    }


    /**
     * Puts ClientComObj to string
     * @return string
     */
    @Override
    public String toString() {
        return "ClientComObj{" +
                "clientSocket=" + clientSocket +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", playerID=" + playerID +
                '}';
    }


    /*public static ClientComObj getClientObjForPlayerId(ArrayList<ClientComObj> clients, int seekedPlayerSocket){
        for (ClientComObj obj : clients) {
            if (seekedPlayerSocket == obj.playerID) {
                return obj;
            }
        }
        return null;
    }

    public static Socket getSocketForPlayerId(ArrayList<ClientComObj> clients, int seekedPlayerSocket){
        for (ClientComObj obj : clients) {
            if (seekedPlayerSocket == obj.playerID) {
                return obj.clientSocket;
            }
        }
        return null;
    }*/

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
