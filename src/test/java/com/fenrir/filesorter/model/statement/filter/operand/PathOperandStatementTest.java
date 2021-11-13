package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PathOperandStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void executeShouldReturnFileSourcePath() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FilterOperandStatement<Path> statement = new PathOperandStatement();
        Path actualPath = statement.execute(fileData);
        assertEquals(path, actualPath);
    }

}