package com.fenrir.filesorter.model.file;

import com.fenrir.filesorter.model.file.utils.FileCategoryType;
import com.fenrir.filesorter.model.file.utils.Dimension;
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
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import static org.junit.jupiter.api.Assertions.*;

class FileDataTest {

    @TempDir
    Path tempDir;

    @Test
    public void getCreationTimeShouldReturnFileTimeOfFileCreationTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        long creationTimeInMillis = fileData.creationTime().toMillis();
        assertEquals(time.toMillis(), creationTimeInMillis);
    }

    @Test
    public void getLastModifiedTimeShouldReturnFileTimeOfFileLastModifiedTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).lastModifiedTime();
        long lastModifiedTimeInMillis = fileData.lastModifiedTime().toMillis();
        assertEquals(time.toMillis(), lastModifiedTimeInMillis);
    }

    @Test
    public void getLastModifiedTimeShouldReturnFileTimeOfFileLastAccessTime() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FileTime time = Files.readAttributes(path, BasicFileAttributes.class).lastAccessTime();
        long lastAccessTimeInMillis = fileData.lastAccessTime().toMillis();
        assertEquals(time.toMillis(), lastAccessTimeInMillis);
    }

    @Test
    public void getFileNameWithExtensionShouldReturnFileNameWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileName();
        assertEquals("testfile.txt", fileName);
    }

    @Test
    public void getFileNameWithoutExtensionShouldReturnFileNameWithoutExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileNameWithoutExtension();
        assertEquals("testfile", fileName);
    }

    @Test
    public void getFileNameWithoutExtensionShouldReturnFileNameWithoutExtensionForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        String fileName = fileData.getFileNameWithoutExtension();
        assertEquals("testfile", fileName);
    }

    @Test
    public void getFileNameWithoutExtensionShouldReturnEmptyStringForFileNameWithOnlyExtension() throws IOException {
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
    public void hasExtensionShouldReturnTrueForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        boolean hasExtension = fileData.hasExtension();
        assertTrue(hasExtension);
    }

    @Test
    public void hasExtensionShouldReturnFalseForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        boolean hasExtension = fileData.hasExtension();
        assertFalse(hasExtension);
    }

    @Test
    public void hasExtensionShouldReturnFalseForDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        boolean hasExtension = fileData.hasExtension();
        assertFalse(hasExtension);
    }

    @Test
    public void getFileExtensionShouldReturnExtensionForFileWithExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        String extension = fileData.getFileExtension();
        assertEquals("txt", extension);
    }

    @Test
    public void getFileExtensionShouldReturnExtensionForFileWithMultipleDots() throws IOException {
        Path path = FileUtils.createFile(tempDir, "test.file.txt");
        FileData fileData = new FileData(path);
        String extension = fileData.getFileExtension();
        assertEquals("txt", extension);
    }

    @Test
    public void getFileExtensionShouldReturnEmptyStringForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        String extension = fileData.getFileExtension();
        assertEquals("", extension);
    }

    @Test
    public void getFileExtensionShouldReturnEmptyStringForDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir.test");
        FileData fileData = new FileData(path);
        String extension = fileData.getFileExtension();
        assertEquals("", extension);
    }

    @Test
    public void getRealExtensionShouldReturnExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        String extension = fileData.getRealFileExtension();
        assertEquals("txt", extension);
    }

    @Test
    public void getRealFileExtensionShouldReturnExtensionForFileWithoutExtensionInFileName() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        String extension = fileData.getRealFileExtension();
        assertEquals("txt", extension);
    }

    @Test
    public void getRealFileExtensionShouldReturnEmptyStringForDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir.test");
        FileData fileData = new FileData(path);
        String extension = fileData.getRealFileExtension();
        assertEquals("", extension);
    }

    @Test
    public void getFileCategoryShouldReturnTextCategoryForFileWithTextExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FileCategoryType category = fileData.getFileCategory();
        assertEquals(FileCategoryType.TEXT, category);
    }

    @Test
    public void getFileCategoryShouldReturnCategoryForFileWithoutExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        FileCategoryType category = fileData.getFileCategory();
        assertEquals(FileCategoryType.TEXT, category);
    }

    @Test
    public void getFileCategoryShouldReturnCategoryForFileWithOnlyExtension() throws IOException {
        Path path = FileUtils.createFile(tempDir, ".txt");
        FileData fileData = new FileData(path);
        FileCategoryType category = fileData.getFileCategory();
        assertEquals(FileCategoryType.TEXT, category);
    }

    @Test
    public void getFileCategoryShouldReturnOtherCategoryForDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        FileCategoryType category = fileData.getFileCategory();
        assertEquals(FileCategoryType.OTHERS, category);
    }

    @Test
    public void getFileCategoryShouldReturnTextCategoryForFileWithIncorrectExtensionInFileName() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.mp3");
        FileData fileData = new FileData(path);
        FileCategoryType category = fileData.getFileCategory();
        assertEquals(FileCategoryType.TEXT, category);
    }

    @Test
    public void getFileSizeShouldReturnFileSize() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        long size = fileData.getFileSize();
        assertEquals(12L, size);
    }

    @Test
    public void isImageShouldReturnFalseForNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        boolean isImage = fileData.isImage();
        assertFalse(isImage);
    }

    @Test
    public void isImageShouldReturnFalseForDirectory() throws IOException {
        FileData fileData = new FileData(tempDir);
        boolean isImage = fileData.isImage();
        assertFalse(isImage);
    }

    @Test
    public void isImageTest() throws IOException {
        FileData fileData = new FileData(Path.of("/home/fenrir/Documents/Test_environment/wall/HD wallpaper person leaning on tree illustration, landscape, digital art, coast.jpeg"));
        assertTrue(fileData.isImage());
    }

    @Test
    public void getImageDimensionShouldReturnNullForNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Dimension dimension = fileData.getImageDimension();
        assertNull(dimension);
    }

    @Test
    public void testResolveTargetPathForFileWithExtensionAndCountEqualZero() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/testfile.txt");
        fileData.setTargetPath(targetPath, 0);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        assertEquals(targetPath, resolvedTargetPath);
    }

    @Test
    public void testResolveTargetPathForFileWithExtensionAndCountEqualOne() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/testfile.txt");
        fileData.setTargetPath(targetPath, 1);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        Path expectedTargetPath = Path.of("/home/user/Documents/testfile (1).txt");
        assertEquals(expectedTargetPath, resolvedTargetPath);
    }

    @Test
    public void testResolveTargetPathForDirectory() throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/dir");
        fileData.setTargetPath(targetPath, 2);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        assertEquals(targetPath, resolvedTargetPath);
    }

    @Test
    public void testResolveTargetPathForFileWithoutExtensionAndCountEqualOne() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile");
        FileData fileData = new FileData(path);
        Path targetPath = Path.of("/home/user/Documents/testfile");
        fileData.setTargetPath(targetPath, 1);
        Path resolvedTargetPath = fileData.resolveTargetPath();
        Path expectedTargetPath = Path.of("/home/user/Documents/testfile (1)");
        assertEquals(expectedTargetPath, resolvedTargetPath);
    }

    @Nested
    class TestImageFileWithExtension {
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
        public void testTempImageShouldPassIfFileIsImage() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertTrue(file.getAbsolutePath().endsWith("testimg.png"));
        }

        @Test
        public void isImageShouldReturnTrueForImage() throws IOException {
            FileData fileData = new FileData(path);
            boolean isImage = fileData.isImage();
            assertTrue(isImage);
        }

        @Test
        public void getImageDimensionShouldReturnImageDimensionForImageFile() throws IOException {
            FileData fileData = new FileData(path);
            Dimension actualDimension = fileData.getImageDimension();
            Dimension expectedDimension = Dimension.of(320, 480);
            assertEquals(expectedDimension, actualDimension);
        }
    }

    @Nested
    class TestImageFileWithoutExtension {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            path = tempDir.resolve("testimg");
            file = path.toFile();

            try (FileOutputStream out = new FileOutputStream(file)) {
                BufferedImage image = new BufferedImage(320, 480, BufferedImage.TYPE_BYTE_GRAY);
                ImageIO.write(image, "png", out);
            } catch (IOException e) {
                System.err.println("Error creating image: " + e);
            }
        }

        @Test
        public void testTempImageShouldPassIfFileIsImage() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertTrue(file.getAbsolutePath().endsWith("testimg"));
        }

        @Test
        public void isImageShouldReturnTrueForImageWithoutExtensionInFilename() throws IOException {
            FileData fileData = new FileData(path);
            boolean isImage = fileData.isImage();
            assertTrue(isImage);
        }

        @Test
        public void getImageDimensionShouldReturnImageDimensionForImageWithoutExtensionInFilename() throws IOException {
            FileData fileData = new FileData(path);
            Dimension actualDimension = fileData.getImageDimension();
            Dimension expectedDimension = Dimension.of(320, 480);
            assertEquals(expectedDimension, actualDimension);
        }
    }

    @Test
    public void isDirectoryShouldReturnTrueForDirectory()throws IOException {
        Path path = FileUtils.createDirectory(tempDir, "dir");
        FileData fileData = new FileData(path);
        boolean isDir = fileData.isDirectory();
        assertTrue(isDir);
    }

    @Test
    public void isDirectoryShouldReturnFalseForFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        boolean isDir = fileData.isDirectory();
        assertFalse(isDir);
    }
}