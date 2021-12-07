package com.fenrir.filesorter.controllers.input;

import com.fenrir.filesorter.controllers.InputControllerMediator;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public abstract class ArgumentInputController {
    @FXML
    private HBox inputHBox;

    public ArgumentInputController() {
        InputControllerMediator.getInstance().registerInputController(this);
    }

    public abstract String readArguments();

    @FXML
    public void deleteInputContainer() {
        InputControllerMediator.getInstance().unregisterInputController(this);
    }

    public HBox getInputContainer() {
        return inputHBox;
    }

}
