package com.fenrir.filesorter.model.tokens.filter;

public enum FilterOperatorTokenType {
    EQUAL("=="), NOT_EQUAL("!="),
    GREATER(">"), GREATER_EQUAL(">="), SMALLER("<"), SMALLER_EQUAL("<="),
    //NOT("!"),

    CONTAINS("CON"), STARTS_WITH("SW"), ENDS_WITH("EW");

    private final String token;

    FilterOperatorTokenType(String token) {
        this.token = token;
    }

    public static FilterOperatorTokenType get(String token) {
        FilterOperatorTokenType[] tokenTypes = FilterOperatorTokenType.values();
        for (FilterOperatorTokenType tokenType: tokenTypes) {
            if (tokenType.token.equals(token)) {
                return tokenType;
            }
        }
        return null;
    }
}
