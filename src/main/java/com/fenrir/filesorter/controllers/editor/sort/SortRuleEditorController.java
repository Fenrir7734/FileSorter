package com.fenrir.filesorter.controllers.editor.sort;

import com.fenrir.filesorter.controllers.confirm.ConfirmationController;
import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.confirm.ConfirmController;
import com.fenrir.filesorter.controllers.editor.EditorController;
import com.fenrir.filesorter.controllers.editor.ExpressionEditorController;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.parsers.SortRuleParser;
import com.fenrir.filesorter.model.rule.Rule;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SortRuleEditorController implements EditorController, ConfirmationController {
    private final Logger logger = LoggerFactory.getLogger(SortRuleEditorController.class);

    @FXML private ExpressionEditorController expressionEditorController;
    @FXML private SortRuleBuilderController sortRuleBuilderController;
    @FXML private ConfirmController confirmController;

    @FXML private TabPane ruleEditorTabPane;
    @FXML private Tab ruleBuilderTab;
    @FXML private Tab expressionEditorTab;

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerSortRuleEditorController(this);
        expressionEditorController.setParent(this);
        confirmController.setParent(this);

        ruleBuilderTab.setDisable(true);
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

    }

    @Override
    public void unlockTab() {

    }

    @Override
    public void confirm() {
        try {
            String expression = expressionEditorController.getExpression();
            Rule rule = new Rule(expression);
            SortRuleParser parser = new SortRuleParser();
            parser.resolveRule(rule);
            ControllerMediator.getInstance().sendReadySortRule(rule);
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
