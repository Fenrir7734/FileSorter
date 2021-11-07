package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

import java.io.File;
import java.io.IOException;

public class FileSizeOperandStatement implements FilterOperandStatement<Long> {

    @Override
    public Long execute(FileData fileData) throws IOException {
        return getFileSize(fileData);
    }

    private long getFileSize(FileData fileData) {
        File file = fileData.getSourcePath().toFile();
        return file.length();
    }
}
