package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class LiteralStatement implements StringStatement {
    private final String literal;

    public LiteralStatement(StringStatementDescription description) {
        this.literal = description.getLiteral();
    }

    @Override
    public String execute(FileData fileData) throws IOException {
        return literal;
    }
}
