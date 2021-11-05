package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;

import java.util.function.Predicate;

public interface FilterOperatorStatement {
    Predicate<FileData> execute();
}
