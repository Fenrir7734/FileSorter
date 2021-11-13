package com.fenrir.filesorter.model.rule;

import java.util.Arrays;
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
        List<String> argsList;

        int i = token.indexOf(":");
        if (i == -1) {
            argsList = null;
        } else {
            String[] args = extractArgs(token, i + 1);
            argsList = Arrays.stream(args)
                    .map(String::trim)
                    .filter(arg -> arg.length() != 0)
                    .collect(Collectors.toList());
            argsList = argsList.isEmpty() ? null : argsList;
            token = token.substring(0, i);
        }

        RuleElement element = new RuleElement(token, true, argsList);
        container.add(element);
    }

    private String[] extractArgs(String token, int index) {
        String args = token.substring(index);
        return args.split(",");
    }

    @Override
    public Iterator<RuleElement> getRuleElementsIterator() {
        return container.iterator();
    }
}
