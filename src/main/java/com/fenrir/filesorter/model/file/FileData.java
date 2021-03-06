package com.fenrir.filesorter.model.file;

import com.fenrir.filesorter.model.file.utils.FileCategoryType;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.file.utils.FilesCategory;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.io.FilenameUtils;
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

    private FilePath filePath;

    private final String fileName;
    private final String extension;
    private final long fileSize;

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
        filePath = FilePath.of(path);
        this.extension = extractExtensionFromFileName();
        this.fileName = extractFileName();
        this.fileSize = filePath.source().toFile().length();

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
            String fileName = filePath.source().getFileName().toString();
            int i = fileName.lastIndexOf(".");
            if (i != -1) {
                return fileName.substring(i + 1).trim();
            }
        }
        return "";
    }

    private String extractFileName() {
        String fileName = filePath.source().getFileName().toString();
        if (hasExtension()) {
            int extensionIndex = fileName.lastIndexOf(extension);
            fileName = fileName.substring(0, extensionIndex - 1);
        }
        return fileName;
    }

    public Path resolveTargetPath() {
        if (isDirectory) {
            return filePath.target();
        }
        filePath.resolveTargetPath();
        return filePath.resolvedTargetPath();
    }

    public Path getResolvedTargetPath() {
        return filePath.resolvedTargetPath();
    }

    public Path getSourcePath() {
        return filePath.source();
    }

    public Path getTargetPath() {
        return filePath.target();
    }

    public FilePath getFilePath() {
        return filePath;
    }

    public String getSourceParentDirectoryName() {
        return filePath.source().getParent()
                .getFileName()
                .toString();
    }

    public Path getSourceParentDirectoryPath() {
        return filePath.source().getParent();
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
        return mimeType.toString().equals("application/octet-stream")
                || mimeType.getExtension() == null
                || mimeType.getExtension().isBlank();
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
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath.source().toFile()))) {
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
            ImageInfo info = Imaging.getImageInfo(filePath.source().toFile());
            int width = info.getWidth();
            int height = info.getHeight();
            dimension = Dimension.of(width, height);
        } catch (IOException | ImageReadException e) {
            logger.warn(e.getMessage() + " " + getSourcePath());
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
        filePath.setTarget(targetPath, count);
    }

    public void setIncluded(boolean included) {
        isIncluded = included;
    }

    @Override
    public String toString() {
        return filePath.toString();
    }

    public static Path normalizeFilePath(Path path) {
        String pathStr = path.toAbsolutePath().toString();
        pathStr = FilenameUtils.normalize(pathStr);
        return pathStr != null ? Path.of(pathStr) : null;
    }
}
