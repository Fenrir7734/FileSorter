package com.fenrir.filesorter.controllers.editor.string;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.confirm.ConfirmController;
import com.fenrir.filesorter.controllers.confirm.ConfirmationController;
import com.fenrir.filesorter.controllers.editor.EditorController;
import com.fenrir.filesorter.controllers.editor.ExpressionEditorController;
import com.fenrir.filesorter.model.rule.Rule;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StringRuleEditorController implements EditorController, ConfirmationController {
    private final Logger logger = LoggerFactory.getLogger(StringRuleEditorController.class);

    @FXML private StringRuleBuilderController stringRuleBuilderController;
    @FXML private ExpressionEditorController expressionEditorController;
    @FXML private ConfirmController confirmController;

    @FXML private TabPane ruleEditorTabPane;
    @FXML private Tab ruleBuilderTab;
    @FXML private Tab expressionEditorTab;

    @FXML
    public void initialize() {
        try {
            ControllerMediator.getInstance().registerStringRuleEditorController(this);
            expressionEditorController.setParent(this);
            confirmController.setParent(this);
            ruleEditorTabPane.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(((observable, oldValue, newValue) -> onTabChange(newValue)));
        } catch (Exception e) {
            logger.error("Error during initializing StringRuleEditor: {}", e.getMessage());
        }
    }

    private void onTabChange(Tab newValue) {
        if (newValue.equals(expressionEditorTab)) {
            String expression = getExpression();
            expressionEditorController.setExpression(expression);
        }
    }

    public void receiveRule(Rule rule) {
        if (rule != null) {
            stringRuleBuilderController.setRule(rule);
        }
    }

    @Override
    public String getExpression() {
        if (expressionEditorController.isEditEnabled()) {
            return expressionEditorController.getExpression();
        }
        return stringRuleBuilderController.buildExpression();
    }

    @Override
    public void lockTab() {
        ruleBuilderTab.setDisable(true);
    }

    @Override
    public void unlockTab() {
        ruleBuilderTab.setDisable(false);
    }

    @Override
    public void close() {
        Stage stage = (Stage) ruleEditorTabPane.getScene().getWindow();
        stage.close();
    }
}
