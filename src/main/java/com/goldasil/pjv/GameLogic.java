package com.goldasil.pjv;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GameLogic {

    Player playerMain;
    Player playerOpp;
    LinkedList <Card> stock;
    LinkedList <Card> waste;

    public void playMove(Player player, MoveDTO move) {

        switch (move.getMoveType()) {
            case PLAY:
                player.removeCards(move.getMove());
                addCardsToStock(move.getMove());
                break;
            case DOUBLE_PLAY:
                break;
            case DRAW:
                for (int i = 0; i < move.getDrawCards(); i++) {
                    player.addCard(stock.remove());
                }
                break;
            case PASS:
                break;
            case LOSE:
                break;
        }


    }

    private void addCardsToStock(ArrayList<Card> move) {
        for (Card card : move) {
            stock.add(card);
        }
    }

    public void popDrawnFromStock(int numCardsDrawn) {
        for (int i = 0; i < numCardsDrawn; i++) {
            stock.remove();
        }
    }

    public void dealCards() {

        LinkedList<Card> pack = getPack();
        Collections.shuffle(pack);
        playerMain.setCards(getCardsFromDeck(pack, 5));
        playerOpp.setCards(getCardsFromDeck(pack, 5));
        stock = new LinkedList<Card>();
        for (Card card : pack) {
            stock.add(card);
        }
        waste = new LinkedList<Card>();
    }

    public void afterMoveCheck() {
        if (isStockEmpty()) {
            turnWasteOver();
        }
    }

    private LinkedList<Card> getPack(){
        LinkedList<Card> pack = new LinkedList<Card>();
        for (Rank rank : Rank.values()){
            for (Suit suit : Suit.values()) {
                pack.add(new Card(rank, suit));
            }
        }
        return pack;
    }

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

    private boolean isStockEmpty() {
        return stock.size() == 0;
    }

    private void turnWasteOver () {
        /*if (waste.size() == 0) {
            return false;
        }*/
        for (Card card : waste) {
            stock.add(waste.remove());
        }
    }

    Card getLastStockCard() {
        return stock.getLast();
    }


}
