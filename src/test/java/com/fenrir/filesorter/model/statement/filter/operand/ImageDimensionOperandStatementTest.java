package com.fenrir.filesorter.model.statement.filter.operand;

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

class ImageDimensionOperandStatementTest {
    @TempDir
    Path tempDir;

    @Test
    public void getResolutionOfNonImageFile() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        FileData fileData = new FileData(path);
        FilterOperandStatement<Dimension> statement = new ImageDimensionOperandStatement();
        Dimension actualDimension = statement.execute(fileData);
        assertNull(actualDimension);
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
        public void testTempImage() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertTrue(file.getAbsolutePath().endsWith("testimg.png"));
        }

        @Test
        public void getResolutionOfImage() throws IOException {
            FileData fileData = new FileData(path);
            FilterOperandStatement<Dimension> statement = new ImageDimensionOperandStatement();
            Dimension actualDimension = statement.execute(fileData);
            Dimension expectedDimension = Dimension.of(320, 480);
            assertEquals(expectedDimension, actualDimension);
        }
    }
}