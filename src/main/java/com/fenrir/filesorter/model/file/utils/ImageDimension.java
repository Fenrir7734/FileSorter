package com.fenrir.filesorter.model.file.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImageDimension {
    private static ImageDimension instance;

    private final String path = "src/main/resources/image_dimensions.json";
    private final List<Dimension> dimensions = new ArrayList<>();

    public ImageDimension() throws IOException {
        readDimensionsFromFile();
    }

    private void readDimensionsFromFile() throws IOException {
        String content = new String(Files.readAllBytes(Path.of(path)));
        JSONObject object = new JSONObject(content);
        JSONArray dimensionsJSONArray = object.getJSONArray("dimensions");

        for (int i = 0; i < dimensionsJSONArray.length(); i++) {
            JSONObject dimensionJSONObject = dimensionsJSONArray.getJSONObject(i);
            Dimension resolution = getDimensionFromJSONObject(dimensionJSONObject);
            dimensions.add(resolution);
        }
    }

    private Dimension getDimensionFromJSONObject(JSONObject object) {
        int width = object.getInt("width");
        int height = object.getInt("height");
        return Dimension.of(width, height);
    }

    public boolean matchDimension(Dimension dimension) {
        return dimensions.contains(dimension);
    }

    public static ImageDimension getInstance() throws IOException {
        if (instance == null) {
            instance = new ImageDimension();
        }
        return instance;
    }
}
