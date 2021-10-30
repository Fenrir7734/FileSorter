package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.statement.StatementDescription;

import java.io.File;
import java.io.IOException;

public class FileSeparatorStatement implements StringStatement {

    public FileSeparatorStatement(FileData data, StatementDescription description) { }

    @Override
    public String execute() throws IOException {
        return File.separator;
    }
}
