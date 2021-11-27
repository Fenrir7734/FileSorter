package com.fenrir.filesorter.model.rule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RuleGroup {
    private StringRule renameRule;
    private StringRule sortRule;
    private final ObservableList<FilterRule> filterRules = FXCollections.observableArrayList();

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

    public ObservableList<FilterRule> getFilterRules() {
        return filterRules;
    }
}
