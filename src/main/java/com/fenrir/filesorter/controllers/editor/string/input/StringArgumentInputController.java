package com.fenrir.filesorter.controllers.editor.string.input;

import com.fenrir.filesorter.controllers.confirm.ConfirmController;
import com.fenrir.filesorter.controllers.confirm.ConfirmationController;
import com.fenrir.filesorter.controllers.editor.string.InputControllerMediator;
import javafx.fxml.FXML;

public abstract class StringArgumentInputController implements ConfirmationController {
    @FXML private ConfirmController confirmController;

    private InputControllerMediator inputControllerMediator;

    @FXML
    public void initialize() {
        confirmController.setParent(this);
    }

    public void sendArguments(String args) {
        inputControllerMediator.sendArgument(args);
    }

    public void setInputControllerMediator(InputControllerMediator inputControllerMediator) {
        this.inputControllerMediator = inputControllerMediator;
    }
}
