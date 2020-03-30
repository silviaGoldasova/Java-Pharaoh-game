package com.goldasil.pjv;

import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;

public class Card {

    private Suit suit;
    private Rank rank;

    public Card(Rank rank, Suit suit) {
        this.suit = suit;
        this.rank = rank;
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
