package com.fenrir.filesorter.file;

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
        this.targetPath = isDirectory ? null : path;
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
}
