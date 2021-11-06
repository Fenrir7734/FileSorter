package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileNameStatement implements StringStatement {

    public FileNameStatement(StringStatementDescription description) { }

    @Override
    public String execute(FileData fileData) throws IOException {
        return getFileName(fileData);
    }

    private String getFileName(FileData fileData) throws IOException {
        String fileName =  fileData.getSourcePath().getFileName().toString();
        int extensionIndex = fileName.lastIndexOf(".");
        return extensionIndex != -1 ?
                fileName.substring(0, extensionIndex) : fileName;
    }
}
