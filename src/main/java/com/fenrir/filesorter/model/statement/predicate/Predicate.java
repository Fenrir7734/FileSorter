package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public interface Predicate<T extends Comparable<T>> {
    boolean test(FileData fileData) throws IOException;
}
