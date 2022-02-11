package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.file.FilePath;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
 * root1
 * |-- dir1
 * |  |-- file3.png
 * |  |-- img.jpg
 * |-- dir2
 * |  |-- file4.mp3
 * |-- file1.txt
 * |-- file2.txt
 *
 * root2
 * |-- dir1
 *    |-- dir1
 *    |-- dir2
 *    |  |-- file5.avi
 *    |-- file6.txt
 * */

class ProcessorTest {
    @TempDir
    Path tempDir;
    Deque<Path> dirStructure;
    List<Path> sourcePaths;
    Path targetPath;
    List<RuleGroup> ruleGroups;
    List<FilePath> expectedFilePaths;

    @BeforeEach
    public void init() throws ExpressionFormatException {
        Path root1 = FileUtils.createDirectory(tempDir, "root1");
        Path dir1InsideRoot1 = FileUtils.createDirectory(root1, "dir1");
        Path dir2InsideRoot1 = FileUtils.createDirectory(root1, "dir2");
        Path file1InsideRoot1 = FileUtils.createFile(root1, "file1.txt");
        Path file2InsideRoot1 = FileUtils.createFile(root1, "file2.txt");
        Path file1InsideDir1Root1 = FileUtils.createFile(dir1InsideRoot1, "file3.png");
        Path file2InsideDir1Root1 = FileUtils.createFile(dir1InsideRoot1, "img.jpg");
        Path file1InsideDir2Root1 = FileUtils.createFile(dir2InsideRoot1, "file4.mp3");

        Path root2 = FileUtils.createDirectory(tempDir, "root2");
        Path dir1InsideRoot2 = FileUtils.createDirectory(root2, "dir1");
        Path dir1InsideDir1Root2 = FileUtils.createDirectory(dir1InsideRoot2, "dir1");
        Path dir2InsideDir1Root2 = FileUtils.createDirectory(dir1InsideRoot2, "dir2");
        Path file1InsideDir1Root2 = FileUtils.createFile(dir1InsideRoot2, "file6.txt");
        Path file1InsideDir1Dir2Root2 = FileUtils.createFile(dir2InsideDir1Root2, "file5.avi");

        sourcePaths = List.of(root1, root2);
        sourcePaths = new ArrayList<>(sourcePaths);
        dirStructure = new ArrayDeque<>();
        dirStructure.addAll(List.of(
                dir2InsideRoot1,
                dir1InsideRoot1,
                root1,
                dir2InsideDir1Root2,
                dir1InsideDir1Root2,
                dir1InsideRoot2,
                root2
                )
        );

        targetPath = FileUtils.createDirectory(tempDir, "targetDir");

        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(CAT)"));
        ruleGroup1.setFilterRules(List.of(new Rule("%(INC)%(EXT)%(==:txt)")));

        RuleGroup ruleGroup2 = new RuleGroup();
        ruleGroup2.setRenameRule(new Rule("%(FIN)"));
        ruleGroup2.setSortRule(new Rule("%(EXT)"));

        ruleGroups = List.of(ruleGroup1, ruleGroup2);

        expectedFilePaths = Arrays.asList(
                FilePath.of(file1InsideRoot1, targetPath.resolve(Path.of("text/file1.txt"))),
                FilePath.of(file2InsideRoot1, targetPath.resolve("text/file2.txt")),
                FilePath.of(file1InsideDir1Root1, targetPath.resolve("png/file3")),
                FilePath.of(file2InsideDir1Root1, targetPath.resolve("jpg/img")),
                FilePath.of(file1InsideDir2Root1, targetPath.resolve("mp3/file4")),
                FilePath.of(file1InsideDir1Root2, targetPath.resolve("text/file6.txt")),
                FilePath.of(file1InsideDir1Dir2Root2, targetPath.resolve("avi/file5"))
        );
        expectedFilePaths = new ArrayList<>(expectedFilePaths);
    }

    @Test
    public void constructorShouldThrowExpressionFormatExceptionIfInvalidRuleHasBeenGiven()
            throws ExpressionFormatException {
        RuleGroup ruleGroup = new RuleGroup();
        ruleGroup.setSortRule(new Rule("%(Incorrect)"));
        assertThrows(
                ExpressionFormatException.class,
                () -> new Processor(sourcePaths, targetPath, List.of(ruleGroup))
        );
    }

    @Test
    public void getDirectoriesPathsShouldReturnDirectoryStructure() throws ExpressionFormatException, IOException {
        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        Deque<Path> actualDirectoriesPaths = processor.getSourceDirectoriesPaths();
        assertEquals(dirStructure.size(), actualDirectoriesPaths.size());
        assertTrue(dirStructure.containsAll(actualDirectoriesPaths));
        assertTrue(actualDirectoriesPaths.containsAll(dirStructure));
        assertArrayEquals(dirStructure.toArray(), actualDirectoriesPaths.toArray());
    }

