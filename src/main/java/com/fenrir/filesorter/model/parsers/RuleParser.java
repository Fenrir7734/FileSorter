package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.rules.FilterRule;
import com.fenrir.filesorter.model.rules.RenameRule;
import com.fenrir.filesorter.model.rules.RuleGroup;
import com.fenrir.filesorter.model.rules.SortRule;
import com.fenrir.filesorter.model.statement.StatementGroup;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class RuleParser {
    private StatementGroup statementGroup;
    private final RenameRuleParser renameRuleParser;
    private final SortRuleParser sortRuleParser;
    private final FilterRuleParser filterRuleParser;

    public RuleParser() {
        renameRuleParser = new RenameRuleParser();
        sortRuleParser = new SortRuleParser();
        filterRuleParser = new FilterRuleParser();
    }

    public StatementGroup parse(RuleGroup ruleGroup) throws IOException {
        initNewStatementGroup();
        createRenameStatement(ruleGroup.getRenameRule());
        createSortStatement(ruleGroup.getSortRule());
        createFilterStatement(ruleGroup.getFilterRules());
        return statementGroup;
    }

    private void initNewStatementGroup() {
        statementGroup = new StatementGroup();
    }

    private void createRenameStatement(RenameRule rule) throws IOException {
        statementGroup.setRenameStatement(renameRuleParser.resolveRule(rule));
    }

    private void createSortStatement(SortRule rule) throws IOException {
        statementGroup.setSortStatement(sortRuleParser.resolveRule(rule));
    }

    private void createFilterStatement(List<FilterRule> rules) throws IOException {
        for (FilterRule rule: rules) {
            statementGroup.addFilterStatement(filterRuleParser.resolveRule(rule));
        }
    }



}
