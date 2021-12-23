package com.fenrir.filesorter.model.file;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FilePathTest {
    @Test
    public void ofShouldReturnInstanceOfFilePathClassIfGivenSourcePath() {
        Path path = Path.of("/home/user/Documents/testfile.txt");
        FilePath filePath = FilePath.of(path);
        assertNotNull(filePath);
        assertEquals(path, filePath.source());
        assertNull(filePath.target());
        assertNull(filePath.resolvedTargetPath());
    }

    @Test
    public void ofShouldReturnInstanceOfFilePathClassIfGivenSourceAndTargetPath() {
        Path sourcePath = Path.of("/home/user/Documents/sourcefile.txt");
        Path targetPath = Path.of("/home/user/Desktop/targetfile.txt");
        FilePath filePath = FilePath.of(sourcePath, targetPath);
        assertNotNull(filePath);
        assertEquals(sourcePath, filePath.source());
        assertEquals(targetPath, filePath.target());
        assertEquals(targetPath, filePath.resolvedTargetPath());
    }

    @Test
    public void testResolveTargetPathForFileWithExtensionAndCountEqualZero() {
        Path sourcePath = Path.of("/home/user/Documents/sourcefile.txt");
        Path targetPath = Path.of("/home/user/Desktop/targetfile.txt");
        FilePath filePath = FilePath.of(sourcePath);
        filePath.setTarget(targetPath, 0);
        filePath.resolveTargetPath();
        assertEquals(targetPath, filePath.resolvedTargetPath());
    }

    @Test
    public void testResolveTargetPathForFileWithExtensionAndCountEqualOne() {
        Path sourcePath = Path.of("/home/user/Documents/sourcefile.txt");
        Path targetPath = Path.of("/home/user/Desktop/targetfile.txt");
        FilePath filePath = FilePath.of(sourcePath);
        filePath.setTarget(targetPath, 1);
        filePath.resolveTargetPath();
        Path expectedResolvedTargetPath = Path.of("/home/user/Desktop/targetfile (1).txt");
        assertEquals(expectedResolvedTargetPath, filePath.resolvedTargetPath());
    }

    @Test
    public void testResolveTargetPathForFileWithoutExtensionAndCountEqualOne() {
        Path sourcePath = Path.of("/home/user/Documents/sourcefile.txt");
        Path targetPath = Path.of("/home/user/Desktop/targetfile");
        FilePath filePath = FilePath.of(sourcePath);
        filePath.setTarget(targetPath, 1);
        filePath.resolveTargetPath();
        Path expectedResolvedTargetPath = Path.of("/home/user/Desktop/targetfile (1)");
        assertEquals(expectedResolvedTargetPath, filePath.resolvedTargetPath());
    }

    @Test
    public void resolveTargetPathShouldThrowIllegalArgumentExceptionIfTargetPathWasNotSet() {
        Path sourcePath = Path.of("/home/user/Documents/sourcefile.txt");
        FilePath filePath = FilePath.of(sourcePath);
        assertThrows(
                IllegalArgumentException.class,
                filePath::resolveTargetPath
        );
    }
}