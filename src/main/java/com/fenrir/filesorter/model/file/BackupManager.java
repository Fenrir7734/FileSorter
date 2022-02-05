package com.fenrir.filesorter.model.file;

import com.fenrir.filesorter.model.Sorter;
import com.fenrir.filesorter.model.exceptions.InvalidBackupException;
import com.fenrir.filesorter.model.file.utils.Backup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

public class BackupManager {
    private final Logger logger = LoggerFactory.getLogger(BackupManager.class);

    private final static String BACKUP_DIR_PATH = "src/main/resources/backup/";
    private final static String ACTION_KEY = "action";
    private final static String DIRECTORIES_KEY = "directories";
    private final static String FILES_KEY = "files";
    private final static String SOURCE_KEY = "from";
    private final static String TARGET_KEY = "to";
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final String pathToBackupDirectory;

    public BackupManager() throws IOException {
        this(BACKUP_DIR_PATH);
    }

    BackupManager(String pathToBackupDirectory) throws IOException {
        this.pathToBackupDirectory = pathToBackupDirectory;
        checkForBackupDirectory();
    }

    private void checkForBackupDirectory() throws IOException {
        File dir = new File(pathToBackupDirectory);
        if (!dir.exists() || !dir.isDirectory()) {
            if (!dir.mkdirs()) {
                throw new IOException(
                        "The backup directory does not exist and the attempt " +
                                "to create a new backup directory has failed."
                );
            }
        }
    }

    public Backup readBackup(String name) throws IOException, JSONException, InvalidBackupException {
        try {
            Path backupFilePath = Path.of(pathToBackupDirectory).resolve(name);
            String content = new String(Files.readAllBytes(backupFilePath));
            JSONObject backup = new JSONObject(content);
            return mapJSONToBackup(backup);
        } catch (IOException | JSONException e) {
            logger.error("Error during reading backup {} message: {}", name, e.getMessage());
            throw e;
        }
    }

    private Backup mapJSONToBackup(JSONObject object) throws InvalidBackupException {
        String actionName = object.getString(ACTION_KEY);
        JSONArray dirPathsJSONArray = object.getJSONArray(DIRECTORIES_KEY);
        JSONArray filePathsJSONArray = object.getJSONArray(FILES_KEY);
        Sorter.Action action = Sorter.Action.getAction(actionName);
        if (action == null) {
            throw new InvalidBackupException("The action type written to the read backup file could not be recognised");
        }
        Deque<Path> dirPaths = mapJSONArrayToQueueOfPaths(dirPathsJSONArray);
        List<FilePath> filePaths = mapJSONArrayToListOfFilePaths(filePathsJSONArray);
        return new Backup(action, dirPaths, filePaths);
    }

    private Deque<Path> mapJSONArrayToQueueOfPaths(JSONArray array) {
        Deque<Path> paths = new ArrayDeque<>();
        for (int i = 0; i < array.length(); i++) {
            Path dirPath = Path.of(array.getString(i));
            paths.offerLast(dirPath);
        }
        return paths;
    }

    private List<FilePath> mapJSONArrayToListOfFilePaths(JSONArray array) {
        List<FilePath> paths = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            FilePath filePath = mapJSONObjectToFilePath(object);
            paths.add(filePath);
        }
        return paths;
    }

    private FilePath mapJSONObjectToFilePath(JSONObject object) {
        Path sourcePath = Path.of(object.getString(SOURCE_KEY));
        Path targetPath = Path.of(object.getString(TARGET_KEY));
        return FilePath.of(sourcePath, targetPath);
    }

    String makeBackup(
            Sorter.Action action,
            Deque<Path> targetDirPaths,
            List<FilePath> filePaths,
            LocalDateTime localDateTime
    ) throws IOException {

        JSONArray dirPathsAsJSON = parseDirPaths(targetDirPaths);
        JSONArray filePathsAsJSON = parseFilePaths(filePaths);
        JSONObject backupAsJSON = new JSONObject();
        backupAsJSON.put(ACTION_KEY, action.getName())
                .put(DIRECTORIES_KEY, dirPathsAsJSON)
                .put(FILES_KEY, filePathsAsJSON);
        String fileName = generateUniqueNameForBackup(localDateTime);
        String pathToBackupFile = createBackupFile(fileName);
        writeToFile(pathToBackupFile, backupAsJSON);
        return Path.of(pathToBackupFile).getFileName()
                .toString();
    }

    public String makeBackup(
            Sorter.Action action,
            Deque<Path> targetDirPaths,
            List<FilePath> filePaths
    ) throws IOException {

        return makeBackup(action, targetDirPaths, filePaths, LocalDateTime.now());
    }

    private JSONArray parseDirPaths(Deque<Path> paths) {
        JSONArray array = new JSONArray();
        while (!paths.isEmpty()) {
            Path path = paths.pollFirst();
            array.put(path);
        }
        return array;
    }

    private JSONArray parseFilePaths(List<FilePath> paths) {
        JSONArray array = new JSONArray();
        for (FilePath filePath : paths) {
            JSONObject filePathsBackup = parsePair(filePath);
            array.put(filePathsBackup);
        }
        return array;
    }

    private JSONObject parsePair(FilePath filePath) {
        JSONObject object = new JSONObject();
        object.put(SOURCE_KEY, filePath.source());
        object.put(TARGET_KEY, filePath.target());
        return object;
    }

    private String createBackupFile(String fileName) throws IOException {
        String pathToBackupFile = Path.of(pathToBackupDirectory)
                .resolve(fileName)
                .toString();
        File file = new File(pathToBackupFile);
        file.createNewFile();
        return pathToBackupFile;
    }

    private void writeToFile(String path, JSONObject backup) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.write(backup.toString(4));
        } catch (IOException e) {
            logger.error("Error during writing backup to {} message: {}", path, e.getMessage());
            throw e;
        }
    }

    private String generateUniqueNameForBackup(LocalDateTime localDateTime) throws IOException {
        List<String> backupNames = getAllBackupsNamesWithoutExtension();
        String date = localDateTime.format(FORMATTER);
        String name = date;

        int i = 1;
        while (backupNames.contains(name)) {
            name = date + " (" + i++ + ")";
        }
        return name + ".json";
    }

    public void deleteBackup(String name) throws IOException {
        try {
            Path path = Path.of(pathToBackupDirectory).resolve(name);
            Files.delete(path);
        } catch (IOException e) {
            logger.error("Error during deleting backup {} message: {}", name, e.getMessage());
            throw e;
        }
    }

    public List<String> getAllBackupsNamesWithoutExtension() throws IOException {
        return getAllBackupsNames().stream()
                .map(s -> s.substring(0, s.lastIndexOf(".json")))
                .toList();
    }

    public List<String> getAllBackupsNames() throws IOException {
        try (Stream<Path> stream = Files.list(Path.of(pathToBackupDirectory))) {
            return stream.filter(f -> !Files.isDirectory(f))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(".json"))
                    .toList();
        }
    }

    public File getBackupFile(String name) throws IOException {
        Path backupFilePath = Path.of(pathToBackupDirectory).resolve(name);
        File backupFile = new File(backupFilePath.toString());
        if (backupFile.exists() && backupFile.isFile() && name.endsWith(".json")) {
            return new File(backupFile.toString());
        } else {
            throw new IOException("File " + backupFilePath + " does not exists.");
        }
    }
}
