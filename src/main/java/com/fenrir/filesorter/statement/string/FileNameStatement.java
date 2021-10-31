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
        String fileName =  fileData.getSourcePath().getFileName().toString();
        int extensionIndex = fileName.lastIndexOf(".");
        return extensionIndex != -1 ?
                fileName.substring(0, extensionIndex) : fileName;
    }
}
