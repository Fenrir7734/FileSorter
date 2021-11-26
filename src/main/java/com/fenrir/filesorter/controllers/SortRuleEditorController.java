package com.fenrir.filesorter.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SortRuleEditorController {
    @FXML private TextField ruleTextField;

    @FXML
    public void confirm() {

    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) ruleTextField.getScene().getWindow();
        stage.close();
    }
}
