package com.fenrir.filesorter.model.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRule extends Rule {

    public StringRule(String rule) {
        super(rule);
        resolveExpression();
    }

    protected void resolveExpression() {
        String expression = getExpression();
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(expression);
        int lastMatchIndex = 0;

        while (matcher.find()) {
            extractLiteral(expression, lastMatchIndex, matcher.start());
            extractToken(matcher);
            lastMatchIndex = matcher.end();
        }
        extractLiteral(expression, lastMatchIndex, expression.length());
    }

    private void extractLiteral(String expression, int lastMatchIndex, int currentMatchIndex) {
        if (lastMatchIndex != currentMatchIndex) {
            String s = expression.substring(lastMatchIndex, currentMatchIndex);
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
