package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.utils.ImageDimension;
import com.fenrir.filesorter.model.file.utils.Dimension;

import java.io.IOException;

public class ImageDimensionStatement implements StringStatement {

    public ImageDimensionStatement(StringStatementDescription description) { }

    @Override
    public String execute(FileData fileData) throws IOException {
        if (!fileData.isImage()) {
            return "NonImage";
        }
        Dimension dimension = fileData.getImageDimension();
        ImageDimension imageDimension = ImageDimension.getInstance();
        return imageDimension.matchResolution(dimension) ? dimension.toString() : "Other";
    }
}
