package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.InvalidBackupException;
import com.fenrir.filesorter.model.file.BackupManager;
import com.fenrir.filesorter.model.file.FilePath;
import com.fenrir.filesorter.model.file.utils.Backup;
import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
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

class SorterTest {

    @Nested
    class TestSortingFiles {
        @TempDir
        Path tempDir;
        Path targetPath;
        List<Path> sourceRootDirs;
        ArrayDeque<Path> sourceDirStructure;
        List<FilePath> filePaths;
        List<Path> expectedTargetPathsAfterSort;
        List<Path> expectedSourcePathsAfterSort;
        List<Path> expectedTargetDirectoriesAfterSort;
        List<Pair<FilePath, String>> expectedFilesChecksum;

        @BeforeEach
        public void init() {
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

            sourceDirStructure = new ArrayDeque<>();
            sourceDirStructure.addAll(List.of(
                            dir2InsideRoot1,
                            dir1InsideRoot1,
                            root1,
                            dir2InsideDir1Root2,
                            dir1InsideDir1Root2,
                            dir1InsideRoot2,
                            root2
                    )
            );
            sourceRootDirs = List.of(root1, root2);

            targetPath = FileUtils.createDirectory(tempDir, "targetDir");

            filePaths = Arrays.asList(
                    FilePath.of(file1InsideRoot1, targetPath.resolve(Path.of("text/file1.txt"))),
                    FilePath.of(file2InsideRoot1, targetPath.resolve("text/file2.txt")),
                    FilePath.of(file1InsideDir1Root1, targetPath.resolve("png/file3")),
                    FilePath.of(file2InsideDir1Root1, targetPath.resolve("jpg/img")),
                    FilePath.of(file1InsideDir2Root1, targetPath.resolve("mp3/file4")),
                    FilePath.of(file1InsideDir1Root2, targetPath.resolve("text/file6.txt")),
                    FilePath.of(file1InsideDir1Dir2Root2, targetPath.resolve("avi/file5"))
            );
            filePaths = new ArrayList<>(filePaths);

            expectedTargetPathsAfterSort = List.of(
                    targetPath.resolve(Path.of("text/file1.txt")),
                    targetPath.resolve("text/file2.txt"),
                    targetPath.resolve("png/file3"),
                    targetPath.resolve("jpg/img"),
                    targetPath.resolve("mp3/file4"),
                    targetPath.resolve("text/file6.txt"),
                    targetPath.resolve("avi/file5"),
                    targetPath.resolve("text"),
                    targetPath.resolve("png"),
                    targetPath.resolve("jpg"),
                    targetPath.resolve("mp3"),
                    targetPath.resolve("avi"),
                    targetPath
            );

            expectedTargetDirectoriesAfterSort = List.of(
                    targetPath.resolve("text"),
                    targetPath.resolve("png"),
                    targetPath.resolve("jpg"),
                    targetPath.resolve("mp3"),
                    targetPath.resolve("avi"),
                    targetPath
            );
            expectedTargetDirectoriesAfterSort = new ArrayList<>(expectedTargetDirectoriesAfterSort);

            expectedSourcePathsAfterSort = new ArrayList<>(sourceDirStructure);
            expectedSourcePathsAfterSort.addAll(filePaths.stream().map(FilePath::source).toList());
            expectedFilesChecksum = new ArrayList<>();
            Random rand = new Random();

            for (FilePath fp: filePaths) {
                try (BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream(fp.source().toFile()))) {
                    byte[] bytes = new byte[100];
                    rand.nextBytes(bytes);
                    bf.write(bytes);
                    bf.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (InputStream is = Files.newInputStream(fp.source())) {
                    String md5 = DigestUtils.md5Hex(is);
                    expectedFilesChecksum.add(new Pair<>(fp, md5));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Test
        public void sortShouldCopyFilesIfCopyActionHasBeenGiven() throws IOException {
            Sorter sorter = new Sorter(Sorter.Action.COPY, sourceDirStructure.clone(), filePaths);
            sorter.sort();

            List<Path> actualTargetPathsAfterSort = Files.walk(targetPath).toList();
            List<Path> actualSourcePathsAfterSort = new ArrayList<>();
            for (Path path: sourceRootDirs) {
                List<Path> paths = Files.walk(path).toList();
                actualSourcePathsAfterSort.addAll(paths);
            }
            assertEquals(expectedTargetPathsAfterSort.size(), actualTargetPathsAfterSort.size());
            assertTrue(expectedTargetPathsAfterSort.containsAll(actualTargetPathsAfterSort));
            assertTrue(actualTargetPathsAfterSort.containsAll(expectedTargetPathsAfterSort));
            assertEquals(expectedSourcePathsAfterSort.size(), actualSourcePathsAfterSort.size());
            assertTrue(expectedSourcePathsAfterSort.containsAll(actualSourcePathsAfterSort));
            assertTrue(actualSourcePathsAfterSort.containsAll(expectedSourcePathsAfterSort));

            for (FilePath fp: filePaths) {
                File targetFile = fp.target().toFile();
                assertNotNull(targetFile);
                assertTrue(targetFile.exists());
                assertTrue(targetFile.isFile());

                File sourceFile = fp.source().toFile();
                assertNotNull(sourceFile);
                assertTrue(sourceFile.exists());
                assertTrue(sourceFile.isFile());
            }

            List<Pair<FilePath, String>> actualTargetFilesChecksum = new ArrayList<>();
            List<Pair<FilePath, String>> actualSourceFilesChecksum = new ArrayList<>();
            for (FilePath fp: filePaths) {
                try (InputStream is = Files.newInputStream(fp.target())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualTargetFilesChecksum.add(new Pair<>(fp, md5));
                }
                try (InputStream is = Files.newInputStream(fp.source())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualSourceFilesChecksum.add(new Pair<>(fp, md5));
                }
            }
            assertEquals(expectedFilesChecksum.size(), actualTargetFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualTargetFilesChecksum));
            assertTrue(actualTargetFilesChecksum.containsAll(expectedFilesChecksum));
            assertEquals(expectedFilesChecksum.size(), actualSourceFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualSourceFilesChecksum));
            assertTrue(actualSourceFilesChecksum.containsAll(expectedFilesChecksum));

            for (Path dirPath: sourceDirStructure) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }

            for (Path dirPath: expectedTargetDirectoriesAfterSort) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }
        }

