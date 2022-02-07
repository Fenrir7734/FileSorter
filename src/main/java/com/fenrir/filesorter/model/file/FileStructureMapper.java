package com.fenrir.filesorter.model.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FileStructureMapper {
    private static final Logger logger = LoggerFactory.getLogger(FileStructureMapper.class);

    private static ArrayDeque<Path> fileHierarchy;

    private FileStructureMapper() {
        throw new UnsupportedOperationException();
    }

    public static Deque<Path> map(Path source) throws IOException {
        fileHierarchy = new ArrayDeque<>();
        Files.walkFileTree(source, new MappingFileVisitor());
        return fileHierarchy.clone();
    }

    private static class MappingFileVisitor extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            storePath(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            storePath(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            if (Files.isDirectory(file)) {
                logger.warn("Failed to open directory: {}", exc.getMessage());
            } else {
                logger.warn("Failed to visit file: {}", exc.getMessage());
            }
            return FileVisitResult.CONTINUE;
        }

        private void storePath(Path path) {
            path = FileData.normalizeFilePath(path);
            if (path != null) {
                fileHierarchy.push(path);
            }
        }
    }
}
