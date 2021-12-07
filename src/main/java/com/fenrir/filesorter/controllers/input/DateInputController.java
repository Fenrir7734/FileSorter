package com.fenrir.filesorter.controllers.input;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateInputController extends ArgumentInputController {
    @FXML private DatePicker datePicker;

    @FXML
    public void initialize() {
        datePicker.setConverter(new StringConverter<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate != null) {
                    return formatter.format(localDate);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String s) {
                if (s != null && !s.isEmpty()) {
                    return LocalDate.parse(s, formatter);
                }
                return null;
            }
        });
    }

    @Override
    public String readArguments() {
        return datePicker.toString();
    }
}
