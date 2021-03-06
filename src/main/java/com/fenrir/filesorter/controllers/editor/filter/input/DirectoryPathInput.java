package com.fenrir.filesorter.controllers.editor.filter.input;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class DirectoryPathInput extends ArgumentInputController {
    @FXML
    private TextField inputTextField;

    @FXML
    public void selectPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(getInputContainer().getScene().getWindow());

        if (selectedDirectory != null) {
            inputTextField.setText(selectedDirectory.getAbsolutePath());
        }
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
