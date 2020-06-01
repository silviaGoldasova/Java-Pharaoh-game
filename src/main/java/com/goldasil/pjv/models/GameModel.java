package com.goldasil.pjv.models;

import com.goldasil.pjv.ApplicationContextProvider;
import com.goldasil.pjv.MoveStateHandler;
import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.entity.GameEntity;
import com.goldasil.pjv.entity.GameRepository;
import com.goldasil.pjv.entity.GameService;
import com.goldasil.pjv.enums.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * Represents model for the game play.
 * Holds the states of the basic entity in the game - players, a stock, a waste.
 */
public class GameModel implements Cloneable {

    List<Player> players; // set to the current player
    LinkedList <Card> stock;
    LinkedList <Card> waste;
    LinkedList <Card> selectedCards;
    GameState currentState = GameState.FIRST_MOVE;
    static final int CARDS_TO_DEAL = 5;
    Card upcard;
    MoveStateHandler moveStateHandler;
    int currentPlayerIdTurn = 0;
    int thisPlayerId = 0;

    MoveDTO lastMoveDTO = null;
    volatile MoveDTO currentMoveDTO = null;

    private SimpleIntegerProperty winnerID = new SimpleIntegerProperty();

    private static final Logger logger = LoggerFactory.getLogger(GameModel.class);

    /**
     * Creates a new game model with an initial state.
     */
    public GameModel() {
        players = new ArrayList<Player>();
        currentState = GameState.FIRST_MOVE;
        moveStateHandler = new MoveStateHandler();
    }

    /**
     * Makes all changes (of the states of the entity of the game) caused by a specific move according to the type of the move.
     * @param playerID ID of the player who made the move
     * @param move the move to process
     */
    public boolean playMove(int playerID, MoveDTO move) {
        Player player = getPlayerByID(playerID);
        move.setPenaltyForSevens(move.getPenaltyForDesired(lastMoveDTO.getPenaltyForSevens()));
        move.setUpcard(upcard);
        logger.debug("Move {} to be played. Prev Move: {}", move, lastMoveDTO.toString());

        if (!moveStateHandler.isValidMove(lastMoveDTO, move, player.getCards().size())) {
            logger.debug("Invalid move");
            return false;
        }
        logger.debug("Valid move");
        checkRequestedSuit(lastMoveDTO, move);
        lastMoveDTO = move;
        currentMoveDTO = move;

        switch (move.getMoveType()) {
            case PLAY:
                player.removeCards(move.getMove());
                addCardsToWaste(move.getMove());
                upcard = waste.getLast();

                logger.debug("move: {}, cards after the move: {}", move.getMove().toString(), player.getCards().toString());
                break;
            case DRAW:
                for (int i = 0; i < move.getDrawCards(); i++) {
                    if (stock.size() < 1) {
                        turnWasteOver();
                    }
                    player.addCard(stock.remove());
                }
                move.setPenaltyForSevens(0);
                break;
            case PASS:
                break;
            case WIN:
                logger.debug("Player with id {} has won!!!!!", playerID);
                winnerID.setValue(playerID);
                break;
        }
        return true;
    }

    private void checkRequestedSuit(MoveDTO prevMoveDTO, Move desiredMove) {
        if (desiredMove.getDrawCards() == 1) {
            desiredMove.setRequestedSuit(prevMoveDTO.getRequestedSuit());
        }
    }

    @Bean
    public void saveGame(String mainPlayerName, String password, Suit requestedSuit) {
        GameService service  = ApplicationContextProvider.getBean(GameService.class);
        service.save(mainPlayerName, players, stock, waste, upcard, lastMoveDTO, currentPlayerIdTurn, password);
    }

    public void initGame(int numberOfRandomPlayers) {
        players = new ArrayList<Player>();

        Player p1 = new Player(0);
        players.add(p1);

        for (int i = 1; i <= numberOfRandomPlayers; i++) {
            Player player = new RandomPlayer(i);
            players.add(player);
        }

        // initializes all players with cards and sets the rest of the cards of the pack to the stock
        dealCardsAndSetStock();
        winnerID.setValue(-1);
        logger.debug("Players and stock initialized.");
    }

    public void initGameMultiplayer(int numberOfPlayers) {
        players = new ArrayList<Player>();

        Player p1 = new Player(0);
        players.add(p1);

        for (int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player(i);
            players.add(player);
        }

        // initializes all players with cards and sets the rest of the cards of the pack to the stock
        dealCardsAndSetStock();
        winnerID.setValue(-1);
        logger.debug("Players and stock initialized.");
    }

