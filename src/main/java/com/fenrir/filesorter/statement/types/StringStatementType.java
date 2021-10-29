package com.fenrir.filesorter.statement.types;

import com.fenrir.filesorter.statement.string.StringStatement;

import java.util.function.Supplier;

public enum StringStatementType {

    private final String token;
    private final Supplier<StringStatement> constructor;

    StringStatementType(String token, Supplier<StringStatement> constructor) {
        this.token = token;
        this.constructor = constructor;
    }

    public String getToken() {
        return token;
    }

    public Supplier<StringStatement> getConstructor() {
        return constructor;
    }
}
