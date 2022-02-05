package com.fenrir.filesorter.controllers.main;

import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.controllers.GUILogPrinter;
import com.fenrir.filesorter.model.Configuration;
import com.fenrir.filesorter.model.Processor;
import com.fenrir.filesorter.model.Sorter;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.SortConfigurationException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FilePath;
import com.fenrir.filesorter.model.file.FileStructureMapper;
import com.fenrir.filesorter.model.log.LogAppender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class SortTabController {
    private static final Logger logger = LoggerFactory.getLogger(SortTabController.class);

    @FXML private ComboBox<Sorter.Action> actionComboBox;
    @FXML private TextField targetPathTextField;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label progressLabel;

    private final ObservableList<Sorter.Action> actionTypeItems = FXCollections.observableArrayList();
    private final Callback<ListView<Sorter.Action>, ListCell<Sorter.Action>> actionCallback = createActionCellFactory();

    private Configuration configuration;

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerSortTabController(this);
        LogAppender.setPrinter(new GUILogPrinter(progressLabel));
        initActionChoiceBox();
        setProgressIndicatorTo0();
    }

    private void initActionChoiceBox() {
        actionTypeItems.addAll(Sorter.Action.values());
        actionComboBox.setItems(FXCollections.observableList(actionTypeItems));
        actionComboBox.setButtonCell(actionCallback.call(null));
        actionComboBox.setCellFactory(actionCallback);
        actionComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observableValue, oldValue, newValue) -> onActionChanged(newValue)));
    }

    private void onActionChanged(Sorter.Action newValue) {
        configuration.setSortAction(newValue);
    }

    private void setProgressIndicatorToIndeterminate() {
        if (progressIndicator.isDisabled()) {
            progressIndicator.setDisable(false);
        }
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressIndicator.setStyle("-fx-padding: 0 0 0 0");
    }

    private void setProgressIndicatorToDone() {
        if (progressIndicator.isDisabled()) {
            progressIndicator.setDisable(false);
        }
        progressIndicator.setProgress(100);
        progressIndicator.setStyle("-fx-padding: 0 0 -16 0");
    }

    private void setProgressIndicatorTo0() {
        if (!progressIndicator.isDisabled()) {
            progressIndicator.setDisable(true);
        }
        progressIndicator.setProgress(0);
        progressIndicator.setStyle("-fx-padding: 0 0 -16 0");
    }

    @FXML
    public void choiceTargetDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(targetPathTextField.getScene().getWindow());

        if (selectedDirectory != null) {
            configuration.setTargetRootDir(selectedDirectory.toPath());
            targetPathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void sort() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    configuration.validate();
                    Platform.runLater(() -> setProgressIndicatorToIndeterminate());
                    Processor processor = new Processor(
                            configuration.getSourcePaths(),
                            configuration.getTargetRootDir(),
                            configuration.getRuleGroups()
                    );
                    List<FilePath> filePaths = processor.process();
                    Sorter sorter = new Sorter(filePaths, configuration.getSortAction());
                    sorter.sort();
                    Platform.runLater(() -> setProgressIndicatorToDone());
                } catch (TokenFormatException e) {
                    Platform.runLater(() -> setProgressIndicatorTo0());
                    logger.error("{} Rule: {} Token: {}", e.getMessage(), e.getRule(), e.getToken());
                } catch (ExpressionFormatException e) {
                    Platform.runLater(() -> setProgressIndicatorTo0());
                    logger.error("{} Rule: {}", e.getMessage(), e.getRule());
                } catch (SortConfigurationException e) {
                    Platform.runLater(() -> setProgressIndicatorTo0());
                    logger.error("{}", e.getMessage());
                } catch (IOException e) {
                    logger.error("Failed IO operation: {}", e.getMessage());
                } catch (Exception e) {
                    logger.error("An unknown error has occurred: {}", e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    public void undo() {
        try {
            Parent parent = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/com/fenrir/filesorter/controllers/BackupHistoryView.fxml"))
            );
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open backup history." + e.getMessage());
            alert.showAndWait();
        }
    }

    public void receiveBackup(List<FilePath> backup) {

    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private Callback<ListView<Sorter.Action>, ListCell<Sorter.Action>> createActionCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<Sorter.Action> call(ListView<Sorter.Action> o) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Sorter.Action type, boolean empty) {
                        super.updateItem(type, empty);

                        if (empty || type == null) {
                            setText(null);
                        } else {
                            setText(type.getName());
                        }
                    }
                };
            }
        };
    }
}
