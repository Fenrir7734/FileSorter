package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
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
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImageDimensionProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnNullForNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Dimension> provider = new ImageDimensionProvider(null);
        Dimension actualDimension = provider.get(fileData);
        assertNull(actualDimension);
    }

    @Test
    public void getAsStringShouldReturnStringForNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Dimension> provider = new ImageDimensionProvider(null);
        String resolution = provider.getAsString(fileData);
        assertEquals("NonImage", resolution);
    }

    @Nested
    class TestForImage {
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
        public void getShouldReturnImageDimensionForImageFile() throws IOException {
            FileData fileData = new FileData(path);
            Provider<Dimension> provider = new ImageDimensionProvider(null);
            Dimension actualDimension = provider.get(fileData);
            Dimension expectedDimension = Dimension.of(320, 480);
            assertEquals(expectedDimension, actualDimension);
        }

        @Test
        public void getAsStringShouldReturnImageDimensionAsStringForImageFileWithMatchingDimension() throws IOException {
            FileData fileData = new FileData(path);
            Provider<Dimension> provider = new ImageDimensionProvider(null);
            String resolution = provider.getAsString(fileData);
            assertEquals("320x480", resolution);
        }
    }

    @Nested
    class TestForImageFileWithNotMatchingDimension {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            path = tempDir.resolve("testimg.png");
            file = path.toFile();

            try (FileOutputStream out = new FileOutputStream(file)) {
                BufferedImage image = new BufferedImage(123, 123, BufferedImage.TYPE_BYTE_GRAY);
                ImageIO.write(image, "png", out);
            } catch (IOException e) {
                System.out.println("Error creating image: " + e);
            }
        }

        @Test
        public void testTempImageShouldPassIfFileIsImage() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertTrue(file.getAbsolutePath().endsWith("testimg.png"));
        }

        @Test
        public void getAsStringShouldReturnStringForImageFileWithNotMatchingDimension() throws IOException {
            FileData fileData = new FileData(path);
            Provider<Dimension> provider = new ImageDimensionProvider(null);
            String resolution = provider.getAsString(fileData);
            assertEquals("Other", resolution);
        }
    }
}