package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public interface FilterOperandStatement<T extends Comparable<T>> {
    T execute(FileData fileData) throws IOException;
}
