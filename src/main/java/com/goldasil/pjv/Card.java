package com.goldasil.pjv;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;

public class Card {

    private Rank rank;
    private Suit suit;

    public Card(){
    }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /*@JsonCreator
    public Card(@JsonProperty("rank") Rank rank, @JsonProperty("suit") Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }*/

    @Override
    public String toString() {
        return "Card obj: rank: " + rank + ", suit: " + suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
