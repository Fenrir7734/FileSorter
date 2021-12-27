package com.fenrir.filesorter.controllers.editor.string.input;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TextInputController extends StringArgumentInputController {
    @FXML private VBox textInputVBox;
    @FXML private TextField inputTextField;

    @Override
    public void confirm() {
        super.sendArguments(inputTextField.getText());
        close();
    }

    @Override
    public void close() {
        Stage stage = (Stage) textInputVBox.getScene().getWindow();
        stage.close();
    }
}
