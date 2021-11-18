package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileSizeOperandStatement implements FilterOperandStatement<Long> {

    @Override
    public Long get(FileData fileData) throws IOException {
        return fileData.getFileSize();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        throw new UnsupportedOperationException();
    }
}
