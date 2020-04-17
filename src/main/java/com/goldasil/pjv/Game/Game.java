package com.goldasil.pjv.Game;

import com.goldasil.pjv.PersonPlayer;
import com.goldasil.pjv.RandomPlayer;
import com.goldasil.pjv.circularLinkedList.CircularLinkedList;
import com.goldasil.pjv.dto.OpponentMoveDTO;

public class Game {

    CircularLinkedList players; // set to the current player
    OpponentMoveDTO lastMoveDTO;
    OpponentMoveDTO currentMoveDTO;


    public Game(){
        players = new CircularLinkedList();
    }

    private void initializeGame() {
        players.insertFirst(new PersonPlayer());
        players.insertAfterActual(new PersonPlayer());
    }


    /*public void runGame() {
        while() {

            switch(){

            }

        }
    }*/







}
