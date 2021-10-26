package com.fenrir.filesorter.tokens;

public enum RenameTokenType {
    FILE_EXTENSION("EXT"),
    CURRENT_FILE_NAME("CUR");

    private final String token;

    RenameTokenType(String token) {
        this.token = token;
    }

    public static RenameTokenType get(String token) {
        RenameTokenType[] tokenTypes = RenameTokenType.values();
        for (int i = 0; i < tokenTypes.length; i++) {
            if (tokenTypes[i].token.equals(token)) {
                return tokenTypes[i];
            }
        }
        return null;
    }
}
