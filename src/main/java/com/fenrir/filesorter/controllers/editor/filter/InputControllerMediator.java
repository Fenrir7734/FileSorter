package com.fenrir.filesorter.controllers.editor.filter;

import com.fenrir.filesorter.controllers.editor.filter.input.ArgumentInputController;

import java.util.ArrayList;
import java.util.List;

public class InputControllerMediator {
    private FilterRuleBuilderController filterRuleBuilderController;
    private final List<ArgumentInputController> inputControllers = new ArrayList<>();

    public void registerFilterRuleBuilderController(FilterRuleBuilderController controller) {
        filterRuleBuilderController = controller;
    }

    public void registerInputController(ArgumentInputController controller) {
        inputControllers.add(controller);
    }

    public void unregisterInputController(ArgumentInputController controller) {
        filterRuleBuilderController.deleteInputField(controller.getInputContainer());
        inputControllers.remove(controller);
    }

    public List<String> receiveArguments() {
        List<String> args = new ArrayList<>();
        for (ArgumentInputController controller: inputControllers) {
            args.add(controller.readArguments());
        }
        return args;
    }

    public void sendArguments(List<String> args) {
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            inputControllers.get(i).setArguments(arg);
        }
    }

    public InputControllerMediator() { }
}
