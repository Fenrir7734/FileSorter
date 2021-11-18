package com.fenrir.filesorter.model.statement.filter;

import com.fenrir.filesorter.model.statement.provider.Provider;

import java.util.List;

public record FilterStatementDescription<T extends Comparable<T>>(
        Provider<T> operand,
        List<T> args) { }


