package com.fenrir.filesorter.model.rules;

import java.util.List;

public record RuleGroup(StringRule renameRule,
                        StringRule sortRule,
                        List<FilterRule> filterRules) {

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
