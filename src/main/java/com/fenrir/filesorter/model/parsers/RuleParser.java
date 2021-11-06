package com.fenrir.filesorter;

import com.fenrir.filesorter.model.rules.RuleGroup;

import java.util.List;

public class RuleProcessor {
    private final List<RuleGroup> ruleGroups;

    public RuleProcessor(List<RuleGroup> ruleGroups) {
        this.ruleGroups = ruleGroups;
    }

    public void createRenameStatement() {
        for (RuleGroup ruleGroup: ruleGroups) {

        }
    }

    public void createFilterStatement() {

    }
}
