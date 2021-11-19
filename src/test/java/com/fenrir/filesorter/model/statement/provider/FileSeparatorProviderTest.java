package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileSeparatorProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldThrowUnsupportedOperationException() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileSeparatorProvider(null);
        assertThrows(
                UnsupportedOperationException.class,
                () -> provider.get(fileData)
        );
    }

    @Test
    public void getAsStringShouldReturnFileSeparator() throws IOException {
        Provider<String> provider = new FileSeparatorProvider(null);
        String separator = provider.getAsString(null);
        assertEquals(File.separator, separator);
    }
}