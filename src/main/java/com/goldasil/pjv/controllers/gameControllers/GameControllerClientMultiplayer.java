package com.goldasil.pjv.controllers.gameControllers;

import com.goldasil.pjv.communication.ComResource;
import com.goldasil.pjv.communication.ComTask;
import com.goldasil.pjv.communication.Receiver;
import com.goldasil.pjv.communication.Sender;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.GameModel;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.views.GameView;
import com.goldasil.pjv.views.TextFieldsButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Represents a Game Controller for the Person Vs Person mode from the perspective of a player playing on the device in the client role, operates based on the Finite State Machine concept.
 * Extends the Game multiplayer mode controller. The added functionality lies in the handling of the communication about the moves and other matters.
 *
 * The player in the role of a client is an outer node of the communication - sends all information to the server = the central node of the communication, the server propages the message as deemed necessary.
 *
 */
public class GameControllerClientMultiplayer extends GameControllerMultiplayer {

    private ComResource resource = new ComResource();
    int clientPort = 5001;

    private static final Logger logger = LoggerFactory.getLogger(GameControllerClientMultiplayer.class);


    public GameControllerClientMultiplayer(GameModel game, GameView view) {
        super(game, view);
    }


    public void initConnectionToServer(TextFieldsButton button) {
        String serverIpAddress = button.getTextInput("Server IP address");
        String serverPort = button.getTextInput("Server port");
        String myPort = button.getTextInput("My port number");
        clientPort = Integer.parseInt(myPort);

        // initialize listerner to changes in MessageReceivedQueue
        resource.newReceivedProperty().addListener(isNewReceivedChangeListener);

        Socket clientSocket = null;
        try {
            clientSocket = new Socket(serverIpAddress, Integer.parseInt(serverPort));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // initialize listener to messsages
        Receiver receiver = new Receiver(clientSocket, clientPort, resource);
        Thread clientListenerThread = new Thread(receiver);
        clientListenerThread.start();

        //Sender sender = new Sender("192.168.0.101", 5556, clientPort, resource);
        Sender sender = new Sender(clientSocket, serverIpAddress, Integer.parseInt(serverPort), clientPort, resource);
        Thread senderThread = new Thread(sender);
        senderThread.start();
        //resource.addTask(new ComTask(-1, "MOVE", "Hello world"));

    }

    /**
     * Processes a received message based on the decoded message type.
     * @return message body
     * @throws IOException
     */
    private void proccessReceived() {

        logger.debug("proccessReceived() has started");

        synchronized (resource) {
            for (ComTask messageObj : new LinkedList<ComTask>(resource.getReceivedMessages())) {
                logger.debug("message type: {}, body: {}", messageObj.getMessageType(), messageObj.getMessageBody());

                Gson gson = new Gson();
                switch (messageObj.getMessageType()) {
                    case "MOVE":
                        Type moveDTOType = new TypeToken<MoveDTO>(){}.getType();
                        MoveDTO moveDTO = gson.fromJson(messageObj.getMessageBody(), moveDTOType);
                        playOneTurn(moveDTO);
                        break;
                    case "ERRO":
                        System.out.println("Error switch");
                        break;
                    case "OVER":
                        System.out.println("gameControllers over");
                        break;
                    case "INIT":
                        logger.debug("INIT");

                        Type gameType = new TypeToken<GameModel>(){}.getType();
                        GameModel gameInitInfo = gson.fromJson(messageObj.getMessageBody(), gameType);

                        logger.debug("received game obj: {}", gameInitInfo.toString());
                        initializeGame(gameInitInfo);
                        view.setGame(gameInitInfo);
                        view.setPlayingBoardGui();
                        view.setRequestedSuit(game.getCurrentMoveDTO().getRequestedSuit());

                        break;
                    default:
                        System.out.println("Error switch");
                        // request new communication
                        break;
                }
                resource.getReceivedMessages().remove();
            }
            resource.setNewReceived(false);
        }
    }


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

    /*public void playOneTurn(MoveDTO moveDTO){
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

    }*/

    private void sendMoveDTO(MoveDTO moveDTO) {
        Gson gson = new Gson();
        String moveDTOstring = gson.toJson(moveDTO);
        resource.addTask(new ComTask(game.getCurrentPlayerIdTurn(), "MOVE", moveDTOstring));
    }


    ChangeListener isNewReceivedChangeListener = new ChangeListener() {
        @Override public void changed(ObservableValue o, Object oldVal, Object newVal){
            if ((boolean) newVal == true) {

                Platform.runLater( ()-> {
                    logger.debug("New message has been changed!");
                    proccessReceived();
                } );

            }
        }
    };

    /**
     * Sets the players, their order of the game, their playing cards.
     */
    public void initializeGame(GameModel gameInitInfo) {
        game = gameInitInfo;
        logger.debug("Game initilizied");
    }


    public void submitMoveFromView(List<Node> cardButtons, Suit requestedSuit) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        ArrayList<Card> moveCards = getSelectedCards(cardButtons);
        MoveDTO moveDTO = new MoveDTO(new Move(moveCards, requestedSuit));
        moveDTO.setUpcard(game.getUpcard());

        sendMoveDTO(moveDTO);

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)){
            setChangedSuit();

            //view.setNewUpdate();
            //game.setNextPlayersTurn();
            logger.debug("randomplayer's move follows");
            updateAndPlayNextTurn();
        }
    }

    public void submitMoveFromView(int numberOfCardsDrawn) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        MoveDTO moveDTO = new MoveDTO(new Move(numberOfCardsDrawn));
        moveDTO.setUpcard(game.getUpcard());

        sendMoveDTO(moveDTO);

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)){
            setChangedSuit();

            //view.setNewUpdate();
            //game.setNextPlayersTurn();
            updateAndPlayNextTurn();
        }
    }

    public void submitMoveFromView(MoveDTO moveDTO) {
        if (game.getCurrentPlayerIdTurn() != game.getThisPlayerId()) {
            return;
        }
        // WIN move
        moveDTO.setUpcard(game.getUpcard());

        sendMoveDTO(moveDTO);

        if (game.playMove(game.getCurrentPlayerIdTurn(), moveDTO)) {
            setChangedSuit();
            view.setNewUpdate();
        }
    }


}
