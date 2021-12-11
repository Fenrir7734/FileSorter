package com.fenrir.filesorter.controllers.editor.rename;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.editor.EditorConfirmController;
import com.fenrir.filesorter.controllers.editor.EditorController;
import com.fenrir.filesorter.controllers.editor.ExpressionEditorController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleBuilderController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleEditorController;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.parsers.RenameRuleParser;
import com.fenrir.filesorter.model.rule.Rule;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RenameRuleEditorController implements EditorController {
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
            ControllerMediator.getInstance().registerRenameRuleEditorController(this);
            expressionEditorController.setParent(this);
            editorConfirmController.setParent(this);
            ruleEditorTabPane.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(((observable, oldValue, newValue) -> onTabChange(newValue)));

            ruleBuilderTab.setDisable(true);
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

    public void receiveRule(Rule rule) {
        if (rule != null) {
            expressionEditorController.setExpression(rule.getExpression());
        }
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
        try {
            String expression = expressionEditorController.getExpression();
            Rule rule = new Rule(expression);
            RenameRuleParser parser = new RenameRuleParser();
            parser.resolveRule(rule);
            ControllerMediator.getInstance().sendReadyRenameRule(rule);
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
