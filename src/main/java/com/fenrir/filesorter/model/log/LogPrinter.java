package com.fenrir.filesorter.model.log;

import ch.qos.logback.classic.spi.ILoggingEvent;

public interface GUILogPrinter {
    void print(String message);
    void highlight(ILoggingEvent event);
    String pattern();
}
