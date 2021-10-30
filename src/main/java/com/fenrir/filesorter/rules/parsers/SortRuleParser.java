package com.fenrir.filesorter.rules.parsers;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.file.FileUtils;
import com.fenrir.filesorter.rules.Rule;
import com.fenrir.filesorter.rules.SortRule;
import com.fenrir.filesorter.statement.string.utils.Category;
import com.fenrir.filesorter.statement.string.utils.FilesCategory;
import com.fenrir.filesorter.statement.string.utils.ImageResolution;
import com.fenrir.filesorter.statement.string.utils.Resolution;
import com.fenrir.filesorter.tokens.DateTokenType;
import com.fenrir.filesorter.tokens.SortTokenType;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class SortRuleParser {
    private Rule rule;
    private FileData fileData;

    public SortRuleParser(SortRule rule, FileData fileData) {
        this.rule = rule;
        this.fileData = fileData;
    }

    public Path resolveRule() throws IOException {
        StringBuilder resolvedRule = new StringBuilder();

        Rule.RuleElement element;
        while ((element = rule.next()) != null) {
            if (element.isToken()) {
                String resolvedFlag = parseToken(element.element());
                resolvedRule.append(resolvedFlag);
            } else {
                resolvedRule.append(element.element());
            }
        }

        this.rule.resetIter();
        return Path.of(resolvedRule.toString());
    }

    private String parseToken(String token) throws IllegalArgumentException, IOException {
        DateTokenType dateTokenType = DateTokenType.get(token);

        if (dateTokenType != null) {
            return getDate(dateTokenType.getPattern());
        }

        SortTokenType sortTokenType = SortTokenType.get(token);

        if (sortTokenType == null) {
            throw new IllegalArgumentException();
        }

        return switch (sortTokenType) {
            case SEPARATOR -> getSeparator();
            case FILE_EXTENSION -> getFileExtension();
            case FILE_CATEGORY -> getFileCategory();
            case IMAGE_RESOLUTION -> getImageResolution();
        };
    }

    private String getSeparator() {
        return File.separator;
    }

    private String getFileExtension() {
        String extension = fileData.getFileExtension();
        return extension != null && !extension.equals("") ? extension : "UNKNOWN";
    }

    private String getFileCategory() throws IOException {
        if (!FileUtils.hasExtension(fileData.getSourcePath())) {
            return Category.OTHERS.getName();
        }
        String extension = fileData.getFileExtension();
        FilesCategory filesCategory = FilesCategory.getInstance();
        Category category = filesCategory.matchCategory(extension);
        return category != null ? category.getName() : Category.OTHERS.getName();
    }

    private String getImageResolution() throws IOException {
        URL url = fileData.getSourcePath().toUri().toURL();
        try (ImageInputStream stream = ImageIO.createImageInputStream(url.openStream())) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(stream);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                Resolution resolution = new Resolution(width, height);
                reader.dispose();
                return ImageResolution.getInstance().contains(resolution) ? resolution.toString() : "Other";
            }
        }
        return "Non-Image";
    }

    private String getFileName() {
        return fileData.getSourcePath().getFileName().toString();
    }

    private String getDate(String pattern) {
        Calendar calendar = fileData.creationTime();
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

}
