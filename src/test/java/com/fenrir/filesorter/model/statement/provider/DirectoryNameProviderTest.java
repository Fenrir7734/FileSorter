package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryNameProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileParentDirectoryName() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new DirectoryNameProvider();
        String actualName = provider.get(fileData);
        String expectedName = path.getParent().getFileName().toString();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void getAsStringShouldReturnFileParentDirectoryName() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new DirectoryNameProvider();
        String actualName = provider.getAsString(fileData);
        String expectedName = path.getParent().getFileName().toString();
        assertEquals(expectedName, actualName);
    }
}