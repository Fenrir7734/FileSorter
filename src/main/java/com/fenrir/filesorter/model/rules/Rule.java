package com.fenrir.filesorter.model.rules;

import java.util.ArrayList;
import java.util.List;

public abstract class Rule {
    protected final List<RuleElement> rule;
    private int iterator;

    public Rule() {
        this.rule = new ArrayList<>();
        this.iterator = 0;
    }

    protected abstract void resolveRule(String rule);

    public RuleElement next() {
        return iterator < rule.size() ? rule.get(iterator++) : null;
    }

    public void resetIter() {
        this.iterator = 0;
    }

    public record RuleElement(String element, boolean isToken, String[] args) { }
}
