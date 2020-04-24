import com.goldasil.pjv.MoveStateHandler;
import com.goldasil.pjv.enums.MoveState;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class MoveStateHandlerTest {

    MoveStateHandler handler = new MoveStateHandler();

    @Test
    public void isValidTransitionGivenWrongTransitionShouldReturnFalse() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.NONSPECIAL_SITUATION);
        previousStates.add(MoveState.ACORNS_PLAYED);
        previousStates.add(MoveState.NINE_PLAYED);
        desiredStates.add(MoveState.PASS);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertFalse("Transition from " + previousStates + " to " + desiredStates + " should be false, not true.", isValid);
    }

    @Test
    public void isValidTransitionGiven_OVERKNAVE_LEAVES_ACORNS_PLAYED_ShouldReturnFalse() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.OVERKNAVE_LEAVES);
        previousStates.add(MoveState.LEAVES_PLAYED);
        previousStates.add(MoveState.OVERKNAVE_PLAYED);
        desiredStates.add(MoveState.ACORNS_PLAYED);
        desiredStates.add(MoveState.NINE_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertFalse("Transition from " + previousStates + " to " + desiredStates + " should be false, not true.", isValid);
    }

    @Test
    public void isValidTransition_OVERKNAVE_LEAVES_SEVENS_PLAYED_ShouldReturnFalse() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.OVERKNAVE_LEAVES);
        previousStates.add(MoveState.LEAVES_PLAYED);
        desiredStates.add(MoveState.SEVENS_PLAYED);
        desiredStates.add(MoveState.ACORNS_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertFalse("Transition from " + previousStates + " to " + desiredStates + " should be false, not true.", isValid);
    }

    @Test
    public void isValidTransition_OVERKNAVE_BELLS_SEVENS_PLAYED_ShouldReturnFalse() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.OVERKNAVE_BELLS);
        previousStates.add(MoveState.BELLS_PLAYED);
        desiredStates.add(MoveState.SEVENS_PLAYED);
        desiredStates.add(MoveState.HEARTS_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertFalse("Transition from " + previousStates + " to " + desiredStates + " should be false, not true.", isValid);
    }

    @Test
    public void isValidTransitionGiven_NONSPECIAL_SITUATION_SAME_RANK_ShouldReturnTrue() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.NONSPECIAL_SITUATION);
        previousStates.add(MoveState.NINE_PLAYED);
        previousStates.add(MoveState.BELLS_PLAYED);
        desiredStates.add(MoveState.NONSPECIAL_SITUATION);
        desiredStates.add(MoveState.NINE_PLAYED);
        desiredStates.add(MoveState.LEAVES_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertTrue("Transition from " + previousStates + " to " + desiredStates + " should be true, not false.", isValid);
    }

    @Test
    public void isValidTransitionGiven_NONSPECIAL_SITUATION_INCORRECT_MOVE_ShouldReturnFalse() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.NONSPECIAL_SITUATION);
        previousStates.add(MoveState.NINE_PLAYED);
        previousStates.add(MoveState.BELLS_PLAYED);
        desiredStates.add(MoveState.NONSPECIAL_SITUATION);
        desiredStates.add(MoveState.TEN_PLAYED);
        desiredStates.add(MoveState.LEAVES_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertFalse("Transition from " + previousStates + " to " + desiredStates + " should be false, not true.", isValid);
    }

    @Test
    public void isValidTransition_LOOKING_FOR_SEVEN_HEARTS_RETURN_NINE_PLAYED_ShouldReturnTrue() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN);
        previousStates.add(MoveState.ACORNS_PLAYED);
        previousStates.add(MoveState.NINE_PLAYED);
        desiredStates.add(MoveState.ACORNS_PLAYED);
        desiredStates.add(MoveState.EIGHT_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertTrue("Transition from " + previousStates + " to " + desiredStates + " should be true, not false.", isValid);
    }

    @Test
    public void isValidTransition_LOOKING_FOR_SEVEN_HEARTS_RETURN_INCORRECT_PLAYED_ShouldReturnFalse() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN);
        previousStates.add(MoveState.ACORNS_PLAYED);
        previousStates.add(MoveState.NINE_PLAYED);
        desiredStates.add(MoveState.BELLS_PLAYED);
        desiredStates.add(MoveState.EIGHT_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertFalse("Transition from " + previousStates + " to " + desiredStates + " should be false, not true.", isValid);
    }

    @Test
    public void isValidTransition_LOOKING_FOR_SEVEN_HEARTS_RETURN_SEVEN_HEARTS_RETURN_PLAYED_ShouldReturnTrue() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.LOOKING_FOR_SEVEN_HEARTS_RETURN);
        previousStates.add(MoveState.OVERKNAVE_LEAVES);
        previousStates.add(MoveState.LEAVES_PLAYED);
        desiredStates.add(MoveState.SEVEN_HEARTS_RETURN_PLAYED);
        desiredStates.add(MoveState.HEARTS_PLAYED);
        desiredStates.add(MoveState.SEVEN_PLAYED);

        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertTrue("Transition from " + previousStates + " to " + desiredStates + " should be true, not false.", isValid);
    }

    @Test
    public void isValidTransition_NONSPECIAL_SITUATION_OVERKNAVE_ShouldReturnTrue() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.NONSPECIAL_SITUATION);
        previousStates.add(MoveState.KING_PLAYED);
        previousStates.add(MoveState.BELLS_PLAYED);

        desiredStates.add(MoveState.OVERKNAVE);
        desiredStates.add(MoveState.HEARTS_PLAYED);
        desiredStates.add(MoveState.LEAVES_PLAYED);
        desiredStates.add(MoveState.ACORNS_PLAYED);
        desiredStates.add(MoveState.BELLS_PLAYED);


        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertTrue("Transition from " + previousStates + " to " + desiredStates + " should be true, not false.", isValid);
    }

    @Test
    public void isValidTransition_OVERKNAVE_TO_OVERKNAVE_ShouldReturnTrue() {
        ArrayList<MoveState> previousStates = new ArrayList<>();
        ArrayList<MoveState> desiredStates = new ArrayList<>();
        previousStates.add(MoveState.OVERKNAVE_ACORNS);
        previousStates.add(MoveState.OVERKNAVE_PLAYED);
        previousStates.add(MoveState.ACORNS_PLAYED);

        desiredStates.add(MoveState.OVERKNAVE);
        desiredStates.add(MoveState.HEARTS_PLAYED);
        desiredStates.add(MoveState.LEAVES_PLAYED);
        desiredStates.add(MoveState.ACORNS_PLAYED);
        desiredStates.add(MoveState.BELLS_PLAYED);


        boolean isValid = handler.isValidTransition(previousStates, desiredStates);
        Assert.assertTrue("Transition from " + previousStates + " to " + desiredStates + " should be true, not false.", isValid);
    }




}
