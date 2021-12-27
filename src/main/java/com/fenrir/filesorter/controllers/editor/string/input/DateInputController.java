package com.fenrir.filesorter.controllers.editor.string.input;

import com.fenrir.filesorter.model.enums.DatePatternType;
import com.fenrir.filesorter.model.parsers.DateParser;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateInputController extends StringArgumentInputController {
    @FXML private VBox textInputVBox;
    @FXML private VBox dateButtonContainer;
    @FXML private TextField inputTextField;
    @FXML private Label previewLabel;

    private final DateParser dateParser = new DateParser();
    private final LocalDateTime localDateTimeForPreview = LocalDateTime.now();

    @FXML
    public void initialize() {
        super.initialize();
        populateDateButtonContainer();
        inputTextField.textProperty()
                .addListener((observable, oldValue, newValue) -> updatePreview());
    }

    private void populateDateButtonContainer() {
        List<Button> buttons = createDateInsertionButtons();
        List<HBox> rows = insertButtonIntoRows(buttons);
        dateButtonContainer.getChildren().addAll(rows);
    }

    private List<Button> createDateInsertionButtons() {
        DatePatternType[] datePatternType = DatePatternType.values();
        List<Button> buttons = new ArrayList<>();
        for (DatePatternType type: datePatternType) {
            Button button = createButtonFor(type);
            buttons.add(button);
        }
        return buttons;
    }

    private Button createButtonFor(DatePatternType type) {
        Button button = new Button();
        button.setPrefWidth(145);
        button.setPrefHeight(60);
        button.setText(String.format("%s\n(%s)", type.getName(), type.getExample()));
        button.setTextAlignment(TextAlignment.CENTER);
        button.setOnMouseClicked(mouseEvent -> onDatePatternInsertionButtonClicked(type));
        return button;
    }

    private void onDatePatternInsertionButtonClicked(DatePatternType type) {
        inputTextField.appendText(String.format("%%%s", type.getToken()));
    }

    private List<HBox> insertButtonIntoRows(List<Button> buttons) {
        List<HBox> buttonRowContainers = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i++) {
            if (i % 3 == 0) {
                HBox container = createButtonRowContainer();
                buttonRowContainers.add(container);
            }
            buttonRowContainers.get(buttonRowContainers.size() - 1)
                    .getChildren()
                    .add(buttons.get(i));
        }
        return buttonRowContainers;
    }

    private HBox createButtonRowContainer() {
        HBox container = new HBox();
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private void updatePreview() {
        String pattern = dateParser.resolveDatePattern(inputTextField.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = localDateTimeForPreview.format(formatter);
        previewLabel.setText(formattedDate);
    }

    @Override
    public void confirm() {
        String pattern = dateParser.resolveDatePattern(inputTextField.getText());
        super.sendArguments(pattern);
        close();
    }

    @Override
    public void close() {
        Stage stage = (Stage) textInputVBox.getScene().getWindow();
        stage.close();
    }
}
