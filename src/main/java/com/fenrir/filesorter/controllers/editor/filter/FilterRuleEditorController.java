package com.fenrir.filesorter.controllers.editor.filter;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.editor.EditorConfirmController;
import com.fenrir.filesorter.controllers.editor.EditorController;
import com.fenrir.filesorter.controllers.editor.ExpressionEditorController;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.parsers.FilterRuleParser;
import com.fenrir.filesorter.model.rule.Rule;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FilterRuleEditorController implements EditorController {
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
            ControllerMediator.getInstance().registerFilterRuleEditorController(this);
            expressionEditorController.setParent(this);
            editorConfirmController.setParent(this);
            ruleEditorTabPane.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> onTabChange(newValue));
        } catch (Exception e) {
            logger.error("Error during initializing FilterRuleEditor: {}", e.getMessage());
        }
    }

    private void onTabChange(Tab newValue) {
        if (newValue.equals(expressionEditorTab)) {
            String expression = getExpression();
            expressionEditorController.setExpression(expression);
        }
    }

    public void receiveRule(Rule rule) {
        filterRuleBuilderController.setRule(rule);
    }

    @Override
    public String getExpression() {
        if (expressionEditorController.isEditEnabled()) {
            return expressionEditorController.getExpression();
        }
        return filterRuleBuilderController.buildExpression();
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
    public void confirm() {
        try {
            String expression = getExpression();
            Rule rule = new Rule(expression);
            FilterRuleParser parser = new FilterRuleParser();
            parser.resolveRule(rule);
            ControllerMediator.getInstance().sendReadyFilterRule(rule);
            close();
        } catch (ExpressionFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void close() {
        Stage stage = (Stage) ruleEditorTabPane.getScene().getWindow();
        stage.close();
    }
}
