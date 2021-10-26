package com.fenrir.filesorter.rules.parsers;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.rules.RenameRule;
import com.fenrir.filesorter.rules.Rule;
import com.fenrir.filesorter.tokens.DateTokenType;
import com.fenrir.filesorter.tokens.RenameTokenType;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RenameRuleParser {
    private Rule rule;
    private FileData fileData;

    public RenameRuleParser(RenameRule rule, FileData fileData) {
        this.rule = rule;
        this.fileData = fileData;
    }

    public String resolveRule() {
        StringBuilder resolvedRule = new StringBuilder();

        Rule.RuleElement element;
        while ((element = rule.next()) != null) {
            if (element.isToken()) {
                String resolvedToken = parseToken(element.element());
                resolvedRule.append(resolvedToken);
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

        RenameTokenType renameTokenType = RenameTokenType.get(token);

        if (renameTokenType == null) {
            throw new IllegalArgumentException();
        }

        return switch (renameTokenType) {
            case CURRENT_FILE_NAME -> getFileName();
            case FILE_EXTENSION -> getFileExtension();
        };
    }

    private String getFileExtension() {
        String extension = fileData.getFileExtension();
        return extension != null && !extension.equals("") ? extension : "Other";
    }

    private String getFileName() {
        return null;
    }

    private String getDate(String pattern) {
        Calendar calendar = fileData.creationTime();
        return new SimpleDateFormat(pattern).format(calendar);
    }
}
