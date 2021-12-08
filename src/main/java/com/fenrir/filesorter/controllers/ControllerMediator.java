package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.controllers.editor.rename.RenameRuleEditorController;
import com.fenrir.filesorter.controllers.editor.sort.SortRuleEditorController;
import com.fenrir.filesorter.controllers.editor.filter.FilterRuleEditorController;
import com.fenrir.filesorter.controllers.main.MainController;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.StringRule;

public class ControllerMediator {
    private static ControllerMediator instance;

    private MainController mainController;
    private RenameRuleEditorController renameRuleEditorController;
    private SortRuleEditorController sortRuleEditorController;
    private FilterRuleEditorController filterRuleEditorController;

    public void registerController(Controller controller) {
        if (MainController.class.equals(controller.getClass())) {
            mainController = (MainController) controller;
        } else if (RenameRuleEditorController.class.equals(controller.getClass())) {
            renameRuleEditorController = (RenameRuleEditorController) controller;
        } else if (SortRuleEditorController.class.equals(controller.getClass())) {
            sortRuleEditorController = (SortRuleEditorController) controller;
        } else if (FilterRuleEditorController.class.equals(controller.getClass())) {
            filterRuleEditorController = (FilterRuleEditorController) controller;
        }
    }

    public void sendReadyRenameRule(StringRule rule) {
        mainController.receiveRenameRule(rule);
    }

    public void sendReadySortRule(StringRule rule) {
        mainController.receiveSortRule(rule);
    }

    public void sendReadyFilterRule(FilterRule rule) {
        mainController.receiveFilterRule(rule);
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
