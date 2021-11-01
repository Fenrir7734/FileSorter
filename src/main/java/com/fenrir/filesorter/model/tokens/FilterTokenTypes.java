package com.fenrir.filesorter.model.tokens;

public enum FilterTokenTypes {
    INCLUDE("INC"), EXCLUDE("EXC"),

    EQUAL("=="), NOT_EQUAL("!="),
    GREATER(">"), GREATER_EQUAL(">="), SMALLER("<"), SMALLER_EQUAL("<="),
    RANGE("RAN"),
    NOT("!"),

    CONTAINS("CON"), STARTS_WITH("SW"), ENDS_WITH("EW"),

    CURRENT_FILE_NAME("CUR"), PATH("PAT"), DATE("DAT"), RESOLUTION("RES"), SIZE("SIZ"), FILE_EXTENSION("EXT"),
    FILE_CATEGORY("CAT");

    private final String token;

    FilterTokenTypes(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
