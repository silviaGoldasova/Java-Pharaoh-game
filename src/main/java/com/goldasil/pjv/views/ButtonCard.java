package com.goldasil.pjv.views;

import com.goldasil.pjv.enums.Rank;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.List;

public class ButtonCard extends Button {

    public final ObjectProperty<Enum<?>> buttonRank = new SimpleObjectProperty<>(Rank.UNSPECIFIED);
    public final ObjectProperty<Enum<?>> buttonSuit = new SimpleObjectProperty<>(Suit.UNSPECIFIED);


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
        /*styleProperty().bind(Bindings.
                when(buttonState.isEqualTo(ButtonState.CRITICAL)).
                then("-fx-base: red;").
                otherwise(""));*/
    }

    public ButtonCard(String label) {
        super(label);
    }

    public ButtonCard(Card card) {
        super(card.toStringForGUI());
        setButtonRank(card.getRank());
        setButtonSuit(card.getSuit());
    }

    public ButtonCard(Rank rank, Suit suit) {
        super();
        setStyle("-fx-border-width: 0px; -fx-background-radius: 12;");
        setPadding(new Insets(4, 4, 4, 4));
        setButtonRank(rank);
        setButtonSuit(suit);
    }

    public ButtonCard(ButtonCard cardButton) {
        super(cardButton.getText());
        setStyle("-fx-border-width: 0px; -fx-background-radius: 12;");
        setPadding(new Insets(4, 4, 4, 4));
        setButtonRank(cardButton.getButtonRank());
        setButtonSuit(cardButton.getButtonSuit());
    }

    public ButtonCard(Suit suit) {
        setStyle("-fx-border-width: 0px; -fx-background-radius: 12;");
        setPadding(new Insets(4, 4, 4, 4));
        setButtonSuit(suit);
        //setMaxHeight(10);
        //setMaxWidth(10);
    }

    public static boolean isOverKnaveSubmitted(ObservableList<Node> buttonCards) {
        for (Node butCard : buttonCards) {
            if ( ((ButtonCard) butCard).getButtonRank() == Rank.OVERKNAVE) {
                return true;
            }
        }
        return false;
    }

    public void highlightButton() {
        setStyle("-fx-background-color: #d6aaaa; -fx-border-width: 0px; -fx-background-radius: 12;");
    }

    public void unHighlightButton() {
        setStyle("-fx-background-color: #faf0e6; -fx-border-width: 0px; -fx-background-radius: 12;");
    }

}
