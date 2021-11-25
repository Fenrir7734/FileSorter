package com.fenrir.filesorter.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        filterListView.setItems(filterItems);
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
        String toRemove = ruleGroupListView.getSelectionModel().getSelectedItem();
        ruleGroupItems.remove(toRemove);
    }

    @FXML
    public void moveRuleGroupUp() {
        moveSelectedItem(ruleGroupListView, ruleGroupItems, MoveDirection.UP);
    }

    @FXML
    public void moveRuleGroupDown() {
        moveSelectedItem(ruleGroupListView, ruleGroupItems, MoveDirection.DOWN);
    }

    @FXML
    public void editRenameRule() {
        try {
            Parent parent = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("RenameView.fxml"))
            );
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void editSortRule() {
        try {
            Parent parent = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("SortView.fxml"))
            );
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void addFilterRule() {
        try {
            Parent parent = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("FilterView.fxml"))
            );
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
        moveSelectedItem(filterListView, filterItems, MoveDirection.UP);
    }

    @FXML
    public void moveFilterRuleDown() {
        moveSelectedItem(filterListView, filterItems, MoveDirection.DOWN);
    }

    private void moveSelectedItem(ListView<String> listView, ObservableList<String> itemList, MoveDirection direction) {
        String toMove = listView.getSelectionModel().getSelectedItem();
        int indexOfItemToMove = itemList.indexOf(toMove);
        int indexOfItemAfterMoving = direction.move(indexOfItemToMove, itemList.size() - 1);

        if (indexOfItemToMove != indexOfItemAfterMoving) {
            Collections.swap(itemList, indexOfItemToMove, indexOfItemAfterMoving);
            listView.getSelectionModel().clearSelection(indexOfItemToMove);
            listView.getSelectionModel().select(indexOfItemAfterMoving);
        }
    }

    enum MoveDirection {
        UP {
            public int move(int index, int upperBound) {
                return index > 0 ? index - 1 : index;
            }
        },
        DOWN {
            public int move(int index, int upperBound) {
                return index < upperBound ? index + 1 : index;
            }
        };

        public abstract int move(int index, int upperBound);
    }
}
