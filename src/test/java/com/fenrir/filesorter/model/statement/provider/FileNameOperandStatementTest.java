package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileNameOperandStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void executeShouldReturnFileNameWithoutExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> statement = new FileNameProvider();
        String name = statement.get(fileData);
        assertEquals("testfile", name);
    }

    @Test
    public void executeShouldReturnFileNameWithoutExtensionForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> statement = new FileNameProvider();
        String name = statement.get(fileData);
        assertEquals("testfile", name);
    }

    @Test
    public void executeShouldReturnEmptyStringForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> statement = new FileNameProvider();
        String name = statement.get(fileData);
        assertEquals("", name);
    }
}