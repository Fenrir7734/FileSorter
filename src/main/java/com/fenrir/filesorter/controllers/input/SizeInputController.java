package com.fenrir.filesorter.controllers.input;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class SizeInputController extends ArgumentInputController {
    @FXML private TextField inputTextField;
    @FXML private ChoiceBox<String> postfixChoiceBox;

    @FXML
    public void initialize() {
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                inputTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        ObservableList<String> sizePostfixObservableList = FXCollections.observableList(
                List.of("B", "kB", "MB", "GB", "TB")
        );
        postfixChoiceBox.setItems(sizePostfixObservableList);
        postfixChoiceBox.getSelectionModel().select(0);
    }

    @Override
    public String readArguments() {
        String postfix = postfixChoiceBox.getSelectionModel().getSelectedItem();
        return inputTextField.getText() + postfix.replace("B", "");
    }
}
