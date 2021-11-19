package com.fenrir.filesorter.model.rule;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FilterRule extends Rule {
    private final RuleElementContainer container = new RuleElementContainer();

    public FilterRule(String expression) {
        super(expression);
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
        List<String> args;

        int i = token.indexOf(":");
        if (i == -1) {
            args = null;
        } else {
            args = extractArgs(token, i + 1);
            token = token.substring(0, i);
        }

        RuleElement element = new RuleElement(token, true, args);
        container.add(element);
    }

    private List<String> extractArgs(String token, int index) {
        String args = token.substring(index);
        List<String> argsList = List.of(args.split(","));
        argsList = argsList.stream()
                .map(String::trim)
                .filter(arg -> arg.length() != 0)
                .collect(Collectors.toList());
        return argsList.isEmpty() ? null : argsList;
    }

    @Override
    public Iterator<RuleElement> getRuleElementsIterator() {
        return container.iterator();
    }
}
