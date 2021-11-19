package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FilePathProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileSourcePath() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Path> provider = new FilePathProvider(null);
        Path actualPath = provider.get(fileData);
        assertEquals(path, actualPath);
    }

    @Test
    public void getAsStringShouldThrowUnsupportedOperationException() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Path> provider = new FilePathProvider(null);
        assertThrows(
                UnsupportedOperationException.class,
                () -> provider.getAsString(fileData)
        );
    }
}