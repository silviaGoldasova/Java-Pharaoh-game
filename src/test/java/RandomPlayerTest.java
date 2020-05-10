import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.MoveState;
import com.goldasil.pjv.enums.MoveType;
import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.models.Player;
import com.goldasil.pjv.models.RandomPlayer;
import org.junit.After;
import org.junit.Assert;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RandomPlayerTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RandomPlayerTest.class);

    Player player = new RandomPlayer(1);


    public RandomPlayerTest() {
    }

    @After
    public void space() {
        logger.debug("\n");
    }

    @Test
    public void chooseMoveFromHandTestNONSPECIAL_SITUATION_KING(){
        ArrayList<Card> hand = new ArrayList<>();
        hand = player.addCardsFromArgs(hand, new Card(Rank.EIGHT, Suit.ACORNS), new Card(Rank.NINE, Suit.ACORNS));
        hand = player.addCardsFromArgs(hand, new Card(Rank.KING, Suit.ACORNS), new Card(Rank.KING, Suit.LEAVES));
        hand.add(new Card(Rank.KING, Suit.HEARTS));
        player.setCards(hand);
        Move generatedMove = player.chooseMoveFromHand(new Card(Rank.TEN, Suit.ACORNS));

        //right answer
        ArrayList<Card> correctMoveCards = new ArrayList<>();
        correctMoveCards = player.addCardsFromArgs(correctMoveCards, new Card(Rank.KING, Suit.ACORNS), new Card(Rank.KING, Suit.HEARTS));
        correctMoveCards.add(new Card(Rank.KING, Suit.LEAVES));
        Move correctMove = new Move(correctMoveCards);

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveNONSPECIAL_SITUATION_KING(){
        // set player's cards in hand
        ArrayList<Card> hand = new ArrayList<>();
        hand = player.addCardsFromArgs(hand, new Card(Rank.EIGHT, Suit.ACORNS), new Card(Rank.NINE, Suit.ACORNS));
        hand = player.addCardsFromArgs(hand, new Card(Rank.KING, Suit.ACORNS), new Card(Rank.KING, Suit.LEAVES));
        player.setCards(hand);

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.TEN, Suit.ACORNS);
        prevMoveCards.add(upcard);
        MoveDTO moveDTO = new MoveDTO(new Move(prevMoveCards), upcard, 0);

        // generate move
        Move generatedMove = player.getMove(moveDTO);

        //right answer
        Move correctMove = Move.getMoveWithPlayedCards(new Card(Rank.KING, Suit.ACORNS), new Card(Rank.KING, Suit.LEAVES));

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveSEVENS_PLAYED_DRAW_PENALTY(){
        // set player's cards in hand
        ArrayList<Card> hand = new ArrayList<>();
        hand = player.addCardsFromArgs(hand, new Card(Rank.EIGHT, Suit.ACORNS), new Card(Rank.NINE, Suit.ACORNS));
        hand = player.addCardsFromArgs(hand, new Card(Rank.KING, Suit.ACORNS), new Card(Rank.KING, Suit.LEAVES));
        player.setCards(hand);

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.SEVEN, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards);
        MoveDTO moveDTO = new MoveDTO(moveObj, upcard, 6);

        // generate move
        Move generatedMove = player.getMove(moveDTO);

        //right answer
        Move correctMove = new Move(6);

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveSEVENS_PLAYED_WITH_UNDEERKNAVE(){
        // set player's cards in hand
        player.addCards(new Card(Rank.UNDERKNAVE, Suit.LEAVES), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCard(new Card(Rank.KING, Suit.LEAVES));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.SEVEN, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards);
        MoveDTO moveDTO = new MoveDTO(moveObj, upcard, 6);
        logger.debug("Prev moveDTO: {}", moveDTO.toString());

        // generate move
        Move generatedMove = player.getMove(moveDTO);

        //right answer
        Move correctMove = Move.getMoveWithPlayedCard(new Card(Rank.UNDERKNAVE, Suit.LEAVES));

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveSEVENS_PLAYED_WITH_SEVENS(){
        // set player's cards in hand
        player.addCards(new Card(Rank.UNDERKNAVE, Suit.LEAVES), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCards(new Card(Rank.KING, Suit.LEAVES), new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.SEVEN, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards);
        MoveDTO moveDTO = new MoveDTO(moveObj, upcard, 3);

        // generate move
        Move generatedMove = player.getMove(moveDTO);

        //right answer
        Move correctMove = Move.getMoveWithPlayedCards(new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS));
        correctMove.setDrawCards(0);

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveACES_ONLY_none(){
        // set player's cards in hand
        player.addCards(new Card(Rank.UNDERKNAVE, Suit.LEAVES), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCards(new Card(Rank.KING, Suit.LEAVES), new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.ACE, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards);
        MoveDTO prevMoveDTO = new MoveDTO(moveObj, upcard, 0);
        prevMoveDTO.addState(MoveState.ACES_ONLY);

        // generate move
        Move generatedMove = player.getMove(prevMoveDTO);

        //right answer
        Move correctMove = new Move(MoveType.PASS);

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }


    @Test
    public void getMoveACES_ONLY_ACE_PLAYED(){
        // set player's cards in hand
        player.addCards(new Card(Rank.ACE, Suit.LEAVES), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCards(new Card(Rank.KING, Suit.LEAVES), new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.ACE, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards);
        MoveDTO prevMoveDTO = new MoveDTO(moveObj, upcard, 0);
        prevMoveDTO.addState(MoveState.ACES_ONLY);

        // generate move
        Move generatedMove = player.getMove(prevMoveDTO);

        //right answer
        Move correctMove = Move.getMoveWithPlayedCard(new Card(Rank.ACE, Suit.LEAVES));

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveOVERKNAVE_SUIT_PLAYED(){
        // set player's cards in hand
        player.addCards(new Card(Rank.ACE, Suit.LEAVES), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCards(new Card(Rank.KING, Suit.LEAVES), new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.OVERKNAVE, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards, Suit.LEAVES);
        MoveDTO prevMoveDTO = new MoveDTO(moveObj, upcard, 0);

        // generate move
        Move generatedMove = player.getMove(prevMoveDTO);

        //right answer
        Move correctMove = Move.getMoveWithPlayedCards(new Card(Rank.KING, Suit.LEAVES), new Card(Rank.KING, Suit.ACORNS));

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }


    @Test
    public void getMoveOVERKNAVE_SUIT_NOT_PLAYED(){
        // set player's cards in hand
        player.addCards(new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCards(new Card(Rank.KING, Suit.HEARTS), new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.OVERKNAVE, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards, Suit.LEAVES);
        MoveDTO prevMoveDTO = new MoveDTO(moveObj, upcard, 0);

        // generate move
        Move generatedMove = player.getMove(prevMoveDTO);

        //right answer
        Move correctMove = new Move(1);

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveRETURN_TO_GAME_PLAYED(){
        // set player's cards in hand
        player.addCards(new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCards(new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS), new Card(Rank.EIGHT, Suit.BELLS));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.OVERKNAVE, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards, Suit.LEAVES);
        MoveDTO prevMoveDTO = new MoveDTO(moveObj, upcard, 0);
        prevMoveDTO.addState(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN);

        // generate move
        Move generatedMove = player.getMove(prevMoveDTO);

        //right answer
        Move correctMove = Move.getMoveWithPlayedCards(new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SEVEN, Suit.BELLS));

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

    @Test
    public void getMoveRETURN_TO_GAME_NOT_PLAYED(){
        // set player's cards in hand
        player.addCards(new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.NINE, Suit.ACORNS), new Card(Rank.KING, Suit.ACORNS));
        player.addCards(new Card(Rank.SEVEN, Suit.ACORNS), new Card(Rank.SEVEN, Suit.BELLS), new Card(Rank.EIGHT, Suit.BELLS));

        // prev move
        ArrayList<Card> prevMoveCards = new ArrayList<>();
        Card upcard = new Card(Rank.OVERKNAVE, Suit.ACORNS);
        prevMoveCards.add(upcard);
        Move moveObj = new Move(prevMoveCards, Suit.LEAVES);
        MoveDTO prevMoveDTO = new MoveDTO(moveObj, upcard, 0);
        prevMoveDTO.addState(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN);

        // generate move
        Move generatedMove = player.getMove(prevMoveDTO);

        //right answer
        Move correctMove = new Move(1);

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

}
