package com.fenrir.filesorter.model.enums;

public enum Group {
    FILE_INFO("File info"),
    DATES("Dates"),
    PHOTO("Photo"),
    NONE("");

    private String name;

    private Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
