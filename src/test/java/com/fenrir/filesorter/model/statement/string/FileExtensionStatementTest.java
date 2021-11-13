package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileExtensionStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void executeShouldReturnFileExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileExtensionStatement(null);
        String extension = statement.execute(fileData);
        assertEquals("txt", extension);
    }

    @Test
    public void executeShouldReturnEmptyStringForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileExtensionStatement(null);
        String extension = statement.execute(fileData);
        assertEquals("", extension);
    }

    @Test
    public void executeShouldReturnFileExtensionForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileExtensionStatement(null);
        String extension = statement.execute(fileData);
        assertEquals("txt", extension);
    }
}