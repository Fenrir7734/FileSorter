package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileCategoryStatementTest {

    @TempDir
    Path tempDir;

    @Nested
    class TestForFileWithExtension {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            try {
                path = tempDir.resolve("testfile.txt");
                file = path.toFile();

                FileWriter fileWriter1 = new FileWriter(file);
                BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
                bufferedWriter1.write("file content");
                bufferedWriter1.close();
            } catch (InvalidPathException e) {
                System.err.println("Error creating temporary test files: " + e);
            } catch (IOException e) {
                System.err.println("Error writing to the temporary files: " + e);
            }
        }

        @Test
        public void testTempFile() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertEquals(file.length(), 12L);
            assertTrue(file.getAbsolutePath().endsWith("testfile.txt"));
        }

        @Test
        public void getFileCategoryForFileWithExtension() throws IOException {
            FileData fileData = new FileData(path);
            FileCategoryStatement statement = new FileCategoryStatement(fileData, null);
            String category = statement.execute();
            assertEquals("text", category);
        }
    }

    @Nested
    class TestForFileWithoutExtension {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            try {
                path = tempDir.resolve("testfile");
                file = path.toFile();

                FileWriter fileWriter1 = new FileWriter(file);
                BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
                bufferedWriter1.write("file content");
                bufferedWriter1.close();
            } catch (InvalidPathException e) {
                System.err.println("Error creating temporary test files: " + e);
            } catch (IOException e) {
                System.err.println("Error writing to the temporary files: " + e);
            }
        }

        @Test
        public void testTempFile() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertEquals(file.length(), 12L);
            assertTrue(file.getAbsolutePath().endsWith("testfile"));
        }

        @Test
        public void getFileCategoryForFileWithoutExtension() throws IOException {
            FileData fileData = new FileData(path);
            FileCategoryStatement statement = new FileCategoryStatement(fileData, null);
            String category = statement.execute();
            assertEquals("others", category);
        }
    }

    @Nested
    class TestForFileWithOnlyExtension {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            try {
                path = tempDir.resolve(".txt");
                file = path.toFile();

                FileWriter fileWriter1 = new FileWriter(file);
                BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
                bufferedWriter1.write("file content");
                bufferedWriter1.close();
            } catch (InvalidPathException e) {
                System.err.println("Error creating temporary test files: " + e);
            } catch (IOException e) {
                System.err.println("Error writing to the temporary files: " + e);
            }
        }

        @Test
        public void testTempFile() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertEquals(file.length(), 12L);
            assertTrue(file.getAbsolutePath().endsWith(".txt"));
        }

        @Test
        public void getFileCategoryForFileWithExtension() throws IOException {
            FileData fileData = new FileData(path);
            FileCategoryStatement statement = new FileCategoryStatement(fileData, null);
            String category = statement.execute();
            assertEquals("text", category);
        }
    }

    @Nested
    class TestForFileWithoutMatchingCategory {
        Path path;
        File file;

        @BeforeEach
        public void init() {
            try {
                path = tempDir.resolve("testfile.xyz");
                file = path.toFile();

                FileWriter fileWriter1 = new FileWriter(file);
                BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
                bufferedWriter1.write("file content");
                bufferedWriter1.close();
            } catch (InvalidPathException e) {
                System.err.println("Error creating temporary test files: " + e);
            } catch (IOException e) {
                System.err.println("Error writing to the temporary files: " + e);
            }
        }

        @Test
        public void testTempFile() {
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertEquals(file.length(), 12L);
            assertTrue(file.getAbsolutePath().endsWith("testfile.xyz"));
        }

        @Test
        public void getFileCategoryForFileWithoutMatchingCategory() throws IOException {
            FileData fileData = new FileData(path);
            FileCategoryStatement statement = new FileCategoryStatement(fileData, null);
            String category = statement.execute();
            assertEquals("others", category);
        }
    }
}
