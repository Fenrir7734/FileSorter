package com.fenrir.filesorter.controllers.editor.string;

import com.fenrir.filesorter.model.statement.types.ProviderType;

public class InputControllerMediator {
    private StringRuleBuilderController renameRuleBuilderController;

    public void registerRenameRuleBuilderController(StringRuleBuilderController controller) {
        this.renameRuleBuilderController = controller;
    }

    public void sendArgument(String arg, ProviderType type) {
        renameRuleBuilderController.receiveArgument(arg, type);
    }
}
