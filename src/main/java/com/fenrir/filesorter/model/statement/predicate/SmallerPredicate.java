package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.Provider;
import com.fenrir.filesorter.model.statement.types.ActionType;

import java.io.IOException;

public class SmallerPredicate<T extends Comparable<T>> implements Predicate<T> {
    private final boolean invert;
    private final Provider<T> operandStatement;
    private final T arg;

    public SmallerPredicate(PredicateOperands<T> operands, boolean invert) {
        this.invert = invert;
        this.operandStatement = operands.operand();
        this.arg = operands.args().get(0);
    }

    @Override
    public boolean test(FileData fileData) throws IOException {
        T operand = operandStatement.get(fileData);
        if (operand == null) {
            return false;
        }
        return operand.compareTo(arg) < 0 ? true ^ invert : false ^ invert;
    }
}
