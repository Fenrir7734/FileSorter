package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileCategoryProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnTextCategoryForFileWithTextExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider= new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getShouldReturnCorrectCategoryForFileWithIncorrectExtensionInFileName() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.mp3");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getShouldReturnCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getShouldReturnCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("text", category);
    }
/*
    @Test
    public void getShouldReturnOtherCategoryForFileWithoutMatchingCategory() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.xyz");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.get(fileData);
        assertEquals("others", category);
    }
 */
    @Test
    public void getAsStringShouldReturnTextCategoryForFileWithTextExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getAsStringShouldReturnCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getAsStringShouldReturnCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("text", category);
    }

    @Test
    public void getAsStringShouldReturnCorrectCategoryForFileWithIncorrectExtensionInFileName() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.mp3");
        FileData fileData = new FileData(path);
        Provider<String> provider = new FileCategoryProvider(null);
        String category = provider.getAsString(fileData);
        assertEquals("text", category);
    }

    @Nested
    class TestForFileWithoutMatchingCategory {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            path = tempDir.resolve("testimg.nef");
            file = path.toFile();

            try (FileOutputStream out = new FileOutputStream(file)) {
                BufferedImage image = new BufferedImage(320, 480, BufferedImage.TYPE_BYTE_GRAY);
                ImageIO.write(image, "nef", out);
            } catch (IOException e) {
                System.err.println("Error creating image: " + e);
            }
        }

        @Test
        public void testTempImageShouldPassIfFileIsImage() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertTrue(file.getAbsolutePath().endsWith("testimg.nef"));
        }

        @Test
        public void getShouldReturnOtherCategoryForFileWithoutMatchingCategory() throws IOException {
            FileData fileData = new FileData(path);
            Provider<String> provider = new FileCategoryProvider(null);
            String category = provider.get(fileData);
            assertEquals("others", category);
        }

        @Test
        public void getAsStringShouldReturnOtherCategoryForFileWithoutMatchingCategory() throws IOException {
            FileData fileData = new FileData(path);
            Provider<String> provider = new FileCategoryProvider(null);
            String category = provider.getAsString(fileData);
            assertEquals("others", category);
        }

    }
}