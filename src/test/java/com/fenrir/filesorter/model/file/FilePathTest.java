package com.fenrir.filesorter.model.file;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.rule.Rule;
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

    @Test
    public void equalShouldReturnTrueIfObjectAreTheSame() {
        FilePath filePath1 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt"),
                Path.of("/home/user/Desktop/targetfile")
        );
        FilePath filePath2 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt"),
                Path.of("/home/user/Desktop/targetfile")
        );
        assertEquals(filePath1, filePath2);
    }

    @Test
    public void equalShouldReturnFalseIfSourcePathAreDifferent() {
        FilePath filePath1 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt"),
                Path.of("/home/user/Desktop/targetfile")
        );
        FilePath filePath2 = FilePath.of(
                Path.of("/home/user/Documents/different_sourcefile.txt"),
                Path.of("/home/user/Desktop/targetfile")
        );
        assertNotEquals(filePath1, filePath2);
    }

    @Test
    public void equalShouldReturnFalseIfTargetPathAreDifferent() {
        FilePath filePath1 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt"),
                Path.of("/home/user/Desktop/targetfile")
        );
        FilePath filePath2 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt"),
                Path.of("/home/user/Desktop/different_targetfile")
        );
        assertNotEquals(filePath1, filePath2);
    }

    @Test
    public void equalShouldReturnFalseIfCountIsDifferent() {
        FilePath filePath1 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt")
        );
        filePath1.setTarget(
                Path.of("/home/user/Desktop/targetfile"), 1
        );
        FilePath filePath2 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt")
        );
        filePath2.setTarget(
                Path.of("/home/user/Desktop/targetfile"), 2
        );
        assertNotEquals(filePath1, filePath2);
    }

    @Test
    public void equalShouldReturnFalseIfOneObjectIsEqualNull() {
        FilePath filePath1 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt"),
                Path.of("/home/user/Desktop/targetfile")
        );
        assertNotEquals(filePath1, null);
    }

    @Test
    public void equalShouldReturnFalseForObjectsOfADifferentType() throws ExpressionFormatException {
        FilePath filePath1 = FilePath.of(
                Path.of("/home/user/Documents/sourcefile.txt"),
                Path.of("/home/user/Desktop/targetfile")
        );
        assertNotEquals(filePath1, new Object());
    }

}