package com.fenrir.filesorter.controllers.editor.rename.input;

import com.fenrir.filesorter.controllers.confirm.ConfirmController;
import com.fenrir.filesorter.controllers.confirm.ConfirmationController;
import com.fenrir.filesorter.controllers.editor.rename.InputControllerMediator;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.fxml.FXML;

public abstract class StringArgumentInputController implements ConfirmationController {
    @FXML private ConfirmController confirmController;

    private InputControllerMediator inputControllerMediator;

    @FXML
    public void initialize() {
        confirmController.setParent(this);
    }

    public void sendArguments(String args, ProviderType type) {
        inputControllerMediator.sendArgument(args, type);
    }

    public void setInputControllerMediator(InputControllerMediator inputControllerMediator) {
        this.inputControllerMediator = inputControllerMediator;
    }
}
