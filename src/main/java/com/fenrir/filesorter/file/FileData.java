package com.fenrir.filesorter.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

public class FileData {
    private Path sourcePath;
    private Path targetPath;
    private long count;
    private BasicFileAttributes attributes;

    public FileData(Path path) throws IOException {
        this.sourcePath = path;
        this.targetPath = path;
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

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public long getCount() {
        return count;
    }
}
