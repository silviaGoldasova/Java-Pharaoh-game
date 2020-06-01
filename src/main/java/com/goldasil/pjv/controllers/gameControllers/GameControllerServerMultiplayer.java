package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.communication.*;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.views.GameView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

/**
 * Represents a Game Controller for the Person Vs Person mode from the perspective of a player playing on the device in the server role, operates based on the Finite State Machine concept.
 * Extends the Game multiplayer mode controller. The added functionality lies in the connection of the players and management of all the communication taking place throughout the game.
 *
 * The player in the role of a server is a central node of the communication - all other players send information to him, then he propagates the information to the others.
 */
public class GameControllerServerMultiplayer extends GameControllerMultiplayer {

    private ComResource resource = new ComResource();
    int listeningPort = 5556;
    int senderLocalPort = 5555;

    private static final Logger logger = LoggerFactory.getLogger(GameControllerServerMultiplayer.class);

    public GameControllerServerMultiplayer(GameModel game, GameView view) {
        super(game, view);
    }


    /**
     * Sets the players, their order of the game, their playing cards.
     */
    public void initializeGame() {
        game.initGameMultiplayer(view.getNewGame().getNumOfOpp());
        view.setRequestedSuit(game.getCurrentMoveDTO().getRequestedSuit());

        // wait for all clients sign up
        while(resource.getClientsList().size() == 0) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.debug("All clients are connected");

        // initialize sender
        ServerSender sender = new ServerSender(senderLocalPort, resource);
        Thread senderThread = new Thread(sender);
        senderThread.start();

        // initialize listener
        ServerListener serverListener = new ServerListener(listeningPort, resource);
        Thread serverListenerThread = new Thread(serverListener);
        serverListenerThread.start();

        // initialize listerner to changes in MessageReceivedQueue
        resource.newReceivedProperty().addListener(isNewReceivedChangeListener);

        logger.debug("ServerListener has started.");

        // send to all players info about the initialized game
        Gson gson = new Gson();
        GameModel gameObjCopy = (GameModel) game.clone();

        for (ClientComObj client : resource.getClientsList()) {
            gameObjCopy.setThisPlayerId(client.getPlayerID());

            String gameString = gson.toJson(game);
            ArrayList<ClientComObj> list = new ArrayList<>();
            list.add(client);

            sendMessage(list, "INIT", gameString);
            //sendMessage(resource.getClientsList(), "MOVE", "Hello world");
        }
    }


    /**
     * Processes a received message based on the decoded message type.
     * @return message body
     * @throws IOException
     */
    private void proccessReceived() throws IOException {

        synchronized (resource) {
            for (ComTask messageObj : new LinkedList<ComTask>(resource.getReceivedMessages())) {
                logger.debug("message type: {}, body: {}", messageObj.getMessageType(), messageObj.getMessageBody());

                Gson gson = new Gson();
                switch (messageObj.getMessageType()) {
                    case "MOVE":

                        Type moveDTOType = new TypeToken<MoveDTO>(){}.getType();
                        MoveDTO moveDTO = gson.fromJson(messageObj.getMessageBody(), moveDTOType);
                        playOneTurn(moveDTO);

                        // send moveDTO to the other players
                        ArrayList<ClientComObj> listReceivers = new ArrayList<>();
                        for (ClientComObj client : resource.getClientsList()) {
                            if (client.getPlayerID() != game.getCurrentPlayerIdTurn()) {
                                listReceivers.add(client);
                            }
                        }
                        sendMessage(listReceivers, "MOVE", messageObj.getMessageBody());

                    case "ERRO":
                        System.out.println("Error switch");

                    default:
                        System.out.println("Error switch");
                        // request new communication
                }
                resource.getReceivedMessages().remove();
            }
            resource.setNewReceived(false);
        }
    }

    public void playOneTurn(MoveDTO moveDTO){
        if (game.getCurrentPlayerIdTurn() == game.getThisPlayerId()) {
            logger.debug("Error, this player's move");
            return;
        }

        logger.debug("\n\nStart of the received move.");

        if (game.runOppTurn(moveDTO)) {

            setChangedSuit();

            Platform.runLater(()->{
                view.updatePlayersBoxFromView();
                view.setNewUpdate();
            });

            try {
                sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.debug("Player's turn has been played (id = {}).", game.getCurrentPlayerIdTurn());
            game.setNextPlayersTurn();

            Platform.runLater(()->{
                view.setPenaltyAndTurnLabel();
            });
        }

    }

    ChangeListener isNewReceivedChangeListener = new ChangeListener() {
        @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
            if ((boolean) newVal == true) {

                Platform.runLater( ()-> {
                    logger.debug("New message has been changed!");
                    try {
                        proccessReceived();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } );

            }
        }
    };

    public void getSidePlayers(){
        logger.debug("getSidePlayers()");
        ChannelGetClients clientsListener = new ChannelGetClients(view.getNewGame().getNumOfOpp(), listeningPort, resource);
        Thread listenerThread = new Thread(clientsListener);
        listenerThread.start();
    }

    private void sendMessage(ArrayList<ClientComObj> clientsList, String messageType, String messageBody) {
        resource.addTask(new ComTask(clientsList, messageType, messageBody));
    }



}
