package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

public class StatementDescription {
    private final String datePattern;

    public StatementDescription(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getDatePattern() {
        return datePattern;
    }
}
