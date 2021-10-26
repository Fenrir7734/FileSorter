package com.fenrir.filesorter.file;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    public static boolean hasExtension(Path path) {
        if (Files.isDirectory(path)) {
            return false;
        }
        String fileName = path.getFileName().toString();
        return fileName.lastIndexOf(".") != -1;
    }
}
