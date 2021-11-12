package com.fenrir.filesorter.model.rule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRule extends Rule {
    private final RuleElementContainer container = new RuleElementContainer();

    public StringRule(String expression) {
        super(expression);
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
            container.add(element);
        }
    }

    private void extractToken(Matcher matcher) {
        String token = matcher.group(1);
        RuleElement element = new RuleElement(token, true, null);
        container.add(element);
    }

    @Override
    public Iterator<RuleElement> getRuleElementsIterator() {
        return container.iterator();
    }
}
