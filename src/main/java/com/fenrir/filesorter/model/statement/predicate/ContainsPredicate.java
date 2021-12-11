package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.Provider;
import com.fenrir.filesorter.model.statement.types.ActionType;
import com.fenrir.filesorter.model.statement.utils.TypeChecker;

import java.io.IOException;
import java.util.List;

public class ContainsPredicate<T extends Comparable<T>> implements Predicate<T> {
    private final ActionType action;
    private final Provider<String> operandStatement;
    private final List<String> args;

    public ContainsPredicate(ActionType action, PredicateOperands<T> operands) throws ExpressionFormatException {
        if (TypeChecker.isInstanceOfString(operands)) {
            this.action = action;
            this.operandStatement = (Provider<String>) operands.operand();
            this.args = (List<String>) operands.args();
        } else {
            throw new ExpressionFormatException("Invalid type of operand for given operator");
        }
    }

    @Override
    public boolean test(FileData fileData) throws IOException {
        String operand = operandStatement.get(fileData);
        if (operand == null) {
            return false;
        }

        for (String arg: args) {
            if (operand.contains(arg)) {
                return action.perform();
            }
        }
        return !action.perform();
    }
}
