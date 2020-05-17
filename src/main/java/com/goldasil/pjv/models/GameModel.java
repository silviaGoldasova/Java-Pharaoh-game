package com.goldasil.pjv.models;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.GameState;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Represents model for the game play.
 * Holds the states of the basic entities in the game - players, a stock, a waste.
 */
public class GameModel {

    List<Player> players; // set to the current player
    LinkedList <Card> stock;
    LinkedList <Card> waste;
    LinkedList <Card> selectedCards;
    GameState currentState = GameState.FIRST_MOVE;
    static final int CARDS_TO_DEAL = 5;

    private static final Logger logger = LoggerFactory.getLogger(GameModel.class);

    /**
     * Creates a new game model with an initial state.
     */
    public GameModel() {
        players = new ArrayList<Player>();
        currentState = GameState.FIRST_MOVE;
    }

    /**
     * Makes all changes (of the states of the entities of the game) caused by a specific move according to the type of the move.
     * @param playerID ID of the player who made the move
     * @param move the move to process
     */
    public void playMove(int playerID, MoveDTO move) {
        logger.debug("Move {} to be played.", move);
        Player player = getPlayerByID(playerID);

        switch (move.getMoveType()) {
            case PLAY:
                player.removeCards(move.getMove());
                logger.debug("move: {}, cards after the move: {}", move.getMove().toString(), player.getCards().toString());
                addCardsToWaste(move.getMove());
                break;
            case DRAW:
                for (int i = 0; i < move.getDrawCards(); i++) {
                    if (stock.size() < 1) {
                        turnWasteOver();
                    }
                    player.addCard(stock.remove());
                }
                break;
            case PASS:
                break;
            case WIN:
                break;

        }
    }

    public void initGame() {
        Player p1 = new Player(1);
        Player p2 = new RandomPlayer(2);
        players.add(p1);
        players.add(p2);

        // initializes all players with cards and sets the rest of the cards of the pack to the stock
        dealCardsAndSetStock();
        logger.debug("Players and stock initialized.");
    }

    public Card getUpcard(){
        return waste.peek();
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
    private Player getPlayerByID(int seekedPlayersID) {
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
        stock = new LinkedList<Card>();
        for (Card card : pack) {
            stock.add(card);
        }
        logger.debug("stock: {}", stock.toString());
        waste = new LinkedList<Card>();
        selectedCards = new LinkedList<Card>();
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

        for (Rank rank : ranks){
            for (Suit suit : suits) {
                pack.add(new Card(rank, suit));
            }
        }
        return pack;
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
        /*if (waste.size() == 0) {
            return false;
        }*/
        for (Card card : waste) {
            stock.add(waste.remove());
        }
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
}
