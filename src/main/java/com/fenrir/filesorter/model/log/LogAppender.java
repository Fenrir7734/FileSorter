package com.fenrir.filesorter.model.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class LogAppender extends AppenderBase<ILoggingEvent> {
    private static LogPrinter out = null;
    private PatternLayout patternLayout;

    @Override
    public void start() {
        patternLayout = new PatternLayout();
        patternLayout.setContext(getContext());
        patternLayout.setPattern("%level\t| %msg%n");
        patternLayout.start();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (out != null) {
            String msg = patternLayout.doLayout(iLoggingEvent);
            out.print(msg);
        }
    }

    public static void setPrinter(LogPrinter out) {
        LogAppender.out = out;
    }
}
