package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileNameStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void getFileNameForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileNameStatement(fileData, null);
        String name = statement.execute();
        assertEquals("testfile", name);
    }

    @Test
    public void getFileNameForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileNameStatement(fileData, null);
        String name = statement.execute();
        assertEquals("testfile", name);
    }

    @Test
    public void getFileNameForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        StringStatement statement = new FileNameStatement(fileData, null);
        String name = statement.execute();
        assertEquals("", name);
    }
}