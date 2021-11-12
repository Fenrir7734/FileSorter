package com.fenrir.filesorter.model.rule;


public abstract class Rule {
    private final String expression;

    public Rule(String expression) {
        this.expression = expression;
    }

    protected abstract void resolveExpression();

    public abstract Iterator<RuleElement> getRuleElementsIterator();

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}
