package com.fenrir.filesorter.model.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class GUIAppender extends AppenderBase<ILoggingEvent> {
    private static LogPrinter out = null;
    private static PatternLayout patternLayout;

    @Override
    public void start() {
        patternLayout = new PatternLayout();
        patternLayout.setContext(getContext());
        patternLayout.setPattern("%msg");
        patternLayout.start();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (out != null) {
            String message = patternLayout.doLayout(iLoggingEvent);
            out.highlight(iLoggingEvent);
            out.print(message);
        }
    }

    public static void setPrinter(LogPrinter out) {
        GUIAppender.out = out;
        patternLayout.stop();
        patternLayout.setPattern(out.pattern());
        patternLayout.start();
    }
}
