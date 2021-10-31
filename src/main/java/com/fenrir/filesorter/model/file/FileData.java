package com.fenrir.filesorter.model.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

public class FileData {
    private final Path sourcePath;
    private Path targetPath;
    private long count;
    private final BasicFileAttributes attributes;
    private final boolean isDirectory;

    public FileData(Path path) throws IOException {
        this.isDirectory = Files.isDirectory(path);
        this.sourcePath = path;
        this.targetPath = null;
        this.attributes = Files.readAttributes(path, BasicFileAttributes.class);
    }

    public Calendar creationTime() {
        FileTime fileCreationTime = attributes.creationTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fileCreationTime.toMillis());
        return calendar;
    }

    public Calendar lastModifiedTime() {
        FileTime fileModificationTime = attributes.lastModifiedTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fileModificationTime.toMillis());
        return calendar;
    }

    public String getFileExtension() {
        if (!isDirectory) {
            String fileName = sourcePath.getFileName().toString();
            int i = fileName.lastIndexOf(".");
            if (i != -1) {
                return fileName.substring(i + 1).trim();
            }
        }
        return null;
    }

    public Path resolveTargetPath() {
        if (count <= 0 || isDirectory) {
            return targetPath;
        }

        String pathStr = targetPath.getFileName().toString();
        String toInsert = String.format(" (%d)", count);
        int extensionIndex = pathStr.indexOf(".");

        if (extensionIndex != -1) {
            pathStr = new StringBuilder(pathStr).insert(extensionIndex, toInsert)
                    .toString();
        } else {
            pathStr += toInsert;
        }
        return targetPath.getParent().resolve(Path.of(pathStr));
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getCount() {
        return count;
    }

    public void setTargetPath(Path targetPath, long count) {
        this.targetPath = targetPath;
        this.count = count;
    }
}
