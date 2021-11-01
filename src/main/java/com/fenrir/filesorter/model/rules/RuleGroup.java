package com.fenrir.filesorter.model.rules;

import java.util.Queue;

public class RuleGroup {
    private final Rule renameRule;
    private final Rule sortRule;
    private final Queue<Rule> filterRules;

    public RuleGroup(RenameRule renameRule, SortRule sortRule, Queue<Rule> filterRules) {
        this.sortRule = sortRule;
        this.renameRule = renameRule;
        this.filterRules = filterRules;
    }

    public Rule getRenameRule() {
        return renameRule;
    }

    public Rule getSortRule() {
        return sortRule;
    }

    public Queue<Rule> getFilterRules() {
        return filterRules;
    }
}