    @Test
    public void getDirectoriesPathsShouldReturnDirectoryStructureWithoutDuplicates()
            throws ExpressionFormatException, IOException {
        sourcePaths.add(sourcePaths.get(0));
        sourcePaths.add(sourcePaths.get(0));
        sourcePaths.add(sourcePaths.get(1).resolve("dir1"));

        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        Deque<Path> actualDirectoriesPaths = processor.getSourceDirectoriesPaths();
        assertEquals(dirStructure.size(), actualDirectoriesPaths.size());
        assertTrue(dirStructure.containsAll(actualDirectoriesPaths));
        assertTrue(actualDirectoriesPaths.containsAll(dirStructure));
        assertArrayEquals(dirStructure.toArray(), actualDirectoriesPaths.toArray());
    }

    @Test
    public void getDirectoriesPathsShouldReturnDirectoryStructureWithoutDirectoriesThatCouldNotBeAccessed()
            throws ExpressionFormatException, IOException {
        Path path = FileUtils.createDirectory(sourcePaths.get(0), "testDir");
        FileUtils.createFile(path, "testFile");
        File dir = path.toFile();
        assertNotNull(dir);
        assertTrue(dir.exists());
        assertTrue(dir.setReadable(false));

        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        Deque<Path> actualDirectoriesPaths = processor.getSourceDirectoriesPaths();
        assertEquals(dirStructure.size(), actualDirectoriesPaths.size());
        assertTrue(dirStructure.containsAll(actualDirectoriesPaths));
        assertTrue(actualDirectoriesPaths.containsAll(dirStructure));
        assertArrayEquals(dirStructure.toArray(), actualDirectoriesPaths.toArray());
    }

    @Test
    public void getDirectoriesPathsShouldReturnEmptyDequeIfEmptyListOfSourcePathHasBeenGiven()
            throws ExpressionFormatException, IOException {
        Processor processor = new Processor(new ArrayList<>(), targetPath, ruleGroups);
        assertTrue(processor.getSourceDirectoriesPaths().isEmpty());
    }

    @Test
    public void getDirectoriesPathsShouldReturnEmptyDequeIfSourcePathDoesNotExists()
            throws ExpressionFormatException, IOException {
        List<Path> incorrectSourcePaths = List.of(Path.of("incorrect/path"));
        Processor processor = new Processor(incorrectSourcePaths, targetPath, ruleGroups);
        assertTrue(processor.getSourceDirectoriesPaths().isEmpty());
    }

    @Test
    public void getFilePathsShouldReturnFilePathsOfProcessedFiles() throws ExpressionFormatException, IOException {
        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertEquals(expectedFilePaths.size(), actualFilePaths.size());
        assertTrue(expectedFilePaths.containsAll(actualFilePaths));
        assertTrue(actualFilePaths.containsAll(expectedFilePaths));
    }

    @Test
    public void getFilePathsShouldReturnFilePathsOfProcessedFilesWithoutDuplicateTargetPaths()
            throws ExpressionFormatException, IOException {
        Path file1 = FileUtils.createFile(sourcePaths.get(0), "newFile.mp3");
        Path file2 = FileUtils.createFile(sourcePaths.get(1), "newFile.mp3");
        expectedFilePaths.add(FilePath.of(file2, targetPath.resolve("mp3/newFile")));
        FilePath filePath = FilePath.of(file1);
        filePath.setTarget(targetPath.resolve("mp3/newFile"), 1);
        filePath.resolveTargetPath();
        expectedFilePaths.add(filePath);

        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        List<FilePath> actualFilePaths = processor.getFilePaths();

        List<Path> expectedSourcePaths = expectedFilePaths.stream()
                .map(FilePath::source)
                .sorted()
                .toList();
        List<Path> actualSourcePaths = actualFilePaths.stream()
                .map(FilePath::source)
                .sorted()
                .toList();
        assertEquals(expectedSourcePaths, actualSourcePaths);

        List<FilePath> expectedTargetPathsAndFileNames = expectedFilePaths.stream()
                .map(f -> FilePath.of(f.source().getFileName(), f.resolvedTargetPath()))
                .sorted(Comparator.comparing(FilePath::source))
                .toList();
        List<FilePath> actualTargetPathsAndFileNames = actualFilePaths.stream()
                .map(f -> FilePath.of(f.source().getFileName(), f.resolvedTargetPath()))
                .sorted(Comparator.comparing(FilePath::source))
                .toList();
        assertEquals(expectedTargetPathsAndFileNames, actualTargetPathsAndFileNames);
    }

    @Test
    public void getFilePathsShouldReturnListOfUniqFilePaths() throws ExpressionFormatException, IOException {
        sourcePaths.add(sourcePaths.get(0));
        sourcePaths.add(sourcePaths.get(0));
        sourcePaths.add(sourcePaths.get(1).resolve("dir1"));

        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertEquals(expectedFilePaths.size(), actualFilePaths.size());
        assertTrue(expectedFilePaths.containsAll(actualFilePaths));
        assertTrue(actualFilePaths.containsAll(expectedFilePaths));
    }

