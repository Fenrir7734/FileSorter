package com.fenrir.filesorter.model.statement.filter;

import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

import java.util.List;

public record FilterStatementDescription<T extends Comparable<T>>(
        FilterOperandStatement<T> operand,
        List<T> args) { }


