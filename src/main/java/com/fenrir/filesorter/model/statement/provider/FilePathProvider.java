package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.nio.file.Path;

public class PathOperandStatement implements FilterOperandStatement<Path> {
    @Override
    public Path get(FileData fileData) {
        return fileData.getSourcePath();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        throw new UnsupportedOperationException();
    }
}
