package com.fenrir.filesorter.statement.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesCategory {
    private static FilesCategory instance;

    private final String path = "src/main/resources/extensions.json";
    private final Map<Category, List<String>> extensions = new HashMap<>();

    private FilesCategory() throws IOException {
        readExtensionsFromFile();
    }

    private void readExtensionsFromFile() throws IOException {
        String content = new String(Files.readAllBytes(Path.of(path)));
        JSONObject object = new JSONObject(content);

        for (Category category: Category.values()) {
            JSONArray array = object.getJSONArray(category.getName());
            List<String> extensionsList = getExtensionsFromJSONArray(array);
            extensions.put(category, extensionsList);
        }
    }

    private List<String> getExtensionsFromJSONArray(JSONArray array) {
        List<String> extensions = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            String extension = array.getString(i);
            extensions.add(extension);
        }
        return extensions;
    }

    public Category matchCategory(String extension) {
        for (Category category: extensions.keySet()) {
            List<String> fileCategoryExtensions = extensions.get(category);
            if (fileCategoryExtensions.contains(extension)) {
                return category;
            }
        }
        return null;
    }

    public static FilesCategory getInstance() throws IOException {
        if (instance == null) {
            instance = new FilesCategory();
        }
        return instance;
    }
}
