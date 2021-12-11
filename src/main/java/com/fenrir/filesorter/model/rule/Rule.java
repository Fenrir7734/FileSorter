package com.fenrir.filesorter.model.rule;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Rule {
    private final String expression;
    private final TokenContainer container = new TokenContainer();

    public Rule(String expression) throws ExpressionFormatException {
        this.expression = expression;
        resolveExpression();
    }

    private void resolveExpression() throws ExpressionFormatException{
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(expression);
        StringBuilder check = new StringBuilder();

        while (matcher.find()) {
            extractToken(matcher);
            check.append(matcher.group());
        }

        if (!check.toString().equals(expression)) {
            throw new ExpressionFormatException("Invalid expression");
        }
    }

    private void extractToken(Matcher matcher) {
        String extractedToken = matcher.group(1);
        List<String> args;

        int i = extractedToken.indexOf(":");
        if (i == -1) {
            args = null;
        } else {
            args = extractArgs(extractedToken, i + 1);
            extractedToken = extractedToken.substring(0, i);
        }

        Token token = new Token(extractedToken, args);
        container.add(token);
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

    public Iterator<Token> getTokenIterator() {
        return container.iterator();
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}
