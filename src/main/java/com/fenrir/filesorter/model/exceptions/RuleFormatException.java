package com.fenrir.filesorter.model.exceptions;

import com.fenrir.filesorter.model.rules.Rule;

public class RuleFormatException extends Exception {
    private Rule rule;

    public RuleFormatException(String message) {
        super(message);
    }

    public RuleFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleFormatException(String message, Throwable cause, Rule rule) {
        super(message, cause);
        this.rule = rule;
    }

    public RuleFormatException(String message, Rule rule) {
        super(message);
        this.rule = rule;
    }

    public RuleFormatException(Throwable cause, Rule rule) {
        super(cause);
        this.rule = rule;
    }

    public Rule getRule() {
        return rule;
    }
}
