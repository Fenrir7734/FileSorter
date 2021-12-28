package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.file.BackupManager;
import com.fenrir.filesorter.model.file.FilePath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class BackupHistoryController {
    @FXML private VBox backupHistoryVBox;
    @FXML private ListView<String> backupListView;
    @FXML private Button undoButton;
    @FXML private Button deleteButton;

    private final ObservableList<String> backupNameItems = FXCollections.observableArrayList();
    private BackupManager backupManager;

    @FXML
    public void initialize() throws IOException {
        ControllerMediator.getInstance().registerBackupHistoryController(this);
        backupManager = new BackupManager();
        initBackupListView();
        disableButtons();
    }

    private void initBackupListView() throws IOException {
        backupNameItems.addAll(backupManager.getAllBackupsNamesWithoutExtension());
        backupListView.setItems(backupNameItems);
        backupListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> onSelectedBackup(oldValue, newValue));
    }

    private void onSelectedBackup(String oldValue, String newValue) {
        if (oldValue == null && newValue != null) {
            enableButtons();
        }
        if (oldValue != null && newValue == null) {
            disableButtons();
        }
    }

    private void enableButtons() {
        undoButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    private void disableButtons() {
        undoButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    @FXML
    public void undo() {
        String selectedBackupName = backupListView.getSelectionModel()
                .getSelectedItem();
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to move these files to their previous location?",
                ButtonType.YES,
                ButtonType.CANCEL
        );
        alert.showAndWait();

        if (alert.getResult() != null && alert.getResult() == ButtonType.YES) {
            List<FilePath> backup = getBackup(selectedBackupName);
            ControllerMediator.getInstance().sendBackupPathList(backup);
            close();
        }
    }

    private List<FilePath> getBackup(String name) {
        try {
            return backupManager.readBackup(name + ".json");
        } catch (IOException | JSONException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load backup file. " + e.getMessage());
            alert.showAndWait();
        }
        return null;
    }

    @FXML
    public void delete() {
        String selectedBackupName = backupListView.getSelectionModel()
                .getSelectedItem();
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this backup?",
                ButtonType.YES,
                ButtonType.CANCEL
        );
        alert.showAndWait();

        if (alert.getResult() != null && alert.getResult() == ButtonType.YES) {
            deleteBackupFile(selectedBackupName);
        }
    }

    private void deleteBackupFile(String name) {
        try {
            backupManager.deleteBackup(name + ".json");
            backupNameItems.remove(name);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete selected backup file");
            alert.showAndWait();
        }
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) backupHistoryVBox.getScene().getWindow();
        stage.close();
    }
}
