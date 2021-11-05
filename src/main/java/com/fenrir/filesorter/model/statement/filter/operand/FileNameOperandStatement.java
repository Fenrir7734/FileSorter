package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

public class FileNameOperandStatement implements FilterOperandStatement<String> {
    @Override
    public String execute(FileData fileData) {
        return fileData.getSourcePath().getFileName().toString();
    }
}
