package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.nio.file.Path;

public class FileExtensionStatement implements StringStatement {
    private FileData fileData;

    public FileExtensionStatement(FileData fileData, StringStatementDescription description) {
        this.fileData = fileData;
    }

    @Override
    public String execute() throws IOException {
        return getFileExtension();
    }

    public String getFileExtension() {
        Path sourcePath = fileData.getSourcePath();
        String fileName = sourcePath.getFileName().toString();
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            return fileName.substring(i + 1).trim();
        }
        return "";
    }
}
