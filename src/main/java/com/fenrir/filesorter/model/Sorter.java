package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.file.FileData;
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

    private final Processor processor;

    public Sorter(Processor processor) {
        this.processor = processor;
    }

    public void sort() throws IOException {
        processor.process();
        List<FileData> filesToSort = processor.getFileStructure();

        for(FileData file: filesToSort) {
            if (file.getTargetPath() != null
                    && !file.isDirectory()
                    && file.isIncluded()) {
                Path newPath = copyFile(file);
            }
        }
        logger.info("Sort complete");
    }

    private Path copyFile(FileData file) throws IOException {
        Path sourcePath = file.getSourcePath();
        Path targetPath = file.resolveTargetPath();
        Path dirPath = targetPath.getParent();

        if (Files.notExists(dirPath)) {
            logger.info("Creating directory {}", dirPath.toAbsolutePath());
            Files.createDirectories(dirPath);
        }

        logger.info("Copying file from: {} to: {}", sourcePath.toAbsolutePath(), targetPath.toFile());
        return Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
    }
}
