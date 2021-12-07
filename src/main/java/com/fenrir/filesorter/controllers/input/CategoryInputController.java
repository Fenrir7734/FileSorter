package com.fenrir.filesorter.controllers.input;

import com.fenrir.filesorter.controllers.InputControllerMediator;
import com.fenrir.filesorter.model.file.utils.FileCategoryType;
import com.fenrir.filesorter.model.file.utils.FilesCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;

public class CategoryInputController extends ArgumentInputController {
    @FXML private ChoiceBox<String> categoryChoiceBox;

    @FXML
    public void initialize() {
        List<String> categoriesNames = Arrays.stream(FileCategoryType.values())
                .map(FileCategoryType::getName)
                .toList();
        ObservableList<String> categoryObservableList = FXCollections.observableArrayList();
        categoryObservableList.addAll(categoriesNames);
        categoryChoiceBox.setItems(categoryObservableList);
        categoryChoiceBox.getSelectionModel().select(0);
    }

    @Override
    public String readArguments() {
        return categoryChoiceBox.getSelectionModel().getSelectedItem();
    }
}
