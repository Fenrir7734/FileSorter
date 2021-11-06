package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.nio.file.Path;

public class FileExtensionStatement implements StringStatement {

    public FileExtensionStatement(StringStatementDescription description) { }

    @Override
    public String execute(FileData fileData) throws IOException {
        return getFileExtension(fileData);
    }

    public String getFileExtension(FileData fileData) {
        Path sourcePath = fileData.getSourcePath();
        String fileName = sourcePath.getFileName().toString();
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            return fileName.substring(i + 1).trim();
        }
        return "";
    }
}
