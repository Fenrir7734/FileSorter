package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.controllers.editor.rename.RenameRuleEditorController;
import com.fenrir.filesorter.controllers.editor.sort.SortRuleEditorController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleEditorController;
import com.fenrir.filesorter.controllers.main.RuleTabController;
import com.fenrir.filesorter.model.rule.Rule;

public class ControllerMediator {
    private static ControllerMediator instance;

    private RuleTabController ruleTabController;
    private RenameRuleEditorController renameRuleEditorController;
    private SortRuleEditorController sortRuleEditorController;
    private FilterRuleEditorController filterRuleEditorController;

    public void registerRuleTabController(RuleTabController controller) {
        this.ruleTabController = controller;
    }

    public void registerRenameRuleEditorController(RenameRuleEditorController controller) {
        this.renameRuleEditorController = controller;
    }

    public void registerSortRuleEditorController(SortRuleEditorController controller) {
        this.sortRuleEditorController = controller;
    }

    public void registerFilterRuleEditorController(FilterRuleEditorController controller) {
        this.filterRuleEditorController = controller;
    }

    public void sendReadyRenameRule(Rule rule) {
        ruleTabController.receiveRenameRule(rule);
    }

    public void sendReadySortRule(Rule rule) {
        ruleTabController.receiveSortRule(rule);
    }

    public void sendReadyFilterRule(Rule rule) {
        ruleTabController.receiveFilterRule(rule);
    }

    public void sendRenameRuleToEdit(Rule rule) {
        renameRuleEditorController.receiveRule(rule);
    }

    public void sendSortRuleToEdit(Rule rule) {
        sortRuleEditorController.receiveRule(rule);
    }

    public void sendFilterRuleToEdit(Rule rule) {
        filterRuleEditorController.receiveRule(rule);
    }

    private ControllerMediator() { }

    public static ControllerMediator getInstance() {
        if (instance == null) {
            instance = new ControllerMediator();
        }
        return instance;
    }
}
