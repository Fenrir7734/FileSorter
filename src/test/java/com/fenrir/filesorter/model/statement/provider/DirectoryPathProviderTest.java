package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryPathProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileParentDirectoryPath() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Path> provider = new DirectoryPathProvider();
        Path actualPath = provider.get(fileData);
        Path expectedPath = path.getParent();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void getAsStringShouldThrowUnsupportedOperationException() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Path> provider = new DirectoryPathProvider();
        assertThrows(
                UnsupportedOperationException.class,
                () -> provider.getAsString(fileData)
        );
    }
}