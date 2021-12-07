package com.fenrir.filesorter.controllers.editor;

import com.fenrir.filesorter.controllers.Controller;
import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.model.rule.StringRule;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SortRuleEditorController implements Controller {
    @FXML private TextField ruleTextField;

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerController(this);
    }

    @FXML
    public void confirm() {
        String expression = ruleTextField.getText();
        StringRule sortRule = new StringRule(expression);
        ControllerMediator.getInstance().sendReadySortRule(sortRule);
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

    public void receiveRule(StringRule sortRule) {
        if (sortRule != null) {
            ruleTextField.setText(sortRule.getExpression());
        }
    }
}
