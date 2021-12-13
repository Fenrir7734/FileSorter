package com.fenrir.filesorter.controllers.editor.filter.input;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NumberInputController extends ArgumentInputController {
    @FXML private TextField inputTextField;

    @FXML
    public void initialize() {
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                inputTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @Override
    public String readArguments() {
        return inputTextField.getText();
    }

    @Override
    public void setArguments(String arg) {
        inputTextField.setText(arg);
    }
}
