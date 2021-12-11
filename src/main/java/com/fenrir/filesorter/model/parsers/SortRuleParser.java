package com.fenrir.filesorter.model.parsers;


import com.fenrir.filesorter.model.enums.Scope;

public class SortRuleParser extends StringRuleParser {
    public SortRuleParser() {
        super(Scope.SORT);
    }
}
