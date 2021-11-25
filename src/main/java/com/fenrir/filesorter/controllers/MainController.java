package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.rule.FilterRule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        sourcesListView.setItems(sourceItems);
        sourcesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ruleGroupListView.setCellFactory(TextFieldListCell.forListView());
        ruleGroupListView.setItems(ruleGroupItems);
        ruleGroupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        filterListView.setCellFactory(TextFieldListCell.forListView());
        filterListView.setItems(filterItems);
        ruleGroupListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void sort() {

    }

    @FXML
    public void choiceTargetDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(tabPane.getScene().getWindow());

        if (selectedDirectory != null) {
            targetPathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void addFileToSource() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(tabPane.getScene().getWindow());

        if (selectedFile != null) {
            List<String> selectedFilePaths = selectedFile.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());
            sourceItems.addAll(selectedFilePaths);
        }
    }

    @FXML
    public void addDirectoryToSource() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(tabPane.getScene().getWindow());

        if (selectedDirectory != null) {
            sourceItems.add(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void removeFromSource() {
        ObservableList<String> toRemove = sourcesListView.getSelectionModel().getSelectedItems();
        sourceItems.removeAll(toRemove);
    }

    @FXML
    public void addRuleGroup() {
        String ruleGroupName = "Rule Group " + (ruleGroupItems.size() + 1);
        ruleGroupItems.add(ruleGroupName);
    }

    @FXML
    public void removeRuleGroup() {
        ObservableList<String> toRemove = ruleGroupListView.getSelectionModel().getSelectedItems();
        ruleGroupItems.removeAll(toRemove);
    }

    @FXML
    public void moveRuleGroupUp() {

    }

    @FXML
    public void moveRuleGroupDown() {

    }

    @FXML
    public void editRenameRule() {
        //TODO: Rename rule editor window
    }

    @FXML
    public void editSortRule() {
        //TODO: Sort rule editor window
    }

    @FXML
    public void addFilterRule() {
        filterItems.add("");
    }

    @FXML
    public void editFilterRule() {
        //TODO: Filter rule editor window
    }

    @FXML
    public void removeFilterRule() {
        List<String> toRemove = filterListView.getSelectionModel().getSelectedItems();
        filterItems.removeAll(toRemove);
    }

    @FXML
    public void moveFilterRuleUp() {

    }

    @FXML
    public void moveFilterRuleDown() {

    }
}
