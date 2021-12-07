package com.fenrir.filesorter.controllers.editor.filter.input;

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
        String width = widthInputField.getText();
        String height = heightInputField.getText();
        return !width.isEmpty() && !height.isEmpty() ? String.format("%sx%s", width, height) : "";
    }

    @Override
    public void setArguments(String arg) {
        String[] dimension = arg.split("x");
        widthInputField.setText(dimension[0]);
        heightInputField.setText(dimension[1]);
    }
}