        @Test
        public void sortShouldCopyFilesAndPreserveEmptyDirectoriesIfCopyActionHasBeenGiven() throws IOException {
            Path newDir = FileUtils.createDirectory(sourceRootDirs.get(0), "newDir");
            expectedSourcePathsAfterSort.add(newDir);
            sourceDirStructure.add(newDir);

            Sorter sorter = new Sorter(Sorter.Action.COPY, sourceDirStructure.clone(), filePaths);
            sorter.sort();

            List<Path> actualTargetPathsAfterSort = Files.walk(targetPath).toList();
            List<Path> actualSourcePathsAfterSort = new ArrayList<>();
            for (Path path: sourceRootDirs) {
                List<Path> paths = Files.walk(path).toList();
                actualSourcePathsAfterSort.addAll(paths);
            }
            assertEquals(expectedTargetPathsAfterSort.size(), actualTargetPathsAfterSort.size());
            assertTrue(expectedTargetPathsAfterSort.containsAll(actualTargetPathsAfterSort));
            assertTrue(actualTargetPathsAfterSort.containsAll(expectedTargetPathsAfterSort));
            assertEquals(expectedSourcePathsAfterSort.size(), actualSourcePathsAfterSort.size());
            assertTrue(expectedSourcePathsAfterSort.containsAll(actualSourcePathsAfterSort));
            assertTrue(actualSourcePathsAfterSort.containsAll(expectedSourcePathsAfterSort));

            for (FilePath fp: filePaths) {
                File targetFile = fp.target().toFile();
                assertNotNull(targetFile);
                assertTrue(targetFile.exists());
                assertTrue(targetFile.isFile());

                File sourceFile = fp.source().toFile();
                assertNotNull(sourceFile);
                assertTrue(sourceFile.exists());
                assertTrue(sourceFile.isFile());
            }

            List<Pair<FilePath, String>> actualTargetFilesChecksum = new ArrayList<>();
            List<Pair<FilePath, String>> actualSourceFilesChecksum = new ArrayList<>();
            for (FilePath fp: filePaths) {
                try (InputStream is = Files.newInputStream(fp.target())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualTargetFilesChecksum.add(new Pair<>(fp, md5));
                }
                try (InputStream is = Files.newInputStream(fp.source())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualSourceFilesChecksum.add(new Pair<>(fp, md5));
                }
            }
            assertEquals(expectedFilesChecksum.size(), actualTargetFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualTargetFilesChecksum));
            assertTrue(actualTargetFilesChecksum.containsAll(expectedFilesChecksum));
            assertEquals(expectedFilesChecksum.size(), actualSourceFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualSourceFilesChecksum));
            assertTrue(actualSourceFilesChecksum.containsAll(expectedFilesChecksum));

            for (Path dirPath: sourceDirStructure) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }

            for (Path dirPath: expectedTargetDirectoriesAfterSort) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }
        }

        @Test
        public void sortShouldMoveFilesAndPreserveDirectoriesIfMoveActionHasBeenGiven() throws IOException {
            Sorter sorter = new Sorter(Sorter.Action.MOVE, filePaths);
            sorter.sort();

            List<Path> actualTargetPathsAfterSort = Files.walk(targetPath).toList();
            List<Path> actualSourcePathsAfterSort = new ArrayList<>();
            for (Path path: sourceRootDirs) {
                List<Path> paths = Files.walk(path).toList();
                actualSourcePathsAfterSort.addAll(paths);
            }
            assertEquals(expectedTargetPathsAfterSort.size(), actualTargetPathsAfterSort.size());
            assertTrue(expectedTargetPathsAfterSort.containsAll(actualTargetPathsAfterSort));
            assertTrue(actualTargetPathsAfterSort.containsAll(expectedTargetPathsAfterSort));
            assertEquals(sourceDirStructure.size(), actualSourcePathsAfterSort.size());
            assertTrue(sourceDirStructure.containsAll(actualSourcePathsAfterSort));
            assertTrue(actualSourcePathsAfterSort.containsAll(sourceDirStructure));

            for (FilePath fp: filePaths) {
                File targetFile = fp.target().toFile();
                assertNotNull(targetFile);
                assertTrue(targetFile.exists());
                assertTrue(targetFile.isFile());

                File sourceFile = fp.source().toFile();
                assertNotNull(sourceFile);
                assertFalse(sourceFile.exists());
            }

            List<Pair<FilePath, String>> actualTargetFilesChecksum = new ArrayList<>();
            for (FilePath fp: filePaths) {
                try (InputStream is = Files.newInputStream(fp.target())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualTargetFilesChecksum.add(new Pair<>(fp, md5));
                }
            }
            assertEquals(expectedFilesChecksum.size(), actualTargetFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualTargetFilesChecksum));
            assertTrue(actualTargetFilesChecksum.containsAll(expectedFilesChecksum));

            for (Path dirPath: sourceDirStructure) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }

            for (Path dirPath: expectedTargetDirectoriesAfterSort) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }
        }

        @Test
        public void sortShouldMoveFilesAndRemoveEmptyDirectoriesIfMoveActionAndDirectoriesPathsHasBeenGiven()
                throws IOException {
            Sorter sorter = new Sorter(Sorter.Action.MOVE, sourceDirStructure.clone(), filePaths);
            sorter.sort();
            List<Path> actualTargetPathsAfterSort = Files.walk(targetPath).toList();
            assertEquals(expectedTargetPathsAfterSort.size(), actualTargetPathsAfterSort.size());
            assertTrue(expectedTargetPathsAfterSort.containsAll(actualTargetPathsAfterSort));
            assertTrue(actualTargetPathsAfterSort.containsAll(expectedTargetPathsAfterSort));

            for (FilePath fp: filePaths) {
                File targetFile = fp.target().toFile();
                assertNotNull(targetFile);
                assertTrue(targetFile.exists());
                assertTrue(targetFile.isFile());

                File sourceFile = fp.source().toFile();
                assertNotNull(sourceFile);
                assertFalse(sourceFile.exists());
            }

            List<Pair<FilePath, String>> actualTargetFilesChecksum = new ArrayList<>();
            for (FilePath fp: filePaths) {
                try (InputStream is = Files.newInputStream(fp.target())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualTargetFilesChecksum.add(new Pair<>(fp, md5));
                }
            }
            assertEquals(expectedFilesChecksum.size(), actualTargetFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualTargetFilesChecksum));
            assertTrue(actualTargetFilesChecksum.containsAll(expectedFilesChecksum));

            for (Path dirPath: sourceDirStructure) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertFalse(file.exists());
            }

            for (Path dirPath: expectedTargetDirectoriesAfterSort) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }
        }

        @Test
        public void sortShouldMoveFilesAndPreserveEmptyDirectoriesThatAreNotInTheGivenDirectoriesPaths()
                throws IOException {
            Path newDir = FileUtils.createDirectory(sourceRootDirs.get(0), "newDir");

            Sorter sorter = new Sorter(Sorter.Action.MOVE, sourceDirStructure.clone(), filePaths);
            sorter.sort();
            List<Path> actualTargetPathsAfterSort = Files.walk(targetPath).toList();
            assertEquals(expectedTargetPathsAfterSort.size(), actualTargetPathsAfterSort.size());
            assertTrue(expectedTargetPathsAfterSort.containsAll(actualTargetPathsAfterSort));
            assertTrue(actualTargetPathsAfterSort.containsAll(expectedTargetPathsAfterSort));

            for (FilePath fp: filePaths) {
                File targetFile = fp.target().toFile();
                assertNotNull(targetFile);
                assertTrue(targetFile.exists());
                assertTrue(targetFile.isFile());

                File sourceFile = fp.source().toFile();
                assertNotNull(sourceFile);
                assertFalse(sourceFile.exists());
            }

            List<Pair<FilePath, String>> actualTargetFilesChecksum = new ArrayList<>();
            for (FilePath fp: filePaths) {
                try (InputStream is = Files.newInputStream(fp.target())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualTargetFilesChecksum.add(new Pair<>(fp, md5));
                }
            }
            assertEquals(expectedFilesChecksum.size(), actualTargetFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualTargetFilesChecksum));
            assertTrue(actualTargetFilesChecksum.containsAll(expectedFilesChecksum));

            for (Path dirPath: expectedTargetDirectoriesAfterSort) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }

            assertEquals(2, sourceRootDirs.size());
            assertFalse(sourceRootDirs.get(1).toFile().exists());
            assertTrue(sourceRootDirs.get(0).toFile().exists());

            List<Path> actualSourcePathsAfterSort = Files.walk(sourceRootDirs.get(0)).toList();
            List<Path> expectedSourcePathsAfterSort = List.of(sourceRootDirs.get(0), newDir);
            assertEquals(2, actualSourcePathsAfterSort.size());
            assertTrue(actualSourcePathsAfterSort.containsAll(expectedSourcePathsAfterSort));
            assertTrue(expectedSourcePathsAfterSort.containsAll(actualSourcePathsAfterSort));

            for (Path dirPath: actualSourcePathsAfterSort) {
                File file = dirPath.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }
        }

        @Test
        public void sortShouldCreateBackupFile() throws IOException, InvalidBackupException {
            Path backupDirPath = FileUtils.createDirectory(tempDir, "BackupDir");
            Sorter sorter = new Sorter(Sorter.Action.MOVE, sourceDirStructure.clone(), filePaths, backupDirPath.toString());

            sorter.sort();
            Optional<Path> optionalBackupFilePath = sorter.getBackupFilePath();
            assertTrue(optionalBackupFilePath.isPresent());
            Path backupFilePath = optionalBackupFilePath.get();
            File backupFile = backupFilePath.toFile();
            assertTrue(backupFile.exists());
            assertTrue(backupFile.isFile());

            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            Backup backup = backupManager.readBackup(backupFilePath.getFileName().toString());

            List<FilePath> actualFilePath = backup.filePaths();
            assertEquals(filePaths.size(), actualFilePath.size());
            assertTrue(filePaths.containsAll(actualFilePath));
            assertTrue(actualFilePath.containsAll(filePaths));

            Deque<Path> actualDirPaths = backup.dirTargetPaths();
            expectedTargetDirectoriesAfterSort.remove(targetPath);
            assertEquals(expectedTargetDirectoriesAfterSort.size(), actualDirPaths.size());
            assertTrue(expectedTargetDirectoriesAfterSort.containsAll(actualDirPaths));
            assertTrue(actualDirPaths.containsAll(expectedTargetDirectoriesAfterSort));
        }
    }

    @Nested
    class TestRestoringFiles {
        @TempDir
        Path tempDir;
        List<Path> targetDirectoryStructure;
        List<Path> expectedSourcePathsAfterRestoring;
        List<Path> sourceRootDirs;
        List<Pair<FilePath, String>> expectedFilesChecksum;
        List<FilePath> filePaths;
        Path targetPath;
        Backup backupWithCopyAction;
        Backup backupWithMoveAction;

        @BeforeEach
        public void init() throws IOException, InvalidBackupException {
            targetPath = FileUtils.createDirectory(tempDir, "targetDir");

            Path sourcePath1 = FileUtils.createDirectory(tempDir, "sourceDir1");
            Path sourcePath2 = FileUtils.createDirectory(tempDir, "sourceDir2");
            sourceRootDirs = List.of(sourcePath1, sourcePath2);

            filePaths = Arrays.asList(
                    FilePath.of(sourcePath1.resolve("file1.txt"), targetPath.resolve(Path.of("text/file1.txt"))),
                    FilePath.of(sourcePath1.resolve("file2.txt"), targetPath.resolve("text/file2.txt")),
                    FilePath.of(sourcePath1.resolve("dir1/file3.png"), targetPath.resolve("png/file3")),
                    FilePath.of(sourcePath1.resolve("dir1/img.jpg"), targetPath.resolve("jpg/img")),
                    FilePath.of(sourcePath1.resolve("dir2/file4.mp3"), targetPath.resolve("mp3/file4")),
                    FilePath.of(sourcePath2.resolve("dir1/file6.txt"), targetPath.resolve("text/file6.txt")),
                    FilePath.of(sourcePath2.resolve("dir1/dir2/file5.avi"), targetPath.resolve("avi/file5"))
            );
            filePaths = new ArrayList<>(filePaths);

            targetDirectoryStructure = List.of(
                    targetPath.resolve("text"),
                    targetPath.resolve("png"),
                    targetPath.resolve("jpg"),
                    targetPath.resolve("mp3"),
                    targetPath.resolve("avi")
            );

            expectedSourcePathsAfterRestoring = new ArrayList<>();
            for (FilePath fp: filePaths) {
                expectedSourcePathsAfterRestoring.add(fp.source());
            }
            expectedSourcePathsAfterRestoring.addAll(sourceRootDirs);
            expectedSourcePathsAfterRestoring.addAll(List.of(
                    sourcePath1.resolve("dir1"),
                    sourcePath1.resolve("dir2"),
                    sourcePath2.resolve("dir1"),
                    sourcePath2.resolve("dir1/dir2")
            ));

            for (Path dir: targetDirectoryStructure) {
                FileUtils.createDirectory(targetPath, dir.getFileName().toString());
            }

            Random rand = new Random();
            expectedFilesChecksum = new ArrayList<>();

            for (FilePath filePath: filePaths) {
                FileUtils.createFile(
                        filePath.resolvedTargetPath().getParent(),
                        filePath.resolvedTargetPath().getFileName().toString()
                );

                try (BufferedOutputStream bf = new BufferedOutputStream(
                        new FileOutputStream(filePath.resolvedTargetPath().toFile())
                )) {
                    byte[] bytes = new byte[100];
                    rand.nextBytes(bytes);
                    bf.write(bytes);
                    bf.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try (InputStream is = Files.newInputStream(filePath.resolvedTargetPath())) {
                    String md5 = DigestUtils.md5Hex(is);
                    expectedFilesChecksum.add(new Pair<>(filePath, md5));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Path backupDir = FileUtils.createDirectory(tempDir, "backup");
            BackupManager backupManager = new BackupManager(backupDir.toString());
            String backupName = backupManager.makeBackup(
                    Sorter.Action.COPY, new ArrayDeque<>(targetDirectoryStructure), filePaths
            );
            backupWithCopyAction = backupManager.readBackup(backupName);
            backupName = backupManager.makeBackup(
                    Sorter.Action.MOVE, new ArrayDeque<>(targetDirectoryStructure), filePaths
            );
            backupWithMoveAction = backupManager.readBackup(backupName);
        }

        @Test
        public void restoreShouldRemoveFilesAndSubdirectoriesOfTargetDirectoryIfCopyActionHasBeenGiven()
                throws IOException {
            Sorter sorter = new Sorter(
                    backupWithCopyAction.action(),
                    backupWithCopyAction.dirTargetPaths(),
                    backupWithCopyAction.filePaths()
            );
            sorter.restore();

            List<Path> targetPaths = Files.walk(targetPath).toList();
            assertEquals(1, targetPaths.size());
            assertTrue(targetPaths.contains(targetPath));
        }

        @Test
        public void restoreShouldMoveFilesToTheirPreviousLocationAndRemoveTargetSubDirectoriesIfMoveActionHasBeenGiven()
                throws IOException {
            Sorter sorter = new Sorter(
                    backupWithMoveAction.action(),
                    backupWithMoveAction.dirTargetPaths(),
                    backupWithMoveAction.filePaths()
            );
            sorter.restore();

            List<Path> targetPaths = Files.walk(targetPath).toList();
            assertEquals(1, targetPaths.size());
            assertTrue(targetPaths.contains(targetPath));

            List<Path> actualSourcePathsAfterRestoring = new ArrayList<>();
            for (Path path: sourceRootDirs) {
                List<Path> paths = Files.walk(path).toList();
                actualSourcePathsAfterRestoring.addAll(paths);
            }
            assertEquals(expectedSourcePathsAfterRestoring.size(), actualSourcePathsAfterRestoring.size());
            assertTrue(expectedSourcePathsAfterRestoring.containsAll(actualSourcePathsAfterRestoring));
            assertTrue(actualSourcePathsAfterRestoring.containsAll(expectedSourcePathsAfterRestoring));

            for (FilePath fp: filePaths) {
                File restoredFile = fp.source().toFile();
                assertNotNull(restoredFile);
                assertTrue(restoredFile.exists());
                assertTrue(restoredFile.isFile());
            }

            List<Pair<FilePath, String>> actualRestoredFilesChecksum = new ArrayList<>();
            for (FilePath fp: filePaths) {
                try (InputStream is = Files.newInputStream(fp.source())) {
                    String md5 = DigestUtils.md5Hex(is);
                    actualRestoredFilesChecksum.add(new Pair<>(fp, md5));
                }
            }
            assertEquals(expectedFilesChecksum.size(), actualRestoredFilesChecksum.size());
            assertTrue(expectedFilesChecksum.containsAll(actualRestoredFilesChecksum));
            assertTrue(actualRestoredFilesChecksum.containsAll(expectedFilesChecksum));
        }

        @Test
        public void restoreShouldNotRemoveDirectoriesNotIncludedInListOfDirectoriesPaths() throws IOException {
            Path newDir = FileUtils.createDirectory(targetPath, "newDir");

            Sorter sorter = new Sorter(
                    backupWithCopyAction.action(),
                    backupWithCopyAction.dirTargetPaths(),
                    backupWithCopyAction.filePaths()
            );
            sorter.restore();

            List<Path> expectedTargetPathsAfterRestoring = List.of(targetPath, newDir);
            List<Path> actualTargetPathsAfterRestoring = Files.walk(targetPath).toList();
            assertEquals(2, actualTargetPathsAfterRestoring.size());
            assertTrue(expectedTargetPathsAfterRestoring.containsAll(actualTargetPathsAfterRestoring));
            assertTrue(actualTargetPathsAfterRestoring.containsAll(expectedTargetPathsAfterRestoring));

            for (Path path: expectedTargetPathsAfterRestoring) {
                File file = path.toFile();
                assertNotNull(file);
                assertTrue(file.exists());
                assertTrue(file.isDirectory());
            }
        }
    }
}