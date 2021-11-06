package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.File;
import java.io.IOException;

public class FileSeparatorStatement implements StringStatement {

    public FileSeparatorStatement(StringStatementDescription description) { }

    @Override
    public String execute(FileData fileData) throws IOException {
        return File.separator;
    }
}
