package com.fenrir.filesorter.controllers.editor;

import javafx.fxml.FXML;

public class EditorConfirmController {
    private EditorController parentController;

    @FXML
    public void confirm() {
        parentController.confirm();
    }

    @FXML
    public void cancel() {
        parentController.close();
    }

    public void setParent(EditorController editorController) {
        parentController = editorController;
    }
}
