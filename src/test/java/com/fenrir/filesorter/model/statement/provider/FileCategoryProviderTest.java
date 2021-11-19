package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileCategoryProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnTextCategoryForFileWithTextExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider= new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getShouldReturnAudioCategoryForFileWithAudioExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.mp3");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("audio", category);
    }

    @Test
    public void getShouldReturnOtherCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("others", category);
    }

    @Test
    public void getShouldReturnCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getShouldReturnOtherCategoryForFileWithoutMatchingCategory() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.xyz");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("others", category);
    }

    @Test
    public void getAsStringShouldReturnTextCategoryForFileWithTextExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getAsStringShouldReturnAudioCategoryForFileWithAudioExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.mp3");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("audio", category);
    }

    @Test
    public void getAsStringShouldReturnOthersCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("others", category);
    }

    @Test
    public void getAsStringShouldReturnCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getAsStringShouldReturnOthersCategoryForFileWithoutMatchingCategory() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.xyz");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("others", category);
    }
}