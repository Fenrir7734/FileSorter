package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.enums.DatePatternType;

import java.util.ArrayList;
import java.util.List;

public class DateParser {

    public String resolveDatePattern(String datePatter) {
        List<String> resolvedPattern = new ArrayList<>();
        resolvedPattern.add("'");
        String[] patternArray = datePatter.isEmpty() ? new String[0] : datePatter.split("");

        int i = 0;
        while (i < patternArray.length) {
            if (isToken(patternArray, i)) {
                DatePatternType datePatternType = DatePatternType.getType(patternArray[i + 1]);
                resolvedPattern.add("'");
                resolvedPattern.add(datePatternType.getPattern());
                resolvedPattern.add("'");
                i++;
            } else if (isEscapeCharacter(patternArray, i)) {
                resolvedPattern.add("%");
                i++;
            } else if (patternArray[i].equals("'")) {
                resolvedPattern.add("'");
                resolvedPattern.add("''");
                resolvedPattern.add("'");
            } else {
                resolvedPattern.add(patternArray[i]);
            }
            i++;
        }

        resolvedPattern.add("'");
        resolvedPattern = removeSubsequentQuotes(resolvedPattern);
        return  String.join("", resolvedPattern);
    }

    private boolean isToken(String[] patternArray, int index) {
        return patternArray[index].equals("%")
                && index + 1 < patternArray.length
                && DatePatternType.getType(patternArray[index + 1]) != null;
    }

    private boolean isEscapeCharacter(String[] patternArray, int index) {
        return patternArray[index].equals("%")
                && index + 1 < patternArray.length
                && patternArray[index + 1].equals("%");
    }

    public List<String> removeSubsequentQuotes(List<String> list) {
        List<String> newList = new ArrayList<>(list.size());
        int i = 0;
        while (i < list.size()) {
            if (isSubsequentQuote(list, i)) {
                i++;
            } else {
                newList.add(list.get(i));
            }
            i++;
        }
        return newList;
    }

    private boolean isSubsequentQuote(List<String> list, int index) {
        return list.get(index).equals("'")
                && index + 1 < list.size()
                && list.get(index + 1).equals("'");
    }

}
