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
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateAccessedProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileLastAccessTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FileTime accessTime = Files.readAttributes(path, BasicFileAttributes.class).lastAccessTime();
        FileTime newFileAccessTime = FileTime.fromMillis(accessTime.toMillis() + 100);
        Files.setAttribute(path, "lastAccessTime", newFileAccessTime);
        Provider<ChronoLocalDate> provider = new DateAccessedProvider(null);
        ChronoLocalDate actualDate = provider.get(fileData);
        ChronoLocalDate expectedDate = LocalDate.ofInstant(newFileAccessTime.toInstant(), ZoneId.systemDefault());
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void getAsStringShouldReturnFileLastAccessTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        String pattern = "YYYY-MM-DD HH:mm:ss";
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).lastAccessTime();
        FileTime newFileAccessTime = FileTime.fromMillis(time.toMillis() + 100);
        Files.setAttribute(path, "lastAccessTime", newFileAccessTime);
        ProviderDescription description = ProviderDescription.ofDate(pattern);
        Provider<ChronoLocalDate> provider = new DateAccessedProvider(description);
        String actualDate = provider.getAsString(fileData);
        String expectedDate = time.toInstant()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(pattern));
        assertEquals(expectedDate, actualDate);
    }
}