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

class ImageWidthProviderTest {
    @TempDir
    Path tempDir;

    @Test
    public void getShouldReturnNullForNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Integer> provider = new ImageWidthProvider();
        Integer width = provider.get(fileData);
        assertNull(width);
    }

    @Test
    public void getAsStringShouldReturnStringForNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        Provider<Integer> provider = new ImageWidthProvider();
        String width = provider.getAsString(fileData);
        assertEquals("NonImage", width);
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
        public void getShouldReturnImageWidthForImageFile() throws IOException {
            FileData fileData = new FileData(path);
            Provider<Integer> provider = new ImageWidthProvider();
            Integer width = provider.get(fileData);
            assertEquals(320, width);
        }

        @Test
        public void getAsStringShouldReturnImageWidthAsStringForImageFile() throws IOException {
            FileData fileData = new FileData(path);
            Provider<Integer> provider = new ImageWidthProvider();
            String width = provider.getAsString(fileData);
            assertEquals("320", width);
        }
    }
}