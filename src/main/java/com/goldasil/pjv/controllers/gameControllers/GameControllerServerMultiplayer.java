package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.communication.*;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.views.GameView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    private ServerSocket listeningSocket;

    private static final Logger logger = LoggerFactory.getLogger(GameControllerServerMultiplayer.class);

    public GameControllerServerMultiplayer(GameModel game, GameView view) {
        super(game, view);
        try {
            listeningSocket = new ServerSocket(listeningPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the server listener, initialize sender for each client, players, their order, their playing cards.
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

        // initialize sender for each client
        for (ClientComObj client : resource.getClientsList()) {
            ServerSender sender = new ServerSender(client.getPlayerID(), client.getClientSocket(), senderLocalPort, resource);
            Thread senderThread = new Thread(sender);
            senderThread.start();
        }

        // initialize listener
        for (ClientComObj client : resource.getClientsList()) {
            ServerListener serverListener = new ServerListener(listeningSocket, client, resource);
            Thread serverListenerThread = new Thread(serverListener);
            serverListenerThread.start();
        }

        // initialize listerner to changes in MessageReceivedQueue
        resource.newReceivedProperty().addListener(isNewReceivedChangeListener);

        logger.debug("ServerListener has started.");

        // send to all players info about the initialized game
        Gson gson = new Gson();
        GameModel gameObjCopy = (GameModel) game.clone();

        for (ClientComObj client : resource.getClientsList()) {

            gameObjCopy.setThisPlayerId(client.getPlayerID());
            String gameString = gson.toJson(gameObjCopy);

            sendMessage(client.getPlayerID(), "INIT", gameString);

        }

        view.setPlayingBoardGui();
        view.setRequestedSuit(game.getCurrentMoveDTO().getRequestedSuit());

    }

    /**
     * Processes a received message based on the decoded message type.
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
                        for (ClientComObj client : resource.getClientsList()) {
                            if (client.getPlayerID() != game.getCurrentPlayerIdTurn()) {
                                sendMessage(client.getPlayerID(), "MOVE", messageObj.getMessageBody());
                            }
                        }
                        break;
                    case "ERRO":
                        System.out.println("Error switch");
                        break;
                    default:
                        System.out.println("Error switch");
                        // request new communication
                        break;
                }
                resource.getReceivedMessages().remove();
            }
            resource.newReceivedProperty().setValue(false);
        }
    }

    /**
     * Plays a move based on received mesage.
     * @param moveDTO move to be played
     */
    private void playOneTurn(MoveDTO moveDTO){
        logger.debug("\n\nStart of the received move.");

        //moveDTO.setUpcard(game.getUpcard());
        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)){
            Platform.runLater(()->{
                view.updatePlayersBoxFromView();
            });
            updateAndPlayNextTurn();
        }

        logger.debug("Player's turn has been played (id = {}).", game.getCurrentPlayerIdTurn());

    }

    ChangeListener isNewReceivedChangeListener = new ChangeListener() {
        @Override public void changed(ObservableValue o, Object oldVal, Object newVal){

            logger.debug("isNewReceivedChangeListener");

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

    /**
     * Listens to opponents and create a list of their sockets
     */
    public void getSidePlayers(){
        logger.debug("getSidePlayers()");
        ChannelGetClients clientsListener = new ChannelGetClients(view.getNewGame().getNumOfOpp(), listeningSocket, resource);
        Thread listenerThread = new Thread(clientsListener);
        listenerThread.start();
    }

    /**
     * Send a message to another player
     * @param playerID
     * @param messageType
     * @param messageBody
     */
    private void sendMessage(int playerID, String messageType, String messageBody) {
        resource.addTask(new ComTask(playerID, messageType, messageBody));
    }

    /**
     * Send a message with move information to another player
     * @param moveDTO move to be send
     */
    private void sendMoveDTO(MoveDTO moveDTO) {
        Gson gson = new Gson();
        String moveDTOstring = gson.toJson(moveDTO);

        for ( ClientComObj client : resource.getClientsList()) {
            logger.debug("game.getCurrentPlayerIdTurn(): {}", game.getCurrentPlayerIdTurn());
            if (client.getPlayerID() != game.getCurrentPlayerIdTurn()) {
                resource.addTask(new ComTask(client.getPlayerID(), "MOVE", moveDTOstring));
            }
        }
    }

    /**
     * Process the submitted move
     * @param cardButtons
     * @param requestedSuit
     */
    public void submitMoveFromView(List<Node> cardButtons, Suit requestedSuit) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        ArrayList<Card> moveCards = getSelectedCards(cardButtons);
        MoveDTO moveDTO = new MoveDTO(new Move(moveCards, requestedSuit));
        moveDTO.setUpcard(game.getUpcard());

        sendMoveDTO(moveDTO);

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)){
            updateAndPlayNextTurn();
        }
    }

    /**
     * Process the submitted move
     * @param numberOfCardsDrawn
     */
    @Override
    public void submitMoveFromView(int numberOfCardsDrawn) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        MoveDTO moveDTO = new MoveDTO(new Move(numberOfCardsDrawn));
        moveDTO.setUpcard(game.getUpcard());

        sendMoveDTO(moveDTO);

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)){
            updateAndPlayNextTurn();
        }
    }

    /**
     * Process the submitted move
     * @param moveDTO
     */
    public void submitMoveFromView(MoveDTO moveDTO) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        // WIN move
        moveDTO.setUpcard(game.getUpcard());

        sendMoveDTO(moveDTO);

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)) {
            //setChangedSuit();
            view.setNewUpdate();
        }
    }

    /**
     * Update GUI after a new move has been processed
     */
    @Override
    public void updateAndPlayNextTurn() {
        Runnable runnable = () -> {

            Platform.runLater(()-> {
                setChangedSuit();
                view.setNewUpdate();
                game.setNextPlayersTurn();
                view.setPenaltyAndTurnLabel();
            });

            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //playOneTurn();
        };
        Thread playTurn = new Thread(runnable);
        playTurn.start();
    }

    /**
     * Set end of network communication
     */
    public void closeResource() {
        resource.setGameOn(false);
    }

}
