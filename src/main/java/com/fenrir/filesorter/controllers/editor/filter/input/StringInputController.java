package com.fenrir.filesorter.controllers.editor.filter.input;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class StringInputController extends ArgumentInputController {
    @FXML private TextField inputTextField;

    @Override
    public String readArguments() {
        return inputTextField.getText();
    }

    @Override
    public void setArguments(String arg) {
        inputTextField.setText(arg);
    }
}
