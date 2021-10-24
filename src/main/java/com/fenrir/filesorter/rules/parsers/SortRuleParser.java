package com.fenrir.filesorter.rules.parsers;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.rules.Rule;
import com.fenrir.filesorter.tokens.DateTokenType;
import com.fenrir.filesorter.tokens.SortTokenType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SortRuleParser {
    private Rule rule;
    private FileData fileData;

    public SortRuleParser(Rule rule, FileData fileData) {
        this.rule = rule;
        this.fileData = fileData;
    }

    public String resolveRule() {
        StringBuilder resolvedRule = new StringBuilder();

        Rule.RuleElement element;
        while ((element = rule.next()) != null) {
            if (element.isFlag()) {
                String resolvedFlag = parseToken(element.element());
                resolvedRule.append(resolvedFlag);
            } else {
                resolvedRule.append(element.element());
            }
        }
        return resolvedRule.toString();
    }

    private String parseToken(String token) throws IllegalArgumentException {
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
            case CURRENT_FILE_NAME -> getFileName();
        };
    }

    private String getSeparator() {
        return File.separator;
    }

    private String getFileExtension() {
        String extension = fileData.getFileExtension();
        return extension != null && !extension.equals("") ? extension : "Other";
    }

    private String getFileCategory() {
        return null;
    }

    private String getImageResolution() {
        return null;
    }

    private String getFileName() {
        return null;
    }

    private String getDate(String pattern) {
        Calendar calendar = fileData.creationTime();
        return new SimpleDateFormat(pattern).format(calendar);
    }

}
