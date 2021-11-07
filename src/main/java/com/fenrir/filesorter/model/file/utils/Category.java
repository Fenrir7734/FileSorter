package com.fenrir.filesorter.model.file.utils;

public enum Category {
    TEXT("text"),
    EBOOK("ebook"),
    AUDIO("audio"),
    VIDEO("video"),
    IMAGE("image"),
    FONT("font"),
    CODE("code"),
    ARCHIVE("archive"),
    EXECUTABLE("executable"),
    SHEET("sheet"),
    SLIDE("slide"),
    OTHERS("others");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
