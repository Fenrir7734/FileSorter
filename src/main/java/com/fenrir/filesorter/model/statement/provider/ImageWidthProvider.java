package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class ImageWidthProvider implements Provider<Integer> {
    @Override
    public Integer get(FileData fileData) throws IOException {
        return fileData.isImage() ? fileData.getImageDimension().getWidth() : null;
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        if (!fileData.isImage()) {
            return "NonImage";
        }
        int width = fileData.getImageDimension().getWidth();
        return String.valueOf(width);
    }
}
