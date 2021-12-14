package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.Provider;
import com.fenrir.filesorter.model.statement.types.ActionType;

import java.io.IOException;
import java.util.List;

public class EqualPredicate<T extends Comparable<T>> implements Predicate<T> {
    private final boolean invert;
    private final Provider<T> operandStatement;
    private final List<T> args;

    public EqualPredicate(PredicateOperands<T> operands, boolean invert) {
        this.invert = invert;
        this.operandStatement = operands.operand();
        this.args = operands.args();
    }

    @Override
    public boolean test(FileData fileData) throws IOException {
        T operand = operandStatement.get(fileData);
        if (operand == null) {
            return false;
        }

        for (T arg : args) {
            if (arg.compareTo(operand) == 0) {
                return true ^ invert;
            }
        }
        return false ^ invert;
    }
}
