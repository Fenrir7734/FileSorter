package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.utils.Dimension;

import java.io.IOException;

public class ImageDimensionOperandStatement implements FilterOperandStatement<Dimension> {
    @Override
    public Dimension execute(FileData fileData) throws IOException {
        return fileData.isImage() ? fileData.getImageDimension() : null;
    }
}
