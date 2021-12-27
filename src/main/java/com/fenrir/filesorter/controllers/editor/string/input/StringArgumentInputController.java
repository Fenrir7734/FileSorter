package com.fenrir.filesorter.controllers.editor.string.input;

import com.fenrir.filesorter.controllers.confirm.ConfirmController;
import com.fenrir.filesorter.controllers.confirm.ConfirmationController;
import com.fenrir.filesorter.controllers.editor.string.InputControllerMediator;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.fxml.FXML;

public abstract class StringArgumentInputController implements ConfirmationController {
    @FXML private ConfirmController confirmController;

    private InputControllerMediator inputControllerMediator;

    private ProviderType providerType;

    @FXML
    public void initialize() {
        confirmController.setParent(this);
    }

    public void sendArguments(String args) {
        inputControllerMediator.sendArgument(args, providerType);
    }

    public void setInputControllerMediator(InputControllerMediator inputControllerMediator) {
        this.inputControllerMediator = inputControllerMediator;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }
}
