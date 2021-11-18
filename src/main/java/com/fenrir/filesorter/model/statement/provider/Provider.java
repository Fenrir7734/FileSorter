package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public interface FilterOperandStatement<T extends Comparable<T>> {
    T get(FileData fileData) throws IOException;
    String getAsString(FileData fileData) throws IOException;
}
