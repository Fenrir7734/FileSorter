package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileNameWithExtensionProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileNameWithExtensionForFileWithExtension() throws IOException {
        String fileName = "testfile.txt";
        Path path = FileUtils.createFile(tempDir, fileName);
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameWithExtensionProvider();
        String actualName = provider.get(fileData);
        assertEquals(fileName, actualName);
    }

    @Test
    public void getShouldReturnFileNameWithoutExtensionForFileWithoutExtension() throws IOException {
        String fileName = "testfile";
        Path path = FileUtils.createFile(tempDir, fileName);
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameWithExtensionProvider();
        String actualName = provider.get(fileData);
        assertEquals(fileName, actualName);
    }

    @Test
    public void getShouldReturnFileExtensionForFileNameWithOnlyExtension() throws IOException {
        String fileName = ".txt";
        Path path = FileUtils.createFile(tempDir, fileName);
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameWithExtensionProvider();
        String actualName = provider.get(fileData);
        assertEquals(fileName, actualName);
    }

    @Test
    public void getAsStringShouldReturnFileNameWithExtensionForFileWithExtension() throws IOException {
        String fileName = "testfile.txt";
        Path path = FileUtils.createFile(tempDir, fileName);
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameWithExtensionProvider();
        String actualName = provider.getAsString(fileData);
        assertEquals(fileName, actualName);
    }

    @Test
    public void getAsStringShouldReturnFileNameWithoutExtensionForFileWithoutExtension() throws IOException {
        String fileName = "testfile";
        Path path = FileUtils.createFile(tempDir, fileName);
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameWithExtensionProvider();
        String actualName = provider.getAsString(fileData);
        assertEquals(fileName, actualName);
    }

    @Test
    public void getAsStringShouldReturnFileExtensionForFileNameWithOnlyExtension() throws IOException {
        String fileName = ".txt";
        Path path = FileUtils.createFile(tempDir, fileName);
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameWithExtensionProvider();
        String actualName = provider.getAsString(fileData);
        assertEquals(fileName, actualName);
    }
}