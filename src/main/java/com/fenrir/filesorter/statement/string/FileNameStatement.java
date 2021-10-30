package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.statement.StatementDescription;

import java.io.IOException;

public class FileNameStatement implements StringStatement {
    private FileData fileData;

    public FileNameStatement(FileData fileData, StatementDescription description) {
        this.fileData = fileData;
    }

    @Override
    public String execute() throws IOException {
        return fileData.getSourcePath().getFileName().toString();
    }
}
