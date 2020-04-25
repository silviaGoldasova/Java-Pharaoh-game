import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.Move;
import com.goldasil.pjv.models.RandomPlayer;
import org.junit.Assert;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.logging.Logger;

public class RandomPlayerTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RandomPlayerTest.class);

    RandomPlayer player = new RandomPlayer(1);


    public RandomPlayerTest() {

    }

    @Test
    public void chooseMoveFromHandTest(){
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(new Card(Rank.EIGHT, Suit.ACORNS));
        hand.add(new Card(Rank.NINE, Suit.ACORNS));
        hand.add(new Card(Rank.KING, Suit.ACORNS));
        hand.add(new Card(Rank.KING, Suit.LEAVES));
        hand.add(new Card(Rank.KING, Suit.HEARTS));
        player.setCards(hand);
        Move generatedMove = player.chooseMoveFromHand(new Card(Rank.TEN, Suit.ACORNS));

        //right answer
        ArrayList<Card> correctMoveCards = new ArrayList<>();
        correctMoveCards.add(new Card(Rank.KING, Suit.ACORNS));
        correctMoveCards.add(new Card(Rank.KING, Suit.HEARTS));
        correctMoveCards.add(new Card(Rank.KING, Suit.LEAVES));
        Move correctMove = new Move(correctMoveCards);

        Assert.assertTrue("Correct move should be " + correctMove + " not " + generatedMove + ".", player.compareMoves(correctMove, generatedMove));
    }

}
