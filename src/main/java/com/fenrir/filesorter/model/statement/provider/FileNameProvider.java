package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileNameOperandStatement implements FilterOperandStatement<String> {
    @Override
    public String get(FileData fileData) {
        return fileData.getFileNameWithoutExtension();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return get(fileData);
    }
}
