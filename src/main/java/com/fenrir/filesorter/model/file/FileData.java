package com.fenrir.filesorter.model.file;

import com.fenrir.filesorter.model.file.utils.FileCategoryType;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.file.utils.FilesCategory;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Iterator;

public class FileData {
    private static Logger logger = LoggerFactory.getLogger(FileData.class);

    private final Path sourcePath;
    private Path targetPath;
    private long count;
    private final BasicFileAttributes attributes;
    private final boolean isDirectory;
    private boolean isIncluded;
    private Dimension dimension;

    public FileData(Path path) throws IOException {
        this.isDirectory = Files.isDirectory(path);
        this.sourcePath = path;
        this.targetPath = null;
        this.attributes = Files.readAttributes(path, BasicFileAttributes.class);
        this.isIncluded = false;
        this.dimension = null;
    }

    public Calendar creationTime() {
        FileTime fileCreationTime = attributes.creationTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fileCreationTime.toMillis());
        return calendar;
    }

    public Calendar lastModifiedTime() {
        FileTime fileModificationTime = attributes.lastModifiedTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fileModificationTime.toMillis());
        return calendar;
    }

    public String getFileName() {
        return sourcePath.getFileName().toString();
    }

    public String getFileNameWithoutExtension() {
        String fileName =  getFileName();
        int extensionIndex = fileName.lastIndexOf(".");
        return extensionIndex != -1 ?
                fileName.substring(0, extensionIndex) : fileName;
    }

    public boolean hasExtension() {
        if (isDirectory) {
            return false;
        }
        String fileName = sourcePath.getFileName().toString();
        return fileName.lastIndexOf(".") != -1;
    }

    public String getFileExtension() {
        if (!isDirectory) {
            String fileName = sourcePath.getFileName().toString();
            int i = fileName.lastIndexOf(".");
            if (i != -1) {
                return fileName.substring(i + 1).trim();
            }
        }
        return null;
    }

    public FileCategoryType getFileCategory() throws IOException {
        if (isDirectory) {
            return null;
        }
        if (!hasExtension()) {
            return FileCategoryType.OTHERS;
        }
        String extension = getFileExtension();
        FilesCategory filesCategory = FilesCategory.getInstance();
        FileCategoryType category = filesCategory.matchCategory(extension);
        return category != null ? category : FileCategoryType.OTHERS;
    }

    public long getFileSize() {
        File file = sourcePath.toFile();
        return file.length();
    }

    public boolean isImage() throws IOException {
        if (!isDirectory) {
            Tika tika = new Tika();
            String mimeType = tika.detect(sourcePath.toString());
            return mimeType.startsWith("image");
        }
        return false;
    }

    public Dimension getImageDimension1() throws IOException {
        //System.out.println("dimension");
        if (isImage() && dimension == null) {
            extractDimensionFromImage();
        }
        return dimension;
    }

    private void extractDimensionFromImage() {
        try {
            ImageInfo info = Imaging.getImageInfo(sourcePath.toFile());
            int width = info.getWidth();
            int height = info.getHeight();
            dimension = Dimension.of(width, height);
            //System.out.println(dimension);
        } catch (IOException | ImageReadException e) {
            logger.error(e.getMessage());
        }
    }

    public Dimension getImageDimension() throws IOException {
        InputStream inputStream = getInputStream();
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream)) {
            ImageReader reader = getImageReader(imageInputStream);
            if (reader != null) {
                try {
                    reader.setInput(imageInputStream);
                    return getImageDimensionFromReader(reader);
                } finally {
                    reader.dispose();
                }
            }
        }
        return null;
    }

    private InputStream getInputStream() throws IOException {
        URL url = sourcePath.toUri().toURL();
        return url.openStream();
    }

    private ImageReader getImageReader(ImageInputStream inputStream) {
        Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);
        return readers.hasNext() ? readers.next() : null;
    }

    private Dimension getImageDimensionFromReader(ImageReader reader) throws IOException {
        int width = reader.getWidth(0);
        int height = reader.getHeight(0);
        return Dimension.of(width, height);
    }

    public Path resolveTargetPath() {
        if (count <= 0 || isDirectory) {
            return targetPath;
        }

        String pathStr = targetPath.getFileName().toString();
        String toInsert = String.format(" (%d)", count);
        int extensionIndex = pathStr.indexOf(".");

        if (extensionIndex != -1) {
            pathStr = new StringBuilder(pathStr).insert(extensionIndex, toInsert)
                    .toString();
        } else {
            pathStr += toInsert;
        }
        return targetPath.getParent().resolve(Path.of(pathStr));
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isIncluded() {
        return isIncluded;
    }

    public long getCount() {
        return count;
    }

    public void setTargetPath(Path targetPath, long count) {
        this.targetPath = targetPath;
        this.count = count;
    }

    public void setIncluded(boolean included) {
        isIncluded = included;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "sourcePath=" + sourcePath +
                ", targetPath=" + targetPath +
                '}';
    }
}
