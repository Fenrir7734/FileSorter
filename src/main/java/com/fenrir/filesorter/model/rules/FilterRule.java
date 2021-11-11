package com.fenrir.filesorter.model.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterRule extends Rule {

    public FilterRule(String rule) {
        super(rule);
        resolveExpression();
    }

    @Override
    protected void resolveExpression() {
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(getExpression());

        while (matcher.find()) {
            extractToken(matcher);
        }
    }

    private void extractToken(Matcher matcher) {
        String token = matcher.group(1);
        String[] args;

        int i = token.indexOf(":");
        if (i == -1) {
            args = null;
        } else {
            args = extractArgs(token, i + 1);
            token = token.substring(0, i);
        }

        RuleElement element = new RuleElement(token, true, args);
        this.rule.add(element);
    }

    private String[] extractArgs(String token, int index) {
        String args = token.substring(index);
        return args.split(",");
    }
}
