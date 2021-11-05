package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

public interface FilterOperandStatement<T extends Comparable<T>> {
    Comparable<T> execute(FileData fileData);
}
