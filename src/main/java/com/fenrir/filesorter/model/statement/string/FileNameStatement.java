package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileNameStatement implements StringStatement {

    public FileNameStatement(StringStatementDescription description) { }

    @Override
    public String execute(FileData fileData) throws IOException {
        return fileData.getFileNameWithoutExtension();
    }
}
