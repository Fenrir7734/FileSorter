package com.fenrir.filesorter.model.file;

import com.fenrir.filesorter.model.file.utils.Category;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.statement.string.FileCategoryStatement;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class FileDataTest {

    @TempDir
    Path tempDir;

    @Test
    public void getCreationTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        assertEquals(time.toMillis(), fileData.creationTime().getTimeInMillis());
        long creationTimeInMillis = fileData.creationTime().getTimeInMillis();
        assertEquals(time.toMillis(), creationTimeInMillis);
    }

    @Test
    public void getLastModifiedTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).lastModifiedTime();
        long lastModifiedTimeInMillis = fileData.lastModifiedTime().getTimeInMillis();
        assertEquals(time.toMillis(), lastModifiedTimeInMillis);
    }

    @Test
    public void getFileNameWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileName();
        assertEquals("testfile.txt", fileName);
    }

    @Test
    public void getFileNameWithoutExtensionOfFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileNameWithoutExtension();
        assertEquals("testfile", fileName);
    }

    @Test
    public void getFileNameWithoutExtensionOfFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileNameWithoutExtension();
        assertEquals("testfile", fileName);
    }

    @Test
    public void getFileNameWithoutExtensionOfFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileNameWithoutExtension();
        assertEquals("", fileName);
    }

    @Test
    public void getFileNameWithoutExtensionOfFileWithNameContainingMultipleDots() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.tar.gz");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileNameWithoutExtension();
        assertEquals("testfile.tar", fileName);
    }

    @Test
    public void hasExtensionOfFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        boolean hasExtension = fileData.hasExtension();
        assertTrue(hasExtension);
    }

    @Test
    public void hasExtensionOfFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        boolean hasExtension = fileData.hasExtension();
        assertFalse(hasExtension);
    }

    @Test
    public void hasExtensionOfDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        boolean hasExtension = fileData.hasExtension();
        assertFalse(hasExtension);
    }

    @Test
    public void getFileCategoryOfFileWithTextExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Category category = fileData.getFileCategory();
        assertEquals(Category.TEXT, category);
    }

    @Test
    public void getFileCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Category category = fileData.getFileCategory();
        assertEquals(Category.OTHERS, category);
    }

    @Test
    public void getFileCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".mp3");
        FileData fileData = new FileData(path);
        Category category = fileData.getFileCategory();
        assertEquals(Category.AUDIO, category);
    }

    @Test
    public void getFileCategoryForDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        Category category = fileData.getFileCategory();
        assertNull(category);
    }

    @Test
    public void getFileCategoryForFileWithoutMatchingCategory() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.xyz");
        FileData fileData = new FileData(path);
        Category category = fileData.getFileCategory();
        assertEquals(Category.OTHERS, category);
    }

    @Test
    public void getFileSize() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        long size = fileData.getFileSize();
        assertEquals(12L, size);
    }

    @Test
    public void isImageForNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        boolean isImage = fileData.isImage();
        assertFalse(isImage);
    }

    @Test
    public void getImageDimensionOfNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Dimension dimension = fileData.getImageDimension();
        assertNull(dimension);
    }

    @Test
    public void resolveTargetPathForFileWithExtensionAndCountEqualZero() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/testfile.txt");
        fileData.setTargetPath(targetPath, 0);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        assertEquals(targetPath, resolvedTargetPath);
    }

    @Test
    public void resolveTargetPathForFileWithExtensionAndCountEqualOne() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/testfile.txt");
        fileData.setTargetPath(targetPath, 1);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        Path expectedTargetPath = Path.of("/home/user/Documents/testfile (1).txt");
        assertEquals(expectedTargetPath, resolvedTargetPath);
    }

    @Test
    public void resolveTargetPathForDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/dir");
        fileData.setTargetPath(targetPath, 2);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        assertEquals(targetPath, resolvedTargetPath);
    }

    @Test
    public void resolveTargetPathForFileWithoutExtensionAndCountEqualOne() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/testfile");
        fileData.setTargetPath(targetPath, 1);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        Path expectedTargetPath = Path.of("/home/user/Documents/testfile (1)");
        assertEquals(expectedTargetPath, resolvedTargetPath);
    }

    @Nested
    class TestImageFile {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            path = tempDir.resolve("testimg.png");
            file = path.toFile();

            try (FileOutputStream out = new FileOutputStream(file)) {
                BufferedImage image = new BufferedImage(320, 480, BufferedImage.TYPE_BYTE_GRAY);
                ImageIO.write(image, "png", out);
            } catch (IOException e) {
                System.err.println("Error creating image: " + e);
            }
        }

        @Test
        public void testTempImage() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertTrue(file.getAbsolutePath().endsWith("testimg.png"));
        }

        @Test
        public void isImage() throws IOException {
            FileData fileData = new FileData(path);
            boolean isImage = fileData.isImage();
            assertTrue(isImage);
        }

        @Test
        public void getImageDimension() throws IOException {
            FileData fileData = new FileData(path);
            Dimension actualDimension = fileData.getImageDimension();
            Dimension expectedDimension = Dimension.of(320, 480);
            assertEquals(expectedDimension, actualDimension);
        }
    }

    @Test
    public void isDirectoryForDirectory()throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        boolean isDir = fileData.isDirectory();
        assertTrue(isDir);
    }

    @Test
    public void isDirectoryForFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        boolean isDir = fileData.isDirectory();
        assertFalse(isDir);
    }
}