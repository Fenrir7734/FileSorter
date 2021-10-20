package com.fenrir.filesorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileInfo {
    private Path path;
    private boolean isDirectory;
    private String name;

    public FileInfo(Path path) {
        this.path = path;
    }

    private void readAttributes() {

    }

    public void isExcluded() {

    }
}
