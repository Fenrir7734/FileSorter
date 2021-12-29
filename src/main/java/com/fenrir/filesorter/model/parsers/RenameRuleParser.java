package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.statement.types.enums.Scope;

public class RenameRuleParser extends StringRuleParser {
    public RenameRuleParser() {
        super(Scope.RENAME);
    }
}
