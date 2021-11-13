package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilsTest {
    @TempDir
    Path tempDir;

    @Test
    public void createFileShouldReturnValidFile() {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");

        assertNotNull(path);

        File file = path.toFile();

        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertEquals(file.length(), 12L);
        assertTrue(file.getAbsolutePath().endsWith("testfile.txt"));
    }

    @Test
    public void createDirectoryShouldReturnValidDirectory() {
        Path path = FileUtils.createDirectory(tempDir, "dir");

        assertNotNull(path);

        File file = path.toFile();

        assertTrue(file.exists());
        assertTrue(file.isDirectory());
        assertTrue(file.getAbsolutePath().endsWith("dir"));
    }
}
