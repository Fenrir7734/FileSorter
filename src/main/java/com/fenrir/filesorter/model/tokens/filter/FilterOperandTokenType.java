package com.fenrir.filesorter.model.tokens.filter;

public enum FilterOperandTokenType {
    CURRENT_FILE_NAME("CUR"), PATH("PAT"), DATE("DAT"), DIMENSION("DIM"), SIZE("SIZ"), FILE_EXTENSION("EXT"),
    FILE_CATEGORY("CAT");

    private final String token;

    FilterOperandTokenType(String token) {
        this.token = token;
    }

    public static FilterOperandTokenType get(String token) {
        FilterOperandTokenType[] tokenTypes = FilterOperandTokenType.values();
        for (FilterOperandTokenType tokenType: tokenTypes) {
            if (tokenType.token.equals(token)) {
                return tokenType;
            }
        }
        return null;
    }
}
