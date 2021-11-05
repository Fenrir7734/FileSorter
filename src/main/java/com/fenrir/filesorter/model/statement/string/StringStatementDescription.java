package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

public class StringStatementDescription {
    private final String datePattern;

    public StringStatementDescription(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getDatePattern() {
        return datePattern;
    }
}
