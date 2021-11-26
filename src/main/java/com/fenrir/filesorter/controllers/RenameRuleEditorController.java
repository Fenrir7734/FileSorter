package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.rule.StringRule;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RenameRuleEditorController implements Controller {
    @FXML private TextField ruleTextField;

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerController(this);
    }

    @FXML
    public void confirm() {
        String expression = ruleTextField.getText();
        StringRule renameRule = new StringRule(expression);
        ControllerMediator.getInstance().sendReadyRenameRule(renameRule);
        close();
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) ruleTextField.getScene().getWindow();
        stage.close();
    }

    public void receiveRule(StringRule renameRule) {
        ruleTextField.setText(renameRule.getExpression());
    }
}
