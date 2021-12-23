package com.fenrir.filesorter.model.file;

import java.nio.file.Path;

public class FilePath {
    private final Path source;
    private Path target;
    private Path resolvedTargetPath;
    private long count;

    public FilePath(Path source, Path target, int count) {
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

    public void resolveTargetPath() {
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
    public String toString() {
        return "FilePath{" +
                "source=" + source +
                ", resolvedTargetPath=" + resolvedTargetPath +
                '}';
    }
}
