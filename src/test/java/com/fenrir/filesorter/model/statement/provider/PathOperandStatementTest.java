package com.fenrir.filesorter.model.statement.provider;

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
        Provider<Path> statement = new FilePathProvider();
        Path actualPath = statement.get(fileData);
        assertEquals(path, actualPath);
    }

}