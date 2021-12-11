package com.fenrir.filesorter.model.statement.types;


public enum ActionType {
    INCLUDE("INC", "Include") {
        @Override
        public boolean perform() {
            return true;
        }
    },
    EXCLUDE("EXC", "Exclude") {
        @Override
        public boolean perform() {
            return false;
        }
    };

    private final String token;
    private final String name;

    ActionType(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public abstract boolean perform();

    public static ActionType getType(String token) {
        ActionType[] types = ActionType.values();
        for (ActionType type: types) {
            if (token.equals(type.getToken())) {
                return type;
            }
        }
        return null;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
