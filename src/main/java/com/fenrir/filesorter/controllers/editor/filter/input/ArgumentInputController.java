package com.fenrir.filesorter.controllers.editor.filter.input;

import com.fenrir.filesorter.controllers.editor.filter.InputControllerMediator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public abstract class ArgumentInputController {
    private InputControllerMediator inputControllerMediator;
    @FXML private HBox inputHBox;

    public abstract String readArguments();

    public abstract void setArguments(String arg);

    @FXML
    public void deleteInputContainer() {
        inputControllerMediator.unregisterInputController(this);
    }

    public HBox getInputContainer() {
        return inputHBox;
    }

    public void setInputControllerMediator(InputControllerMediator inputControllerMediator) {
        this.inputControllerMediator = inputControllerMediator;
        inputControllerMediator.registerInputController(this);
    }
}
