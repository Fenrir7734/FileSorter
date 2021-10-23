package com.fenrir.filesorter.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Rule {
    private String[] rule;
    private int iterator;

    public Rule(String rule) {
        this.iterator = 0;
        resolveRule(rule);
    }

    private void resolveRule(String rule) {
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(rule);
        List<String> tmpList = new ArrayList<>();

        while (matcher.find()) {
            tmpList.add(matcher.group());
        }
        this.rule = tmpList.toArray(new String[0]);
    }

    public String next() {
        return iterator < rule.length ? rule[iterator++] : null;
    }
}
