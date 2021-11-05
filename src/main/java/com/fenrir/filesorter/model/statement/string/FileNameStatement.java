package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileNameStatement implements StringStatement {
    private FileData fileData;

    public FileNameStatement(FileData fileData, StringStatementDescription description) {
        this.fileData = fileData;
    }

    @Override
    public String execute() throws IOException {
        String fileName =  fileData.getSourcePath().getFileName().toString();
        int extensionIndex = fileName.lastIndexOf(".");
        return extensionIndex != -1 ?
                fileName.substring(0, extensionIndex) : fileName;
    }
}
