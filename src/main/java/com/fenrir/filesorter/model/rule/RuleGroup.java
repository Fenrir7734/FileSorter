package com.fenrir.filesorter.model.rule;

import java.util.List;

public record RuleGroup(StringRule renameRule,
                        StringRule sortRule,
                        List<FilterRule> filterRules) { }
