package com.goldasil.pjv;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.goldasil.pjv.dto.OpponentMoveDTO;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import java.io.IOException;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws IOException {

        Rank rank = Rank.SEVEN;
        Card card = new Card(Rank.EIGHT, Suit.HEARTS);
        Card card2 = new Card(Rank.OVERKNAVE, Suit.LEAVES);
        //System.out.println(Rank.getEnumFromStr("eight"));
        //System.out.println(Rank.EIGHT.name());

        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        cards.add(card2);
        OpponentMoveDTO move = new OpponentMoveDTO(MoveType.PLAY, cards, 3, Suit.ACORNS, 5, null);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(move);
        //System.out.println(jsonString);

        String jsonRank = mapper.writeValueAsString(rank);
        System.out.println(jsonRank);

        String cardStr = mapper.writeValueAsString(card2);
        System.out.println(cardStr);
        Card cardBack = mapper.readValue(cardStr, Card.class);
        System.out.println(cardBack);
        System.out.println(card.getSuit());




            OpponentMoveDTO moveBack = mapper.readValue(jsonString, OpponentMoveDTO.class);
        System.out.println(moveBack);
    }
}


