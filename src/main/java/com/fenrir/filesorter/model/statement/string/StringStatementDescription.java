package com.fenrir.filesorter.model.statement.string;

public class StringStatementDescription {
    private final String datePattern;
    private final String literal;

    public StringStatementDescription(String datePattern, String literal) {
        this.datePattern = datePattern;
        this.literal = literal;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public String getLiteral() {
        return literal;
    }
}
