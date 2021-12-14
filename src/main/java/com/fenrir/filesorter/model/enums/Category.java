package com.fenrir.filesorter.model.enums;

public enum Category {
    FILE_INFO("File info"),
    DATES("Dates"),
    PHOTO("Photo"),
    NONE("");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
