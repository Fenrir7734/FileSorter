package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileCategoryStatement implements StringStatement {

    public FileCategoryStatement(StringStatementDescription description) { }

    @Override
    public String execute(FileData fileData) throws IOException {
        return fileData.getFileCategory().getName();
    }
}
