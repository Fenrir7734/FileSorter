package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.file.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class Sorter {
    private static final Logger logger = LoggerFactory.getLogger(Sorter.class);

    private final List<FilePath> filesToSort;
    private final List<FilePath> sortedFiles;
    private final Action action;

    public Sorter(List<FilePath> filePaths, Action action) {
        this.filesToSort = filePaths;
        this.sortedFiles = new ArrayList<>(filesToSort.size());
        this.action = action;
    }

    public Sorter(List<FilePath> filePaths) {
        this(filePaths, Action.COPY);
    }

    public void sort() throws IOException {
        if (action == Action.COPY) {
            copyFiles();
        } else {
            moveFile();
        }
        logger.info("Sort complete");
    }

    private void copyFiles() throws IOException {
        for(FilePath paths : filesToSort) {
            Path sourcePath = paths.source();
            Path targetPath = paths.resolvedTargetPath();
            Path dirPath = targetPath.getParent();
            createDirectoryIfNotExists(dirPath);
            performCopyActionFor(sourcePath, targetPath);
        }
    }

    private void moveFile() throws IOException {
        for (FilePath paths: filesToSort) {
            Path sourcePath = paths.source();
            Path targetPath = paths.resolvedTargetPath();
            Path dirPath = targetPath.getParent();
            createDirectoryIfNotExists(dirPath);
            performMoveActionFor(sourcePath, targetPath);
        }
    }

    private void createDirectoryIfNotExists(Path dirPath) throws IOException {
        if (Files.notExists(dirPath)) {
            logger.info("Creating directory {}", dirPath.toAbsolutePath());
            Files.createDirectories(dirPath);
        }
    }

    private void performCopyActionFor(Path sourcePath, Path targetPath) throws IOException {
        logger.info("Copying file from: {} to: {}", sourcePath.toAbsolutePath(), targetPath.toFile());
        Path realTargetPath = Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
        sortedFiles.add(FilePath.of(sourcePath, realTargetPath));
    }

    private void performMoveActionFor(Path sourcePath, Path targetPath) throws IOException {
        logger.info("Moving file from: {} to: {}", sourcePath.toAbsolutePath(), targetPath.toFile());
        Path realTargetPath = Files.move(sourcePath, targetPath);
        sortedFiles.add(FilePath.of(sourcePath, realTargetPath));
    }

    public List<FilePath> getSortedFiles() {
        return sortedFiles;
    }

    public enum Action {
        COPY("Copy"),
        MOVE("Move");

        private String name;

        Action(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
