package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
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
    public void getAsStringShouldReturnCurrentTime() throws IOException {
        LocalDateTime currentDate = LocalDateTime.now();
        String pattern = "YYYY-MM-DD HH:mm:ss A";
        ProviderDescription description = ProviderDescription.ofDate(pattern);
        DateCurrentProvider provider = new DateCurrentProvider(description);
        String actualDate = provider.getAsString(currentDate);
        String expectedDate = currentDate.format(DateTimeFormatter.ofPattern(pattern));
        assertEquals(expectedDate, actualDate);
    }
}