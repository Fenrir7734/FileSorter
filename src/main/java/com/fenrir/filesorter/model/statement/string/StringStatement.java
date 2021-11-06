package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public interface StringStatement {
    String execute(FileData fileData) throws IOException;
}
