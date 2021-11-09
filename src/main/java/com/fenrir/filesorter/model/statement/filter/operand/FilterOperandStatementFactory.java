package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.tokens.filter.FilterOperandTokenType;

public class FilterOperandStatementFactory<T extends Comparable<T>> {
    public static <T extends Comparable<T>> FilterOperandStatement<T> get(FilterOperandTokenType type) {
        return switch (type) {
            case CURRENT_FILE_NAME -> (FilterOperandStatement<T>) new FileNameOperandStatement();
            case PATH -> (FilterOperandStatement<T>) new PathOperandStatement();
            case DATE -> (FilterOperandStatement<T>) new DateOperandStatement();
            case DIMENSION -> null;
            case SIZE -> null;
            case FILE_EXTENSION -> null;
            case FILE_CATEGORY -> null;
        };
    }
    /*
    public static <T extends FilterOperandStatement> T get(FilterOperandTokenType type, Class<T> tClass) {
        return switch (type) {
            case CURRENT_FILE_NAME -> tClass.cast(new FileNameOperandStatement());
            case PATH -> tClass.cast(new PathOperandStatement());
            case DATE -> tClass.cast(new DateOperandStatement());
            case DIMENSION -> null;
            case SIZE -> null;
            case FILE_EXTENSION -> null;
            case FILE_CATEGORY -> null;
        };
    }
     */
    /*
    public static FilterOperandStatement<? extends Comparable<?>> get(FilterOperandTokenType type) {
        return switch (type) {
            case CURRENT_FILE_NAME -> new FileNameOperandStatement();
            case PATH -> new PathOperandStatement();
            case DATE -> new DateOperandStatement();
            case DIMENSION -> null;
            case SIZE -> null;
            case FILE_EXTENSION -> null;
            case FILE_CATEGORY -> null;
        };
    }
     */
}