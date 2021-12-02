package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.log.LogPrinter;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class GUILogPrinter implements LogPrinter {
    private TextArea console;
    private Text text;

    public GUILogPrinter(TextArea area) {
        this.console = area;
    }

    public void print(String text) {
        Platform.runLater(() -> console.appendText(text));
    }
}
