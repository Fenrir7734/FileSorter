package com.fenrir.filesorter.model.exceptions;

import com.fenrir.filesorter.model.rules.Rule;

import java.util.List;

public class ArgumentFormatException extends TokenFormatException {
    private String arg;

    public ArgumentFormatException(String message, Rule rule, String token, String arg) {
        super(message, rule, token);
        this.arg = arg;
    }

    public ArgumentFormatException(String message, Throwable cause, Rule rule, String token, String arg) {
        super(message, cause, rule, token);
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }
}
