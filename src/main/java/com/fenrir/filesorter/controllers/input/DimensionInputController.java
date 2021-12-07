package com.fenrir.filesorter.controllers.input;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class DimensionInputController extends ArgumentInputController {
    @FXML private TextField widthInputField;
    @FXML private TextField heightInputField;

    @FXML
    public void initialize() {
        widthInputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d")) {
                widthInputField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        heightInputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d")) {
                heightInputField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @Override
    public String readArguments() {
        return String.format("%sx%s", widthInputField.getText(), heightInputField.getText());
    }
}
