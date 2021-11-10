package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateOperandStatementTest {

    @TempDir
    Path tempDir;

    @Test
    public void getFileCreationTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        FilterOperandStatement<ChronoLocalDate> statement = new DateOperandStatement();
        ChronoLocalDate actualDate = statement.execute(fileData);
        ChronoLocalDate expectedDate = LocalDate.ofInstant(time.toInstant(), ZoneId.systemDefault());
        assertEquals(expectedDate, actualDate);
    }
}