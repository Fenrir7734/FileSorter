package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.nio.file.Path;

public class DirectoryPathProvider implements Provider<Path> {
    @Override
    public Path get(FileData fileData) throws IOException {
        return null;
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return null;
    }
}
