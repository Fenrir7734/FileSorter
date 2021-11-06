package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileCategoryStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void getFileCategoryForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FileCategoryStatement statement = new FileCategoryStatement(null);
        String category = statement.execute(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getFileCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FileCategoryStatement statement = new FileCategoryStatement(null);
        String category = statement.execute(fileData);
        assertEquals("others", category);
    }

    @Test
    public void getFileCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        FileCategoryStatement statement = new FileCategoryStatement(null);
        String category = statement.execute(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getFileCategoryForFileWithoutMatchingCategory() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.xyz");
        FileData fileData = new FileData(path);
        FileCategoryStatement statement = new FileCategoryStatement(null);
        String category = statement.execute(fileData);
        assertEquals("others", category);
    }
}
