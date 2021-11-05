package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.string.StatementDescription;

import java.util.function.Predicate;

public class EqualStatement {
    private StatementDescription description;

    public EqualStatement(StatementDescription description) {
        this.description = description;
    }

    public Predicate<FileData> execute() {
        return new Predicate<FileData>() {
            @Override
            public boolean test(FileData fileData) {
                return false;
            }
        };
    }
}
