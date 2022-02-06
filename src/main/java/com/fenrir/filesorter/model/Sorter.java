package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.file.BackupManager;
import com.fenrir.filesorter.model.file.FilePath;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class Sorter {
    private static final Logger logger = LoggerFactory.getLogger(Sorter.class);

    private final BackupManager backupManager;
    private final List<FilePath> filesToSort;
    private final Deque<Path> sourceDirectoriesPaths;
    private final List<FilePath> sortedFiles;
    private final Deque<Path> createdTargetDirectories;
    private final Action action;

    public Sorter(Action action, Deque<Path> directoriesPaths, List<FilePath> filePaths) throws IOException {
        this.backupManager = new BackupManager();
        this.filesToSort = filePaths;
        this.sourceDirectoriesPaths = directoriesPaths;
        this.sortedFiles = new ArrayList<>(filesToSort.size());
        this.createdTargetDirectories = new ArrayDeque<>();
        this.action = action;
    }

    public Sorter(Action action, List<FilePath> filePaths) throws IOException {
        this(action, new ArrayDeque<>(), filePaths);
    }

    public void sort() throws IOException {
        try {
            if (action == Action.COPY) {
                copyFiles();
            } else {
                moveFile();
                removeEmptyDirectories();
            }
            logger.info("Sorting completed");
        } finally {
            backupManager.makeBackup(action, createdTargetDirectories, sortedFiles);
        }
    }

    private void copyFiles() throws IOException {
        for(FilePath paths : filesToSort) {
            Path sourcePath = paths.source();
            Path targetPath = paths.resolvedTargetPath();
            Path dirPath = targetPath.getParent();
            createDirectoryIfNotExistsAndStoreIt(dirPath);
            performCopyActionFor(sourcePath, targetPath);
        }
    }

    private void moveFile() throws IOException {
        for (FilePath paths: filesToSort) {
            Path sourcePath = paths.source();
            Path targetPath = paths.resolvedTargetPath();
            Path dirPath = targetPath.getParent();
            createDirectoryIfNotExistsAndStoreIt(dirPath);
            performMoveActionFor(sourcePath, targetPath);
        }
    }

    private void createDirectoryIfNotExistsAndStoreIt(Path dirPath) throws IOException {
        Deque<Path> directoriesToCreate = new ArrayDeque<>();
        while (Files.notExists(dirPath) && dirPath.getParent() != null) {
            directoriesToCreate.offerFirst(dirPath);
            dirPath = dirPath.getParent();
        }

        while (!directoriesToCreate.isEmpty()) {
            Path dirToCreate = directoriesToCreate.pollFirst();
            logger.info("Creating directory: {}", dirToCreate.toAbsolutePath());
            Path createdDir = Files.createDirectory(dirToCreate);
            createdTargetDirectories.offerLast(createdDir);
        }
    }

    private void performCopyActionFor(Path sourcePath, Path targetPath) throws IOException {
        if (sourcePath.toFile().exists()) {
            if (!targetPath.toFile().exists()) {
                logger.info("Copying file from: {} to: {}", sourcePath.toAbsolutePath(), targetPath.toFile());
                Path realTargetPath = Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
                sortedFiles.add(FilePath.of(sourcePath, realTargetPath));
            } else {
                logger.info("Target file already exists: {}", sourcePath.toAbsolutePath());
            }
        } else {
            logger.info("Source file not exists: {}", sourcePath.toAbsolutePath());
        }
    }

    private void performMoveActionFor(Path sourcePath, Path targetPath) throws IOException {
        if (sourcePath.toFile().exists()) {
            if (!targetPath.toFile().exists()) {
                logger.info("Moving file from: {} to: {}", sourcePath.toAbsolutePath(), targetPath.toFile());
                Path realTargetPath = Files.move(sourcePath, targetPath);
                sortedFiles.add(FilePath.of(sourcePath, realTargetPath));
            } else {
                logger.info("Target file already exists: {}", sourcePath.toAbsolutePath());
            }
        } else {
            logger.info("Source file not exists: {}", sourcePath.toAbsolutePath());
        }
    }

    public void restore() throws IOException {
        if (action == Action.COPY) {
            removeFiles();
        } else {
            reverseMoveFile();
        }
        removeEmptyDirectories();
        logger.info("Restoring completed");
    }

    private void reverseMoveFile() throws IOException {
        for (FilePath paths: filesToSort) {
            Path sourcePath = paths.target();
            Path targetPath = paths.source();
            Path dirPath = targetPath.getParent();
            createDirectoryIfNotExists(dirPath);
            performMoveActionFor(sourcePath, targetPath);
        }
    }

    private void removeFiles() throws IOException {
        for (FilePath path: filesToSort) {
            logger.info("Deleting file from: {}", path.target().toAbsolutePath());
            Files.deleteIfExists(path.target());
        }
    }

    private void createDirectoryIfNotExists(Path dirPath) throws IOException {
        if (Files.notExists(dirPath)) {
            logger.info("Creating directory: {}", dirPath.toAbsolutePath());
            Files.createDirectories(dirPath);
        }
    }

    private void removeEmptyDirectories() throws IOException {
        while (!sourceDirectoriesPaths.isEmpty()) {
            Path dirPath = sourceDirectoriesPaths.pollFirst();
            File dir = dirPath.toFile();
            if (dir.exists() && dir.isDirectory() && FileUtils.isEmptyDirectory(dir)) {
                dir.delete();
            }
        }
    }

    public enum Action {
        COPY("Copy"),
        MOVE("Move");

        private final String name;

        Action(String name) {
            this.name = name;
        }

        public static Action getAction(String name) {
            Action[] actions = Action.values();
            for (Action action: actions) {
                if (action.name.equals(name)) {
                    return action;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }
    }
}
