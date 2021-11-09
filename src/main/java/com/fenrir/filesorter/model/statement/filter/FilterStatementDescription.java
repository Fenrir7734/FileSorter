package com.fenrir.filesorter.model.statement.filter;

import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

import java.util.List;

public record FilterStatementDescription<T extends Comparable<T>>(
        FilterOperandStatement<T> operand,
        List<T> args) {

    public FilterOperandStatement<T> getOperand() {
        return operand;
    }

    public List<T> getArgs() {
        return args;
    }
}


