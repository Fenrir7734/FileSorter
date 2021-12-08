package com.fenrir.filesorter.controllers.main.ruletab;

import com.fenrir.filesorter.model.rule.FilterRule;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class RuleManagerController {
    @FXML private TitledPane ruleManagerPane;
    @FXML private TextField renameRuleTextField;
    @FXML private TextField sortRuleTextField;
    @FXML private ListView<FilterRule> filterListView;

    @FXML private Button editFilterRuleButton;
    @FXML private Button removeFilterRuleButton;
    @FXML private Button moveUpFilterRuleButton;
    @FXML private Button moveDownFilterRuleButton;

    @FXML
    public void editRenameRule() {

    }

    @FXML
    public void editSortRule() {

    }

    @FXML
    public void addFilterRule() {

    }

    @FXML
    public void editFilterRule() {

    }

    @FXML
    public void removeFilterRule() {

    }

    @FXML
    public void moveUpFilterRule() {

    }

    @FXML
    public void moveDownFilterRule() {

    }
}
