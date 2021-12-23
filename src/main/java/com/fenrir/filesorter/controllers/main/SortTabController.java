package com.fenrir.filesorter.controllers.main;

import com.fenrir.filesorter.controllers.GUILogPrinter;
import com.fenrir.filesorter.model.Configuration;
import com.fenrir.filesorter.model.Processor;
import com.fenrir.filesorter.model.Sorter;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.SortConfigurationException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.file.FilePath;
import com.fenrir.filesorter.model.log.LogAppender;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class SortTabController {
    private static final Logger logger = LoggerFactory.getLogger(SortTabController.class);

    @FXML private TextField targetPathTextField;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label progressLabel;

    private Configuration configuration;

    @FXML
    public void initialize() {
        LogAppender.setPrinter(new GUILogPrinter(progressLabel));
        setProgressIndicatorTo0();
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

    private void test() {
        try {
            configuration = new Configuration();
            configuration.setTargetRootDir(Path.of("/home/fenrir/Documents/Test_environment/wall_sorted_test"));
            configuration.addSourcePaths(List.of(Path.of("/home/fenrir/Documents/Test_environment/wallpapers")));
            RuleGroup group = new RuleGroup();
            group.setSortRule(new Rule("%(DIM)"));
            group.setRenameRule(new Rule("%(FIX)"));
            group.addFilterRule(new Rule("%(INC)%(FIN)%(NCO:HD)"));
            //group.addFilterRule(new Rule("%(INC)%(DIN)%(==:test)"));
            configuration.addNamedRuleGroup("aaa", group);
        } catch (ExpressionFormatException e) {
            e.printStackTrace();
        }
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
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    configuration.validate();
                    Platform.runLater(() -> setProgressIndicatorToIndeterminate());
                    Processor processor = new Processor(configuration);
                    Sorter sorter = new Sorter(processor);
                    sorter.sort();
                    Platform.runLater(() -> setProgressIndicatorToDone());
                } catch (TokenFormatException e) {
                    Platform.runLater(() -> setProgressIndicatorTo0());
                    logger.error("{} Rule: {} Token: {}", e.getMessage(), e.getRule(), e.getToken());
                } catch (ExpressionFormatException e) {
                    Platform.runLater(() -> setProgressIndicatorTo0());
                    logger.error("{} Rule: {}", e.getMessage(), e.getRule());
                } catch (SortConfigurationException | IOException e) {
                    Platform.runLater(() -> setProgressIndicatorTo0());
                    logger.error("{}", e.getMessage());
                } catch (Exception e) {
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open backup history.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void receiveBackup(List<FilePath> backup) {

    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
