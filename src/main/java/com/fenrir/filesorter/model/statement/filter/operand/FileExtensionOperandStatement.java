package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileExtensionOperandStatement implements FilterOperandStatement<String> {
    @Override
    public String execute(FileData fileData) throws IOException {
        String extension = fileData.getFileExtension();
        return extension != null ? extension : "";
    }
}
