package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileNameProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileNameWithoutExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameProvider(null);
        String name = provider.get(fileData);
        assertEquals("testfile", name);
    }

    @Test
    public void getShouldReturnFileNameWithoutExtensionForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameProvider(null);
        String name = provider.get(fileData);
        assertEquals("testfile", name);
    }

    @Test
    public void getShouldReturnEmptyStringForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameProvider(null);
        String name = provider.get(fileData);
        assertEquals("", name);
    }

    @Test
    public void getAsStringShouldReturnFileNameWithoutExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameProvider(null);
        String name = provider.getAsString(fileData);
        assertEquals("testfile", name);
    }

    @Test
    public void getAsStringShouldReturnFileNameWithoutExtensionForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameProvider(null);
        String name = provider.getAsString(fileData);
        assertEquals("testfile", name);
    }

    @Test
    public void getAsStringShouldReturnEmptyStringForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileNameProvider(null);
        String name = provider.getAsString(fileData);
        assertEquals("", name);
    }
}