package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.log.LogPrinter;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class GUILogPrinter implements LogPrinter {
    private Label label;
    private Text text;

    public GUILogPrinter(Label area) {
        this.label = area;
    }

    public void print(String text) {
        Platform.runLater(() -> label.setText(text));
    }
}
