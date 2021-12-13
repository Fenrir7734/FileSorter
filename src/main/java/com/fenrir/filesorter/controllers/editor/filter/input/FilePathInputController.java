package com.fenrir.filesorter.controllers.editor.filter.input;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class FilePathInputController extends ArgumentInputController {
    @FXML
    private TextField inputTextField;

    @FXML
    public void selectPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFile = fileChooser.showOpenDialog(getInputContainer().getScene().getWindow());

        if (selectedFile != null) {
            inputTextField.setText(selectedFile.getAbsolutePath());
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
