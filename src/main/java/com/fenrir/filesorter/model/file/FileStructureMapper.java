package com.fenrir.filesorter.model.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStructureMapper {
    private Path source;

    public FileStructureMapper(Path source) {
        this.source = source;
    }

    public List<FileData> map() throws IOException {
        List<Path> fileStructure = Files.walk(source).toList();
        List<FileData> fileDataList = new ArrayList<>();
        for (Path path: fileStructure) {
            FileData fileData = new FileData(path);
            fileDataList.add(fileData);
        }
        return fileDataList;
    }
}
