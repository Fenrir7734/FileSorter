package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.controllers.input.ArgumentInputController;

import java.util.ArrayList;
import java.util.List;

public class InputControllerMediator {
    private static InputControllerMediator instance;

    private FilterRuleEditorController filterRuleEditorController;
    private final List<ArgumentInputController> inputControllers = new ArrayList<>();

    public void registerFilterRuleEditorController(FilterRuleEditorController controller) {
        filterRuleEditorController = controller;
    }

    public void registerInputController(ArgumentInputController controller) {
        inputControllers.add(controller);
    }

    public void unregisterInputController(ArgumentInputController controller) {
        filterRuleEditorController.deleteInputField(controller.getInputContainer());
        inputControllers.remove(controller);
    }

    public void unregisterAllInputControllers() {
        inputControllers.clear();
    }

    public List<String> receiveArguments() {
        List<String> args = new ArrayList<>();
        for (ArgumentInputController controller: inputControllers) {
            args.add(controller.readArguments());
        }
        return args;
    }

    public InputControllerMediator() { }
}
