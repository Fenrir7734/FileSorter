package com.fenrir.filesorter.model.rules;

import java.util.ArrayList;
import java.util.List;

public abstract class Rule {
    private final String expression;
    protected final List<RuleElement> rule;
    private int iterator;

    public Rule(String expression) {
        this.expression = expression;
        this.rule = new ArrayList<>();
        this.iterator = 0;
    }

    protected abstract void resolveExpression();

    public RuleElement next() {
        return iterator < rule.size() ? rule.get(iterator++) : null;
    }

    public void resetIter() {
        this.iterator = 0;
    }

    public record RuleElement(String element, boolean isToken, String[] args) { }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}
