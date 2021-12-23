package com.fenrir.filesorter.model.file;

import javafx.util.Pair;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileBackup {
    private final Logger logger = LoggerFactory.getLogger(FileBackup.class);

    private final static String BACKUP_DIR_PATH = "src/main/resources/rule_group.json";
    private final static String SOURCE_KEY = "from";
    private final static String TARGET_KEY = "to";
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void makeBackup(List<Pair<Path, Path>> paths) throws IOException {
        JSONArray backupAsJSON = parsePaths(paths);
        String pathToBackupFile = createBackupFile();
        writeToFile(pathToBackupFile, backupAsJSON);
    }

    private JSONArray parsePaths(List<Pair<Path, Path>> paths) {
        JSONArray root = new JSONArray();
        for (Pair<Path, Path> pathPair: paths) {
            JSONObject filePathsBackup = parsePair(pathPair);
            root.put(filePathsBackup);
        }
        return root;
    }

    private JSONObject parsePair(Pair<Path, Path> pathPair) {
        JSONObject object = new JSONObject();
        object.put(SOURCE_KEY, pathPair.getKey());
        object.put(TARGET_KEY, pathPair.getValue());
        return object;
    }

    private String createBackupFile() throws IOException {
        String fileName = generateNameForBackup();
        String pathToBackupFile = Path.of(BACKUP_DIR_PATH)
                .resolve(fileName)
                .toString();
        File file = new File(pathToBackupFile);
        file.createNewFile();
        return pathToBackupFile;
    }

    private void writeToFile(String path, JSONArray backup) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(path))) {
            writer.write(backup.toString(4));
        } catch (IOException e) {
            logger.error("Error during writing backup to {} message: {}", path, e.getMessage());
            throw e;
        }
    }

    private String generateNameForBackup() throws IOException {
        List<String> backupNames = getAllBackupsNamesWithoutExtension();
        String date = LocalDateTime.now().format(FORMATTER);
        String name = date;

        int i = 1;
        while (backupNames.contains(name)) {
            name = date + " (" + i++ + ")";
        }
        return name + ".json";
    }

    public List<String> getAllBackupsNamesWithoutExtension() throws IOException {
        return getAllBackupsNames().stream()
                .map(s -> s.substring(0, s.lastIndexOf(".json")))
                .toList();
    }

    public List<String> getAllBackupsNames() throws IOException {
        try (Stream<Path> stream = Files.list(Path.of(BACKUP_DIR_PATH))) {
            return stream.filter(f -> !Files.isDirectory(f))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(".json"))
                    .toList();
        }
    }

    public List<Pair<Path, Path>> readBackup(String name) throws IOException, JSONException {
        try {
            Path backupFilePath = Path.of(BACKUP_DIR_PATH).resolve(name);
            String content = new String(Files.readAllBytes(backupFilePath));
            JSONArray backup = new JSONArray(content);
            return mapJSONArrayToListOfPaths(backup);
        } catch (IOException | JSONException e) {
            logger.error("Error during reading backup {} message: {}", name, e.getMessage());
            throw e;
        }
    }

    private List<Pair<Path, Path>> mapJSONArrayToListOfPaths(JSONArray array) {
        List<Pair<Path, Path>> paths = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Pair<Path, Path> pairOfPaths = mapJSONObjectToPairOfPaths(object);
            paths.add(pairOfPaths);
        }
        return paths;
    }

    private Pair<Path, Path> mapJSONObjectToPairOfPaths(JSONObject object) {
        Path sourcePath = Path.of(object.getString(SOURCE_KEY));
        Path targetPath = Path.of(object.getString(TARGET_KEY));
        return new Pair<>(sourcePath, targetPath);
    }
}
