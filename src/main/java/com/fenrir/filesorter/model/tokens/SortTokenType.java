package com.fenrir.filesorter.model.tokens;

public enum SortTokenType {
    SEPARATOR("/"),
    FILE_EXTENSION("EXT"),
    FILE_CATEGORY("CAT"),
    IMAGE_DIMENSION("DIM");

    private final String token;

    SortTokenType(String token) {
        this.token = token;
    }

    public static SortTokenType get(String token) {
        SortTokenType[] tokenTypes = SortTokenType.values();
        for (SortTokenType tokenType : tokenTypes) {
            if (tokenType.token.equals(token)) {
                return tokenType;
            }
        }
        return null;
    }
}
