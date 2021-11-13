package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileCategoryOperandStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void executeShouldReturnTextCategoryForFileWithTextExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FilterOperandStatement<String> statement = new FileCategoryOperandStatement();
        String category = statement.execute(fileData);
        assertEquals("text", category);
    }

    @Test
    public void executeShouldReturnAudioCategoryForFileWithAudioExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.mp3");
        FileData fileData = new FileData(path);
        FilterOperandStatement<String> statement = new FileCategoryOperandStatement();
        String category = statement.execute(fileData);
        assertEquals("audio", category);
    }

    @Test
    public void executeShouldReturnOtherCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FilterOperandStatement<String> statement = new FileCategoryOperandStatement();
        String category = statement.execute(fileData);
        assertEquals("others", category);
    }

    @Test
    public void executeShouldReturnCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        FilterOperandStatement<String> statement = new FileCategoryOperandStatement();
        String category = statement.execute(fileData);
        assertEquals("text", category);
    }

    @Test
    public void executeShouldReturnOtherCategoryForFileWithoutMatchingCategory() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.xyz");
        FileData fileData = new FileData(path);
        FilterOperandStatement<String> statement = new FileCategoryOperandStatement();
        String category = statement.execute(fileData);
        assertEquals("others", category);
    }
}