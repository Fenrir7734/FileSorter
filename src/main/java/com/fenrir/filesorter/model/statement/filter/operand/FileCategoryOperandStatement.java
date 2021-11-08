package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileCategoryOperandStatement implements FilterOperandStatement<String> {
    @Override
    public String execute(FileData fileData) throws IOException {
        return fileData.getFileCategory().getName();
    }
}
