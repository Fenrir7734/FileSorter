package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileExtensionProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileExtensionProvider(null);
        String extension = provider.get(fileData);
        assertEquals("txt", extension);
    }

    @Test
    public void getShouldReturnEmptyStringForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileExtensionProvider(null);
        String extension = provider.get(fileData);
        assertEquals("", extension);
    }

    @Test
    public void getShouldReturnFileExtensionForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileExtensionProvider(null);
        String extension = provider.get(fileData);
        assertEquals("txt", extension);
    }

    @Test
    public void getAsStringShouldReturnFileExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileExtensionProvider(null);
        String extension = provider.getAsString(fileData);
        assertEquals("txt", extension);
    }

    @Test
    public void getAsStringShouldReturnUnknownForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileExtensionProvider(null);
        String extension = provider.getAsString(fileData);
        assertEquals("Unknown", extension);
    }

    @Test
    public void getAsStringShouldReturnFileExtensionForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileExtensionProvider(null);
        String extension = provider.getAsString(fileData);
        assertEquals("txt", extension);
    }
}