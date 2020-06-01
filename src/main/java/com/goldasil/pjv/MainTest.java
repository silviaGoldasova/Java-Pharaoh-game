package com.goldasil.pjv;

import com.goldasil.pjv.communication.*;

import static java.lang.Thread.sleep;

public class MainTest {

    public static void main(String[] args) {

        ComResource resource = new ComResource();

        // from notebook / sender
        //Sender sender = new Sender("192.168.0.101", 5556, 5001, resource);
        //Thread senderThread = new Thread(sender);
        //senderThread.start();

        // from PC / receiver
        /*Receiver receiver = new Receiver(5556, resource);
        Thread clientThread = new Thread(receiver);
        clientThread .start();*/

        // from notebook / sender 2
        //Sender sender2 = new Sender("192.168.0.101", 5556, 5001, resource);
        //Thread senderThread2 = new Thread(sender2);
        //senderThread2.start();


        ServerListener serverListener = new ServerListener(5556, resource);
        Thread serverListenerThread = new Thread(serverListener);
        serverListenerThread.start();


        resource.addTask(new ComTask(null, "MOVE", "Hello world"));
        resource.addTask(new ComTask(null, "MOVE", "Hello there"));

        // get all side players
        /*ChannelGetClients getClients = new ChannelGetClients(1, 5556, resource);
        Thread getClientsThread = new Thread(getClients);
        getClientsThread.start();*/




        /*
        Thread guiLaunchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                javafx.application.Application.launch(ViewTest.class);
            }
        });
        guiLaunchThread.start();
        ViewTest viewTest = ViewTest.waitForStartUpTest();
        viewTest.printSomething();
        */

    }


}