    public void initGame(List<Player> players, LinkedList<Card> stock, LinkedList<Card> waste, Card upcard, MoveDTO move, int currentPlayerToPlay){
        thisPlayerId = 0;
        players = new ArrayList<Player>();

        for (Player player : players) {
            Player newPlayer;
            if (player.getPlayerID() == 0) {
                newPlayer = new Player(player.getCards(), player.getPlayerID());
            } else {
                newPlayer = new RandomPlayer(player.getCards(), player.getPlayerID());
            }
            this.players.add(newPlayer);
        }

        this.stock = stock;
        this.waste = waste;
        this.upcard = upcard;
        this.lastMoveDTO = move;
        this.currentMoveDTO = move;
        currentPlayerIdTurn = currentPlayerToPlay;
        winnerID.setValue(-1);
        logger.debug("Players and stock initialized.");
        logger.debug("Players initialized as follows: {}, current player id: {}, current move:{}", players.toString(), currentPlayerIdTurn, currentMoveDTO);
    }

    private MoveDTO getPrevFirstMoveDTO(){

        Card lastCard;
        if (stock.size() == 0) {
            lastCard = upcard;
        } else {
            lastCard = stock.getLast();
        }
        Rank upcardRank = upcard.getRank();

        if (upcardRank != lastCard.getRank() && upcardRank != Rank.OVERKNAVE) {
            MoveDTO moveDTO = new MoveDTO(new Move(1));
            moveDTO.setUpcard(upcard);
            return moveDTO;
        }

        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setUpcard(upcard);
        ArrayList<Card> moveCards = new ArrayList<>();
        moveDTO.setMoveType(MoveType.PLAY);

        switch (upcard.getRank()) {
            case SEVEN:
                moveCards.add(lastCard);
                moveCards.add(upcard);
                moveDTO.setMove(moveCards);
                moveDTO.setPenaltyForSevens(3);
                break;
            case OVERKNAVE:
                moveCards.add(upcard);
                moveDTO.setRequestedSuit(lastCard.getSuit());
                break;
            case ACE:
                moveCards.add(upcard);
                moveDTO.setMove(moveCards);
                moveDTO.addState(MoveState.ACES_PLAYED);
                moveDTO.setPlayerIdAcesStarter(1);
                moveDTO.setNumAcesPlayed(1);
                break;
            default:
                moveCards.add(upcard);
        }
        moveDTO.setMove(moveCards);
        return moveDTO;
    }

