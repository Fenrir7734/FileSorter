package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
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
    public void getFileExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileExtensionStatement(fileData, null);
        String extension = statement.execute();
        assertEquals("txt", extension);
    }

    @Test
    public void getFileExtensionForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileExtensionStatement(fileData, null);
        String extension = statement.execute();
        assertEquals("", extension);
    }

    @Test
    public void getFileExtensionForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileExtensionStatement(fileData, null);
        String extension = statement.execute();
        assertEquals("txt", extension);
    }
}