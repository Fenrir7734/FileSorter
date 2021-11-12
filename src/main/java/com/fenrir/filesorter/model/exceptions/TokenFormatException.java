package com.fenrir.filesorter.model.exceptions;

import com.fenrir.filesorter.model.rule.Rule;

public class TokenFormatException extends ExpressionFormatException {
    private String token;

    public TokenFormatException(String message) {
        super(message);
    }

    public TokenFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenFormatException(String message, String token) {
        super(message);
        this.token = token;
    }

    public TokenFormatException(String message, Rule rule, String token) {
        super(message, rule);
        this.token = token;
    }

    public TokenFormatException(String message, Throwable cause, Rule rule, String token) {
        super(message, cause, rule);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
