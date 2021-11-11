package com.fenrir.filesorter.model.rules;

import java.util.List;

public class RuleGroup {
    private final RenameRule renameRule;
    private final SortRule sortRule;
    private final List<FilterRule> filterRules;

    public RuleGroup(RenameRule renameRule, SortRule sortRule, List<FilterRule> filterRules) {
        this.sortRule = sortRule;
        this.renameRule = renameRule;
        this.filterRules = filterRules;
    }

    public RenameRule getRenameRule() {
        return renameRule;
    }

    public SortRule getSortRule() {
        return sortRule;
    }

    public List<FilterRule> getFilterRules() {
        return filterRules;
    }
}
