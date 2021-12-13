package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateCurrentProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldThrowUnsupportedOperationException() throws IOException {
        ProviderDescription description = ProviderDescription.ofDate("YYYY-MM-DD HH:mm:ss");
        Provider<ChronoLocalDate> provider = new DateCurrentProvider(description);
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        assertThrows(
                UnsupportedOperationException.class,
                () -> provider.get(fileData)
        );
    }

    @Test
    public void getAsStringShouldReturnFileLastAccessTime() throws IOException {
        Date currentDate = new Date();
        String pattern = "YYYY-MM-DD HH:mm:ss";
        ProviderDescription description = ProviderDescription.ofDate(pattern);
        Provider<ChronoLocalDate> provider = new DateCurrentProvider(description);
        String actualDate = provider.getAsString(null);
        String expectedDate = new SimpleDateFormat(pattern).format(currentDate);
        assertEquals(expectedDate, actualDate);
    }
}