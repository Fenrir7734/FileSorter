package com.fenrir.filesorter.controllers.editor;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ExpressionEditorController {
    private EditorController parentController;

    @FXML private TextField expressionTextField;
    @FXML private CheckBox editExpressionCheckBox;

    @FXML
    public void initialize() {
        editExpressionCheckBox.selectedProperty().addListener(
                (observable, oldValue, newValue) -> onEditExpression(newValue)
        );
    }

    private void onEditExpression(boolean newValue) {
        if (newValue) {
            expressionTextField.setDisable(false);
            parentController.lockTab();
        } else {
            String expression = parentController.getExpression();
            expressionTextField.setText(expression);
            expressionTextField.setDisable(true);
            parentController.unlockTab();
        }
    }

    public void setExpression(String expression) {
        expressionTextField.setText(expression);
    }

    public void setParent(EditorController editorController) {
        parentController = editorController;
    }
}
