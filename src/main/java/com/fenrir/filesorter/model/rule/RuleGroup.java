package com.fenrir.filesorter.model.rule;

import java.util.ArrayList;
import java.util.List;

public class RuleGroup {
    private StringRule renameRule;
    private StringRule sortRule;
    private final List<FilterRule> filterRules = new ArrayList<>();

    public void setRenameRule(StringRule rule) {
        renameRule = rule;
    }

    public void setSortRule(StringRule sortRule) {
        this.sortRule = sortRule;
    }

    public void addFilterRule(FilterRule rule) {
        filterRules.add(rule);
    }

    public void removeFilterRule(FilterRule rule) {
        filterRules.remove(rule);
    }

    public StringRule getRenameRule() {
        return renameRule;
    }

    public StringRule getSortRule() {
        return sortRule;
    }

    public List<FilterRule> getFilterRules() {
        return filterRules;
    }
}
