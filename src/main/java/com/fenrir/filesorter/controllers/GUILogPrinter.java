package com.fenrir.filesorter.controllers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fenrir.filesorter.model.log.GUILogPrinter;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class GUIConsolePrinter implements GUILogPrinter {
    private TextArea console;
    private Text text;

    public GUIConsolePrinter(TextArea area) {
        this.console = area;
    }

    @Override
    public void print(String message) {
        console.appendText(message);
    }

    @Override
    public void highlight(ILoggingEvent event) {
        text = new Text();

        event.getLevel();
        Level level = event.getLevel();
        if (Level.TRACE.equals(level)) {
            text.setStyle(LogStyle.TRACE.getStyle());
        } else if (Level.DEBUG.equals(level)) {
            text.setStyle(LogStyle.DEBUG.getStyle());
        } else if (Level.INFO.equals(level)) {
            text.setStyle(LogStyle.INFO.getStyle());
        } else if (Level.WARN.equals(level)) {
            text.setStyle(LogStyle.WARN.getStyle());
        } else if (Level.ERROR.equals(level)) {
            text.setStyle(LogStyle.ERROR.getStyle());
        }
    }

    public String pattern() {
        return "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
    }

    private enum LogStyle {
        TRACE(""),
        DEBUG(""),
        INFO("-fx-fill: #F0AD4E"),
        WARN("-fx-fill: #FFCC00"),
        ERROR("-fx-fill: #CC3300");

        private String style;

        private LogStyle(String style) {
            this.style = style;
        }

        public String getStyle() {
            return style;
        }
    }
}
