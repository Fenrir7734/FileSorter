package com.fenrir.filesorter.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Rule {
    private final List<RuleElement> rule;
    private int iterator;

    public Rule(String rule) {
        this.rule = new ArrayList<>();
        this.iterator = 0;
        resolveRule(rule);
    }

    private void resolveRule(String rule) {
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(rule);
        int lastMatchIndex = 0;

        while (matcher.find()) {
            extractNonFlagElement(rule, lastMatchIndex, matcher.start());
            extractFlagElement(matcher);
            lastMatchIndex = matcher.end();
        }
        extractNonFlagElement(rule, lastMatchIndex, rule.length());
    }

    private void extractNonFlagElement(String rule, int lastMatchIndex, int currentMatchIndex) {
        if (lastMatchIndex != currentMatchIndex) {
            String s = rule.substring(lastMatchIndex, currentMatchIndex);
            RuleElement element = new RuleElement(s, false);
            this.rule.add(element);
        }
    }

    private void extractFlagElement(Matcher matcher) {
        String flag = matcher.group(1);
        RuleElement element = new RuleElement(flag, true);
        this.rule.add(element);
    }

    public RuleElement next() {
        return iterator < rule.size() ? rule.get(iterator++) : null;
    }

    public record RuleElement(String element, boolean isFlag) { }
}
