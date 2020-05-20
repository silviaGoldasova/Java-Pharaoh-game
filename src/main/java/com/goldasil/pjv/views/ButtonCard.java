package com.goldasil.pjv.views;

import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;

public class ButtonCard extends Button {

    public final ObjectProperty<Enum<?>> buttonRank = new SimpleObjectProperty<>(Rank.UNSPECIFIED);
    public final ObjectProperty<Enum<?>> buttonSuit = new SimpleObjectProperty<>(Suit.UNSPECIFIED);

    private boolean selected;


    public final ObjectProperty<Enum<?>> buttonRankProperty() {
        return this.buttonRank;
    }

    public final ObjectProperty<Enum<?>> buttonSuitProperty() {
        return this.buttonSuit;
    }

    public final Enum<?> getButtonRank() {
        return this.buttonRankProperty().get();
    }

    public final Enum<?> getButtonSuit() {
        return this.buttonSuitProperty().get();
    }

    public final void setButtonRank(final Enum<?> buttonRank) {
        this.buttonRankProperty().set(buttonRank);
    }

    public final void setButtonSuit(final Enum<?> buttonSuit) {
        this.buttonSuitProperty().set(buttonSuit);
    }

    public ButtonCard() {
        super();
        selected = false;
        /*styleProperty().bind(Bindings.
                when(buttonState.isEqualTo(ButtonState.CRITICAL)).
                then("-fx-base: red;").
                otherwise(""));*/
    }

    public ButtonCard(String label) {
        super(label);
        selected = false;
    }

    public ButtonCard(Card card) {
        super(card.toStringForGUI());
        selected = false;
        setButtonRank(card.getRank());
        setButtonSuit(card.getSuit());
    }

    public ButtonCard(ButtonCard cardButton) {
        super(cardButton.getText());
        selected = false;
        setButtonRank(cardButton.getButtonRank());
        setButtonSuit(cardButton.getButtonSuit());
    }

}
