package com.fenrir.filesorter.statement.utils;

public record Resolution(int width, int height) {
    @Override
    public String toString() {
        return width + "x" + height;
    }
}
