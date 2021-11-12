package com.fenrir.filesorter.model.exceptions;

import com.fenrir.filesorter.model.rule.Rule;

public class ExpressionFormatException extends Exception {
    private Rule rule;

    public ExpressionFormatException(String message) {
        super(message);
    }

    public ExpressionFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpressionFormatException(String message, Throwable cause, Rule rule) {
        super(message, cause);
        this.rule = rule;
    }

    public ExpressionFormatException(String message, Rule rule) {
        super(message);
        this.rule = rule;
    }

    public ExpressionFormatException(Throwable cause, Rule rule) {
        super(cause);
        this.rule = rule;
    }

    public Rule getRule() {
        return rule;
    }
}
