package com.fenrir.filesorter.controllers.editor.rename;

import com.fenrir.filesorter.controllers.editor.rename.input.StringArgumentInputController;
import com.fenrir.filesorter.model.statement.types.ProviderType;

public class InputControllerMediator {
    private RenameRuleBuilderController renameRuleBuilderController;

    public void registerRenameRuleBuilderController(RenameRuleBuilderController controller) {
        this.renameRuleBuilderController = controller;
    }

    public void sendArgument(String arg, ProviderType type) {
        renameRuleBuilderController.receiveArgument(arg, type);
    }
}
