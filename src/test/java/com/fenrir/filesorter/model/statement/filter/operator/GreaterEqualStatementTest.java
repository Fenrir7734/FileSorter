package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GreaterEqualStatementTest {
    @TempDir
    private Path tempDir;
    private FileData file;

    @BeforeEach
    public void init() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }
}