package com.fenrir.filesorter.controllers.save;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.confirm.ConfirmationController;
import com.fenrir.filesorter.controllers.confirm.ConfirmController;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.rule.SavedRuleGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class SaveRuleGroupController implements ConfirmationController {
    private final Logger logger = LoggerFactory.getLogger(SaveRuleGroupController.class);

    @FXML private ConfirmController confirmController;
    @FXML private VBox saveRuleGroupVBox;
    @FXML private TextField ruleGroupNameTextField;

    private SavedRuleGroup savedRuleGroup;
    private RuleGroup ruleGroupToSave;

    @FXML
    public void initialize() {
        try {
            ControllerMediator.getInstance().registerSaveRuleGroupController(this);
            confirmController.setParent(this);
            savedRuleGroup = new SavedRuleGroup();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load saved rule groups");
            alert.showAndWait();
            close();
        }
    }

    @FXML
    public void confirm() {
        try {
            if (saveRuleGroup()) {
                close();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    public boolean saveRuleGroup() throws IOException {
        String ruleGroupName = ruleGroupNameTextField.getText();
        List<String> namesOfSavedRuleGroups = savedRuleGroup.getRuleGroupNames();
        if (!namesOfSavedRuleGroups.contains(ruleGroupName)) {
            savedRuleGroup.appendRuleGroup(ruleGroupName, ruleGroupToSave);
            savedRuleGroup.saveRuleGroupsToFile();
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Rule Group with this name already exists");
            alert.showAndWait();
        }
        return false;
    }

    @Override
    public void close() {
        Stage stage = (Stage) saveRuleGroupVBox.getScene().getWindow();
        stage.close();
    }

    public void receiveRuleGroup(String name, RuleGroup ruleGroup) {
        ruleGroupToSave = ruleGroup;
        ruleGroupNameTextField.setText(name);
    }
}
