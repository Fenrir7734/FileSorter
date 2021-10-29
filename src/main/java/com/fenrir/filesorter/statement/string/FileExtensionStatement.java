package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.statement.StatementDescription;

import java.io.IOException;
import java.nio.file.Path;

public class FileExtensionStatement implements StringStatement {
    private FileData fileData;

    public FileExtensionStatement(FileData fileData, StatementDescription description) {
        this.fileData = fileData;
    }

    @Override
    public String execute(FileData file) throws IOException {
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
