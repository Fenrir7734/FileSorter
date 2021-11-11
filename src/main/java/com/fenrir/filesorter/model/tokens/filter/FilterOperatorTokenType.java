package com.fenrir.filesorter.model.tokens.filter;

import static com.fenrir.filesorter.model.tokens.filter.ArgumentNumber.MULTIPLE;
import static com.fenrir.filesorter.model.tokens.filter.ArgumentNumber.SINGLE;

public enum FilterOperatorTokenType {
    EQUAL("==", MULTIPLE), NOT_EQUAL("!=", MULTIPLE),

    GREATER(">", SINGLE), GREATER_EQUAL(">=", SINGLE), SMALLER("<", SINGLE),
    SMALLER_EQUAL("<=", SINGLE),

    CONTAINS("CON", MULTIPLE), STARTS_WITH("SW", MULTIPLE), ENDS_WITH("EW", MULTIPLE);

    private final String token;
    private final ArgumentNumber argumentNumber;

    FilterOperatorTokenType(String token, ArgumentNumber argumentNumber) {
        this.token = token;
        this.argumentNumber = argumentNumber;
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

    public ArgumentNumber getArgumentNumber() {
        return argumentNumber;
    }
}
