package com.fenrir.filesorter.statement;

public enum FilesCategory {
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
    SLIDE("slide");

    private String name;

    FilesCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
