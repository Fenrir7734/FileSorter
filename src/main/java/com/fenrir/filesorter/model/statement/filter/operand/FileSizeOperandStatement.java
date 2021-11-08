package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileSizeOperandStatement implements FilterOperandStatement<Long> {

    @Override
    public Long execute(FileData fileData) throws IOException {
        return fileData.getFileSize();
    }
}
