package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.file.utils.ImageDimension;

import java.io.IOException;

public class ImageDimensionOperandStatement implements FilterOperandStatement<Dimension> {
    @Override
    public Dimension get(FileData fileData) throws IOException {
        return fileData.isImage() ? fileData.getImageDimension() : null;
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        if (!fileData.isImage()) {
            return "NonImage";
        }
        Dimension dimension = fileData.getImageDimension();
        ImageDimension imageDimension = ImageDimension.getInstance();
        return imageDimension.matchResolution(dimension) ? dimension.toString() : "Other";
    }
}
