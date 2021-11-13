package com.fenrir.filesorter.model.rule;

import java.util.List;

public record RuleElement(String element, boolean isToken, List<String> args) { }
