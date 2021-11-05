package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.tokens.filter.FilterOperandTokenType;

public class FilterOperandStatementFactory {
    public static FilterOperandStatement<? extends Comparable<?>> get(FilterOperandTokenType type) {
        return switch (type) {
            case CURRENT_FILE_NAME -> new FileNameOperandStatement();
            case PATH -> new PathOperandStatement();
            case DATE -> new DateOperandStatement();
            case RESOLUTION -> null;
            case SIZE -> null;
            case FILE_EXTENSION -> null;
            case FILE_CATEGORY -> null;
        };
    }
}
