package com.fenrir.filesorter.rules.parsers;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.rules.Rule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.fenrir.filesorter.flags.SortFlags.*;

public class RuleSortParser {
    private Rule rule;
    private FileData fileData;

    public RuleSortParser(Rule rule, FileData fileData) {
        this.rule = rule;
        this.fileData = fileData;
    }

    public String resolveRule() {
        StringBuilder resolvedRule = new StringBuilder();

        Rule.RuleElement element;
        while ((element = rule.next()) != null) {
            if (element.isFlag()) {
                String resolvedFlag = parseFlag(element.element());
                resolvedRule.append(resolvedFlag);
            } else {
                resolvedRule.append(element.element());
            }
        }
        return resolvedRule.toString();
    }

    private String parseFlag(String flag) throws IllegalArgumentException {
        return switch (flag) {
            case SEPARATOR -> File.separator;
            case FILE_EXTENSION -> getFileExtension();
            case YEAR -> getYear(YEAR);
            case YEAR_TWO_DIGIT -> getYear(YEAR_TWO_DIGIT);
            case MONTH_NUMBER -> getMonth(MONTH_NUMBER);
            case MONTH_NUMBER_0 -> getMonth(MONTH_NUMBER_0);
            case MONTH_NAME_ABBREVIATION -> getMonth(MONTH_NAME_ABBREVIATION);
            case MONTH_NAME -> getMonth(MONTH_NAME);
            default -> throw new IllegalArgumentException();
        };
    }

    private String getFileExtension() {
        String extension = fileData.getFileExtension();
        return extension != null && !extension.equals("") ? extension : "Other";
    }

    private String getYear(String format) {
        Calendar calendar = fileData.creationTime();
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        if (format.equals(YEAR_TWO_DIGIT)) {
            year = year.length() > 2 ? year.substring(year.length() - 2) : year;
        }
        return year;
    }

    private String getMonth(String format) {
        Calendar calendar = fileData.creationTime();
        int month = calendar.get(Calendar.MONTH);

        if (format.equals(MONTH_NUMBER)) {
            return String.valueOf(month);
        }

        if (format.equals(MONTH_NUMBER_0) && month < 10) {
            return new SimpleDateFormat("MM")
                    .format(calendar.getTime());
        }

        if (format.equals(MONTH_NAME)) {
            return new SimpleDateFormat("MMM")
                    .format(calendar.getTime());
        }

        return new SimpleDateFormat("MMMM")
                .format(calendar.getTime());
    }
}
