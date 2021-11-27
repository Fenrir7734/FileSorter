package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.rule.FilterRule;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FilterRuleEditorController implements Controller {
    @FXML private TextField ruleTextField;

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerController(this);
    }

    @FXML
    public void confirm() {
        String expression = ruleTextField.getText();
        FilterRule filterRule = new FilterRule(expression);
        ControllerMediator.getInstance().sendReadyFilterRule(filterRule);
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

    public void receiveRule(FilterRule filterRule) {
        if (filterRule != null) {
            ruleTextField.setText(filterRule.getExpression());
        }
    }
}
