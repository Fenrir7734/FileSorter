package com.fenrir.filesorter.model.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStructureMapper {
    private Path source;

    public FileStructureMapper(Path source) {
        this.source = source;
    }

    public List<Path> map() throws IOException {
        return Files.walk(source)
                .map(FileData::normalizeFilePath)
                .filter(Objects::nonNull)
                .toList();
    }
}
