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

class DateModifiedProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnFileLastModifiedTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FileTime modifiedTime = Files.readAttributes(path, BasicFileAttributes.class).lastModifiedTime();
        FileTime newFileModifiedTime = FileTime.fromMillis(modifiedTime.toMillis() + 100);
        Files.setAttribute(path, "lastModifiedTime", newFileModifiedTime);
        Provider<ChronoLocalDate> provider = new DateModifiedProvider(null);
        ChronoLocalDate actualDate = provider.get(fileData);
        ChronoLocalDate expectedDate = LocalDate.ofInstant(newFileModifiedTime.toInstant(), ZoneId.systemDefault());
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void getAsStringShouldReturnFileLastModifiedTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        String pattern = "YYYY-MM-DD HH:mm:ss";
        FileData fileData = new FileData(path);
        FileTime modifiedTime = Files.readAttributes(path, BasicFileAttributes.class).lastModifiedTime();
        FileTime newFileModifiedTime = FileTime.fromMillis(modifiedTime.toMillis() + 100);
        Files.setAttribute(path, "lastModifiedTime", newFileModifiedTime);
        ProviderDescription description = ProviderDescription.ofDate(pattern);
        Provider<ChronoLocalDate> provider = new DateModifiedProvider(description);
        String actualDate = provider.getAsString(fileData);
        String expectedDate = new SimpleDateFormat(pattern).format(newFileModifiedTime.toMillis());
        assertEquals(expectedDate, actualDate);
    }
}