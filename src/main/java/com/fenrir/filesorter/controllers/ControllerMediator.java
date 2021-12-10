package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.controllers.editor.rename.RenameRuleEditorController;
import com.fenrir.filesorter.controllers.editor.sort.SortRuleEditorController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleEditorController;
import com.fenrir.filesorter.controllers.main.RuleTabController;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.StringRule;

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

    public void sendReadyRenameRule(StringRule rule) {
        ruleTabController.receiveRenameRule(rule);
    }

    public void sendReadySortRule(StringRule rule) {
        ruleTabController.receiveSortRule(rule);
    }

    public void sendReadyFilterRule(FilterRule rule) {
        ruleTabController.receiveFilterRule(rule);
    }

    public void sendRenameRuleToEdit(StringRule rule) {
        renameRuleEditorController.receiveRule(rule);
    }

    public void sendSortRuleToEdit(StringRule rule) {
        sortRuleEditorController.receiveRule(rule);
    }

    public void sendFilterRuleToEdit(FilterRule rule) {
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
