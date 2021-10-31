package com.fenrir.filesorter.model.tokens;

public enum RenameTokenType {
    FILE_EXTENSION("EXT"),
    CURRENT_FILE_NAME("CUR");

    private final String token;

    RenameTokenType(String token) {
        this.token = token;
    }

    public static RenameTokenType get(String token) {
        RenameTokenType[] tokenTypes = RenameTokenType.values();
        for (RenameTokenType tokenType : tokenTypes) {
            if (tokenType.token.equals(token)) {
                return tokenType;
            }
        }
        return null;
    }
}
