package com.fenrir.filesorter.tokens;

public enum SortTokenTypeEnum {
    SEPARATOR("/"),
    FILE_EXTENSION("EXT"),
    FILE_CATEGORY("CAT"),
    IMAGE_RESOLUTION("RES"),
    CURRENT_FILE_NAME("CUR");

    private final String literal;

    SortTokenTypeEnum(String literal) {
        this.literal = literal;
    }

    public static SortTokenTypeEnum get(String literal) {
        SortTokenTypeEnum[] tokenTypes = SortTokenTypeEnum.values();
        for (int i = 0; i < tokenTypes.length; i++) {
            if (tokenTypes[i].literal.equals(literal)) {
                return tokenTypes[i];
            }
        }
        return null;
    }
}
