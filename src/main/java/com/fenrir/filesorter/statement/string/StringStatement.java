package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.statement.StatementDescription;

import java.io.IOException;

public interface StringStatement {
    String execute() throws IOException;
}
