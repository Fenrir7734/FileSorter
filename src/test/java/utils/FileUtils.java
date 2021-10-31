package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class FileUtils {
    public static Path createFile(Path tempDirPath, String fileName) {
        try {
            Path path = tempDirPath.resolve(fileName);
            File file = path.toFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("file content");
            bufferedWriter.close();
            return path;
        } catch (InvalidPathException e) {
            System.err.println("Error creating temporary test files: " + e);
        } catch (IOException e) {
            System.err.println("Error writing to the temporary files: " + e);
        }
        return null;
    }
}
