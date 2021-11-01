package com.fenrir.filesorter.model.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRule extends Rule {

    public StringRule(String rule) {
        super();
        resolveRule(rule);
    }

    protected void resolveRule(String rule) {
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(rule);
        int lastMatchIndex = 0;

        while (matcher.find()) {
            extractLiteral(rule, lastMatchIndex, matcher.start());
            extractToken(matcher);
            lastMatchIndex = matcher.end();
        }
        extractLiteral(rule, lastMatchIndex, rule.length());
    }

    private void extractLiteral(String rule, int lastMatchIndex, int currentMatchIndex) {
        if (lastMatchIndex != currentMatchIndex) {
            String s = rule.substring(lastMatchIndex, currentMatchIndex);
            RuleElement element = new RuleElement(s, false, null);
            this.rule.add(element);
        }
    }

    private void extractToken(Matcher matcher) {
        String token = matcher.group(1);
        RuleElement element = new RuleElement(token, true, null);
        this.rule.add(element);
    }
}
