package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.controllers.editor.string.StringRuleEditorController;
import com.fenrir.filesorter.controllers.editor.string.rename.RenameRuleEditorController;
import com.fenrir.filesorter.controllers.editor.string.sort.SortRuleEditorController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleEditorController;
import com.fenrir.filesorter.controllers.main.RuleTabController;
import com.fenrir.filesorter.controllers.main.SortTabController;
import com.fenrir.filesorter.controllers.save.SaveRuleGroupController;
import com.fenrir.filesorter.model.file.FilePath;
import com.fenrir.filesorter.model.file.utils.Backup;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.List;

public class ControllerMediator {
    private static ControllerMediator instance;

    private SortTabController sortTabController;
    private RuleTabController ruleTabController;
    private StringRuleEditorController stringRuleEditorController;
    private FilterRuleEditorController filterRuleEditorController;
    private SaveRuleGroupController saveRuleGroupController;
    private BackupHistoryController backupHistoryController;

    public void registerSortTabController(SortTabController controller) {
        this.sortTabController = controller;
    }

    public void registerRuleTabController(RuleTabController controller) {
        this.ruleTabController = controller;
    }

    public void registerStringRuleEditorController(StringRuleEditorController controller) {
        this.stringRuleEditorController = controller;
    }

    public void registerFilterRuleEditorController(FilterRuleEditorController controller) {
        this.filterRuleEditorController = controller;
    }

    public void registerSaveRuleGroupController(SaveRuleGroupController controller) {
        this.saveRuleGroupController = controller;
    }

    public void registerBackupHistoryController(BackupHistoryController controller) {
        this.backupHistoryController = controller;
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
        stringRuleEditorController.receiveRule(rule);
    }

    public void sendSortRuleToEdit(Rule rule) {
        stringRuleEditorController.receiveRule(rule);
    }

    public void sendFilterRuleToEdit(Rule rule) {
        filterRuleEditorController.receiveRule(rule);
    }

    public void sendRuleGroupForSave(String name, RuleGroup ruleGroup) {
        saveRuleGroupController.receiveRuleGroup(name, ruleGroup);
    }

    public void sendRuleGroupFromLoad(String name, RuleGroup ruleGroup) {
        ruleTabController.receiveRuleGroup(name, ruleGroup);
    }

    public void sendBackupPathList(Backup backup) {
        sortTabController.receiveBackup(backup);
    }

    private ControllerMediator() { }

    public static ControllerMediator getInstance() {
        if (instance == null) {
            instance = new ControllerMediator();
        }
        return instance;
    }
}
