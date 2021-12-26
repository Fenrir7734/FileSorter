package com.fenrir.filesorter.controllers.editor.string.sort;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.editor.string.StringRuleEditorController;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.parsers.SortRuleParser;
import com.fenrir.filesorter.model.rule.Rule;
import javafx.scene.control.Alert;


public class SortRuleEditorController extends StringRuleEditorController {
    @Override
    public void confirm() {
        try {
            String expression = getExpression();
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
}
