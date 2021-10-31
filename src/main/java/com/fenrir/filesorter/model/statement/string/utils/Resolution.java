package com.fenrir.filesorter.model.statement.string.utils;

public record Resolution(int width, int height) {
    @Override
    public String toString() {
        return width + "x" + height;
    }
}
