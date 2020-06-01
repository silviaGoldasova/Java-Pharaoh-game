package com.goldasil.pjv.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class TextFieldsButton extends Button {

    private ArrayList<TextField> textFields;


    public TextFieldsButton(String label) {
        super(label);
        setStyle("-fx-font-size: 1.5em; -fx-background-color: #faf0e6; -fx-border-width: 0px; -fx-background-radius: 10;");
        setMinWidth(180);
        setAlignment(Pos.CENTER);
        this.textFields = new ArrayList<>();
    }

    public void addTextField(TextField textField) {
        textFields.add(textField);
    }

    public void addAllTextField(TextField textField1, TextField textField2, TextField textField3) {
        textFields.add(textField1);
        textFields.add(textField2);
        textFields.add(textField3);

    }

    public String getTextInput(String textLabel){
        for (TextField field : textFields) {
            if ((field.getPromptText()).equals(textLabel)) {
                return field.getText();
            }
        }
        return null;
    }

    public boolean validateFields(){
        for (TextField field : textFields) {
            String input = field.getText();
            if (input.length() < 4) {
                return false;
            }
            for (char character : input.toCharArray()) {
                if (!(Character.isDigit(character) || character == '.')) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<TextField> getButtons() {
        return textFields;
    }

    public void setButtons(ArrayList<TextField> buttons) {
        this.textFields = buttons;
    }
}
