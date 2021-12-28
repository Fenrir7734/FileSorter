package com.fenrir.filesorter.controllers.editor.string.input;

import com.fenrir.filesorter.controllers.confirm.ConfirmController;
import com.fenrir.filesorter.controllers.confirm.ConfirmationController;
import com.fenrir.filesorter.controllers.editor.string.InputControllerMediator;
import com.fenrir.filesorter.controllers.editor.string.StringRuleBuilderController;
import javafx.fxml.FXML;

public abstract class StringArgumentInputController implements ConfirmationController {
    @FXML private ConfirmController confirmController;

    private InputControllerMediator inputControllerMediator;
    private StringRuleBuilderController.ProviderArgPair providerArgPair;

    @FXML
    public void initialize() {
        confirmController.setParent(this);
    }

    public void setInputControllerMediator(InputControllerMediator inputControllerMediator) {
        this.inputControllerMediator = inputControllerMediator;
    }

    public void setProviderArgPair(StringRuleBuilderController.ProviderArgPair pair) {
        this.providerArgPair = pair;
    }

    public void sendArguments(String args) {
        providerArgPair.setArgs(args);
        inputControllerMediator.sendProviderData(providerArgPair);
    }
}
