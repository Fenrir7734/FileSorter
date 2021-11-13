package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeOperandStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void executeShouldReturnFileSize() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FilterOperandStatement<Long> statement = new FileSizeOperandStatement();
        long size = statement.execute(fileData);
        assertEquals(12L, size);
    }
}