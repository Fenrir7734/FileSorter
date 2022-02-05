package com.fenrir.filesorter.model.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileStructureMapper {
    private FileStructureMapper() {
        throw new UnsupportedOperationException();
    }

    public static Deque<Path> map(Path source) throws IOException {
        Deque<Path> fileHierarchy = new ArrayDeque<>();
        Files.walk(source)
                .map(FileData::normalizeFilePath)
                .filter(Objects::nonNull)
                .forEach(fileHierarchy::push);
        return fileHierarchy;
    }

    public static Deque<Path> mapDirectoriesHierarchy(Path source) throws IOException {
        Deque<Path> directoriesHierarchy = new ArrayDeque<>();
        Files.walk(source)
                .filter(Files::isDirectory)
                .forEach(directoriesHierarchy::push);
        return directoriesHierarchy;
    }
}
