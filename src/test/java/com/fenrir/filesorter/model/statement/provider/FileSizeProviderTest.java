package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileSize() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Long> provider = new FileSizeProvider(null);
        long size = provider.get(fileData);
        assertEquals(12L, size);
    }

    @Test
    public void getAsStringShouldThrowUnsupportedOperationException() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Long> provider = new FileSizeProvider(null);
        assertThrows(
                UnsupportedOperationException.class,
                () -> provider.getAsString(fileData)
        );
    }
}