    public boolean runOppTurn() {

        Player player = getPlayerByID(currentPlayerIdTurn);
        Move desiredMove = player.chooseMove(lastMoveDTO);
        MoveDTO moveDTO = new MoveDTO(desiredMove);
        moveDTO.setUpcard(getUpcard());

        logger.debug("move generated by randomplayer: ", moveDTO.toString());

        boolean isMoveCorrect = playMove(currentPlayerIdTurn, moveDTO);

        if (!isMoveCorrect) {
            logger.debug("Incorrect move from the randomplayer");
            return false;
        }

        return true;

    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a new player with the specified ID to the game.
     * @param playerID ID of the new player
     */
    public void addPlayer(int playerID){
        players.add(new Player(playerID));
    }

    /**
     * Gets the player with the specified ID number.
     * @param seekedPlayersID the ID of the player that we are searching for.
     * @return player of the ID number seekedPlayersID
     */
    public Player getPlayerByID(int seekedPlayersID) {
        for (Player player : players) {
            if (player.playerID == seekedPlayersID) {
                return player;
            }
        }
        return null;
    }

    /**
     * Adds specified cards, played in a move, to the waste.
     * @param move list of played cards to be added to the waste
     */
    private void addCardsToWaste(List<Card> move) {
        for (Card card : move) {
            waste.add(card);
        }
    }

    /**
     * Pops the specified number of cards a player had drawn from the stock.
     * @param numCardsDrawn number of cards to pop
     */
    public void popDrawnFromStock(int numCardsDrawn) {
        for (int i = 0; i < numCardsDrawn; i++) {
            if (stock.size() < 1) {
                turnWasteOver();
            }
            stock.remove();
        }
    }

    /**
     * Deals the cards to the players.
     * Additionally, it sets the stock and waste.
     */
    public void dealCardsAndSetStock() {
        LinkedList<Card> pack = getPack();
        Collections.shuffle(pack);
        for (Player player : players) {
            player.setCards(getCardsFromDeck(pack, CARDS_TO_DEAL));
        }
        upcard = pack.remove();
        stock = new LinkedList<Card>();
        for (Card card : pack) {
            stock.add(card);
        }
        logger.debug("stock size: {}, stock: {}", stock.size(), stock.toString());
        waste = new LinkedList<Card>();
        waste.add(upcard);
        selectedCards = new LinkedList<Card>();
        lastMoveDTO = getPrevFirstMoveDTO();
        currentMoveDTO = lastMoveDTO;
        logger.debug("Prev moveDTO: {}", currentMoveDTO.toString());
    }

    /**
     * Checks the state of the stock after each move.
     * Turns the waste over if the stock is empty.
     */
    public void afterMoveCheck() {
        if (isStockEmpty()) {
            turnWasteOver();
        }
    }

    /**
     * Generates the whole pack of German playing cards.
     * @return the pack of cards
     */
    private LinkedList<Card> getPack(){
        LinkedList<Card> pack = new LinkedList<Card>();

        List<Suit> suits = new ArrayList<>(Arrays.asList(Suit.values()));
        suits.remove(Suit.UNSPECIFIED);

        List<Rank> ranks = new ArrayList<>(Arrays.asList(Rank.values()));
        ranks.remove(Rank.UNSPECIFIED);
        ranks.remove(Rank.ACE);

        for (Rank rank : ranks){
            for (Suit suit : suits) {
                pack.add(new Card(rank, suit));
            }
        }
        return pack;
    }

    public void setNextPlayersTurn(){
        currentPlayerIdTurn = (currentPlayerIdTurn + 1) % players.size();
    }

    /**
     * Gets a specified number of cards from the specified deck of cards, pops them from the source deck.
     * @param deck a list of cards from which to take cards
     * @param numOfCards number of cards to get
     * @return generated deck of cards
     */
    private ArrayList<Card> getCardsFromDeck(LinkedList<Card> deck, int numOfCards) {
        if (numOfCards > deck.size()) {
            // exception
            System.out.println("Method getAndRemoveCards:  numOfCards > deck size");
        }
        ArrayList<Card> cardProportion = new ArrayList<Card>();
        while (numOfCards > 0) {
            cardProportion.add(deck.remove());
            numOfCards--;
        }
        return cardProportion;
    }

    /**
     * Checks the state of the stock.
     * @return true if the stock is empty.
     */
    private boolean isStockEmpty() {
        return stock.size() == 0;
    }

    /**
     * Turns a list of cards in waste over to a new stock.
     * Leaves waste empty
     */
    private void turnWasteOver () {
        //upcard = new Card(waste.getLast().getRank(), waste.getLast().getSuit());

        if (waste.size() == 0) {
            return;
        }

        for (Card card : new LinkedList<>(waste)) {
            stock.add(card);
            waste.remove(card);
        }

        waste.add(stock.removeLast());
        logger.debug("waste turned over");

        /*for (Card card : waste) {
            stock.add(new Card(card.getRank(), card.getSuit()));
        }
        upcard = new Card(waste.getLast().getRank(), waste.getLast().getSuit());
        for (Card card : new LinkedList<>(waste)) {
            stock.add(waste.remove());
        }*/
    }

    @Override
    public String toString() {
        return "GameModel{" +
                "players=" + players +
                ", stock=" + stock +
                ", waste=" + waste +
                ", selectedCards=" + selectedCards +
                ", currentState=" + currentState +
                ", upcard=" + upcard +
                ", moveStateHandler=" + moveStateHandler +
                ", currentPlayerIdTurn=" + currentPlayerIdTurn +
                ", thisPlayerId=" + thisPlayerId +
                ", lastMoveDTO=" + lastMoveDTO +
                ", currentMoveDTO=" + currentMoveDTO +
                ", winnerID=" + winnerID +
                '}';
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Peeked at the last card of the stock. Necessary action at the game start.
     * @return the last card of the stock
     */
    Card getLastStockCard() {
        return stock.getLast();
    }

    /**
     * Gets the players playing in the game.
     * @return a list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the stock of the game.
     * @return a list of cards in stock
     */
    public LinkedList<Card> getStock() {
        return stock;
    }

    /**
     * sets the stock of the game.
     * @param stock a list of cards in stock
     */
    public void setStock(LinkedList<Card> stock) {
        this.stock = stock;
    }

    /**
     * Gets the waste of the game
     * @return a list of cards in the waste
     */
    public LinkedList<Card> getWaste() {
        return waste;
    }

    /**
     * Sets the waste of the game
     * @param waste a list of cards in the waste
     */
    public void setWaste(LinkedList<Card> waste) {
        this.waste = waste;
    }

    /**
     * Gets current state of the game.
     * @return the current game state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Sets the new state of the game.
     * @param newState new state to which the game is to move
     */
    public void setCurrentState(GameState newState) {
        this.currentState = newState;
    }

    public Card getUpcard() {
        return upcard;
    }

    public void setUpcard(Card upcard) {
        this.upcard = upcard;
    }

    public int getCurrentPlayerIdTurn() {
        return currentPlayerIdTurn;
    }

    public void setCurrentPlayerIdTurn(int currentPlayerIdTurn) {
        this.currentPlayerIdTurn = currentPlayerIdTurn;
    }

    public int getThisPlayerId() {
        return thisPlayerId;
    }

    public void setThisPlayerId(int thisPlayerId) {
        this.thisPlayerId = thisPlayerId;
    }

    public MoveDTO getCurrentMoveDTO() {
        return currentMoveDTO;
    }

    public void setCurrentMoveDTO(MoveDTO currentMoveDTO) {
        this.currentMoveDTO = currentMoveDTO;
    }

    public int getWinnerID() {
        return winnerID.get();
    }

    public SimpleIntegerProperty winnerIDProperty() {
        return winnerID;
    }

    public void setWinnerID(int winnerID) {
        this.winnerID.set(winnerID);
    }
}
