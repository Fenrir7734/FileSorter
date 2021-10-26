package com.fenrir.filesorter.rules.parsers;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.rules.RenameRule;
import com.fenrir.filesorter.rules.Rule;

public class RenameRuleParser {
    private Rule rule;
    private FileData fileData;

    public RenameRuleParser(RenameRule rule, FileData fileData) {
        this.rule = rule;
        this.fileData = fileData;
    }
}
