package com.fenrir.filesorter.model.statement.string.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImageResolution {
    private static ImageResolution instance;

    private final String path = "src/main/resources/image_resolutions.json";
    private final List<Resolution> resolutions = new ArrayList<>();

    public ImageResolution() throws IOException {
        readResolutionsFromFile();
    }

    private void readResolutionsFromFile() throws IOException {
        String content = new String(Files.readAllBytes(Path.of(path)));
        JSONObject object = new JSONObject(content);
        JSONArray resolutionsJSONArray = object.getJSONArray("resolutions");

        for (int i = 0; i < resolutionsJSONArray.length(); i++) {
            JSONObject resolutionJSONObject = resolutionsJSONArray.getJSONObject(i);
            Resolution resolution = getResolutionFromJSONObject(resolutionJSONObject);
            resolutions.add(resolution);
        }
    }

    private Resolution getResolutionFromJSONObject(JSONObject object) {
        int width = object.getInt("width");
        int height = object.getInt("height");
        return new Resolution(width, height);
    }

    public boolean matchResolution(Resolution resolution) {
        return resolutions.contains(resolution);
    }

    public static ImageResolution getInstance() throws IOException {
        if (instance == null) {
            instance = new ImageResolution();
        }
        return instance;
    }
}
