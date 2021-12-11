package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.Provider;
import com.fenrir.filesorter.model.statement.types.ActionType;

import java.io.IOException;
import java.util.List;

public class NotEqualPredicate<T extends Comparable<T>> implements Predicate<T> {
    private final ActionType action;
    private final Provider<T> operandStatement;
    private final List<T> args;

    public NotEqualPredicate(ActionType action, PredicateOperands<T> operands) {
        this.action = action;
        this.operandStatement = operands.operand();
        this.args = operands.args();
    }

    @Override
    public boolean test(FileData fileData) throws IOException {
        T operand = operandStatement.get(fileData);
        if (operand == null) {
            return false;
        }

        for (T arg: args) {
            if (operand.compareTo(arg) == 0) {
                return !action.perform();
            }
        }
        return action.perform();
    }
}