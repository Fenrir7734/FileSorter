package com.fenrir.filesorter.model.statement;

import com.fenrir.filesorter.model.statement.provider.Provider;

import java.util.List;

public record PredicateOperands<T extends Comparable<T>>(
        Provider<T> operand, List<T> args) {
}
