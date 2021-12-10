package com.fenrir.filesorter.controllers.main;

import com.fenrir.filesorter.model.Configuration;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SourceTabController {
    @FXML private ListView<Path> sourcesListView;

    private Configuration configuration;

    @FXML
    public void initialize() {
        sourcesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void addFileToSource() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(sourcesListView.getScene().getWindow());

        if (selectedFile != null) {
            addPathToSource(selectedFile.toArray(new File[0]));
        }
    }

    @FXML
    public void addDirectoryToSource() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(sourcesListView.getScene().getWindow());

        if (selectedDirectory != null) {
            addPathToSource(selectedDirectory);
        }
    }

    private void addPathToSource(File... files) {
        List<Path> toAdd = Arrays.stream(files)
                .map(File::toPath)
                .collect(Collectors.toList());
        configuration.addSourcePaths(toAdd);
    }

    @FXML
    public void removeFromSource() {
        List<Path> toRemove = sourcesListView.getSelectionModel()
                .getSelectedItems();
        configuration.removeSourcePaths(toRemove);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        sourcesListView.setItems(configuration.getSourcePaths());
    }
}
