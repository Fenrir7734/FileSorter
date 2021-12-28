package com.fenrir.filesorter.controllers.editor.string;

public class InputControllerMediator {
    private StringRuleBuilderController renameRuleBuilderController;

    public void registerRenameRuleBuilderController(StringRuleBuilderController controller) {
        this.renameRuleBuilderController = controller;
    }

    public void sendProviderData(StringRuleBuilderController.ProviderArgPair pair) {
        renameRuleBuilderController.receiveArgument(pair);
    }
}
