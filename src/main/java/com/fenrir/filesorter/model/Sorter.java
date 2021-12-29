package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.file.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class Sorter {
    private static final Logger logger = LoggerFactory.getLogger(Sorter.class);

    private List<FilePath> filesToSort;

    public Sorter(List<FilePath> filePaths) {
        this.filesToSort = filePaths;
    }

    public void sort() throws IOException {
        for(FilePath paths: filesToSort) {
            copyFile(paths);
        }
        logger.info("Sort complete");
    }

    private Path copyFile(FilePath path) throws IOException {
        Path sourcePath = path.source();
        Path targetPath = path.resolvedTargetPath();
        Path dirPath = targetPath.getParent();

        if (Files.notExists(dirPath)) {
            logger.info("Creating directory {}", dirPath.toAbsolutePath());
            Files.createDirectories(dirPath);
        }

        logger.info("Copying file from: {} to: {}", sourcePath.toAbsolutePath(), targetPath.toFile());
        return Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
    }
}
