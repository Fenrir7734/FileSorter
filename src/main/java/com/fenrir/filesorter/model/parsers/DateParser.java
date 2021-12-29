package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.statement.types.DatePatternType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateParser {

    public String resolveDatePattern(String expression) {
        if (!expression.isEmpty()) {
            List<String> expressionAsList = Arrays.stream(expression.split("")).toList();
            List<String> resolvedPattern = transformToPattern(expressionAsList);
            resolvedPattern = removeSubsequentQuotes(resolvedPattern);
            resolvedPattern = handleSubsequentSamePatterns(resolvedPattern);
            return  String.join("", resolvedPattern);
        } else {
            return "";
        }
    }

    private List<String> transformToPattern(List<String> list) {
        List<String> newList = new ArrayList<>();
        newList.add("'");

        int i = 0;
        while (i < list.size()) {
            if (isEscapeCharacter(list, i)) {
                newList.add("%");
                i++;
            } else if (isToken(list, i)) {
                DatePatternType datePatternType = DatePatternType.getType(list.get(i + 1));
                newList.add("'");
                newList.add(datePatternType.getPattern());
                newList.add("'");
                i++;
            } else if (list.get(i).equals("'")) {
                newList.add("'");
                newList.add("''");
                newList.add("'");
            } else {
                newList.add(list.get(i));
            }
            i++;
        }
        newList.add("'");
        return newList;
    }

    private boolean isToken(List<String> list, int index) {
        return list.get(index).equals("%")
                && index + 1 < list.size()
                && DatePatternType.getType(list.get(index + 1)) != null;
    }

    private boolean isEscapeCharacter(List<String> list, int index) {
        return list.get(index).equals("%")
                && index + 1 < list.size()
                && list.get(index + 1).equals("%");
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

    private List<String> handleSubsequentSamePatterns(List<String> list) {
        List<String> newList = new ArrayList<>(list.size());
        int i = 0;
        while (i < list.size()) {
            if (isSubsequentSamePattern(list, i)) {
                newList.add(String.format("[%s]", list.get(i)));
            } else {
                newList.add(list.get(i));
            }
            i++;
        }
        return newList;
    }

    private boolean isSubsequentSamePattern(List<String> list, int index) {
        return index + 1 < list.size()
                && (list.get(index).contains(list.get(index + 1))
                    || list.get(index + 1).contains(list.get(index))
                ) && list.get(index).matches("^[a-zA-Z]+$");
    }

}
