package com.fenrir.filesorter.model.file.utils;

import java.util.Arrays;
import java.util.Objects;

public class Dimension implements Comparable<Dimension>{
    private final int width;
    private final int height;

    private Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static Dimension of(int width, int height) {
        return new Dimension(width, height);
    }

    public static Dimension of(String str) throws IllegalArgumentException, NumberFormatException {
        Integer[] dimensions = Arrays.stream(str.split("x|X"))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
        if (dimensions.length != 2) {
            throw new IllegalArgumentException();
        }
        return new Dimension(dimensions[0], dimensions[1]);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }

    @Override
    public int compareTo(Dimension o) {
        int size1 = width * height;
        int size2 = o.width * o.height;
        return Integer.compare(size1, size2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimension that = (Dimension) o;
        return width == that.width && height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }
}
