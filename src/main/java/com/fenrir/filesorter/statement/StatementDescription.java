package com.fenrir.filesorter.statement;

public class StatementDescription {
    private String datePattern;

    public StatementDescription(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getDatePattern() {
        return datePattern;
    }
}
