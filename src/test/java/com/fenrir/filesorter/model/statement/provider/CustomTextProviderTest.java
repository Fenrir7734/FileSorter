package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CustomTextProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldThrowUnsupportedOperationException() throws IOException {
        ProviderDescription description = ProviderDescription.ofLiteral("abc");
        Provider<String> provider = new CustomTextProvider(description);
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        assertThrows(
                UnsupportedOperationException.class,
                () -> provider.get(fileData)
        );
    }

    @Test
    public void getAsStringShouldReturnCustomText() throws IOException {
        ProviderDescription description = ProviderDescription.ofLiteral("abc");
        Provider<String> provider = new CustomTextProvider(description);
        String actualLiteral = provider.getAsString(null);
        assertEquals("abc", actualLiteral);
    }
}