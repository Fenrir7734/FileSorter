package com.fenrir.filesorter.model.statement.types;

public enum ActionType {
    INCLUDE("INC", "Include"),
    EXCLUDE("EXC", "Exclude");

    private final String token;
    private final String name;

    ActionType(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
