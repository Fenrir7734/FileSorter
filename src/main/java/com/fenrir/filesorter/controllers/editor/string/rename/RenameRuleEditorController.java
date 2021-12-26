package com.fenrir.filesorter.controllers.editor.string.rename;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.editor.string.StringRuleEditorController;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.parsers.RenameRuleParser;
import com.fenrir.filesorter.model.rule.Rule;
import javafx.scene.control.Alert;

public class RenameRuleEditorController extends StringRuleEditorController {
    @Override
    public void confirm() {
        try {
            String expression = getExpression();
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
}
