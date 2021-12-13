package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateCreatedProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileCreationTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        Provider<ChronoLocalDate> provider = new DateCreatedProvider(null);
        ChronoLocalDate actualDate = provider.get(fileData);
        ChronoLocalDate expectedDate = LocalDate.ofInstant(time.toInstant(), ZoneId.systemDefault());
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void getAsStringShouldReturnCreationTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        String pattern = "YYYY-MM-DD HH:mm:ss";
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        ProviderDescription description = ProviderDescription.ofDate(pattern);
        Provider<ChronoLocalDate> provider = new DateCreatedProvider(description);
        String actualDate = provider.getAsString(fileData);
        String expectedDate = new SimpleDateFormat(pattern).format(time.toMillis());
        assertEquals(expectedDate, actualDate);
    }
}