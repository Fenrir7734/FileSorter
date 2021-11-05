package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.string.utils.ImageResolution;
import com.fenrir.filesorter.model.statement.string.utils.Resolution;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

public class ImageResolutionStatement implements StringStatement {
    private FileData fileData;

    public ImageResolutionStatement(FileData fileData, StringStatementDescription description) {
        this.fileData = fileData;
    }

    @Override
    public String execute() throws IOException {
        return getImageResolutionAsString();
    }

    private String getImageResolutionAsString() throws IOException {
        Resolution resolution = getImageResolution();

        if (resolution == null) {
            return "NonImage";
        }

        ImageResolution imageResolution = ImageResolution.getInstance();
        return imageResolution.matchResolution(resolution) ? resolution.toString() : "Other";
    }

    private Resolution getImageResolution() throws IOException {
        InputStream inputStream = getInputStream();
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

    private InputStream getInputStream() throws IOException {
        URL url = fileData.getSourcePath().toUri().toURL();
        return url.openStream();
    }

    private ImageReader getImageReader(ImageInputStream inputStream) throws IOException {
        Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);
        return readers.hasNext() ? readers.next() : null;
    }

    private Resolution getImageResolutionFromReader(ImageReader reader) throws IOException {
        int width = reader.getWidth(0);
        int height = reader.getHeight(0);
        return new Resolution(width, height);
    }


}
