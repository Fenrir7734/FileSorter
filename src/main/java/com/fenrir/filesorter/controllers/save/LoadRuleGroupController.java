package com.fenrir.filesorter.controllers.save;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.rule.SavedRuleGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadRuleGroupController {
    @FXML private VBox loadRuleGroupVBox;
    @FXML private ListView<String> ruleGroupListView;
    @FXML private Button loadButton;
    @FXML private Button deleteButton;

    private final ObservableList<String> ruleGroupNameItems = FXCollections.observableArrayList();
    private SavedRuleGroup savedRuleGroup;

    @FXML
    public void initialize() {
        try {
            savedRuleGroup = new SavedRuleGroup();
            initRuleGroupListView();
            disableButtons();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load saved rule groups");
            alert.showAndWait();
            close();
        }
    }

    private void initRuleGroupListView() {
        ruleGroupNameItems.addAll(savedRuleGroup.getRuleGroupNames());
        ruleGroupListView.setItems(ruleGroupNameItems);
        ruleGroupListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> onSelectedRuleGroup(oldValue, newValue));
    }

    private void onSelectedRuleGroup(String oldValue, String newValue) {
        if (oldValue == null && newValue != null) {
            enableButtons();
        }
        if (oldValue != null && newValue == null) {
            disableButtons();
        }
    }

    private void enableButtons() {
        loadButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    private void disableButtons() {
        loadButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    public void load() {
        try {
            String selectedRuleGroupName = ruleGroupListView.getSelectionModel()
                            .getSelectedItem();
            RuleGroup ruleGroup = savedRuleGroup.getRuleGroup(selectedRuleGroupName);
            ControllerMediator.getInstance().sendRuleGroupFromLoad(selectedRuleGroupName, ruleGroup);
            close();
        } catch (ExpressionFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load rule group: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void delete() {
        String selectedRuleGroupName = ruleGroupListView.getSelectionModel()
                .getSelectedItem();
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Remove " + selectedRuleGroupName + " rule group?",
                ButtonType.YES,
                ButtonType.CANCEL
        );
        alert.showAndWait();

        if (alert.getResult() != null && alert.getResult() == ButtonType.YES) {
            deleteRuleGroup(selectedRuleGroupName);
        }
    }

    private void deleteRuleGroup(String name) {
        try {
            savedRuleGroup.removeRuleGroup(name);
            savedRuleGroup.saveRuleGroupsToFile();
            ruleGroupNameItems.remove(name);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete saved rule group");
            alert.showAndWait();
        }
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) loadRuleGroupVBox.getScene().getWindow();
        stage.close();
    }
}
