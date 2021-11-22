package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class Sorter {
    private final List<FileData> filesToSort;

    public Sorter(List<FileData> filesToSort) {
        this.filesToSort = filesToSort;
    }

    public void sort() throws IOException {
        for(FileData file: filesToSort) {
            if (file.getTargetPath() != null) {
                copyFile(file);
            }
        }
    }

    private void copyFile(FileData file) throws IOException {
        Path sourcePath = file.getSourcePath();
        Path targetPath = file.resolveTargetPath();
        Path dirPath = targetPath.getParent();

        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
    }
}
