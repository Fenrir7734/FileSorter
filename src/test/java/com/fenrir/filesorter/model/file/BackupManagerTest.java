package com.fenrir.filesorter.model.file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BackupManagerTest {
    @TempDir
    Path tempDir;
    Path backupDirPath;

    @Test
    public void constructorShouldReturnNewInstanceAndCreateBackupDirectoryIfNotExists() throws IOException {
        Path directoryPath = tempDir.resolve("backup/");
        File dir = new File(directoryPath.toString());
        assertFalse(dir.exists());
        BackupManager backupManager = new BackupManager(directoryPath.toString());
        assertNotNull(backupManager);
        assertTrue(dir.exists());
    }

    @Test
    public void constructorShouldThrowIOExceptionIfBackupDirectoryNotExistsAndAttemptToCreateDirectoryFailed()
            throws IOException {
        Path path = tempDir.resolve("backup");
        File file = new File(path.toString());
        file.createNewFile();
        assertTrue(file.exists());
        assertThrows(
                IOException.class,
                () -> new BackupManager(path.toString())
        );
    }

    @Nested
    class TestForEmptyBackupDirectory {
        @BeforeEach
        public void initDir() {
            backupDirPath = tempDir.resolve("backup/");
            new File(backupDirPath.toString()).mkdirs();
        }

        @Test
        public void readBackupShouldThrowIOExceptionIfFileWithGivenNameNotExists() throws IOException, JSONException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertThrows(
                    IOException.class,
                    () -> backupManager.readBackup("incorrect.json")
            );
        }

        @Test
        public void deleteBackupShouldThrowIOExceptionIfDeletingFileFailed() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertThrows(
                    IOException.class,
                    () -> backupManager.deleteBackup("incorrect.json")
            );
        }

        @Test
        public void getAllBackupsNamesShouldReturnEmptyListIfThereIsNoBackupFiles() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            List<String> backupFiles = backupManager.getAllBackupsNames();
            assertTrue(backupFiles.isEmpty());
        }

        @Test
        public void getAllBackupNamesShouldThrowIOExceptionWhenListingBackupFilesFailed() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            File backupDir = new File(backupDirPath.toString());
            assertTrue(Files.deleteIfExists(backupDir.toPath()));
            assertFalse(backupDir.exists());
            assertThrows(
                    IOException.class,
                    backupManager::getAllBackupsNames
            );
        }

        @Test
        public void getAllBackupsNamesWithoutExtensionShouldReturnEmptyListIfThereIsNoBackupFiles()
                throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            List<String> backupFiles = backupManager.getAllBackupsNamesWithoutExtension();
            assertTrue(backupFiles.isEmpty());
        }

        @Test
        public void getAllBackupsNamesWithoutExtensionShouldThrowIOExceptionWhenListingBackupFilesFailed()
                throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            File backupDir = new File(backupDirPath.toString());
            assertTrue(Files.deleteIfExists(backupDir.toPath()));
            assertFalse(backupDir.exists());
            assertThrows(
                    IOException.class,
                    backupManager::getAllBackupsNamesWithoutExtension
            );
        }

        @Test
        public void getBackupFileShouldThrowIOExceptionIfBackupWithGivenNameNotExists() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertThrows(
                    IOException.class,
                    () -> backupManager.getBackupFile("incorrect.json")
            );
        }

        @Test
        public void getBackupFileShouldThrowIOExceptionIfGivenBackupNameIsNotNameOfFile() throws IOException {
            String fileName = "dir.json";
            File file = new File(backupDirPath.resolve(fileName).toString());
            assertTrue(file.mkdirs());
            assertTrue(file.exists());
            assertTrue(file.isDirectory());
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertThrows(
                    IOException.class,
                    () -> backupManager.getBackupFile(fileName)
            );
        }

        @Test
        public void getBackupFileShouldThrowIOExceptionIfGivenNameNotEndsWithJSONExtension() throws IOException {
            String fileName = "backup";
            File file = new File(backupDirPath.resolve(fileName).toString());
            assertTrue(file.createNewFile());
            assertTrue(file.exists());
            assertTrue(file.isFile());
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertThrows(
                    IOException.class,
                    () -> backupManager.getBackupFile(fileName)
            );
        }

        @Test
        public void readBackupShouldReturnEmptyListIfNoPathHaveBeenWrittenToFile() throws IOException {
            String backupFileName = "backupfile.json";
            Path backupFilePath = backupDirPath.resolve(backupFileName);
            new File(backupFilePath.toString()).createNewFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(backupFilePath.toString()))) {
                writer.write("[]");
            }
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            List<FilePath> filePaths = backupManager.readBackup(backupFileName);
            assertTrue(filePaths.isEmpty());
        }

        @Test
        public void readBackupShouldThrowJSONExceptionIfBackupFileContentIsInvalid() throws IOException {
            String backupFileName = "backupfile.json";
            Path backupFilePath = backupDirPath.resolve(backupFileName);
            new File(backupFilePath.toString()).createNewFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(backupFilePath.toString()))) {
                writer.write("Invalid");
            }
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertThrows(
                    JSONException.class,
                    () -> backupManager.readBackup(backupFilePath.toString())
            );
        }

        @Test
        public void makeBackupShouldCreateNewBackupFileForGivenEmptyListOfPaths() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            String backupFileName = backupManager.makeBackup(new ArrayList<>());
            String backupFileContent = new String(Files.readAllBytes(backupDirPath.resolve(backupFileName)));
            assertEquals("[]", backupFileContent);
        }

        @Test
        public void makeBackupShouldReturnNameOfCreatedFile() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            String name = backupManager.makeBackup(new ArrayList<>());
            assertNotNull(name);
            try (Stream<Path> stream = Files.list(Path.of(backupDirPath.toString()))) {
                List<String> actualBackupFileNames = stream.filter(f -> !Files.isDirectory(f))
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(s -> s.endsWith(".json"))
                        .toList();
                assertTrue(actualBackupFileNames.contains(name));
            }
        }

        @Test
        public void makeBackupShouldReturnNameOfCreatedFileInFormOfISOLocalDateTimeFormat() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            LocalDateTime localDateTime = LocalDateTime.now();
            String actualName = backupManager.makeBackup(new ArrayList<>(), localDateTime);
            String expectedName = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".json";
            assertEquals(expectedName, actualName);
        }

        @Test
        public void makeBackupShouldReturnUniqueNameOfCreatedFile() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            LocalDateTime localDateTime = LocalDateTime.now();
            backupManager.makeBackup(new ArrayList<>(), localDateTime);
            String actualName = backupManager.makeBackup(new ArrayList<>(), localDateTime);
            String expectedName = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " (1).json";
            assertEquals(expectedName, actualName);
        }

        @Test
        public void makeBackupShouldCreateNewBackupFileForGivenNonEmptyListOfPaths() throws IOException {
            List<FilePath> filePaths = List.of(
                    FilePath.of(
                            Path.of("/home/user/Documents/sourcefile1.txt"),
                            Path.of("/home/user/Desktop/targetfile1")
                    ),
                    FilePath.of(
                            Path.of("/home/user/Documents/sourcefile2.txt"),
                            Path.of("/home/user/Desktop/targetfile2")
                    )
            );
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            String newBackupFileName = backupManager.makeBackup(filePaths);
            JSONObject object1 = new JSONObject();
            object1.put("from", filePaths.get(0).source());
            object1.put("to", filePaths.get(0).target());
            JSONObject object2 = new JSONObject();
            object2.put("from", filePaths.get(1).source());
            object2.put("to", filePaths.get(1).target());
            JSONArray expectedArray = new JSONArray();
            expectedArray.put(object1);
            expectedArray.put(object2);
            String backupFileContent = new String(Files.readAllBytes(backupDirPath.resolve(newBackupFileName)));
            JSONArray actualArray = new JSONArray(backupFileContent);
            assertEquals(expectedArray.toString(), actualArray.toString());
        }
    }

    @Nested
    class TestForCorrectDataInBackupFiles {
        String name1;
        String name1WithoutExtension;
        List<FilePath> filePaths1;
        JSONArray backupFileContent1;
        String name2;
        String name2WithoutExtension;
        List<FilePath> filePaths2;
        JSONArray backupFileContent2;

        @BeforeEach
        public void initFile() throws IOException {
            backupDirPath = tempDir.resolve("backup/");
            new File(backupDirPath.toString()).mkdirs();

            name1WithoutExtension = "backup1";
            name1 = name1WithoutExtension + ".json";
            FilePath filePath1 = FilePath.of(Path.of("./file1.txt"), Path.of("./home/file1.txt"));
            filePaths1 = new ArrayList<>();
            filePaths1.add(filePath1);
            backupFileContent1 = new JSONArray();
            JSONObject object1 = new JSONObject();
            object1.put("from", filePath1.source().toString());
            object1.put("to", filePath1.target().toString());
            backupFileContent1 = new JSONArray();
            backupFileContent1.put(object1);
            Path backup1FilePath = backupDirPath.resolve(name1);
            new File(backup1FilePath.toString()).createNewFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(backup1FilePath.toString()))) {
                writer.write(backupFileContent1.toString(4));
            }

            name2WithoutExtension = "backup2";
            name2 = name2WithoutExtension + ".json";
            FilePath filePath2 = FilePath.of(Path.of("./home/file2.txt"), Path.of("./home/user/file2.txt"));
            FilePath filePath3 = FilePath.of(Path.of("./documents/file3.txt"), Path.of("./home/dir/file3.txt"));
            filePaths2 = new ArrayList<>();
            filePaths2.add(filePath2);
            filePaths2.add(filePath3);
            JSONObject object2 = new JSONObject();
            object2.put("from", filePath2.source().toString());
            object2.put("to", filePath2.target().toString());
            JSONObject object3 = new JSONObject();
            object3.put("from", filePath3.source());
            object3.put("to", filePath3.target());
            backupFileContent2 = new JSONArray();
            backupFileContent2.put(object2);
            backupFileContent2.put(object3);
            Path backup2FilePath = backupDirPath.resolve(name2);
            new File(backup2FilePath.toString()).createNewFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(backup2FilePath.toString()))) {
                writer.write(backupFileContent2.toString(4));
            }
        }

        @Test
        public void readBackupShouldReturnListOfFilePathFromBackupFileWithGivenName() throws IOException, JSONException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            List<FilePath> expectedFilePaths = backupManager.readBackup(name2);
            assertEquals(filePaths2, expectedFilePaths);
        }

        @Test
        public void deleteBackupShouldDeleteBackupFileWithGivenName() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertEquals(List.of(name2, name1), backupManager.getAllBackupsNames());
            backupManager.deleteBackup(name1);
            assertEquals(List.of(name2), backupManager.getAllBackupsNames());
        }

        @Test
        public void getAllBackupsNamesWithoutExtensionShouldReturnListOfBackupFilesNamesWithoutExtension()
                throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertEquals(
                    List.of(name2WithoutExtension, name1WithoutExtension),
                    backupManager.getAllBackupsNamesWithoutExtension()
            );
        }

        @Test
        public void getAllBackupsNamesWithoutExtensionShouldIgnoreFilesWithoutJSONExtensionInBackupDirectory()
                throws IOException {
            File file = new File(backupDirPath.resolve("file.txt").toString());
            assertTrue(file.createNewFile());
            assertTrue(file.exists());
            assertTrue(file.isFile());
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertEquals(
                    List.of(name2WithoutExtension, name1WithoutExtension),
                    backupManager.getAllBackupsNamesWithoutExtension()
            );
        }

        @Test
        public void getAllBackupsNamesWithoutExtensionShouldIgnoreAllChildDirectoriesOfBackupDirectory()
                throws IOException {
            File dir = new File(backupDirPath.resolve("dir").toString());
            assertTrue(dir.mkdirs());
            assertTrue(dir.exists());
            assertTrue(dir.isDirectory());
            File file = new File(backupDirPath.resolve("dir/file.txt").toString());
            assertTrue(file.createNewFile());
            assertTrue(file.exists());
            assertTrue(file.isFile());
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertEquals(
                    List.of(name2WithoutExtension, name1WithoutExtension),
                    backupManager.getAllBackupsNamesWithoutExtension()
            );
        }


        @Test
        public void getAllBackupNamesShouldReturnListOfBackupFileNames() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            assertEquals(
                    List.of(name2, name1),
                    backupManager.getAllBackupsNames()
            );
        }

        @Test
        public void makeBackupShouldCreateNewBackupFileAndKeepAllOtherBackupFile() throws IOException {
            List<FilePath> filePaths = List.of(
                    FilePath.of(
                            Path.of("/home/user/Documents/sourcefile1.txt"),
                            Path.of("/home/user/Desktop/targetfile1")
                    )
            );
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            LocalDateTime localDateTime = LocalDateTime.now();
            backupManager.makeBackup(filePaths, localDateTime);
            String expectedBackupFileName = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".json";
            List<String> expectedBackupFileNames = List.of(name2, expectedBackupFileName, name1);
            try (Stream<Path> stream = Files.list(Path.of(backupDirPath.toString()))) {
                List<String> actualBackupFileNames = stream.filter(f -> !Files.isDirectory(f))
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(s -> s.endsWith(".json"))
                        .toList();
                assertEquals(expectedBackupFileNames.size(), actualBackupFileNames.size());
                assertTrue(expectedBackupFileNames.containsAll(actualBackupFileNames));
                assertTrue(actualBackupFileNames.containsAll(expectedBackupFileNames));
            }
        }

        @Test
        public void getBackupFileShouldReturnBackupFileWithGivenName() throws IOException {
            BackupManager backupManager = new BackupManager(backupDirPath.toString());
            File file = backupManager.getBackupFile(name1);
            assertTrue(file.exists());
            assertTrue(file.isFile());
            assertEquals(file.toPath(), backupDirPath.resolve(name1));
            String content = new String(Files.readAllBytes(file.toPath()));
            assertEquals(backupFileContent1.toString(), new JSONArray(content).toString());
        }
    }
}