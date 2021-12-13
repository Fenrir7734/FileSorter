package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class ImageHeightProvider implements Provider<Integer> {
    @Override
    public Integer get(FileData fileData) throws IOException {
        return fileData.isImage() ? fileData.getImageDimension().getHeight() : null;
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        if (!fileData.isImage()) {
            return "NonImage";
        }
        int height = fileData.getImageDimension().getHeight();
        return String.valueOf(height);
    }
}
