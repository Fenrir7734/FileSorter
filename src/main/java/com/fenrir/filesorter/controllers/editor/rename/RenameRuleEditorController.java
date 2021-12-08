package com.fenrir.filesorter.controllers.editor.rename;

import com.fenrir.filesorter.controllers.Controller;
import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.editor.EditorConfirmController;
import com.fenrir.filesorter.controllers.editor.EditorController;
import com.fenrir.filesorter.controllers.editor.ExpressionEditorController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleBuilderController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleEditorController;
import com.fenrir.filesorter.model.rule.StringRule;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RenameRuleEditorController implements Controller, EditorController {
    private final Logger logger = LoggerFactory.getLogger(FilterRuleEditorController.class);

    @FXML private ExpressionEditorController expressionEditorController;
    @FXML private FilterRuleBuilderController filterRuleBuilderController;
    @FXML private EditorConfirmController editorConfirmController;

    @FXML private TabPane ruleEditorTabPane;
    @FXML private Tab ruleBuilderTab;
    @FXML private Tab expressionEditorTab;

    @FXML
    public void initialize() {
        try {
            ControllerMediator.getInstance().registerController(this);
            expressionEditorController.setParent(this);
            editorConfirmController.setParent(this);
            ruleEditorTabPane.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(((observable, oldValue, newValue) -> onTabChange(newValue)));
        } catch (Exception e) {
            logger.error("Error during initializing RenameRuleEditor: {}", e.getMessage());
        }
    }

    private void onTabChange(Tab newValue) {
        if (newValue.equals(expressionEditorTab)) {
            String expression = getExpression();
            expressionEditorController.setExpression(expression);
        }
    }

    public void receiveRule(StringRule renameRule) {

    }

    @Override
    public String getExpression() {
        return null;
    }

    @Override
    public void lockTab() {
        ruleBuilderTab.setDisable(true);
    }

    @Override
    public void unlockTab() {
        ruleBuilderTab.setDisable(false);
    }

    @FXML
    public void confirm() {

    }

    @Override
    public void close() {

    }
}
