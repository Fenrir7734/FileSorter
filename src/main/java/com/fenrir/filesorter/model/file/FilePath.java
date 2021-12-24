package com.fenrir.filesorter.model.file;

import java.nio.file.Path;
import java.util.Objects;

public class FilePath {
    private final Path source;
    private Path target;
    private Path resolvedTargetPath;
    private long count;

    private FilePath(Path source, Path target, int count) {
        this.source = source;
        this.target = target;
        this.count = count;
    }

    public static FilePath of(Path source) {
        return new FilePath(source, null, 0);
    }

    public static FilePath of(Path source, Path target) {
        FilePath filePath = new FilePath(source, target, 0);
        filePath.resolveTargetPath();
        return filePath;
    }

    public void resolveTargetPath() throws IllegalArgumentException {
        if (target == null) {
            throw new IllegalArgumentException("Target path is missing");
        }

        if (count > 0) {
            String pathStr = target.getFileName().toString();
            String toInsert = String.format(" (%d)", count);
            int extensionIndex = pathStr.indexOf(".");

            if (extensionIndex != -1) {
                pathStr = new StringBuilder(pathStr).insert(extensionIndex, toInsert)
                        .toString();
            } else {
                pathStr += toInsert;
            }
            resolvedTargetPath = target.getParent().resolve(Path.of(pathStr));
        } else {
            resolvedTargetPath = target;
        }
    }

    public void setTarget(Path target, long count) {
        this.target = target;
        this.count = count;
    }

    public Path source() {
        return source;
    }

    public Path target() {
        return target;
    }

    public Path resolvedTargetPath() {
        return resolvedTargetPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePath filePath = (FilePath) o;
        return count == filePath.count
                && Objects.equals(source, filePath.source)
                && Objects.equals(target, filePath.target)
                && Objects.equals(resolvedTargetPath, filePath.resolvedTargetPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, resolvedTargetPath, count);
    }

    @Override
    public String toString() {
        return "FilePath{" +
                "source=" + source +
                ", resolvedTargetPath=" + resolvedTargetPath +
                '}';
    }
}
