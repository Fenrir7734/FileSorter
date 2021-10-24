package com.fenrir.filesorter.tokens;

public enum SortTokenType {
    SEPARATOR("/"),
    FILE_EXTENSION("EXT"),
    FILE_CATEGORY("CAT"),
    IMAGE_RESOLUTION("RES"),
    CURRENT_FILE_NAME("CUR");

    private final String token;

    SortTokenType(String literal) {
        this.token = literal;
    }

    public static SortTokenType get(String literal) {
        SortTokenType[] tokenTypes = SortTokenType.values();
        for (int i = 0; i < tokenTypes.length; i++) {
            if (tokenTypes[i].token.equals(literal)) {
                return tokenTypes[i];
            }
        }
        return null;
    }
}
