package com.fenrir.filesorter.model.tokens;

public enum SortTokenType {
    SEPARATOR("/"),
    FILE_EXTENSION("EXT"),
    FILE_CATEGORY("CAT"),
    IMAGE_RESOLUTION("RES");

    private final String token;

    SortTokenType(String token) {
        this.token = token;
    }

    public static SortTokenType get(String token) {
        SortTokenType[] tokenTypes = SortTokenType.values();
        for (int i = 0; i < tokenTypes.length; i++) {
            if (tokenTypes[i].token.equals(token)) {
                return tokenTypes[i];
            }
        }
        return null;
    }
}
