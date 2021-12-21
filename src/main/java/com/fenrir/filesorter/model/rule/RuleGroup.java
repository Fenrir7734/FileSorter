package com.fenrir.filesorter.model.rule;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class RuleGroup {
    private Rule renameRule;
    private Rule sortRule;
    private ObservableList<Rule> filterRules = FXCollections.observableArrayList();

    public void setRenameRule(Rule rule) {
        renameRule = rule;
    }

    public void setSortRule(Rule rule) {
        this.sortRule = rule;
    }

    public void setFilterRules(List<Rule> rules) {
        this.filterRules = FXCollections.observableArrayList(rules);
    }

    public void addFilterRule(Rule rule) {
        filterRules.add(rule);
    }

    public void removeFilterRule(Rule rule) {
        filterRules.remove(rule);
    }

    public Rule getRenameRule() {
        return renameRule;
    }

    public Rule getSortRule() {
        return sortRule;
    }

    public ObservableList<Rule> getFilterRules() {
        return filterRules;
    }

}
