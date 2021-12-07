package com.fenrir.filesorter.controllers.input;

import com.fenrir.filesorter.controllers.InputControllerMediator;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public abstract class ArgumentInputController {
    private InputControllerMediator inputControllerMediator;
    @FXML
    private HBox inputHBox;

    public abstract String readArguments();

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
