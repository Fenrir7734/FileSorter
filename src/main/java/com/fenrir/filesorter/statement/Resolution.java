package com.fenrir.filesorter.statement;

public record Dimension(int width, int height) {
    @Override
    public String toString() {
        return width + "x" + height;
    }
}
