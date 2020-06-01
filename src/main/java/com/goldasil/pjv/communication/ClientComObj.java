package com.goldasil.pjv.communication;

import java.net.Socket;
import java.util.ArrayList;

public class ClientComObj {

    private Socket clientSocket;
    private String ipAddress;
    private int port;
    private int playerID;

    public ClientComObj(Socket clientSocket, int playerID) {
        this.clientSocket = clientSocket;
        this.playerID = playerID;
        this.port = clientSocket.getPort();
    }


    @Override
    public String toString() {
        return "ClientComObj{" +
                "clientSocket=" + clientSocket +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", playerID=" + playerID +
                '}';
    }

    public static ClientComObj getClientObjForPlayerId(ArrayList<ClientComObj> clients, int seekedPlayerSocket){
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
    }


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
