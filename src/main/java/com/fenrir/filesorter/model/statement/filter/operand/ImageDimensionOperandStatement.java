package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.string.utils.Dimension;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

public class ImageDimensionOperandStatement implements FilterOperandStatement<Dimension> {
    @Override
    public Dimension execute(FileData fileData) throws IOException {
        return getImageResolution(fileData);
    }

    private Dimension getImageResolution(FileData fileData) throws IOException {
        InputStream inputStream = getInputStream(fileData);
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream)) {
            ImageReader reader = getImageReader(imageInputStream);
            if (reader != null) {
                try {
                    reader.setInput(imageInputStream);
                    return getImageResolutionFromReader(reader);
                } finally {
                    reader.dispose();
                }
            }
        }
        return null;
    }

    private InputStream getInputStream(FileData fileData) throws IOException {
        URL url = fileData.getSourcePath().toUri().toURL();
        return url.openStream();
    }

    private ImageReader getImageReader(ImageInputStream inputStream) throws IOException {
        Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);
        return readers.hasNext() ? readers.next() : null;
    }

    private Dimension getImageResolutionFromReader(ImageReader reader) throws IOException {
        int width = reader.getWidth(0);
        int height = reader.getHeight(0);
        return Dimension.of(width, height);
    }
}
