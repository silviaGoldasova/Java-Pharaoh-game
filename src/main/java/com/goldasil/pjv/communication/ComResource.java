package com.goldasil.pjv.communication;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.LinkedList;

public class ComResource {

    //private volatile boolean newReceived;
    private volatile BooleanProperty newReceived;
    private volatile boolean gameOn;

    private volatile LinkedList<ComTask> taskList;
    private volatile LinkedList<ComTask> receivedMessages;
    private volatile ArrayList<ClientComObj> clientsList;


    public ComResource() {
        gameOn = true;
        taskList = new LinkedList<>();  //remove and add
        clientsList = new ArrayList<>();
        receivedMessages = new LinkedList<>();
        newReceived = new SimpleBooleanProperty();
    }

    @Override
    public String toString() {
        return "ComResource{" +
                "newReceived=" + newReceived +
                ", gameOn=" + gameOn +
                ", taskList=" + taskList +
                ", receivedMessages=" + receivedMessages +
                ", clientsList=" + clientsList +
                '}';
    }

    public LinkedList<ComTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(LinkedList<ComTask> taskList) {
        this.taskList = taskList;
    }

    public ArrayList<ClientComObj> getClientsList() {
        return clientsList;
    }

    public void setClientsList(ArrayList<ClientComObj> clientsList) {
        this.clientsList = clientsList;
    }

    public synchronized void addTask(ComTask task) {
        taskList.add(task);
    }

    public synchronized void addMessage(ComTask message) {
        receivedMessages.add(message);
    }

    public synchronized ComTask removeTask() {
        return taskList.remove();
    }


    public LinkedList<ComTask> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(LinkedList<ComTask> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public synchronized boolean isGameOn() {
        return gameOn;
    }

    public synchronized void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public boolean isNewReceived() {
        return newReceived.get();
    }

    public BooleanProperty newReceivedProperty() {
        return newReceived;
    }

    public void setNewReceived(boolean newReceived) {
        this.newReceived.set(newReceived);
    }
}
