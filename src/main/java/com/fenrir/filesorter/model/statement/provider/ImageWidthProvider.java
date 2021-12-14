package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.utils.Dimension;

import java.io.IOException;

public class ImageWidthProvider implements Provider<Integer> {
    @Override
    public Integer get(FileData fileData) throws IOException {
        if (!fileData.isImage()) {
            return null;
        }
        Dimension dimension = fileData.getImageDimension();
        return dimension != null ? dimension.getWidth() : null;
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        if (!fileData.isImage()) {
            return "NonImage";
        }
        Dimension dimension = fileData.getImageDimension();
        return dimension != null ? String.valueOf(dimension.getWidth()) : "Other";
    }
}
