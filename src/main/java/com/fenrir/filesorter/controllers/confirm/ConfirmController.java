package com.fenrir.filesorter.controllers.confirm;

import javafx.fxml.FXML;

public class ConfirmController {
    private ConfirmationController parentController;

    @FXML
    public void confirm() {
        parentController.confirm();
    }

    @FXML
    public void cancel() {
        parentController.close();
    }

    public void setParent(ConfirmationController editorController) {
        parentController = editorController;
    }
}
