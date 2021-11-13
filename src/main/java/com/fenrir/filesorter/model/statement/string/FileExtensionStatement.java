package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileExtensionStatement implements StringStatement {

    public FileExtensionStatement(StringStatementDescription description) { }

    @Override
    public String execute(FileData fileData) throws IOException {
        String extension = fileData.getFileExtension();
        return extension != null ? extension : "";
    }
}