    @Test
    public void getFilePathsShouldReturnListOfFilePathsWithoutFileThatCouldNotBeAccessed()
            throws ExpressionFormatException, IOException {
        Path path = FileUtils.createFile(sourcePaths.get(0), "testFile");
        File file = path.toFile();
        assertNotNull(file);
        assertTrue(file.exists());
        assertTrue(file.setReadable(false));

        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertEquals(expectedFilePaths.size(), actualFilePaths.size());
        assertTrue(expectedFilePaths.containsAll(actualFilePaths));
    }

    @Test
    public void getFilePathsShouldReturnListOfFilePathsWithoutFilesInsideDirectoryThatCouldNotBeAccessed()
            throws ExpressionFormatException, IOException {
        Path dirPath = FileUtils.createDirectory(sourcePaths.get(0), "testDir");
        FileUtils.createFile(dirPath, "testFile");
        File dir = dirPath.toFile();
        assertNotNull(dir);
        assertTrue(dir.setExecutable(false));

        Processor processor = new Processor(sourcePaths, targetPath, ruleGroups);
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertEquals(expectedFilePaths.size(), actualFilePaths.size());
        assertTrue(expectedFilePaths.containsAll(actualFilePaths));
    }


    @Test
    public void getFilePathsShouldReturnListOfFilePathsWithoutConflictsBetweenFileAndDirectoryPaths() throws ExpressionFormatException, IOException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(TXT:txt)"));
        ruleGroup1.addFilterRule(new Rule("%(EXC)%(EXT)%(==:txt)"));
        RuleGroup ruleGroup2 = new RuleGroup();
        ruleGroup2.setRenameRule(new Rule("%(FIX)"));
        ruleGroup2.setSortRule(new Rule("%(EXT)"));

        Processor processor = new Processor(sourcePaths, targetPath, List.of(ruleGroup1, ruleGroup2));
        List<FilePath> actualFilePaths = processor.getFilePaths();

        List<Path> actualSourceFilePaths = actualFilePaths.stream()
                .map(FilePath::source)
                .toList();
        List<Path> expectedSourceFilePaths = expectedFilePaths.stream()
                .map(FilePath::source)
                .toList();
        assertEquals(expectedSourceFilePaths.size(), actualSourceFilePaths.size());
        assertTrue(expectedSourceFilePaths.containsAll(actualSourceFilePaths));
        assertTrue(actualSourceFilePaths.containsAll(expectedSourceFilePaths));

        List<Path> actualTargetFilePaths = actualFilePaths.stream()
                .map(FilePath::resolvedTargetPath)
                .toList();
        List<Path> expectedTargetFilePaths = List.of(
                targetPath.resolve("txt (1)"),
                targetPath.resolve("txt (2)"),
                targetPath.resolve("txt (3)"),
                targetPath.resolve("txt (4)"),
                targetPath.resolve("txt/file1.txt"),
                targetPath.resolve("txt/file2.txt"),
                targetPath.resolve("txt/file6.txt")
        );
        assertEquals(expectedTargetFilePaths.size(), actualTargetFilePaths.size());
        assertTrue(expectedTargetFilePaths.containsAll(actualTargetFilePaths));
        assertTrue(actualTargetFilePaths.containsAll(expectedTargetFilePaths));
    }

    @Test
    public void getFilePathsShouldReturnEmptyListOfFilePathsIfEmptyListOfSourcePathsHasBeenGiven()
            throws ExpressionFormatException, IOException {
        Processor processor = new Processor(new ArrayList<>(), targetPath, ruleGroups);
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertTrue(actualFilePaths.isEmpty());
    }

    @Test
    public void getFilePathsShouldReturnEmptyListOfFilePathsIfGivenSourcePathDoesNotExists()
            throws ExpressionFormatException, IOException {
        Path sourcePath = Path.of("Incorrect/path");
        Processor processor = new Processor(List.of(sourcePath), targetPath, ruleGroups);
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertTrue(actualFilePaths.isEmpty());
    }

    @Test
    public void getFilePathsShouldReturnEmptyListOfFilePathsIfEmptyListOfRuleGroupsHasBeenGiven()
            throws ExpressionFormatException, IOException {
        Processor processor = new Processor(sourcePaths, targetPath, new ArrayList<>());
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertTrue(actualFilePaths.isEmpty());
    }

    @Test
    public void getFilePathsShouldReturnListOfFilePathsWithSameTargetPathsAsSourcePathIfOnlyEmptyRuleGroupWasGiven()
            throws ExpressionFormatException, IOException {
        Processor processor = new Processor(sourcePaths, targetPath, List.of(new RuleGroup()));
        List<FilePath> actualFilePaths = processor.getFilePaths();
        assertTrue(actualFilePaths.isEmpty());
    }
}