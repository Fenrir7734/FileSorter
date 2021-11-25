package com.fenrir.filesorter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML private TabPane tabPane;

    @FXML private TextField targetPathTextField;

    @FXML private ListView<String> sourcesListView;
    @FXML private ListView<String> ruleGroupListView;
    @FXML private ListView<String> filterListView;

    @FXML private TextField renameRuleTextField;
    @FXML private TextField sortRuleTextField;

    @FXML private TextArea logTextArea;
    @FXML private ProgressBar progressBar;

    private final ObservableList<String> sourceItems = FXCollections.observableArrayList();
    private final ObservableList<String> ruleGroupItems = FXCollections.observableArrayList();
    private final ObservableList<String> filterItems = FXCollections.observableArrayList();;

    @FXML
    public void initialize() {

    }

    @FXML
    public void sort() {

    }

    @FXML
    public void choiceTargetDirectory() {

    }

    @FXML
    public void addFileToSource() {

    }

    @FXML
    public void addDirectoryToSource() {

    }

    @FXML
    public void removeFromSource() {

    }

    @FXML
    public void addRuleGroup() {

    }

    @FXML
    public void removeRuleGroup() {

    }

    @FXML
    public void moveRuleGroupUp() {

    }

    @FXML
    public void moveRuleGroupDown() {

    }

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
    public void moveFilterRuleUp() {

    }

    @FXML
    public void moveFilterRuleDown() {

    }

}
