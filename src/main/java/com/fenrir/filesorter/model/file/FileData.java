package com.fenrir.filesorter.model.file;

import com.fenrir.filesorter.model.file.utils.FileCategoryType;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.file.utils.FilesCategory;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class FileData {
    private static final Logger logger = LoggerFactory.getLogger(FileData.class);

    private final Path sourcePath;
    private Path targetPath;
    private long count;

    private String fileName;
    private String extension;
    private long fileSize;

    private final FileTime creationTime;
    private final FileTime lastModifiedTime;
    private final FileTime lastAccessTime;

    private final boolean isDirectory;

    private FileCategoryType category;
    private MimeType mimeType;
    private String realExtension;
    private Dimension dimension;

    private boolean isIncluded;

    public FileData(Path path) throws IOException {
        this.isDirectory = Files.isDirectory(path);
        this.sourcePath = path;
        this.targetPath = null;
        this.extension = extractExtensionFromFileName();
        this.fileName = extractFileName();
        this.fileSize = sourcePath.toFile().length();

        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        this.creationTime = attributes.creationTime();
        this.lastModifiedTime = attributes.lastModifiedTime();
        this.lastAccessTime = attributes.lastAccessTime();

        this.isIncluded = false;
        this.category = null;
        this.mimeType = null;
        this.dimension = null;
    }

    private String extractExtensionFromFileName() {
        if (!isDirectory) {
            String fileName = sourcePath.getFileName().toString();
            int i = fileName.lastIndexOf(".");
            if (i != -1) {
                return fileName.substring(i + 1).trim();
            }
        }
        return "";
    }

    private String extractFileName() {
        String fileName = sourcePath.getFileName().toString();
        if (hasExtension()) {
            int extensionIndex = fileName.lastIndexOf(extension);
            fileName = fileName.substring(0, extensionIndex - 1);
        }
        return fileName;
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

    public String getSourceParentDirectoryName() {
        return sourcePath.getParent()
                .getFileName()
                .toString();
    }

    public Path getSourceParentDirectoryPath() {
        return sourcePath.getParent();
    }

    public String getFileName() {
        return extension.isEmpty() ? fileName : fileName + "." + extension;
    }

    public String getFileNameWithoutExtension() {
        return fileName;
    }

    public String getFileExtension() {
        return extension;
    }

    public String getRealFileExtension() {
        if (realExtension == null) {
            extractRealExtension();
        }
        return realExtension;
    }

    private void extractRealExtension() {
        if (!isDirectory) {
            extractRealExtensionFromFile();
        } else {
            realExtension = "";
        }
    }

    private void extractRealExtensionFromFile() {
        try {
            MimeType type = getMimeType();
            realExtension = isUnknownType(mimeType) ? "" : type.getExtension().substring(1);
        } catch (MimeTypeException e) {
            logger.error("Error during extracting mime type: {}", e.getMessage());
            realExtension = "";
        }
    }

    private boolean isUnknownType(MimeType mimeType) {
        return mimeType.toString().equals("application/octet-stream");
    }

    public boolean hasExtension() {
        return !extension.isEmpty();
    }

    public long getFileSize() {
        return fileSize;
    }

    public FileCategoryType getFileCategory() throws IOException {
        if (category == null) {
            findCategory();
        }
        return category;
    }

    public void findCategory() throws IOException {
        if (isDirectory) {
            category = FileCategoryType.OTHERS;
        } else {
            String extension = getRealFileExtension();
            matchCategory(extension);
        }
    }

    private void matchCategory(String extension) throws IOException {
        FilesCategory filesCategory = FilesCategory.getInstance();
        category = filesCategory.matchCategory(extension);
    }

    private MimeType getMimeType() throws MimeTypeException {
        if (!isDirectory && mimeType == null) {
            extractMimeType();
        }
        return mimeType;
    }

    private void extractMimeType() throws MimeTypeException {
        if (!isDirectory) {
            String type = getMediaType();
            mimeType = MimeTypes.getDefaultMimeTypes().forName(type);
        }
    }

    private String getMediaType() {
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(sourcePath.toFile()))) {
            Detector detector = new DefaultDetector();
            Metadata metadata = new Metadata();

            MediaType mediaType = detector.detect(inputStream, metadata);
            return mediaType.toString();
        } catch (IOException e) {
            logger.error("Error when reading file MediaType: {}", e.getMessage());
        }
        return "application/octet-stream";
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public boolean isImage() throws IOException {
        if (category == null) {
            findCategory();
        }
        return category == FileCategoryType.IMAGE;
    }

    public Dimension getImageDimension() throws IOException {
        if (dimension == null) {
            extractDimensionFromFile();
        }
        return dimension;
    }

    public void extractDimensionFromFile() throws IOException {
        if (isImage()) {
            readDimensionFromImage();
        } else {
            dimension = null;
        }
    }

    public void readDimensionFromImage() {
        try {
            ImageInfo info = Imaging.getImageInfo(sourcePath.toFile());
            int width = info.getWidth();
            int height = info.getHeight();
            dimension = Dimension.of(width, height);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ImageReadException e) {
            logger.warn(e.getMessage());
        }
    }

    public FileTime creationTime() {
        return creationTime;
    }

    public FileTime lastModifiedTime() {
        return lastModifiedTime;
    }

    public FileTime lastAccessTime() {
        return lastAccessTime;
    }

    public boolean isIncluded() {
        return isIncluded;
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
