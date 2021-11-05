package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

import java.nio.file.Path;

public class PathOperandStatement implements FilterOperandStatement<Path> {
    @Override
    public Path execute(FileData fileData) {
        return fileData.getSourcePath();
    }
}
