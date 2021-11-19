package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.PredicateOperands;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.provider.Provider;

import java.io.IOException;

public class GreaterEqualPredicate<T extends Comparable<T>> implements Predicate<T> {
    private final Provider<T> operandStatement;
    private final T arg;

    public GreaterEqualPredicate(PredicateOperands<T> operands) {
        this.operandStatement = operands.operand();
        this.arg = operands.args().get(0);
    }

    @Override
    public boolean test(FileData fileData) throws IOException {
        T operand = operandStatement.get(fileData);
        return operand.compareTo(arg) >= 0;
    }
}
